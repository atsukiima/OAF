package edu.njit.cs.saboc.blu.sno.gui.dialogs.panels.concepthierarchy;

import SnomedShared.Concept;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTAggregatePArea;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTPArea;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTPAreaTaxonomy;
import edu.njit.cs.saboc.blu.sno.datastructure.hierarchy.SCTConceptHierarchy;
import java.util.ArrayList;
import java.util.HashSet;

/**
 *
 * @author Chris O
 */
public class SCTAggregatePAreaHierarchyLoader extends SCTConceptGroupHierarchyLoader<SCTAggregatePArea> {
    
    private SCTPAreaTaxonomy taxonomy;
    
    public SCTAggregatePAreaHierarchyLoader(SCTPAreaTaxonomy taxonomy, SCTAggregatePArea parea, SCTConceptHierarchyViewPanel panel) {
        super(parea, panel);
        
        this.taxonomy = taxonomy;
    }
    
    public SCTConceptHierarchy getGroupHierarchy(SCTAggregatePArea parea) {
        
        SCTAggregatePArea aggregatePArea = parea;
        
        SCTConceptHierarchy aggregateConceptHierarchy = new SCTConceptHierarchy(parea.getRoot());

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
}