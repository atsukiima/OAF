package edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local;

import SnomedShared.Concept;
import edu.njit.cs.saboc.blu.core.abn.GroupHierarchy;
import edu.njit.cs.saboc.blu.core.abn.reduced.ReducingGroup;
import edu.njit.cs.saboc.blu.sno.datastructure.hierarchy.SCTConceptHierarchy;
import java.util.HashSet;

/**
 *
 * @author Chris O
 */
public class ReducedSCTPArea extends SCTPArea implements ReducingGroup<Concept, SCTPArea> {

    
    private GroupHierarchy<SCTPArea> reducedPAreaHierarchy;
    
    public ReducedSCTPArea(SCTPArea parea, HashSet<Integer> reducedParentIds, GroupHierarchy<SCTPArea> reducedPAreaHierarchy) {
        super(parea.getId(), (SCTConceptHierarchy)parea.getHierarchy(), reducedParentIds, parea.getRelationships());
        
        this.reducedPAreaHierarchy = reducedPAreaHierarchy;
    }
    
    public GroupHierarchy<SCTPArea> getReducedGroupHierarchy() {
        return reducedPAreaHierarchy;
    }
    
    public HashSet<SCTPArea> getReducedGroups() {
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
