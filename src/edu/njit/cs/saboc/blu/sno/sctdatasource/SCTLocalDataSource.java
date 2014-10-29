/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.njit.cs.saboc.blu.sno.sctdatasource;

import SnomedShared.Concept;
import SnomedShared.OutgoingLateralRelationship;
import SnomedShared.PAreaDetailsForConcept;
import SnomedShared.SearchResult;
import SnomedShared.overlapping.ClusterSummary;
import SnomedShared.pareataxonomy.ConceptPAreaInfo;
import SnomedShared.pareataxonomy.GroupParentInfo;
import SnomedShared.pareataxonomy.PAreaSummary;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.PAreaTaxonomy;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.LocalPArea;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.LocalPAreaTaxonomy;
import edu.njit.cs.saboc.blu.sno.abn.tan.TribalAbstractionNetwork;
import edu.njit.cs.saboc.blu.sno.abn.tan.local.ConceptClusterInfo;
import edu.njit.cs.saboc.blu.sno.abn.tan.local.LocalCluster;
import edu.njit.cs.saboc.blu.sno.datastructure.hierarchy.SCTConceptHierarchy;
import edu.njit.cs.saboc.blu.sno.datastructure.hierarchy.SCTMultiRootedConceptHierarchy;
import edu.njit.cs.saboc.blu.sno.localdatasource.concept.Description;
import edu.njit.cs.saboc.blu.sno.localdatasource.concept.LocalSnomedConcept;
import edu.njit.cs.saboc.blu.sno.localdatasource.conceptdata.HierarchyMetrics;
import edu.njit.cs.saboc.blu.sno.localdatasource.load.PAreaTaxonomyGenerator;
import edu.njit.cs.saboc.blu.sno.utils.comparators.ConceptNameComparator;
import edu.njit.cs.saboc.blu.sno.utils.comparators.SearchResultComparator;
import java.util.ArrayDeque;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Queue;
import java.util.Stack;

/**
 * 
 * @author Chris
 */
public class SCTLocalDataSource implements SCTDataSource {

    private class DescriptionEntry {

        public Description description;
        public Concept concept;

        public DescriptionEntry(Description d, Concept c) {
            this.description = d;
            this.concept = c;
        }
    }

    private final SCTConceptHierarchy conceptHierarchy;

    private final Map<Long, LocalSnomedConcept> concepts;

    private final HashMap<Character, Integer> startingIndex = new HashMap<Character, Integer>();

    private ArrayList<DescriptionEntry> descriptions;

    private String version;

    public SCTLocalDataSource(Map<Long, LocalSnomedConcept> concepts,
            SCTConceptHierarchy conceptHierarchy, boolean processDescriptions, String version) {

        this.conceptHierarchy = conceptHierarchy; // SNOMED CT Concept

        this.concepts = concepts;
        
        this.version = version;

        if (processDescriptions) {
            descriptions = new ArrayList<DescriptionEntry>();

            for (LocalSnomedConcept c : concepts.values()) {
                for (Description d : c.getDescriptions()) {
                    descriptions.add(new DescriptionEntry(d, c));
                }
            }

            Collections.sort(descriptions, new Comparator<DescriptionEntry>() {
                public int compare(DescriptionEntry a, DescriptionEntry b) {
                    return a.description.getTerm().compareToIgnoreCase(b.description.getTerm());
                }
            });

            char lastChar = Character.toLowerCase(descriptions.get(0).description.getTerm().charAt(0));

            for (int c = 1; c < descriptions.size(); c++) {
                String term = descriptions.get(c).description.getTerm();

                char curChar = Character.toLowerCase(term.charAt(0));

                if (curChar != lastChar) {
                    if (curChar >= 'a' && curChar <= 'z') {
                        startingIndex.put(curChar, c);
                    }

                    lastChar = curChar;
                }
            }
        }
    }

    public SCTConceptHierarchy getConceptHierarchy() {
        return conceptHierarchy;
    }

    public Concept getConceptFromId(long id) {
        return concepts.get(id);
    }

    public ArrayList<Concept> getConceptParents(Concept c) {
        ArrayList<Concept> parents = new ArrayList<Concept>(conceptHierarchy.getParents(c));

        Collections.sort(parents, new ConceptNameComparator());

        return parents;
    }

