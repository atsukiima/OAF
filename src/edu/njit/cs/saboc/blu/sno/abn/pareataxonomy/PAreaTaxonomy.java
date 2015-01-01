package edu.njit.cs.saboc.blu.sno.abn.pareataxonomy;

import SnomedShared.pareataxonomy.Area;
import SnomedShared.Concept;
import SnomedShared.generic.GenericConceptGroup;
import SnomedShared.pareataxonomy.InheritedRelationship;
import SnomedShared.pareataxonomy.PAreaSummary;
import SnomedShared.pareataxonomy.Region;
import edu.njit.cs.saboc.blu.sno.abn.SCTAbstractionNetwork;
import edu.njit.cs.saboc.blu.sno.localdatasource.load.InferredRelationshipsRetriever;
import edu.njit.cs.saboc.blu.sno.abn.generator.PAreaTaxonomyGenerator;
import edu.njit.cs.saboc.blu.sno.sctdatasource.SCTDataSource;
import edu.njit.cs.saboc.blu.sno.sctdatasource.SCTLocalDataSource;
import edu.njit.cs.saboc.blu.sno.sctdatasource.middlewareproxy.MiddlewareAccessorProxy;
import java.util.ArrayDeque;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Queue;


/**
 * Represents a partial-area taxonomy for SNOMED CT
 * @author Chris
 */
public class PAreaTaxonomy extends SCTAbstractionNetwork {

    /**
     * The root partial-area
     */
    protected PAreaSummary rootPArea;

    /**
     * The set of unique lateral relationships in the hierarchy, 
     * along with their abbreviation, or full name. Note that
     * this may contain a subset of relationships in a root subtaxonomy.
     */ 
    private HashMap<Long, String> lateralRels;

    public PAreaTaxonomy(
            Concept SNOMEDHierarchyRoot,
            PAreaSummary rootPArea,
            ArrayList<Area> areas,
            HashMap<Integer, PAreaSummary> pareas,
            HashMap<Integer, HashSet<Integer>> pareaHierarchy,
            HashMap<Long, String> lateralRels,
            String version,
            SCTDataSource dataSource) {

        super(SNOMEDHierarchyRoot, areas, pareas, pareaHierarchy, version, dataSource);

        this.lateralRels = lateralRels;
        this.SNOMEDHierarchyRoot = SNOMEDHierarchyRoot;
        this.rootPArea = rootPArea;
    }

    public PAreaSummary getRootPArea() {
        return rootPArea;
    }
    
    public GenericConceptGroup getRootGroup() {
        return getRootPArea();
    }

    public int getPAreaCount() {
        return getGroupCount();
    }

    public int getAreaCount() {
        return getContainerCount();
    }

    public ArrayList<Area> getHierarchyAreas() {
        return (ArrayList<Area>)containers;
    }

    /**
     * Returns the set of areas which were explicitly chosen as
     * belonging to a relationship subtaxonomy
     * @return 
     */
    public ArrayList<Area> getExplicitHierarchyAreas() {
        ArrayList<Area> explicitAreas = new ArrayList<Area>();

        for(Area a : (ArrayList<Area>)containers) {
            if(!a.isImplicitArea()) {
                explicitAreas.add(a);
            }
        }

        return explicitAreas;
    }

    /**
     * Returns the set of areas which were implicitly added to complete the
     * relationship subtaxonomy when there are relationships that have
     * refined relationships in a hierarchy
     * @return 
     */
    public ArrayList<Area> getImplicitHierarchyAreas() {
        ArrayList<Area> implicitAreas = new ArrayList<Area>();

        for (Area a : (ArrayList<Area>)containers) {
            if (a.isImplicitArea()) {
                implicitAreas.add(a);
            }
        }

        return implicitAreas;
    }

    /**
     * Returns the set of unique lateral relationships in the hierarchy
     * @return 
     */
    public HashMap<Long, String> getLateralRelsInHierarchy() {
        return lateralRels;
    }

    public final HashMap<Integer, PAreaSummary> getPAreas() {
        return (HashMap<Integer, PAreaSummary>)groups;
    }
    
