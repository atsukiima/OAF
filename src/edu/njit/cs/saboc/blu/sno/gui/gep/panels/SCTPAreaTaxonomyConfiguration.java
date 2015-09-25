package edu.njit.cs.saboc.blu.sno.gui.gep.panels;

import SnomedShared.Concept;
import SnomedShared.pareataxonomy.InheritedRelationship;
import edu.njit.cs.saboc.blu.core.abn.GenericParentGroupInfo;
import edu.njit.cs.saboc.blu.core.abn.OverlappingConceptResult;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.listeners.EntitySelectionListener;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.listeners.NavigateToGroupListener;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.listeners.ParentGroupSelectedListener;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.pareataxonomy.PAreaTaxonomyConfiguration;
import edu.njit.cs.saboc.blu.sno.abn.disjointpareataxonomy.DisjointPAreaTaxonomy;
import edu.njit.cs.saboc.blu.sno.abn.disjointpareataxonomy.DisjointPartialArea;
import edu.njit.cs.saboc.blu.sno.abn.disjointpareataxonomy.SCTDisjointPAreaTaxonomyGenerator;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTAggregatePArea;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTArea;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTPArea;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTPAreaTaxonomy;
import edu.njit.cs.saboc.blu.sno.datastructure.hierarchy.SCTConceptHierarchy;
import edu.njit.cs.saboc.blu.sno.datastructure.hierarchy.SCTMultiRootedConceptHierarchy;
import edu.njit.cs.saboc.blu.sno.gui.abnselection.SCTDisplayFrameListener;
import edu.njit.cs.saboc.blu.sno.gui.gep.configuration.SCTPAreaTaxonomyGEPConfiguration;
import edu.njit.cs.saboc.blu.sno.gui.gep.configuration.listener.DisplayConceptBrowserListener;
import edu.njit.cs.saboc.blu.sno.sctdatasource.SCTLocalDataSource;
import edu.njit.cs.saboc.blu.sno.utils.comparators.ConceptNameComparator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Optional;
import java.util.Queue;

/**
 *
 * @author Chris O
 */