    public ArrayList<Concept> getConceptChildren(Concept c) {
        ArrayList<Concept> children = new ArrayList<Concept>(conceptHierarchy.getChildren(c));

        Collections.sort(children, new ConceptNameComparator());

        return children;
    }

    public ArrayList<String> getConceptSynoynms(Concept c) {
        if (c instanceof LocalSnomedConcept) {
            LocalSnomedConcept concept = (LocalSnomedConcept) c;

            ArrayList<String> synonyms = new ArrayList<String>();

            ArrayList<Description> conceptDescriptions = concept.getDescriptions();

            for (Description d : conceptDescriptions) {
                synonyms.add(d.getTerm());
            }

            Collections.sort(synonyms);

            return synonyms;
        } else {
            throw new RuntimeException("Unsupported Concept Type: " + c.getClass());
        }
    }

    public ArrayList<Concept> getConceptSiblings(Concept c) {
        HashSet<Concept> parents = conceptHierarchy.getParents(c);

        HashSet<Concept> siblingSet = new HashSet<Concept>();

        for (Concept parent : parents) {
            siblingSet.addAll(conceptHierarchy.getChildren(parent));
        }

        siblingSet.remove(c);

        ArrayList<Concept> siblings = new ArrayList<Concept>(siblingSet);
        Collections.sort(siblings, new ConceptNameComparator());

        return siblings;
    }

    public ArrayList<OutgoingLateralRelationship> getOutgoingLateralRelationships(Concept c) {
        if (c instanceof LocalSnomedConcept) {
            LocalSnomedConcept concept = (LocalSnomedConcept) c;

            ArrayList<OutgoingLateralRelationship> rels = new ArrayList<OutgoingLateralRelationship>(
                    concept.getAttributeRelationships());

            Collections.sort(rels, new Comparator<OutgoingLateralRelationship>() {
                public int compare(OutgoingLateralRelationship a, OutgoingLateralRelationship b) {
                    if (a.getRelationshipGroup() == b.getRelationshipGroup()) {
                        if (a.getRelationship().equals(b.getRelationship())) {
                            return a.getTarget().getName().compareToIgnoreCase(b.getTarget().getName());
                        } else {
                            return a.getRelationship().getName().compareToIgnoreCase(b.getRelationship().getName());
                        }
                    } else {
                        return a.getRelationshipGroup() - b.getRelationshipGroup();
                    }
                }
            });

            return rels;

        } else {
            throw new RuntimeException("Unsupported Concept Type: " + c.getClass());
        }
    }

    public ArrayList<Concept> getHierarchyRootConcepts() {
        ArrayList<Concept> roots = new ArrayList<Concept>(conceptHierarchy.getChildren(concepts.get(138875005l)));

        Collections.sort(roots, new ConceptNameComparator());

        return roots;
    }

    public ArrayList<PAreaDetailsForConcept> getSummaryOfPAreasContainingConcept(Concept c) {
        
        ArrayList<Concept> hierarchies = this.getHierarchiesConceptBelongTo(c);
        
        if(hierarchies.isEmpty()) {
            return new ArrayList<PAreaDetailsForConcept>();
        } else if(hierarchies.size() == 1) {
            ArrayList<PAreaDetailsForConcept> results = new ArrayList<PAreaDetailsForConcept>();
            
            PAreaTaxonomyGenerator generator = new PAreaTaxonomyGenerator();
            
            LocalPAreaTaxonomy taxonomy = generator.createPAreaTaxonomy(hierarchies.get(0), this);
            
            for(PAreaSummary parea : taxonomy.getPAreas().values()) {
                ArrayList<Concept> pareaConcepts = this.getConceptsInPArea(taxonomy, parea);
                
                if(pareaConcepts.contains(c)) {
                    results.add(new PAreaDetailsForConcept(hierarchies.get(0), 
                            parea.getRoot(), 
                            this.getOutgoingLateralRelationships(parea.getRoot()), 
                            parea.getConceptCount()));
                }
            }
            
            return results;
            
        } else {
            throw new RuntimeException("Concepts in multiple hierarchies not yet supported...");
        }
    }