    public final HashMap<Integer, HashSet<Integer>> getPAreaHierarchy() {
        return groupHierarchy;
    }

    public final HashSet<Integer> getPAreaChildren(int pid) {
        return getGroupChildren(pid);
    }

    public PAreaSummary getPAreaFromRootConceptId(long rootConceptId) {
        return (PAreaSummary)getGroupFromRootConceptId(rootConceptId);
    }
    
    protected PAreaSummary createRootSubtaxonomyPArea(PAreaSummary originalPArea, HashSet<Integer> subtaxonomyParents) {
        return new PAreaSummary(originalPArea.getId(), originalPArea.getRoot(),
                    originalPArea.getConceptCount(), subtaxonomyParents);
    }
    
    public ArrayList<Area> searchAreas(String term) {
        ArrayList<Area> searchResults = new ArrayList<Area>();

        ArrayList<Area> areas = getHierarchyAreas();

        String [] searchedRels = term.split(", ");

        if(searchedRels == null) {
            return new ArrayList<Area>();
        }

        for(Area a : areas) {
            ArrayList<String> relsInArea = new ArrayList<String>();

            for(long rel : a.getRelationships()) {
                relsInArea.add(getLateralRelsInHierarchy().get(rel));
            }

            boolean allRelsFound = true;

            for(String rel : searchedRels) {

                boolean relFound = false;

                for(String areaRel : relsInArea) {
                    if(areaRel.toLowerCase().contains(rel)) {
                        relFound = true;
                        break;
                    }
                }

                if(!relFound) {
                    allRelsFound = false;
                    break;
                }
            }

            if(allRelsFound) {
                searchResults.add(a);
            }
        }

        return searchResults;
    }

