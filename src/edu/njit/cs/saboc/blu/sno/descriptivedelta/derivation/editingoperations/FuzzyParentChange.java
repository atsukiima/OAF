package edu.njit.cs.saboc.blu.sno.descriptivedelta.derivation.editingoperations;

import edu.njit.cs.saboc.blu.sno.localdatasource.concept.SCTConcept;
import java.util.Set;

/**
 * A change in a concept's parents where any or all of the potential new parents
 * replaced a single parent
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