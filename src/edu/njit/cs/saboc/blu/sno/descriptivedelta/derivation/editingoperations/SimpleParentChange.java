
package edu.njit.cs.saboc.blu.sno.descriptivedelta.derivation.editingoperations;

import edu.njit.cs.saboc.blu.sno.localdatasource.concept.SCTConcept;

/**
 * Represents a change from one parent to another, where the given 
 * refinement is specified
 * 
 * @author Chris O
 */
public class SimpleParentChange extends SimpleRelationshipChange implements ParentChange {

    public SimpleParentChange(
            SCTConcept isARelType,
            SCTConcept originalParent, 
            SCTConcept newParent,
            RelationshipRefinementType refinement) {
        
        super(isARelType, originalParent, newParent, refinement);
    }

    @Override
    public SCTConcept getOriginalParent() {
        return super.getOriginalTarget();
    }
    
    public SCTConcept getNewParent() {
        return super.getNewTarget();
    }
}