    /**
     * Creates a root subtaxonomy rooted at the given partial-area
     * @param subhierarchyRootPArea
     * @return 
     */
    public PAreaTaxonomy getRootSubtaxonomy(PAreaSummary subhierarchyRootPArea) {
        ArrayList<Area> hierarchyAreas = new ArrayList<Area>();

        if (!groups.containsKey(subhierarchyRootPArea.getId())) {
            return null;
        }

        ArrayList<Integer> pareaIdsInSubhierarchy = new ArrayList<Integer>();

        HashMap<Integer, ArrayList<Long>> pareaRels = new HashMap<Integer, ArrayList<Long>>();
        HashMap<Integer, HashSet<Integer>> pareaSubhierarchy = new HashMap<Integer, HashSet<Integer>>();

        Queue<Integer> queue = new ArrayDeque<Integer>();

        queue.add(subhierarchyRootPArea.getId());
        
        HashMap<Integer, HashSet<Integer>> hierarchy = groupHierarchy;
        
        System.out.println(hierarchy);
        
        for(Entry<Integer, HashSet<Integer>> entry : hierarchy.entrySet()) {
            System.out.println(entry.getKey() + " | " + entry.getValue());
        }
        
        System.out.println();

        while (!queue.isEmpty()) {
            int pareaId = queue.remove();
            pareaIdsInSubhierarchy.add(pareaId);

            HashSet<Integer> children = groupHierarchy.get(pareaId);

            pareaSubhierarchy.put(pareaId, children);
            
            System.out.println(pareaId + " | " + children);

            if (children != null) {
                for (int i : children) {
                    if (!queue.contains(i)) {
                        queue.add(i);
                    }
                }
            }
        }
        
        ArrayList<PAreaSummary> pareasInSubhierarchy = new ArrayList<PAreaSummary>();

        for(int pareaId : pareaIdsInSubhierarchy) {
            PAreaSummary parea = (PAreaSummary)groups.get(pareaId);
            
            HashSet<Integer> parents = parea.getParentIds();
            HashSet<Integer> parentsInSubhierarchy = new HashSet<Integer>();
            
            for(int pid : parents) {
                if(pareaIdsInSubhierarchy.contains(pid)) {
                    parentsInSubhierarchy.add(pid);
                }
            }

            pareasInSubhierarchy.add(createRootSubtaxonomyPArea(parea, parentsInSubhierarchy));

            ArrayList<Long> pareaType = new ArrayList<Long>();
            ArrayList<InheritedRelationship> rels = parea.getRelationships();

            for (InheritedRelationship rel : rels) {
                pareaType.add(rel.getRelationshipTypeId());
            }

            pareaRels.put(pareaId, pareaType);
        }

        ArrayList<Long> lateralRelsInSubhierarchy = new ArrayList<Long>();

        for (PAreaSummary parea : pareasInSubhierarchy) {
            HashSet<Integer> parents = parea.getParentIds();
            ArrayList<Long> allParentRels = new ArrayList<Long>();

            if (parents == null) {
                continue;
            }

            for (int parent : parents) {
                PAreaSummary parentPArea = (PAreaSummary)groups.get(parent);
                ArrayList<Long> parentRels = pareaRels.get(parentPArea.getId());

                if (parentRels == null) {
                    continue;
                }

                for (long relId : parentRels) {
                    if (!allParentRels.contains(relId)) {
                        allParentRels.add(relId);
                    }
                }
            }

            ArrayList<Long> pareaLateralRels = pareaRels.get(parea.getId());

            for(Long rid : pareaLateralRels) {
                if(!lateralRelsInSubhierarchy.contains(rid)) {
                    lateralRelsInSubhierarchy.add(rid);
                }
            }

            ArrayList<InheritedRelationship> relationships = new ArrayList<InheritedRelationship>();

            if (lateralRels != null) {
                for (long relId : pareaLateralRels) {
                    if (allParentRels.contains(relId)) {
                        relationships.add(new InheritedRelationship(InheritedRelationship.InheritanceType.INHERITED,
                                relId));
                    } else {
                        relationships.add(new InheritedRelationship(InheritedRelationship.InheritanceType.INTRODUCED,
                                relId));
                    }
                }
            }

            parea.setRelationships(relationships);
        }

        HashMap<Integer, PAreaSummary> subhierarchyPAreas = new HashMap<Integer, PAreaSummary>();

        for (PAreaSummary parea : pareasInSubhierarchy) {
            boolean areaFound = false;

            subhierarchyPAreas.put(parea.getId(), parea);

            // Check if an area with the same relationship set exists
            for (Area area : hierarchyAreas) {
                // If it does add this parea to it
                if (relationshipSetsEqual(pareaRels.get(parea.getId()), area.getRelationships())) {
                    area.addPArea(parea);
                    areaFound = true;
                    break;
                }
            }

            // Otherwise create a new area and add this parea to it
            if (!areaFound) {
                Area newArea = new Area(hierarchyAreas.size(), false);
                newArea.setRels(pareaRels.get(parea.getId()));
                newArea.addPArea(parea);
                hierarchyAreas.add(newArea);
            }
        }

        HashMap<Long, String> lateralRelMap = new HashMap<Long, String>();

        for(Long rid : lateralRelsInSubhierarchy) {
            lateralRelMap.put(rid, lateralRels.get(rid));
        }

        for (Area a : hierarchyAreas) {
            for (Region region : a.getRegions()) {
                ArrayList<PAreaSummary> summaries = region.getPAreasInRegion();

                Collections.sort(summaries, new Comparator<PAreaSummary>() {

                    public int compare(PAreaSummary a, PAreaSummary b) {
                        Integer aCount = a.getConceptCount();
                        Integer bCount = b.getConceptCount();
                        
                        return aCount.compareTo(bCount);
                    }
                });

            }
        }
        
        PAreaTaxonomy topLevelTaxonomy;

        if(this instanceof RootSubtaxonomy) {
            topLevelTaxonomy = ((RootSubtaxonomy)this).getTopLevelTaxonomy();
        } else {
            topLevelTaxonomy = this;
        }
        
        PAreaRootSubtaxonomy data = new PAreaRootSubtaxonomy(SNOMEDHierarchyRoot, subhierarchyRootPArea, hierarchyAreas, subhierarchyPAreas,
                pareaSubhierarchy, lateralRelMap, SNOMEDVersion, dataSource, topLevelTaxonomy);
        
        return data;
    }

