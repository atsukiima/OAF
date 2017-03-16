package edu.njit.cs.saboc.blu.sno.descriptivedelta.derivation.editingoperations;

import edu.njit.cs.saboc.blu.sno.localdatasource.concept.SCTConcept;
import java.util.Set;

/**
 * A change where its
 * 
 * @author Chris O
 */
public class FuzzyRelationshipChange extends RelationshipChange {
    private final Set<SCTConcept> potentialNewTargets;
    
    public FuzzyRelationshipChange(SCTConcept relType, 
            SCTConcept originalTarget, 
            Set<SCTConcept> potentialNewTargets,
            RelationshipRefinementType refinement) {
        
        super(relType, originalTarget, refinement);
        
        this.potentialNewTargets = potentialNewTargets;
    }
    
    public Set<SCTConcept> getPotentialNewTargets() {
        return potentialNewTargets;
    }
}