    public ArrayList<Concept> getConceptsInPArea(PAreaTaxonomy taxonomy, PAreaSummary parea) {
        if (parea instanceof LocalPArea) {
            LocalPArea localPArea = (LocalPArea) parea;

            ArrayList<Concept> pareaConcepts = localPArea.getConceptsInPArea();

            Collections.sort(pareaConcepts, new ConceptNameComparator());

            return pareaConcepts;
        } else {
            throw new RuntimeException("Unsupported PArea Type: " + parea.getClass());
        }
    }

    public HashMap<Long, ArrayList<Concept>> getConceptsInPAreaSet(PAreaTaxonomy taxonomy, ArrayList<PAreaSummary> pareas) {
        HashMap<Long, ArrayList<Concept>> pareaSetConcepts = new HashMap<Long, ArrayList<Concept>>();

        for (PAreaSummary parea : pareas) {
            pareaSetConcepts.put(parea.getRoot().getId(), getConceptsInPArea(taxonomy, parea));
        }

        return pareaSetConcepts;
    }

    public ArrayList<Concept> getConceptsInCluster(TribalAbstractionNetwork tan, ClusterSummary cluster) {
        if (cluster instanceof LocalCluster) {
            LocalCluster localCluster = (LocalCluster) cluster;

            ArrayList<Concept> clusterConcepts = new ArrayList<Concept>(localCluster.getConcepts());

            Collections.sort(clusterConcepts, new ConceptNameComparator());

            return clusterConcepts;
        } else {
            throw new RuntimeException("Unsupported Cluster Type: " + cluster.getClass());
        }
    }

    public HashMap<Long, ArrayList<Concept>> getConceptsInClusterSet(TribalAbstractionNetwork tan, ArrayList<ClusterSummary> clusters) {
        HashMap<Long, ArrayList<Concept>> clusterSetConcepts = new HashMap<Long, ArrayList<Concept>>();

        for (ClusterSummary cluster : clusters) {
            clusterSetConcepts.put(cluster.getRoot().getId(), getConceptsInCluster(tan, cluster));
        }

        return clusterSetConcepts;
    }

    public ArrayList<SearchResult> searchExact(String term) {
        
        term = term.toLowerCase();
        
        if (term.length() < 3) {
            return new ArrayList<SearchResult>();
        }

        char firstChar = Character.toLowerCase(term.charAt(0));

        ArrayList<SearchResult> results = new ArrayList<SearchResult>();

        int startIndex;

        if (firstChar < 'a') {
            startIndex = 0;
        } else if (firstChar > 'z') {
            startIndex = startingIndex.get('z');
        } else {
            startIndex = startingIndex.get(firstChar);
        }
                
        // TODO: Replace with binary search...
        
        boolean withinIndexBounds = (firstChar >= 'a' && firstChar <= 'z');

        for (int c = startIndex; c < descriptions.size(); c++) {
            DescriptionEntry entry = descriptions.get(c);
            
            char descFirstChar = Character.toLowerCase(entry.description.getTerm().charAt(0));

            if (withinIndexBounds) {
                if (descFirstChar == firstChar) {
                    if(entry.description.getTerm().equalsIgnoreCase(term)) {
                        results.add(new SearchResult(
                            entry.description.getTerm(), 
                            entry.concept.getName(), 
                            entry.concept.getId())
                        );
                    }
                }
            } else {
                if(firstChar < 'a') {
                    if(descFirstChar == 'a') {
                        break;
                    }
                }
            }
        }
        
        return results;
    }

    public ArrayList<SearchResult> searchStarting(String term) {
        term = term.toLowerCase();
        
        if (term.length() < 3) {
            return new ArrayList<SearchResult>();
        }

        char firstChar = Character.toLowerCase(term.charAt(0));

        ArrayList<SearchResult> results = new ArrayList<SearchResult>();

        int startIndex;

        if (firstChar < 'a') {
            startIndex = 0;
        } else if (firstChar > 'z') {
            startIndex = startingIndex.get('z');
        } else {
            startIndex = startingIndex.get(firstChar);
        }

        for (int c = startIndex; c < descriptions.size(); c++) {
            DescriptionEntry entry = descriptions.get(c);
            
            char descFirstChar = Character.toLowerCase(entry.description.getTerm().charAt(0));

            if (firstChar >= 'a' && firstChar <= 'z') {
                if (descFirstChar == firstChar) {
                    if(entry.description.getTerm().toLowerCase().startsWith(term)) {
                        results.add(new SearchResult(
                            entry.description.getTerm(), 
                            entry.concept.getName(), 
                            entry.concept.getId())
                        );
                    }
                } else {
                    break;
                }
            } else {
                if(firstChar < 'a') {
                    if(descFirstChar == 'a') {
                        break;
                    }
                }
            }
        }
        
        return results;
    }

