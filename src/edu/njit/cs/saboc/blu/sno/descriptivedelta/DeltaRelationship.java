package edu.njit.cs.saboc.blu.sno.descriptivedelta;

import edu.njit.cs.saboc.blu.sno.localdatasource.concept.AttributeRelationship;
import edu.njit.cs.saboc.blu.sno.localdatasource.concept.SCTConcept;

/**
 * A relationship that may be active or inactive in a given release
 * 
 * @author Chris O
 */
public class DeltaRelationship extends AttributeRelationship {
    
    private final boolean active;
    
    public DeltaRelationship(
            SCTConcept relationshipType, 
            SCTConcept target, 
            int relationshipGroup, 
            long characteristicType, 
            boolean active) {
        
        super(relationshipType, target, relationshipGroup, characteristicType);
        
        this.active = active;
    }
    
    public boolean isActive() {
        return active;
    }
}
