package edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local;

import SnomedShared.Concept;
import edu.njit.cs.saboc.blu.core.abn.GroupHierarchy;
import edu.njit.cs.saboc.blu.core.abn.reduced.AggregateableConceptGroup;
import edu.njit.cs.saboc.blu.sno.datastructure.hierarchy.SCTConceptHierarchy;
import java.util.HashSet;

/**
 *
 * @author Chris O
 */
public class SCTAggregatePArea extends SCTPArea implements AggregateableConceptGroup<Concept, SCTPArea> {

    
    private GroupHierarchy<SCTPArea> reducedPAreaHierarchy;
    
    public SCTAggregatePArea(SCTPArea parea, HashSet<Integer> reducedParentIds, GroupHierarchy<SCTPArea> reducedPAreaHierarchy) {
        super(parea.getId(), (SCTConceptHierarchy)parea.getHierarchy(), reducedParentIds, parea.getRelationships());
        
        this.reducedPAreaHierarchy = reducedPAreaHierarchy;
    }
    
    public GroupHierarchy<SCTPArea> getAggregatedGroupHierarchy() {
        return reducedPAreaHierarchy;
    }
    
    public HashSet<SCTPArea> getAggregatedGroups() {
        HashSet<SCTPArea> reducedPAreas = new HashSet<SCTPArea>(reducedPAreaHierarchy.getNodesInHierarchy());
        reducedPAreas.removeAll(reducedPAreaHierarchy.getRoots());
        
        return reducedPAreas;
    }
    
    public HashSet<Concept> getAllGroupsConcepts() {
        HashSet<Concept> allConcepts = new HashSet<Concept>();
        
        allConcepts.addAll(this.getHierarchy().getNodesInHierarchy());
        
        for(SCTPArea reducedPArea : reducedPAreaHierarchy.getNodesInHierarchy()) {
            allConcepts.addAll(reducedPArea.getHierarchy().getNodesInHierarchy());
        }
        
        return allConcepts;
    }
}