    /**
     * Creates a relationship subtaxonomy using the relatationships with the given
     * relationship ids
     * @param relationships
     * @return 
     */
    public PAreaTaxonomy getRelationshipSubtaxonomy(ArrayList<Long> relationships) {
        ArrayList<Area> subset = new ArrayList<Area>();

        for(Area a : (ArrayList<Area>)containers) {
            ArrayList<Long> areaRels = a.getRelationships();

            if(relationships.containsAll(areaRels)) {
                subset.add(a);
            }
        }

        HashMap<Integer, PAreaSummary> pareasInSubset = new HashMap<Integer,PAreaSummary>();

        for(Area a : subset) {
            ArrayList<PAreaSummary> pareasInArea = a.getAllPAreas();

            for(PAreaSummary parea : pareasInArea) {
                pareasInSubset.put(parea.getId(), parea);
            }
        }

        HashMap<Integer, HashSet<Integer>> pareaSubhierarchy = new HashMap<Integer, HashSet<Integer>>();

        for(Integer pid : pareasInSubset.keySet()) {
            HashSet<Integer> subhierarchyChildren = new HashSet<Integer>();
            
            pareaSubhierarchy.put(pid, subhierarchyChildren);

            HashSet<Integer> allChildren = groupHierarchy.get(pid);

            if (allChildren != null) {
                for (Integer cid : allChildren) {
                    if (pareasInSubset.containsKey(cid)) {
                        subhierarchyChildren.add(cid);
                    }
                }
            }
        }

        HashMap<Long, String> lateralRelMap = new HashMap<Long, String>();

        for (Area a : subset) {
            for (Region region : a.getRegions()) {
                ArrayList<PAreaSummary> summaries = region.getPAreasInRegion();

                Collections.sort(summaries, new Comparator<PAreaSummary>() {

                    public int compare(PAreaSummary a, PAreaSummary b) {
                        Integer aCount = a.getConceptCount();
                        Integer bCount = b.getConceptCount();
                        
                        return aCount.compareTo(bCount);
                    }
                });
            }
        }

        for(Area a : subset) {
            ArrayList<Long> areaRels = a.getRelationships();

            for(long rel : areaRels) {
                if(!lateralRelMap.containsKey(rel)) {
                    lateralRelMap.put(rel, lateralRels.get(rel));
                }
            }
        }
        
        PAreaTaxonomy hd = new PAreaTaxonomy(SNOMEDHierarchyRoot, rootPArea, subset, pareasInSubset,
                pareaSubhierarchy, lateralRelMap, SNOMEDVersion, dataSource);

        return hd;
    }

