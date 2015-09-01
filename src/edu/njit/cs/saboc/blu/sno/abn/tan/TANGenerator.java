package edu.njit.cs.saboc.blu.sno.abn.tan;

import SnomedShared.Concept;
import SnomedShared.overlapping.ClusterSummary;
import SnomedShared.overlapping.CommonOverlapSet;
import SnomedShared.overlapping.EntryPoint;
import SnomedShared.overlapping.EntryPointSet;
import SnomedShared.pareataxonomy.GroupParentInfo;
import edu.njit.cs.saboc.blu.core.abn.GroupHierarchy;
import edu.njit.cs.saboc.blu.sno.abn.tan.local.LocalCluster;
import edu.njit.cs.saboc.blu.sno.abn.tan.local.LocalTribalAbstractionNetwork;
import edu.njit.cs.saboc.blu.sno.datastructure.hierarchy.SCTConceptHierarchy;
import edu.njit.cs.saboc.blu.sno.datastructure.hierarchy.SCTMultiRootedConceptHierarchy;
import edu.njit.cs.saboc.blu.sno.localdatasource.concept.LocalSnomedConcept;
import edu.njit.cs.saboc.blu.sno.sctdatasource.SCTLocalDataSource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Stack;


/**
 *
 * @author Chris
 */
public class TANGenerator {
    
    /**
     * Given a hierarchy of concepts, derives a Tribal Abstraction Network
     * @param hierarchyRoot
     * @param snomedVersion
     * @param hierarchy
     * @return 
     */
    public static LocalTribalAbstractionNetwork createTANFromConceptHierarchy(
               String snomedVersion,
               SCTConceptHierarchy hierarchy) {
        
        HashSet<Concept> patriarchs = hierarchy.getChildren(hierarchy.getRoot());
        
        
        SCTMultiRootedConceptHierarchy multiRootedHierarchy = new SCTMultiRootedConceptHierarchy(patriarchs);
        multiRootedHierarchy.addAllHierarchicalRelationships(hierarchy);

        return deriveTANFromMultiRootedHierarchy(multiRootedHierarchy, snomedVersion);
    }
    
