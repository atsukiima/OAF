package edu.njit.cs.saboc.blu.sno.descriptivedelta.derivation.editingoperations;

import edu.njit.cs.saboc.blu.sno.localdatasource.concept.SCTConcept;

/**
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
