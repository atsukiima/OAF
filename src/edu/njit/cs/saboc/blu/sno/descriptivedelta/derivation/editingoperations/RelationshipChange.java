
package edu.njit.cs.saboc.blu.sno.descriptivedelta.derivation.editingoperations;

import edu.njit.cs.saboc.blu.sno.localdatasource.concept.SCTConcept;


/**
 * Base class for defining a change to a concept's attribute relationship
 * 
 * @author Chris O
 */
public abstract class RelationshipChange {
    
    private final RelationshipRefinementType refinementType;
    
    private final SCTConcept relType;
    
    private final SCTConcept originalTarget;
        
    public RelationshipChange(SCTConcept relType, 
            SCTConcept originalTarget, 
            RelationshipRefinementType refinement) {
        
        this.refinementType = refinement;
        this.relType = relType;
        this.originalTarget = originalTarget;
    }
    
    public RelationshipRefinementType getRefinementType() {
        return refinementType;
    }

    public SCTConcept getRelType() {
        return relType;
    }

    public SCTConcept getOriginalTarget() {
        return originalTarget;
    }
}