    public ArrayList<SearchResult> searchAnywhere(String term) {

        term = term.toLowerCase();

        ArrayList<SearchResult> results = new ArrayList<SearchResult>();

        for (DescriptionEntry entry : descriptions) {
            if (entry.description.getTerm().toLowerCase().contains(term)) {
                results.add(new SearchResult(entry.description.getTerm(), entry.concept.getName(), entry.concept.getId()));
            }
        }

        return results;
    }

    public HashMap<Long, String> getUniqueLateralRelationshipsInHierarchy(PAreaTaxonomy taxonomy, Concept hierarchyRoot) {
        throw new RuntimeException("Method not yet supported...");
    }

    public int getConceptCountInPAreaHierarchy(PAreaTaxonomy taxonomy, ArrayList<PAreaSummary> pareas) {
        HashSet<Concept> concepts = new HashSet<Concept>();

        for (PAreaSummary parea : pareas) {
            LocalPArea localPArea = (LocalPArea) parea;
            concepts.addAll(localPArea.getConceptsInPArea());
        }

        return concepts.size();
    }

    public int getConceptCountInClusterHierarchy(TribalAbstractionNetwork tan, ArrayList<ClusterSummary> clusters) {
        HashSet<Concept> concepts = new HashSet<Concept>();

        for (ClusterSummary cluster : clusters) {
            LocalCluster localCluster = (LocalCluster) cluster;
            concepts.addAll(localCluster.getConcepts());
        }

        return concepts.size();
    }

    public ArrayList<ConceptPAreaInfo> getConceptPAreaInfo(PAreaTaxonomy taxonomy, Concept c) {
        Collection<PAreaSummary> pareas = taxonomy.getPAreas().values();
        
        ArrayList<ConceptPAreaInfo> pareasWithConcept = new ArrayList<ConceptPAreaInfo>();
        
        for(PAreaSummary parea : pareas) {
            LocalPArea localPArea = (LocalPArea)parea;
            
            if(localPArea.getConceptsInPArea().contains(c)) {
                pareasWithConcept.add(new ConceptPAreaInfo(taxonomy.getSNOMEDHierarchyRoot().getId(), 
                        parea.getRoot().getId()));
            }
        }
        
        return pareasWithConcept;
    }
    
    public ArrayList<ConceptClusterInfo> getConceptClusterInfo(TribalAbstractionNetwork tan, Concept c) {
        ArrayList<ConceptClusterInfo> clustersWithConcept = new ArrayList<ConceptClusterInfo>();
        
        Collection<ClusterSummary> clusters = tan.getClusters().values();
        
        for(ClusterSummary cluster : clusters) {
            LocalCluster localCluster = (LocalCluster)cluster;
            
            if(localCluster.getConcepts().contains(c)) {
                clustersWithConcept.add(new ConceptClusterInfo(tan.getSNOMEDHierarchyRoot().getId(), localCluster.getRoot().getId()));
            }
        }
        
        return clustersWithConcept;
    }

    public ArrayList<GroupParentInfo> getPAreaParentInfo(PAreaTaxonomy taxonomy, PAreaSummary parea) {
        if (parea instanceof LocalPArea) {
            LocalPArea localPArea = (LocalPArea) parea;

            ArrayList<GroupParentInfo> parentInfo = localPArea.getParentGroupInformation();

            Collections.sort(parentInfo, new Comparator<GroupParentInfo>() {
                public int compare(GroupParentInfo a, GroupParentInfo b) {
                    if (a.getParentPAreaRootId() == b.getParentPAreaRootId()) {
                        return a.getParentConcept().getName().compareToIgnoreCase(b.getParentConcept().getName());
                    } else {
                        Long aId = a.getParentPAreaRootId();
                        Long bId = b.getParentPAreaRootId();

                        return aId.compareTo(bId);
                    }
                }
            });

            return parentInfo;
        } else {
            throw new RuntimeException("Unsupported PArea Type: " + parea.getClass());
        }
    }
    