public class SCTPAreaTaxonomyConfiguration extends PAreaTaxonomyConfiguration<
        Concept, 
        SCTPArea, 
        SCTArea, 
        SCTPAreaTaxonomy, 
        DisjointPartialArea, 
        InheritedRelationship,
        SCTConceptHierarchy,
        DisjointPAreaTaxonomy,
        SCTAggregatePArea> {

    private final SCTPAreaTaxonomy taxonomy;
    
    private final SCTDisplayFrameListener displayListener;
    
    private final SCTPAreaTaxonomyGEPConfiguration gepConfig;
    
    public SCTPAreaTaxonomyConfiguration(SCTPAreaTaxonomy taxonomy, 
            SCTDisplayFrameListener displayListener, 
            SCTPAreaTaxonomyGEPConfiguration gepConfig) {
        
        this.taxonomy = taxonomy;
        this.displayListener = displayListener;
        
        this.gepConfig = gepConfig;
    }
    
    public SCTPAreaTaxonomy getPAreaTaxonomy() {
        return taxonomy;
    }
    
    public SCTDisplayFrameListener getDisplayListener() {
        return displayListener;
    }
    
    public SCTPAreaTaxonomyGEPConfiguration getGEPConfiguration() {
        return gepConfig;
    }
    
    private ArrayList<InheritedRelationship> getSortedRelationships(ArrayList<InheritedRelationship> rels) {
        Collections.sort(rels, new Comparator<InheritedRelationship>() {
            public int compare(InheritedRelationship a, InheritedRelationship b) {
                String aName = taxonomy.getLateralRelsInHierarchy().get(a.getRelationshipTypeId());
                String bName = taxonomy.getLateralRelsInHierarchy().get(b.getRelationshipTypeId());

                return aName.compareToIgnoreCase(bName);
            }
        });
        
        return rels;
    }
    
    @Override
    public ArrayList<InheritedRelationship> getAreaRelationships(SCTArea area) {
        ArrayList<InheritedRelationship> rels = new ArrayList<>(area.getRelationships());
        
        return getSortedRelationships(rels);
    }
    
    @Override
    public ArrayList<InheritedRelationship> getPAreaRelationships(SCTPArea parea) {
        ArrayList<InheritedRelationship> rels = new ArrayList<>(parea.getRelationships());

        return getSortedRelationships(rels);
    }

    @Override
    public Comparator<SCTPArea> getChildPAreaComparator() {
        return new Comparator<SCTPArea>() {
            public int compare(SCTPArea a, SCTPArea b) {
                return a.getRoot().getName().compareToIgnoreCase(b.getRoot().getName());
            }
        };
    }

    @Override
    public String getDisjointGroupName(DisjointPartialArea group) {
        return group.getRoot().getName();
    }
    
    private String getRelationshipNamesCommaSeparated(HashSet<InheritedRelationship> rels) {
        
        if(rels.isEmpty()) {
            return "";
        }
        
        ArrayList<String> relNames = new ArrayList<>();
        
        rels.forEach((InheritedRelationship rel) -> {
            relNames.add(taxonomy.getLateralRelsInHierarchy().get(rel.getRelationshipTypeId()));
        });
        
        Collections.sort(relNames);
        
        String areaName = relNames.get(0);
        
        for(int c = 1; c < relNames.size(); c++) {
            areaName += ", " + relNames.get(c);
        }
        
        return areaName;
    }

    @Override
    public String getContainerName(SCTArea container) {
        if(container.getRelationships().isEmpty()) {
            return "root area (no attribute relationships)";
        } else {
            return getRelationshipNamesCommaSeparated(container.getRelationships());
        }
    }

    @Override
    public ArrayList<SCTPArea> getSortedGroupList(SCTArea area) {
        return area.getAllPAreas();
    }

    @Override
    public String getConceptTypeName(boolean plural) {
        if(plural) {
            return "Concepts";
        } else {
            return "Concept";
        }
    }

    @Override
    public String getConceptName(Concept concept) {
        return concept.getName();
    }

    @Override
    public String getGroupName(SCTPArea group) {
        return group.getRoot().getName();
    }

    @Override
    public ArrayList<Concept> getSortedConceptList(SCTPArea group) {
        ArrayList<Concept> pareaConcepts = group.getConceptsInPArea();
        Collections.sort(pareaConcepts, new ConceptNameComparator());
        
        return pareaConcepts;
    }
    
    private DisjointPAreaTaxonomy createSimpleDisjointPAreaTaxonomy(SCTArea area) {
        SCTDisjointPAreaTaxonomyGenerator generator = new SCTDisjointPAreaTaxonomyGenerator();

        HashSet<SCTPArea> pareas = this.getContainerGroupSet(area);

        HashSet<Concept> roots = new HashSet<>();

        pareas.forEach((SCTPArea parea) -> {
            roots.add(parea.getRoot());
        });

        SCTMultiRootedConceptHierarchy hierarchy = new SCTMultiRootedConceptHierarchy(roots);

        pareas.forEach((SCTPArea parea) -> {
            if (taxonomy.isReduced()) {
                SCTAggregatePArea aggregatePArea = (SCTAggregatePArea) parea;

                hierarchy.addAllHierarchicalRelationships(this.getAggregatedPAreaHierarchy(aggregatePArea));
            } else {
                hierarchy.addAllHierarchicalRelationships(parea.getHierarchy());
            }
        });

        return generator.generateDisjointAbstractionNetwork(taxonomy, pareas, hierarchy);
    }
    
    private Optional<SCTArea> getCompleteArea(SCTArea area) {
        SCTLocalDataSource dataSource = (SCTLocalDataSource)taxonomy.getDataSource();
        
        ArrayList<Concept> rootHierarchies = dataSource.getHierarchiesConceptBelongTo(taxonomy.getRootGroup().getRoot());
        
        Concept hierarchyRoot = rootHierarchies.get(0); // There may be more than one hierarchy a concept belongs to, but we're not dealing with that for now
        
        SCTPAreaTaxonomy completeTaxonomy = dataSource.getCompleteTaxonomy(hierarchyRoot);
        
        ArrayList<SCTArea> completeAreas = completeTaxonomy.getAreas();
        
        Optional<SCTArea> matchingCompleteArea = Optional.empty();
        
        for(SCTArea completeArea : completeAreas) {
            if(completeArea.getRelationshipIds().equals(area.getRelationshipIds())) {
                matchingCompleteArea = Optional.of(completeArea);
                break;
            }
        }
        
        return matchingCompleteArea;
    }
    
    private DisjointPAreaTaxonomy createSubjectDisjointPAreaTaxonomy(SCTArea area) {
        SCTLocalDataSource dataSource = (SCTLocalDataSource)taxonomy.getDataSource();
        
        Optional<SCTArea> completeArea = getCompleteArea(area);
        
        HashSet<Concept> subhierarchyConcepts = area.getConcepts();
        
        HashSet<Concept> subhierarchyRoots = new HashSet<>();
        
        area.getAllPAreas().forEach( (SCTPArea parea) -> {
            subhierarchyRoots.add(parea.getRoot());
        });
        
        if(completeArea.isPresent()) {
            HashSet<Concept> completeAreaConcepts = completeArea.get().getConcepts();
            
            HashSet<SCTPArea> externalOverlappingPAreas = new HashSet<>();
            
            ArrayList<SCTPArea> allPAreas = completeArea.get().getAllPAreas();
            
            HashSet<Concept> externalConceptInArea = new HashSet<>();
                        
            subhierarchyConcepts.forEach((Concept c) -> {
                
                allPAreas.forEach((SCTPArea parea) -> {
                    if (parea.getHierarchy().getConceptsInHierarchy().contains(c)) {
                        externalOverlappingPAreas.add(parea);
                    }
                });
                
                HashSet<Concept> children = dataSource.getConceptHierarchy().getChildren(c);
                
                boolean isLeaf = true;
                
                for(Concept child : children) {
                    if(subhierarchyConcepts.contains(child)) {
                        isLeaf = false;
                        break;
                    }
                }
                
                if(isLeaf) {
                    externalConceptInArea.add(c);
                }
            });
            
            HashSet<SCTPArea> subjectPAreas = new HashSet<>(area.getAllPAreas());
            
            HashSet<SCTPArea> allSubjectOverlappingPAreas = new HashSet<>(subjectPAreas);
            allSubjectOverlappingPAreas.addAll(externalOverlappingPAreas);
            
            HashSet<Concept> roots = new HashSet<>();
            
            allSubjectOverlappingPAreas.forEach( (SCTPArea parea) -> {
                roots.add(parea.getRoot());
            });
            
            SCTMultiRootedConceptHierarchy hierarchy = new SCTMultiRootedConceptHierarchy(roots);
            
            HashSet<Concept> processedConcepts = new HashSet<>();
            HashSet<Concept> inQueueConcepts = new HashSet<>(externalConceptInArea);
            
            Queue<Concept> queue = new LinkedList<>(externalConceptInArea);
            
            while(!queue.isEmpty()) {
                Concept c = queue.remove();
                
                processedConcepts.add(c);
                inQueueConcepts.remove(c);
                
                HashSet<Concept> parents = dataSource.getConceptHierarchy().getParents(c);
                
                parents.forEach( (Concept parent) -> {
                    
                    if (completeAreaConcepts.contains(parent)) {
                         hierarchy.addIsA(c, parent);
                        
                        if (!inQueueConcepts.contains(parent) && !processedConcepts.contains(parent)) {
                            queue.add(parent);
                            inQueueConcepts.add(parent);
                        }
                    }
                });
            }
            
            SCTDisjointPAreaTaxonomyGenerator generator = new SCTDisjointPAreaTaxonomyGenerator();
            
            return generator.generateDisjointAbstractionNetwork(taxonomy, allSubjectOverlappingPAreas, hierarchy);
            
        } else {
            
            // This should never ever happen...
            
            return null;
        }
    }
    
    @Override
    public DisjointPAreaTaxonomy createDisjointAbN(SCTArea area) {
        if(isSubjectSubtaxonomy()) {
            return createSubjectDisjointPAreaTaxonomy(area);
        } else {
            return createSimpleDisjointPAreaTaxonomy(area);
        }
    }
    
    private boolean isSubjectSubtaxonomy() {
        return !taxonomy.getDataSource().getHierarchyRootConcepts().contains(taxonomy.getRootPArea().getRoot());
    }
    
    @Override
    public HashSet<SCTPArea> getContainerGroupSet(SCTArea area) {
        return new HashSet<>(area.getAllPAreas());
    }

    @Override
    public HashSet<Concept> getGroupConceptSet(SCTPArea parea) {
        return new HashSet<>(parea.getConceptsInPArea());
    }
    
    private HashSet<OverlappingConceptResult<Concept, SCTPArea>> getExternalOverlappingResults(SCTArea area) {
        Optional<SCTArea> completeArea = getCompleteArea(area);
        
        if(completeArea.isPresent()) {
            HashSet<SCTPArea> internalPAreas = this.getContainerGroupSet(area);
            
            HashSet<SCTPArea> externalPAreas = this.getContainerGroupSet(completeArea.get());
            
            HashMap<Concept, HashSet<SCTPArea>> conceptPAreas = new HashMap<>();
            
            HashSet<Concept> internalConcepts = area.getConcepts();
            
            internalConcepts.forEach( (Concept c) -> {
                conceptPAreas.put(c, new HashSet<>());
                
                internalPAreas.forEach((SCTPArea internalPArea) -> {
                    if(internalPArea.getHierarchy().getConceptsInHierarchy().contains(c)) {
                        conceptPAreas.get(c).add(internalPArea);
                    }
                });
                
                externalPAreas.forEach((SCTPArea externalPArea) -> {
                    if(externalPArea.getHierarchy().getConceptsInHierarchy().contains(c)) {
                        conceptPAreas.get(c).add(externalPArea);
                    }
                });
            });
            
            HashSet<OverlappingConceptResult<Concept, SCTPArea>> allOverlappingResults = new HashSet<>();
            
            conceptPAreas.forEach((Concept c, HashSet<SCTPArea> pareas) -> {
                if (pareas.size() > 1) {
                    allOverlappingResults.add(new OverlappingConceptResult<>(c, pareas));
                }
            });
            
            return allOverlappingResults;
        } else {
            return new HashSet<>();
        }
    }
    
    @Override
    public HashSet<OverlappingConceptResult<Concept, SCTPArea>> getContainerOverlappingResults(SCTArea area) {
        if(isSubjectSubtaxonomy()) {
            return getExternalOverlappingResults(area);
        } else {
            return super.getContainerOverlappingResults(area);
        }
    }

    @Override
    public int getContainerLevel(SCTArea area) {
        return area.getRelationships().size();
    }
    
    @Override
    public String getGroupsContainerName(SCTPArea parea) {
        return this.getRelationshipNamesCommaSeparated(parea.getRelationships());
    }
    
    @Override
    public SCTConceptHierarchy getAggregatedPAreaHierarchy(SCTAggregatePArea aggregatePArea) {
        SCTConceptHierarchy aggregateConceptHierarchy = new SCTConceptHierarchy(aggregatePArea.getRoot());

        HashSet<SCTPArea> aggregatedPAreaSet = aggregatePArea.getAggregatedGroupHierarchy().getNodesInHierarchy();
        
        aggregatedPAreaSet.forEach((SCTPArea aggregatedPArea) -> {
            aggregateConceptHierarchy.addAllHierarchicalRelationships(aggregatedPArea.getHierarchy());
        });

        HashSet<Concept> conceptsInAggregatedHierarchy = aggregateConceptHierarchy.getConceptsInHierarchy();
        
        aggregatedPAreaSet.forEach((SCTPArea aggregatedPArea) -> {
            conceptsInAggregatedHierarchy.add(aggregatedPArea.getRoot());
        });
        
        aggregatedPAreaSet.forEach((SCTPArea aggregatedPArea) -> {
            if(!aggregatedPArea.equals(aggregatePArea.getAggregatedGroupHierarchy().getRoots().iterator().next())) {
                ArrayList<Concept> aggregatedPAreaClasses = aggregatedPArea.getConceptsInPArea();
                
                aggregatedPAreaClasses.forEach((Concept c) -> {
                    ArrayList<Concept> parents = taxonomy.getDataSource().getConceptParents(c);

                    parents.forEach((Concept parent) -> {
                        if (conceptsInAggregatedHierarchy.contains(parent)) {
                            aggregateConceptHierarchy.addIsA(c, parent);
                        }
                    });
                });
            }
        });
        
        return aggregateConceptHierarchy;
    }
    
    @Override
    public EntitySelectionListener<Concept> getGroupConceptListListener() {
        return new DisplayConceptBrowserListener(this.getDisplayListener(), this.getPAreaTaxonomy().getDataSource());
    }

    @Override
    public EntitySelectionListener<SCTPArea> getChildGroupListener() {
        return new NavigateToGroupListener<>(this.getGEPConfiguration().getGEP());
    }
    
    @Override
    public EntitySelectionListener<GenericParentGroupInfo<Concept, SCTPArea>> getParentGroupListener() {
        return new ParentGroupSelectedListener<>(this.getGEPConfiguration().getGEP());
    }
}
