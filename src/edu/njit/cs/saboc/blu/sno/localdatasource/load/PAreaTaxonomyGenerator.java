package edu.njit.cs.saboc.blu.sno.localdatasource.load;

import SnomedShared.Concept;
import SnomedShared.pareataxonomy.Area;
import SnomedShared.pareataxonomy.GroupParentInfo;
import SnomedShared.pareataxonomy.InheritedRelationship;
import SnomedShared.pareataxonomy.PAreaSummary;
import edu.njit.cs.saboc.blu.core.gui.dialogs.AbNLoadingStatusMonitor;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.LocalPArea;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.LocalPAreaTaxonomy;
import edu.njit.cs.saboc.blu.sno.datastructure.hierarchy.SCTConceptHierarchy;
import edu.njit.cs.saboc.blu.sno.localdatasource.concept.LocalLateralRelationship;
import edu.njit.cs.saboc.blu.sno.localdatasource.concept.LocalSnomedConcept;
import edu.njit.cs.saboc.blu.sno.sctdatasource.SCTLocalDataSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Stack;

/**
 *
 * @author Chris
 */

public class PAreaTaxonomyGenerator {
    
    private AbNLoadingStatusMonitor loadingMonitor = new AbNLoadingStatusMonitor();
    
    public AbNLoadingStatusMonitor getLoadStatusMonitor() {
        return loadingMonitor;
    }
    
    /**
     * A set of PAreas
     */
    private class PAreaSet extends HashSet<LocalPArea> {}

    public LocalPAreaTaxonomy createPAreaTaxonomy(Concept hierarchyRoot, SCTLocalDataSource dataSource, 
            ConceptRelationshipsRetriever conceptRelsRetriever) {
        
        SCTConceptHierarchy hierarchy = conceptRelsRetriever.getConceptHierarchy(dataSource, hierarchyRoot);
        
        return createPAreaTaxonomyForHierarchy(hierarchy, dataSource, conceptRelsRetriever);
    }
        