    public ArrayList<GroupParentInfo> getClusterParentInfo(TribalAbstractionNetwork tan, ClusterSummary cluster) {
        if (cluster instanceof LocalCluster) {
            LocalCluster localCluster = (LocalCluster) cluster;

            ArrayList<GroupParentInfo> parentInfo = localCluster.getParentGroupInformation();

            Collections.sort(parentInfo, new Comparator<GroupParentInfo>() {
                public int compare(GroupParentInfo a, GroupParentInfo b) {
                    if (a.getParentPAreaRootId() == b.getParentPAreaRootId()) {
                        return a.getParentConcept().getName().compareToIgnoreCase(b.getParentConcept().getName());
                    } else {
                        Long aId = a.getParentPAreaRootId();
                        Long bId = b.getParentPAreaRootId();

                        return aId.compareTo(bId);
                    }
                }
            });

            return parentInfo;
        } else {
            throw new RuntimeException("Unsupported Cluster Type: " + cluster.getClass());
        }
    }

    public SCTConceptHierarchy getPAreaConceptHierarchy(PAreaTaxonomy taxonomy, PAreaSummary parea) {
        if (parea instanceof LocalPArea) {
            LocalPArea localPArea = (LocalPArea) parea;

            return localPArea.getConceptHierarchy();
        } else {
            throw new RuntimeException("Unsupported PArea Type: " + parea.getClass());
        }
    }

    public SCTConceptHierarchy getClusterConceptHierarchy(TribalAbstractionNetwork tan, ClusterSummary cluster) {
        if (cluster instanceof LocalCluster) {
            LocalCluster localCluster = (LocalCluster) cluster;

            return localCluster.getConceptHierarchy();
        } else {
            throw new RuntimeException("Unsupported Cluster Type: " + cluster.getClass());
        }
    }

    public SCTMultiRootedConceptHierarchy getRegionConceptHierarchy(PAreaTaxonomy taxonomy, 
            ArrayList<PAreaSummary> pareas) {

        HashSet<Concept> roots = new HashSet<Concept>();

        for (PAreaSummary parea : pareas) {
            roots.add(parea.getRoot());
        }

        SCTMultiRootedConceptHierarchy hierarchy = new SCTMultiRootedConceptHierarchy(roots);

        for (PAreaSummary parea : pareas) {
            LocalPArea localPArea = (LocalPArea) parea;

            SCTConceptHierarchy pareaHierarchy = localPArea.getConceptHierarchy();
            
            hierarchy.addHierarchy(pareaHierarchy);
        }

        return hierarchy;
    }
    
    private ArrayList<SearchResult> searchWithinUniqueConceptList(HashSet<Concept> uniqueConcepts, String term) {
        ArrayList<SearchResult> results = new ArrayList<SearchResult>();

        for (Concept concept : uniqueConcepts) {
            LocalSnomedConcept localConcept = (LocalSnomedConcept) concept;

            for (Description d : localConcept.getDescriptions()) {
                if (d.getTerm().toLowerCase().contains(term)) {
                    results.add(new SearchResult(d.getTerm(), concept.getName(), concept.getId()));
                }
            }
        }
        
        return results;
    }
    
    public ArrayList<SearchResult> searchForConceptsWithinTaxonomy(PAreaTaxonomy taxonomy,
            ArrayList<PAreaSummary> pareas, String term) {
        
        term = term.toLowerCase();

        if (term.length() < 3) {
            return new ArrayList<SearchResult>();
        }
        
        HashSet<Concept> uniqueConcepts = new HashSet<Concept>();
        
        for(PAreaSummary parea : pareas) {
            LocalPArea localPArea = (LocalPArea)parea;
            uniqueConcepts.addAll(localPArea.getConceptsInPArea());
        }
        
        ArrayList<SearchResult> results = searchWithinUniqueConceptList(uniqueConcepts, term);
        
        Collections.sort(results, new SearchResultComparator());
        
        return results;
    }
    
    public ArrayList<SearchResult> searchForConceptsWithinTAN(TribalAbstractionNetwork tan, 
            ArrayList<ClusterSummary> clusters, String term) {
        
        term = term.toLowerCase();

        if (term.length() < 3) {
            return new ArrayList<SearchResult>();
        }
        
        HashSet<Concept> uniqueConcepts = new HashSet<Concept>();
        
        for(ClusterSummary cluster : clusters) {
            LocalCluster localCluster = (LocalCluster)cluster;
            uniqueConcepts.addAll(localCluster.getConcepts());
        }
        
        ArrayList<SearchResult> results = searchWithinUniqueConceptList(uniqueConcepts, term);
        
        Collections.sort(results, new SearchResultComparator());
        
        return results;
    }
    
