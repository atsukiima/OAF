package edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local;

import edu.njit.cs.saboc.blu.core.abn.reduced.ReducingGroup;
import edu.njit.cs.saboc.blu.sno.datastructure.hierarchy.SCTConceptHierarchy;
import java.util.HashSet;

/**
 *
 * @author Chris O
 */
public class ReducedSCTPArea extends SCTPArea implements ReducingGroup<SCTPArea> {
    
    private HashSet<SCTPArea> reducedPAreas;
    
    public ReducedSCTPArea(SCTPArea parea, HashSet<Integer> reducedParentIds, HashSet<SCTPArea> reducedPAreas) {
        super(parea.getId(), (SCTConceptHierarchy)parea.getHierarchy(), reducedParentIds, parea.getRelationships());
        
        this.reducedPAreas = reducedPAreas;
    }
    
    public HashSet<SCTPArea> getReducedGroups() {
        return reducedPAreas;
    }
}
