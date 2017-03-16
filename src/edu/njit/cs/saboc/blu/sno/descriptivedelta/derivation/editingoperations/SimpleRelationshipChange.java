package edu.njit.cs.saboc.blu.sno.descriptivedelta.derivation.editingoperations;

import edu.njit.cs.saboc.blu.sno.localdatasource.concept.SCTConcept;

/**
 * Represents a change in target for an attribute relationship, where the new target
 * has the specified refinement in relation to the original target.
 * 
 * @author Chris O
 */
public class SimpleRelationshipChange extends RelationshipChange {
    
    private final SCTConcept newTarget;
    
    public SimpleRelationshipChange(SCTConcept relType, 
            SCTConcept originalTarget, 
            SCTConcept newTarget,
            RelationshipRefinementType refinement) {
        
        super(relType, originalTarget, refinement);
        
        this.newTarget = newTarget;
    }
    
    public SCTConcept getNewTarget() {
        return newTarget;
    }
}