    /**
     * Returns which top-level hierarchies a given concepts belongs to. 
     * In almost all cases there will only be one, but there is a chance
     * that a concept belongs to two or more.
     * @param c
     * @return 
     */
    public ArrayList<Concept> getHierarchiesConceptBelongTo(Concept c) {
        ArrayList<Concept> roots = this.getHierarchyRootConcepts();
        
        ArrayList<Concept> conceptHierarchies = new ArrayList<Concept>();
        
        for(Concept root : roots) {
            Stack<Concept> stack = new Stack<Concept>();
            stack.add(root);
            
            HashSet<Concept> processedConcepts = new HashSet<Concept>();
            processedConcepts.add(root);
            
            while(!stack.isEmpty()) {
                Concept concept = stack.pop();
                
                HashSet<Concept> children = conceptHierarchy.getChildren(concept);
                
                if(concept.equals(c)) {
                    conceptHierarchies.add(root);
                    break;
                }
                
                for(Concept child : children) {
                    if(!processedConcepts.contains(child)) {
                        processedConcepts.add(child);
                        stack.add(child);
                    }
                }
            }
        }
        
        return conceptHierarchies;
    }
    
    /**
     * Returns the hierarchy of ancestors (up to the root of the hierarch(ies)) 
     * @param hierarchyRoot
     * @param c
     * @return 
     */
    public SCTConceptHierarchy getAncestorHierarchy(Concept hierarchyRoot, Concept c) {
        SCTConceptHierarchy hierarchy = new SCTConceptHierarchy(hierarchyRoot);
        
        Queue<Concept> queue = new ArrayDeque<Concept>();
        
        HashSet<Concept> processed = new HashSet<Concept>();
        processed.add(c);
        
        queue.add(c);
        
        while(!queue.isEmpty()) {
            Concept concept = queue.remove();
            
            HashSet<Concept> parents = conceptHierarchy.getParents(concept);
            
            for(Concept parent : parents) {
                if(!processed.contains(parent)) {
                    processed.add(parent);
                    
                    if(!parent.equals(hierarchyRoot)) {
                        queue.add(parent);
                    }
                }
                
                hierarchy.addIsA(concept, parent);
            }
        }

        return hierarchy;
    }
    
    public ArrayList<Concept> getAllAncestorsAsList(Concept c) {

        ArrayList<Concept> hierarchies = this.getHierarchiesConceptBelongTo(c);
        
        HashSet<Concept> ancestorSet = new HashSet<Concept>();
        
        for(Concept hierarchy : hierarchies) {
            ancestorSet.addAll(this.getAncestorHierarchy(hierarchy, c).getConceptsInHierarchy());
        }

        ancestorSet.remove(c);

        ArrayList<Concept> ancestorList = new ArrayList<Concept>(ancestorSet);

        Collections.sort(ancestorList, new ConceptNameComparator());

        return ancestorList;
    }
    
    /**
     * Returns all of the descendents of the given concept
     * @param c
     * @return 
     */
    public ArrayList<Concept> getAllDescendantsAsList(Concept c) {
        SCTConceptHierarchy hierarchy = (SCTConceptHierarchy)conceptHierarchy.getSubhierarchyRootedAt(c);
        
        HashSet<Concept> descendantSet = hierarchy.getConceptsInHierarchy();
        descendantSet.remove(c);
        
        ArrayList<Concept> descendantList = new ArrayList<Concept>(descendantSet);
        
        Collections.sort(descendantList, new ConceptNameComparator());
        
        return descendantList;
    }
    