    public static LocalTribalAbstractionNetwork deriveTANFromMultiRootedHierarchy(SCTMultiRootedConceptHierarchy hierarchy, String snomedVersion) {
          
        // The set of concepts in this hierarchy, mapped according to their ID.
        HashMap<Long, Concept> concepts = new HashMap<Long, Concept>();
        
        for(Concept c : hierarchy.getConceptsInHierarchy()) {
            concepts.put(c.getId(), c);
        }

        HashSet<Concept> patriarchs = hierarchy.getRoots();
        
        // The set of tribes a given concept belongs to
        HashMap<Concept, HashSet<Concept>> conceptTribes = new HashMap<Concept, HashSet<Concept>>();
        
        HashMap<Concept, Integer> remainingParentCount = new HashMap<Concept, Integer>();
        
        Stack<Concept> pendingConcepts = new Stack<Concept>();
        
        pendingConcepts.addAll(patriarchs);
        
        for(Concept c : concepts.values()) {
            if(patriarchs.contains(c)) {
                remainingParentCount.put(c, 0);
            } else {
                remainingParentCount.put(c, hierarchy.getParents(c).size());
            }
        }
        
        while(!pendingConcepts.isEmpty()) {
            Concept c = pendingConcepts.pop();
            
            if(patriarchs.contains(c)) {
                conceptTribes.put(c, new HashSet<Concept>(Arrays.asList(c)));
            } else {
                HashSet<Concept> parents = hierarchy.getParents(c);
                
                HashSet<Concept> tribalSet = new HashSet<Concept>();
                
                for(Concept parent : parents) {
                    tribalSet.addAll(conceptTribes.get(parent));
                }
                
                conceptTribes.put(c, tribalSet);
            }
            
            HashSet<Concept> children = hierarchy.getChildren(c);
            
            for(Concept child : children) {
                int parentCount = remainingParentCount.get(child) - 1;

                if(parentCount == 0) {
                    pendingConcepts.push(child);
                } else {
                    remainingParentCount.put(child, parentCount);
                }
            }
        }
        
        // The set of cluster root concepts in the hierarchy
        HashSet<Concept> roots = new HashSet<Concept>();
        roots.addAll(patriarchs);

        // Based on the tribes a given concept's parent(s) belong to, 
        // determine if the concept is a root
        for (Map.Entry<Concept, HashSet<Concept>> entry : conceptTribes.entrySet()) {
            if(roots.contains(entry.getKey())) {
                continue;
            }
            
            HashSet<Concept> myCluster = entry.getValue(); // The patriarchs that have this concept as a descendent

            HashSet<Concept> parents = hierarchy.getParents(entry.getKey()); // Get parents of this concept

            boolean isRoot = true;

            if (parents != null) {
                for (Concept parent : parents) { // For each parent
                    HashSet<Concept> parentTribes = conceptTribes.get(parent);

                    if (parentTribes.equals(myCluster)) { // If a parent has the same group of tribes
                        isRoot = false; // Then this is not a root concept
                        break;
                    }
                }
            }

            if (isRoot) { // If concept is a root
                roots.add(entry.getKey()); // Add it to roots
            }
        }

        int id = 1;
        
        // Stores the hierarchy of concepts summarized by each cluster
        final HashMap<Concept, SCTConceptHierarchy> clusters = new HashMap<Concept, SCTConceptHierarchy>();
        
        // Stores the list of clusters each concept belongs to
        final HashMap<Concept, HashSet<Concept>> conceptClusters = new HashMap<Concept, HashSet<Concept>>();
        
        // Integer ID for cluster (legacy support)
        final HashMap<Concept, Integer> clusterIds = new HashMap<Concept, Integer>();

        for (Concept root : roots) { // For each root
            clusters.put(root, new SCTConceptHierarchy(root)); // Create a new cluster
            
            clusterIds.put(root, id++);

            HashSet<Concept> rootTribalSet = conceptTribes.get(root);

            Stack<Concept> stack = new Stack<Concept>();

            stack.add(root);

            while (!stack.isEmpty()) { // Traverse down DAG and add concepts to cluster
                Concept concept = stack.pop();
                
                HashSet<Concept> conceptTribalSet = conceptTribes.get(concept);

                if (rootTribalSet.equals(conceptTribalSet)) {
                    if (!conceptClusters.containsKey(concept)) {
                        conceptClusters.put(concept, new HashSet<Concept>());
                    }

                    conceptClusters.get(concept).add(root); // Set concept as belonging to current header
                    
                    HashSet<Concept> children = hierarchy.getChildren(concept);

                    for (Concept child : children) { // Process all children
                        if (stack.contains(child) || roots.contains(child) || !conceptTribes.get(child).equals(rootTribalSet)) {
                            continue;
                        }

                        clusters.get(root).addIsA(child, concept);

                        stack.add(child);
                    }
                }
            }
        }

        HashMap<Integer, LocalCluster> hierarchyClusters = new HashMap<Integer, LocalCluster>();
        HashMap<Integer, HashSet<Integer>> clusterHierarchy = new HashMap<Integer, HashSet<Integer>>();
        
        ArrayList<ClusterSummary> patriarchClusters = new ArrayList<ClusterSummary>();

        for (Concept root : roots) {
            HashSet<Concept> rootsTribes = conceptTribes.get(root);
            HashSet<Concept> parents = hierarchy.getParents(root);
            
            HashSet<Concept> parentTribes = new HashSet<Concept>();
            HashSet<Integer> parentClusters = new HashSet<Integer>();
            
            ArrayList<GroupParentInfo> parentInfo = new ArrayList<GroupParentInfo>();
            
            for (Concept parent : parents) {
                // When a single rooted hierarchy is used the IS As to the root are still there. Skip them.
                if(!conceptTribes.containsKey(parent)) {
                    continue;
                }
                
                parentTribes.addAll(conceptTribes.get(parent));
                
                HashSet<Concept> rootParentClusters = conceptClusters.get(parent);
   
                for (Concept parentCluster : rootParentClusters) {
                    int parentClusterId = clusterIds.get(parentCluster);
                    
                    parentInfo.add(new GroupParentInfo(parent, parentCluster.getId()));
                    
                    parentClusters.add(parentClusterId);

                    if (!clusterHierarchy.containsKey(parentClusterId)) {
                        clusterHierarchy.put(parentClusterId, new HashSet<Integer>());
                    }

                    clusterHierarchy.get(parentClusterId).add(clusterIds.get(root));
                }
            }

            EntryPointSet epSet = new EntryPointSet();

            for (Concept rootPatriarch : rootsTribes) {
                if (parentTribes.contains(rootPatriarch)) {
                    epSet.add(new EntryPoint(rootPatriarch.getId(), EntryPoint.InheritanceType.INHERITED));
                } else {
                    epSet.add(new EntryPoint(rootPatriarch.getId(), EntryPoint.InheritanceType.INTRODUCED));
                }
            }

            LocalCluster cluster = new LocalCluster(
                    clusterIds.get(root), 
                    root, 
                    clusters.get(root), 
                    parentClusters, 
                    epSet, 
                    parentInfo);

            hierarchyClusters.put(clusterIds.get(root), cluster);

            if(rootsTribes.size() == 1) {
                patriarchClusters.add(cluster);
            }
        }
        
        HashMap<HashSet<Concept>, CommonOverlapSet> tribalBands = new HashMap<HashSet<Concept>, CommonOverlapSet>();
        
        int bandId = 1;

        for (Concept root : roots) { // For every root
            HashSet<Concept> rootTribes = conceptTribes.get(root); // Get tribes of this root
            
            HashSet<Long> rootTribeConceptIds = new HashSet<Long>();
            
            for(Concept c : rootTribes) {
                rootTribeConceptIds.add(c.getId());
            }

            if (!tribalBands.containsKey(rootTribes)) { // Create a new band if one does not exist
                tribalBands.put(rootTribes, new CommonOverlapSet(bandId++, rootTribeConceptIds));
            }

            tribalBands.get(rootTribes).addClusterToSet(hierarchyClusters.get(clusterIds.get(root)));
        }

        ArrayList<ClusterSummary> nonOverlappingPatriarchs = new ArrayList<ClusterSummary>();

        for (ClusterSummary patriarchCluster : patriarchClusters) {
            boolean overlaps = false;

            long patriarchId = patriarchCluster.getHeaderConcept().getId();

            for(CommonOverlapSet tribalBand : tribalBands.values()) {
                if(tribalBand.getSetEntryPoints().size() > 1 && tribalBand.getSetEntryPoints().contains(patriarchId)) {
                    overlaps = true;
                    break;
                }
            }

            if (!overlaps) {
                nonOverlappingPatriarchs.add(patriarchCluster);
            }
        }
        
        GroupHierarchy<ClusterSummary> convertedHierarchy = new GroupHierarchy<>(new HashSet<>(patriarchClusters));
        
        hierarchyClusters.values().forEach((ClusterSummary summary) -> {
            HashSet<Integer> parentIds = summary.getParentIds();
            
            parentIds.forEach((Integer parentId) -> {
               convertedHierarchy.addIsA(summary, hierarchyClusters.get(parentId));
            });
        });
                
        return new LocalTribalAbstractionNetwork(
                new ArrayList<CommonOverlapSet>(tribalBands.values()), 
                new HashMap<Integer, ClusterSummary>(hierarchyClusters), 
                convertedHierarchy, 
                snomedVersion,
                patriarchClusters,
                nonOverlappingPatriarchs,
                concepts,
                hierarchy,
                new SCTLocalDataSource(new HashMap<Long, LocalSnomedConcept>(), null, false, snomedVersion));
    }
}
