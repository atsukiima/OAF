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
import edu.njit.cs.saboc.blu.sno.utils.comparators.ConceptNameComparator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;

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
        return getRelationshipNamesCommaSeparated(container.getRelationships());
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
    
    @Override
    public DisjointPAreaTaxonomy createDisjointAbN(SCTArea area) {
        SCTDisjointPAreaTaxonomyGenerator generator = new SCTDisjointPAreaTaxonomyGenerator();
        
        HashSet<SCTPArea> pareas = this.getContainerGroupSet(area);
        
        HashSet<Concept> roots = new HashSet<>();
        
        pareas.forEach( (SCTPArea parea) -> {
            roots.add(parea.getRoot());
        });
        
        SCTMultiRootedConceptHierarchy hierarchy = new SCTMultiRootedConceptHierarchy(roots);
        
        pareas.forEach( (SCTPArea parea) -> {
            if(taxonomy.isReduced()) {
                SCTAggregatePArea aggregatePArea = (SCTAggregatePArea)parea;
                
                hierarchy.addAllHierarchicalRelationships(this.getAggregatedPAreaHierarchy(aggregatePArea));
                
            } else {
                hierarchy.addAllHierarchicalRelationships(parea.getHierarchy());
            }
        });
        
        return generator.generateDisjointAbstractionNetwork(taxonomy, pareas, hierarchy);
    }
    
    @Override
    public HashSet<SCTPArea> getContainerGroupSet(SCTArea area) {
        return new HashSet<>(area.getAllPAreas());
    }

    @Override
    public HashSet<Concept> getGroupConceptSet(SCTPArea parea) {
        return new HashSet<>(parea.getConceptsInPArea());
    }

    @Override
    public HashSet<Concept> getContainerOverlappingConcepts(SCTArea area) {
        HashSet<OverlappingConceptResult<Concept, SCTPArea>> overlappingConceptResults = area.getOverlappingConcepts();
        
        HashSet<Concept> overlappingConcepts = new HashSet<>();
        
        overlappingConceptResults.forEach( (OverlappingConceptResult<Concept, SCTPArea> result) -> {
            overlappingConcepts.add(result.getConcept());
        });
        
        return overlappingConcepts;
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
