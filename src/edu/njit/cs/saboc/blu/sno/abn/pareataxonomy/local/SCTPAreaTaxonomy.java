package edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local;

import SnomedShared.Concept;
import SnomedShared.pareataxonomy.InheritedRelationship;
import edu.njit.cs.saboc.blu.core.abn.GroupHierarchy;
import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.GenericPAreaTaxonomy;
import edu.njit.cs.saboc.blu.sno.abn.SCTAbstractionNetwork;
import edu.njit.cs.saboc.blu.sno.abn.generator.SCTPAreaTaxonomyGenerator;
import edu.njit.cs.saboc.blu.sno.datastructure.hierarchy.SCTConceptHierarchy;
import edu.njit.cs.saboc.blu.sno.localdatasource.load.InferredRelationshipsRetriever;
import edu.njit.cs.saboc.blu.sno.sctdatasource.SCTDataSource;
import edu.njit.cs.saboc.blu.sno.sctdatasource.SCTLocalDataSource;
import edu.njit.cs.saboc.blu.sno.sctdatasource.middlewareproxy.MiddlewareAccessorProxy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 *
 * @author Chris O
 */
public class SCTPAreaTaxonomy extends GenericPAreaTaxonomy<SCTPAreaTaxonomy, SCTPArea, 
        SCTArea, SCTRegion, Concept, InheritedRelationship, SCTConceptHierarchy> 
    implements SCTAbstractionNetwork<SCTPAreaTaxonomy> {

    protected SCTDataSource dataSource;
    
    protected String version;
    
    protected Concept sctRootConcept;
    
     /**
     * The set of unique lateral relationships in the hierarchy, 
     * along with their abbreviation, or full name. Note that
     * this may contain a subset of relationships in a root subtaxonomy.
     */ 
    protected HashMap<Long, String> lateralRelNames;

    public SCTPAreaTaxonomy(
            Concept sctRootConcept,
            String version,
            SCTDataSource dataSource,
            SCTConceptHierarchy conceptHierarchy,
            SCTPArea rootPArea,
            ArrayList<SCTArea> areas,
            HashMap<Integer, SCTPArea> pareas,
            GroupHierarchy<SCTPArea> pareaHierarchy, 
            HashMap<Long, String> relNames) {

        super(conceptHierarchy, rootPArea, areas, pareas, pareaHierarchy);

        this.sctRootConcept = sctRootConcept;
        
        this.dataSource = dataSource;
        
        this.version = version;
        
        this.lateralRelNames = relNames;
    }
    
    public SCTPAreaTaxonomy getAbstractionNetwork() {
        return this;
    }
    
    public SCTDataSource getDataSource() {
        return dataSource;
    }
    
    public String getSCTVersion() {
        return version;
    }
    
    public Concept getSCTRootConcept() {
        return sctRootConcept;
    }
    
    public int getPAreaCount() {
        return getGroupCount();
    }

    public int getAreaCount() {
        return getContainerCount();
    }

    public ArrayList<SCTArea> getHierarchyAreas() {
        return (ArrayList<SCTArea>)containers;
    }
    
    /**
     * Returns the set of areas which were explicitly chosen as
     * belonging to a relationship subtaxonomy
     * @return 
    */
    public ArrayList<SCTArea> getExplicitHierarchyAreas() {
        ArrayList<SCTArea> explicitAreas = new ArrayList<SCTArea>();

        for(SCTArea a : (ArrayList<SCTArea>)containers) {
            if(!a.isImplicit()) {
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
    public ArrayList<SCTArea> getImplicitHierarchyAreas() {
        ArrayList<SCTArea> implicitAreas = new ArrayList<SCTArea>();

        for (SCTArea a : (ArrayList<SCTArea>)containers) {
            if (a.isImplicit()) {
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
        return lateralRelNames;
    }
    
    public final HashMap<Integer, SCTPArea> getPAreas() {
        return (HashMap<Integer, SCTPArea>)groups;
    }

    public SCTPArea getPAreaFromRootConceptId(long rootConceptId) {
        return (SCTPArea)getGroupFromRootConceptId(rootConceptId);
    }
        
    public ArrayList<SCTArea> searchAreas(String term) {
        ArrayList<SCTArea> searchResults = new ArrayList<SCTArea>();

        ArrayList<SCTArea> areas = getHierarchyAreas();

        String [] searchedRels = term.split(", ");

        if(searchedRels == null) {
            return new ArrayList<SCTArea>();
        }

        for(SCTArea a : areas) {
            ArrayList<String> relsInArea = new ArrayList<String>();

            for(InheritedRelationship rel : a.getRelationships()) {
                relsInArea.add(getLateralRelsInHierarchy().get(rel.getRelationshipTypeId()));
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
    public SCTPAreaTaxonomy getRootSubtaxonomy(SCTPArea subhierarchyRootPArea) {
        return null;
    }
    
    /**
     * Creates a relationship subtaxonomy using the relatationships with the given
     * relationship ids
     * @param relationships
     * @return 
     */
    public SCTPAreaTaxonomy getRelationshipSubtaxonomy(ArrayList<Long> relationships) {
        ArrayList<SCTArea> subset = new ArrayList<SCTArea>();

        for(SCTArea a : (ArrayList<SCTArea>)containers) {
            HashSet<InheritedRelationship> areaRels = a.getRelationships();
            
            HashSet<Long> relIds = new HashSet<Long>();
            
            for(InheritedRelationship rel : areaRels) {
                relIds.add(rel.getRelationshipTypeId());
            }

            if(relationships.containsAll(relIds)) {
                subset.add(a);
            }
        }

        HashMap<Integer, SCTPArea> pareasInSubset = new HashMap<Integer, SCTPArea>();

        for(SCTArea a : subset) {
            ArrayList<SCTPArea> pareasInArea = a.getAllPAreas();

            for(SCTPArea parea : pareasInArea) {
                pareasInSubset.put(parea.getId(), parea);
            }
        }

        HashMap<Integer, HashSet<Integer>> pareaSubhierarchy = new HashMap<Integer, HashSet<Integer>>();

        for(Integer pid : pareasInSubset.keySet()) {
            HashSet<Integer> subhierarchyChildren = new HashSet<Integer>();
            
            pareaSubhierarchy.put(pid, subhierarchyChildren);

            HashSet<SCTPArea> allChildren = groupHierarchy.getChildren(pareasInSubset.get(pid));

            if (allChildren != null) {
                for (SCTPArea child : allChildren) {
                    if (pareasInSubset.containsKey(child.getId())) {
                        subhierarchyChildren.add(child.getId());
                    }
                }
            }
        }

        for (SCTArea a : subset) {
            for (SCTRegion region : a.getRegions()) {
                ArrayList<SCTPArea> summaries = region.getPAreasInRegion();

                Collections.sort(summaries, new Comparator<SCTPArea>() {

                    public int compare(SCTPArea a, SCTPArea b) {
                        Integer aCount = a.getConceptCount();
                        Integer bCount = b.getConceptCount();
                        
                        return aCount.compareTo(bCount);
                    }
                });
            }
        }
        
        GroupHierarchy<SCTPArea> convertedHierarchy = new GroupHierarchy<>(rootPArea);
        
        pareasInSubset.values().forEach((SCTPArea parea) -> {
            HashSet<Integer> parentIds = parea.getParentIds();
            
            parentIds.forEach((Integer parentId) -> {
               convertedHierarchy.addIsA(parea, pareasInSubset.get(parentId));
            });
        });
        
        SCTPAreaTaxonomy hd = new SCTPAreaTaxonomy(sctRootConcept, version, dataSource, null, rootPArea, subset, pareasInSubset,
                convertedHierarchy, this.getLateralRelsInHierarchy());

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
    public SCTPAreaTaxonomy getImplicitRelationshipSubtaxonomy() {
        ArrayList<SCTArea> subset = (ArrayList<SCTArea>)containers.clone();

        HashMap<Integer, SCTPArea> pareasInSubset = new HashMap<Integer, SCTPArea>();

        for(SCTArea a : subset) {
            ArrayList<SCTPArea> pareasInArea = a.getAllPAreas();

            for(SCTPArea parea : pareasInArea) {
                pareasInSubset.put(parea.getId(), parea);
            }
        }

        ArrayList<Integer> missingParents = new ArrayList<Integer>();

        for(Map.Entry<Integer, SCTPArea> entry : pareasInSubset.entrySet()) {
            HashSet<Integer> pareaPids = entry.getValue().getParentIds();

            for(int pid : pareaPids) {
                if(!pareasInSubset.containsKey(pid) && !missingParents.contains(pid)) {
                    missingParents.add(pid);
                }
            }
        }
        
        SCTPAreaTaxonomy fullTaxonomy = null;
        
        if(dataSource instanceof SCTLocalDataSource) {
            fullTaxonomy = ((SCTLocalDataSource)dataSource).getCompleteTaxonomy(sctRootConcept);
        } else {
            fullTaxonomy = MiddlewareAccessorProxy.getProxy().getPAreaHierarchyData(version, sctRootConcept);
        }

        for(int pareaid : missingParents) {
            pareasInSubset.put(pareaid, fullTaxonomy.getPAreas().get(pareaid));
        }

        ArrayList<SCTArea> implicitAreas = new ArrayList<SCTArea>();

        for (int pareaid : missingParents) {
            SCTPArea parea = pareasInSubset.get(pareaid);
            boolean areaFound = false;

            HashSet<Long> pareaRels = new HashSet<Long>();

            for(InheritedRelationship ir : parea.getRelationships()) {
                pareaRels.add(ir.getRelationshipTypeId());
            }

            // Check if an area with the same relationship set exists
            for (SCTArea area : implicitAreas) {
                // If it does add this parea to it
                if (pareaRels.equals(area.getRelationshipIds())) {
                    area.addPArea(parea);
                    areaFound = true;
                    break;
                }
            }

            // Otherwise create a new area and add this parea to it
            if (!areaFound) {
                SCTArea newArea = new SCTArea(implicitAreas.size() + 9000, parea.getRelsWithoutInheritanceInfo(), true);
                newArea.addPArea(parea);
                implicitAreas.add(newArea);
            }
        }

        subset.addAll(implicitAreas);

        HashMap<Integer, HashSet<Integer>> pareaSubhierarchy = new HashMap<Integer, HashSet<Integer>>();

        for(Integer pid : pareasInSubset.keySet()) {
            HashSet<Integer> subhierarchyChildren = new HashSet<Integer>();
            
            pareaSubhierarchy.put(pid, subhierarchyChildren);

            HashSet<SCTPArea> allChildren = groupHierarchy.getChildren(pareasInSubset.get(pid));

            if (allChildren != null) {
                for (SCTPArea child : allChildren) {
                    if (pareasInSubset.containsKey(child.getId())) {
                        subhierarchyChildren.add(child.getId());
                    }
                }
            }
        }

        for (SCTArea a : subset) {
            for (SCTRegion region : a.getRegions()) {
                ArrayList<SCTPArea> summaries = region.getPAreasInRegion();

                Collections.sort(summaries, new Comparator<SCTPArea>() {

                    public int compare(SCTPArea a, SCTPArea b) {
                        return a.getConceptCount() > b.getConceptCount() ? -1 : 1;
                    }
                });
            }
        }
        
        GroupHierarchy<SCTPArea> convertedHierarchy = new GroupHierarchy<>(rootPArea);

        pareasInSubset.values().forEach((SCTPArea parea) -> {
            HashSet<Integer> parentIds = parea.getParentIds();

            parentIds.forEach((Integer parentId) -> {
                convertedHierarchy.addIsA(parea, pareasInSubset.get(parentId));
            });
        });

        SCTPAreaTaxonomy hd = new SCTPAreaTaxonomy(sctRootConcept, version, dataSource, null, rootPArea, subset, pareasInSubset,
                convertedHierarchy, this.getLateralRelsInHierarchy());

        return hd;
    }
    
    public SCTPAreaTaxonomy getReduced(int minPAreaSize, int maxPAreaSize) {
        SCTPAreaTaxonomyGenerator taxonomyGenerator = new SCTPAreaTaxonomyGenerator(
            this.getSCTRootConcept(), this.getDataSource(), (SCTConceptHierarchy)this.getConceptHierarchy(), new InferredRelationshipsRetriever());

        return super.createReducedTaxonomy(taxonomyGenerator, new ReducedSCTPAreaTaxonomyGenerator(), minPAreaSize, maxPAreaSize);
    }
}
