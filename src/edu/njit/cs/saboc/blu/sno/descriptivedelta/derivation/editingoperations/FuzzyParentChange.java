package edu.njit.cs.saboc.blu.sno.descriptivedelta.derivation.editingoperations;

import edu.njit.cs.saboc.blu.sno.localdatasource.concept.SCTConcept;
import java.util.Set;

/**
 *
 * @author Chris O
 */
public class FuzzyParentChange extends FuzzyRelationshipChange implements ParentChange {

    public FuzzyParentChange(
            SCTConcept isARelType, 
            SCTConcept originalParent, 
            Set<SCTConcept> potentialNewParents, 
            RelationshipRefinementType refinement) {
        
        super(isARelType, originalParent, potentialNewParents, refinement);
    }

    @Override
    public SCTConcept getOriginalParent() {
        return super.getOriginalTarget();
    }

    public Set<SCTConcept> getPotentialNewParents() {
        return super.getPotentialNewTargets();
    }
}