    /**
     * Returns a relationship subtaxonomy that includes areas that are made up of concepts with a
     * more general relationship then the chosen relationship for the subtaxonomy. 
     * 
     * For example, if a user chooses "Finding site - Direct" then the area "Finding site" will be
     * implicitly included, because the relationship "Finding site - direct" is a child of the relationship
     * "Finding site" and some of the partial-areas in any area in "Finding site - direct" will have parents in
     * an area that has the "Finding site" relationship.
     * 
     * TODO: This can be done much more efficiently. Instead of finding missing parents, instead do a traversal
     * from the root and build the hierarchy, including implicit areas, in one shot.
     * @return 
     */
    public PAreaTaxonomy getImplicitRelationshipSubtaxonomy() {
        ArrayList<Area> subset = (ArrayList<Area>)containers.clone();

        HashMap<Integer, PAreaSummary> pareasInSubset = new HashMap<Integer,PAreaSummary>();

        for(Area a : subset) {
            ArrayList<PAreaSummary> pareasInArea = a.getAllPAreas();

            for(PAreaSummary parea : pareasInArea) {
                pareasInSubset.put(parea.getId(), parea);
            }
        }

        ArrayList<Integer> missingParents = new ArrayList<Integer>();

        for(Entry<Integer, PAreaSummary> entry : pareasInSubset.entrySet()) {
            HashSet<Integer> pareaPids = entry.getValue().getParentIds();

            for(int pid : pareaPids) {
                if(!pareasInSubset.containsKey(pid) && !missingParents.contains(pid)) {
                    missingParents.add(pid);
                }
            }
        }
        
        PAreaTaxonomy fullTaxonomy;
        
        if(dataSource instanceof SCTLocalDataSource) {
            PAreaTaxonomyGenerator generator = new PAreaTaxonomyGenerator();
            
            fullTaxonomy = generator.createPAreaTaxonomy(SNOMEDHierarchyRoot, (SCTLocalDataSource)dataSource, new InferredRelationshipsRetriever());
        } else {
            fullTaxonomy = MiddlewareAccessorProxy.getProxy().getPAreaHierarchyData(SNOMEDVersion, SNOMEDHierarchyRoot);
        }

        for(int pareaid : missingParents) {
            pareasInSubset.put(pareaid, fullTaxonomy.getPAreas().get(pareaid));
        }

        ArrayList<Area> implicitAreas = new ArrayList<Area>();

        for (int pareaid : missingParents) {
            PAreaSummary parea = pareasInSubset.get(pareaid);
            boolean areaFound = false;

            ArrayList<Long> pareaRels = new ArrayList<Long>();

            for(InheritedRelationship ir : parea.getRelationships()) {
                pareaRels.add(ir.getRelationshipTypeId());
            }

            // Check if an area with the same relationship set exists
            for (Area area : implicitAreas) {
                // If it does add this parea to it
                if (relationshipSetsEqual(pareaRels, area.getRelationships())) {
                    area.addPArea(parea);
                    areaFound = true;
                    break;
                }
            }

            // Otherwise create a new area and add this parea to it
            if (!areaFound) {
                Area newArea = new Area(implicitAreas.size() + 9000, true);
                newArea.setRels(pareaRels);
                newArea.addPArea(parea);
                implicitAreas.add(newArea);
            }
        }

        subset.addAll(implicitAreas);

        HashMap<Integer, HashSet<Integer>> pareaSubhierarchy = new HashMap<Integer, HashSet<Integer>>();

        for(Integer pid : pareasInSubset.keySet()) {
            HashSet<Integer> subhierarchyChildren = new HashSet<Integer>();
            
            pareaSubhierarchy.put(pid, subhierarchyChildren);

            HashSet<Integer> allChildren = groupHierarchy.get(pid);

            if (allChildren != null) {
                for (Integer cid : allChildren) {
                    if (pareasInSubset.containsKey(cid)) {
                        subhierarchyChildren.add(cid);
                    }
                }
            }
        }

        HashMap<Long, String> lateralRelMap = new HashMap<Long, String>();

        for (Area a : subset) {
            for (Region region : a.getRegions()) {
                ArrayList<PAreaSummary> summaries = region.getPAreasInRegion();

                Collections.sort(summaries, new Comparator<PAreaSummary>() {

                    public int compare(PAreaSummary a, PAreaSummary b) {
                        return a.getConceptCount() > b.getConceptCount() ? -1 : 1;
                    }
                });
            }
        }

        for(Area a : subset) {
            ArrayList<Long> areaRels = a.getRelationships();

            for(long rel : areaRels) {
                if(!lateralRelMap.containsKey(rel)) {
                    lateralRelMap.put(rel, lateralRels.get(rel));
                }
            }
        }

        PAreaTaxonomy hd = new PAreaTaxonomy(SNOMEDHierarchyRoot, rootPArea, subset, pareasInSubset,
                pareaSubhierarchy, lateralRelMap, SNOMEDVersion, dataSource);

        return hd;
    }

    /**
     * Determines if two sets of relationship type IDs are equal.
     * @param set1 A ArrayList of Integers that correspond to the ConceptId of some relationship type
     * @param set2 Another ArrayList of Integers that correspond to the ConceptId of some relationship type
     * @return true if both sets of relationships are considered equal, false otherwise
     */
    private boolean relationshipSetsEqual(ArrayList<Long> set1, ArrayList<Long> set2) {
        if (set1 == null && set2 == null) {
            return true;
        }

        if (set1.size() == set2.size()) {
            return set1.containsAll(set2);
        }

        return false;
    }
}