    /**
     * Returns various metrics about the given concept. 
     * @param c
     * @return 
     */
    public HierarchyMetrics getHierarchyMetrics(Concept c) {
        ArrayList<Concept> hierarchies = this.getHierarchiesConceptBelongTo(c);
        
        Collections.sort(hierarchies, new ConceptNameComparator());
        
        int parentCount = this.getConceptParents(c).size();
        int childCount = this.getConceptChildren(c).size();
        int siblingCount = this.getConceptSiblings(c).size();
        
        int descendantCount = this.conceptHierarchy.getSubhierarchyRootedAt(c).getNodesInHierarchy().size() - 1;
        
        if(descendantCount < 0) {
            descendantCount = 0;
        }
        
        int ancestorCount = 0;
        
        for(Concept hierarchy : hierarchies) {
            ancestorCount += this.getAncestorHierarchy(hierarchy, c).getConceptsInHierarchy().size() - 1;
        }
        
        return new HierarchyMetrics(c, hierarchies, ancestorCount, descendantCount, parentCount, childCount, siblingCount);
    }
    
    
    public ArrayList<ArrayList<Concept>> getAllPathsToConcept(Concept c) {
        
        ArrayList<ArrayList<Concept>> paths = new ArrayList<ArrayList<Concept>>();
        
        ArrayList<Concept> hierarchies = this.getHierarchiesConceptBelongTo(c);
        
        for(Concept hierarchy : hierarchies) {
            ArrayList<ArrayList<Concept>> hierarchyPaths = getAllPathsFromHierarchyRoot(hierarchy, c);
            
            for(ArrayList<Concept> path : hierarchyPaths) {
               paths.add(path);
            }
        }
        
        return paths;
    }
    
    private ArrayList<ArrayList<Concept>> getAllPathsFromHierarchyRoot(Concept hierarchyRoot, Concept c) {
        SCTConceptHierarchy hierarchy = (SCTConceptHierarchy)this.getAncestorHierarchy(hierarchyRoot, c);
        
        HashMap<Concept, Integer> parentCounts = new HashMap<Concept, Integer>();
        
        parentCounts.put(hierarchyRoot, 0);
        
        HashSet<Concept> hierarchyConcepts = hierarchy.getConceptsInHierarchy();
        
        for(Concept concept : hierarchyConcepts) {
            if(!concept.equals(hierarchyRoot)) {
                parentCounts.put(concept, hierarchy.getParents(concept).size());
            }
        }
        
        HashMap<Concept, ArrayList<ArrayList<Concept>>> paths = new HashMap<Concept, ArrayList<ArrayList<Concept>>>();
        
        Stack<Concept> pendingConcepts = new Stack<Concept>();
        pendingConcepts.add(hierarchyRoot);
        
        ArrayList<ArrayList<Concept>> rootPath = new ArrayList<ArrayList<Concept>>();
        rootPath.add(new ArrayList<Concept>(Arrays.asList(hierarchyRoot)));
        
        paths.put(hierarchyRoot, rootPath);
        
        while(!pendingConcepts.isEmpty()) {
            Concept concept = pendingConcepts.pop();
            
            if(!concept.equals(hierarchyRoot)) {
                HashSet<Concept> parents = hierarchy.getParents(concept);
                
                ArrayList<ArrayList<Concept>> pathsToConcept = new ArrayList<ArrayList<Concept>>();
                
                for (Concept parent : parents) {
                    ArrayList<ArrayList<Concept>> parentPaths = (ArrayList<ArrayList<Concept>>)paths.get(parent).clone();
                    
                    for(ArrayList<Concept> parentPath : parentPaths) {
                        ArrayList<Concept> path = (ArrayList<Concept>)parentPath.clone();
                        path.add(concept);
                        
                        pathsToConcept.add(path);
                    } 
                }
                
                if (concept.equals(c)) {
                    return pathsToConcept;
                } else {
                    paths.put(concept, pathsToConcept);
                }
            }

            HashSet<Concept> children = hierarchy.getChildren(concept);

            for (Concept child : children) {
                int childParentCount = parentCounts.get(child) - 1;

                if (childParentCount == 0) {
                    pendingConcepts.add(child);
                } else {
                    parentCounts.put(child, childParentCount);
                }
            }
        }
        
        return new ArrayList<ArrayList<Concept>>();
    }
            
            
    public boolean supportsMultipleVersions() {
        return false;
    }
    
    public ArrayList<String> getSupportedVersions() {
        return new ArrayList<String>(Arrays.asList(version));
    }
    
    public void setVersion(String version) {
        // Do nothin', 'cause LocalDataSource only supports one version at a time right now
    }
    
    public String getSelectedVersion() {
        return version;
    }
}