    public LocalPAreaTaxonomy createPAreaTaxonomyForHierarchy(SCTConceptHierarchy hierarchy, SCTLocalDataSource dataSource, 
            ConceptRelationshipsRetriever conceptRelsRetriever) {
        
        HashSet<Concept> hierarchyConcepts = hierarchy.getConceptsInHierarchy();
                
        Concept hierarchyRoot = hierarchy.getRoot();
        
        ArrayList<LocalPArea> pareas = new ArrayList<LocalPArea>();
        
        // The partial-areas for the hierarchy
        HashMap<Integer, PAreaSummary> partialAreas = new HashMap<Integer, PAreaSummary>();
        
        //Pulls in the local data from the LocalMiddlewareAccessorProxy
        HashMap<Integer, HashSet<Integer>> pareaHierarchy  = new HashMap<Integer, HashSet<Integer>>();
        
        // The set of partial-areas a given concept belongs to
        HashMap<Concept, PAreaSet> pareaSets = new HashMap<Concept, PAreaSet>();
        
        HashMap<Concept, HashSet<Long>> definingAttributeRels = conceptRelsRetriever.getDefiningRelationships(hierarchy);
        
        HashMap<Long, String> relHashMap = new HashMap<Long, String>();

        HashMap<Concept, Integer> remainingParentCount = new HashMap<Concept, Integer>();
        
        loadingMonitor.updateState(AbNLoadingStatusMonitor.STATE_INIT_DATA_STRUCTURES, 1);
        
        for (Concept concept : hierarchyConcepts) {
            pareaSets.put(concept, new PAreaSet());

            LocalSnomedConcept localConcept = (LocalSnomedConcept) concept;
            
            for (LocalLateralRelationship lr : localConcept.getAttributeRelationships()) {
                if (lr.getCharacteristicType() == 0) {
                    relHashMap.put(lr.getRelationship().getId(), lr.getRelationship().getName());
                }
            }
            
            if(concept.equals(hierarchyRoot)) {
                remainingParentCount.put(concept, 0);
            } else {
                remainingParentCount.put(concept, hierarchy.getParents(concept).size());
            }
        }
        
        loadingMonitor.updateState(AbNLoadingStatusMonitor.STATE_BUILD_PAREAS, 10);
        
        int nextPAreaId = 0;
        
        //create a localparea constructor with root and ID, use set functions for the rest
        //pass in empty collections/structures for the other fields
        LocalPArea rootPArea = new LocalPArea(nextPAreaId++, hierarchyRoot);
        rootPArea.setRelationships(new ArrayList<InheritedRelationship>());
        partialAreas.put(rootPArea.getId(), rootPArea);

        pareas.add(rootPArea);

        pareaSets.get(hierarchyRoot).add(rootPArea);
        
        Stack<Concept> pendingConcepts = new Stack<Concept>();
        pendingConcepts.add(hierarchyRoot);
        
        int processedConcepts = 1;
        
        while (!pendingConcepts.isEmpty()) {
            Concept concept = pendingConcepts.pop();
            
            processedConcepts++;
            
            loadingMonitor.updatePercent(10 + (int)(30 * ((double)processedConcepts / (double)hierarchyConcepts.size())));

            HashSet<Long> conceptRels = definingAttributeRels.get(concept);     

            if (!concept.equals(hierarchyRoot)) {
                HashSet<Concept> parents = hierarchy.getParents(concept);

                for (Concept parent : parents) {
                    if (!hierarchyConcepts.contains(parent)) {
                        continue;
                    }

                    HashSet<Long> parentRels = definingAttributeRels.get(parent);

                    // Check if it has the same relationship set as its parents
                    if (relationshipSetsEqual(conceptRels, parentRels)) {
                        PAreaSet conceptPAreas = pareaSets.get(concept);
                        PAreaSet parentPAreas = pareaSets.get(parent);

                        conceptPAreas.addAll(parentPAreas);

                        for (LocalPArea parentPArea : parentPAreas) {
                            parentPArea.getConceptHierarchy().addIsA(concept, parent);
                        }
                    }
                }

                // If the concept belongs to no pareas, make a new one
                if (pareaSets.get(concept).isEmpty()) {
                    LocalPArea newPArea = new LocalPArea(nextPAreaId++, concept);
                    partialAreas.put(newPArea.getId(), newPArea);

                    ArrayList<Long> allParentRels = new ArrayList<Long>();

                    for (Concept parent : parents) {
                        HashSet<Long> parentRels = definingAttributeRels.get(parent);

                        for (long rel : parentRels) {
                            if (!allParentRels.contains(rel)) {
                                allParentRels.add(rel);
                            }
                        }
                    }

                    HashSet<Long> rootRels = definingAttributeRels.get(concept);

                    ArrayList<InheritedRelationship> relationships = new ArrayList<InheritedRelationship>();

                    // Find if the new PArea's relationships are introduced or inherited from
                    // their parent
                    for (long relId : rootRels) {
                        if (allParentRels.contains(relId)) {
                            relationships.add(new InheritedRelationship(InheritedRelationship.InheritanceType.INHERITED, relId));
                        } else {
                            relationships.add(new InheritedRelationship(InheritedRelationship.InheritanceType.INTRODUCED, relId));
                        }
                    }

                    newPArea.setRelationships(relationships);

                    pareas.add(newPArea);
                    pareaSets.get(concept).add(newPArea);
                }
            }

            HashSet<Concept> children = hierarchy.getChildren(concept);

            for(Concept child : children) {
                int parentCount = remainingParentCount.get(child) - 1;

                if(parentCount == 0) {
                    pendingConcepts.push(child);
                } else {
                    remainingParentCount.put(child, parentCount);
                }
            }
        }
        
        int processedPAreas = 0;

        // Link the pareas
        for (LocalPArea p : pareas) {
            
            processedPAreas++;
            
            loadingMonitor.updatePercent(40 + (int)(10 * ((double)processedPAreas / (double)pareas.size())));
            
            Concept pareaRoot = p.getRoot();

            HashSet<Concept> parents = hierarchy.getParents(pareaRoot);
            
            ArrayList<Integer> parentPAreaIds = new ArrayList<Integer>();
            
            ArrayList<GroupParentInfo> parentGroupInfo = new ArrayList<GroupParentInfo>();

            // For all of the parents of the root of this parea
            for (Concept parent : parents) {
                if (parent.getId() == 138875005l) { // Don't link to SNOMED CT Concept
                    continue;
                }

                // Add the parea it belongs to as a parent parea of the current parea
                for (LocalPArea parentArea : pareaSets.get(parent)) {
                    if (!parentPAreaIds.contains(parentArea.getId())) {
                        parentPAreaIds.add(parentArea.getId());
                    }
                }
                
                // AssistMap is a 1:1 mapping of pareas to their rootID, allowing to search in linear time
                // to locate a parea only via its Long root pid

                PAreaSet parentPAreas = pareaSets.get(parent);
                
                for (LocalPArea parentPArea : parentPAreas) {
                    
                    parentGroupInfo.add(new GroupParentInfo(parent, parentPArea.getRoot().getId()));
                    
                    int parentId = parentPArea.getId();

                    if (!pareaHierarchy.containsKey(parentId)) {
                        pareaHierarchy.put(parentId, new HashSet<Integer>());
                    } else {
                        pareaHierarchy.get(parentId).add(p.getId());
                    }
                }
            }
            
            p.setParents(parentPAreaIds);
            p.setParentGroupInformation(parentGroupInfo);
        }

        ArrayList<Area> areas = new ArrayList<Area>();
        
        loadingMonitor.updateState(AbNLoadingStatusMonitor.STATE_BUILD_AREAS, 50);
        
        processedPAreas = 0;

        // TODO: Create a map that stores the rel set of each area. Then finding correct area would be O(1).
        for (LocalPArea parea : pareas) {
            
            processedPAreas++;
            
            loadingMonitor.updatePercent(50 + (int)(30 * ((double)processedPAreas / (double)pareas.size())));
            
            boolean areaFound = false;
            HashSet<Long> pareaType = definingAttributeRels.get(parea.getRoot());

            // Check if an area with the same relationship set exists
            for (Area area : areas) {
                // If it does add this parea to it
                if (relationshipSetsEqual(pareaType, new HashSet<Long>(area.getRelationships()))) {
                    area.addPArea(parea);
                    areaFound = true;
                    break;
                }
            }

            // Otherwise create a new area and add this parea to it
            if (!areaFound) {
                Area newArea = new Area(areas.size(), false);
                newArea.setRels(new ArrayList<Long>(definingAttributeRels.get(parea.getRoot())));
                newArea.addPArea(parea);
                areas.add(newArea);
            }
        }
        
        return new LocalPAreaTaxonomy(
                hierarchy,
                rootPArea, 
                areas, 
                partialAreas, 
                pareaHierarchy, 
                relHashMap, 
                dataSource.getSelectedVersion(),
                dataSource);
    }
    
    public LocalPAreaTaxonomy createFocusTaxonomy(Concept c, SCTLocalDataSource dataSource, 
            ConceptRelationshipsRetriever conceptRelsRetriever) {
        
        ArrayList<Concept> hierarchies = dataSource.getHierarchiesConceptBelongTo(c);
        
        SCTConceptHierarchy conceptHierarchy = new SCTConceptHierarchy(hierarchies.get(0)); // Multi rooted partial-area taxonomy needs research...
        
        SCTConceptHierarchy descdendants = (SCTConceptHierarchy) dataSource.getConceptHierarchy().getSubhierarchyRootedAt(c);
        
        conceptHierarchy.addAllHierarchicalRelationships(descdendants);
        
        for(Concept hierarchy : hierarchies) {
            SCTConceptHierarchy ancestors = dataSource.getAncestorHierarchy(dataSource.getConceptHierarchy(), hierarchy, c);
            conceptHierarchy.addAllHierarchicalRelationships(ancestors);
        }

        return createPAreaTaxonomyForHierarchy(conceptHierarchy, dataSource, conceptRelsRetriever);
    }

    private static boolean relationshipSetsEqual(HashSet<Long> c, HashSet<Long> p) {
        return c.equals(p);
    }
}

