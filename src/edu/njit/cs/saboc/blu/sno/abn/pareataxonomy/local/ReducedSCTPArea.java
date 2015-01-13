package edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local;

import SnomedShared.Concept;
import edu.njit.cs.saboc.blu.core.abn.reduced.ReducingGroup;
import edu.njit.cs.saboc.blu.core.datastructure.hierarchy.ConceptGroupHierarchy;
import edu.njit.cs.saboc.blu.sno.datastructure.hierarchy.SCTConceptHierarchy;
import java.util.HashSet;

/**
 *
 * @author Chris O
 */
public class ReducedSCTPArea extends SCTPArea implements ReducingGroup<Concept, SCTPArea> {

    
    private ConceptGroupHierarchy<SCTPArea> reducedPAreaHierarchy;
    
    public ReducedSCTPArea(SCTPArea parea, HashSet<Integer> reducedParentIds, ConceptGroupHierarchy<SCTPArea> reducedPAreaHierarchy) {
        super(parea.getId(), (SCTConceptHierarchy)parea.getHierarchy(), reducedParentIds, parea.getRelationships());
        
        this.reducedPAreaHierarchy = reducedPAreaHierarchy;
    }
    
    public ConceptGroupHierarchy<SCTPArea> getReducedGroupHierarchy() {
        return reducedPAreaHierarchy;
    }
    
    public HashSet<SCTPArea> getReducedGroups() {
        return new HashSet<SCTPArea>(reducedPAreaHierarchy.getNodesInHierarchy());
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
