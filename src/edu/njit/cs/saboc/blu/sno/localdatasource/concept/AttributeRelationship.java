package edu.njit.cs.saboc.blu.sno.localdatasource.concept;

/**
 *
 * @author Chris
 */
public class AttributeRelationship {

    private final SCTConcept relationshipType;
    
    private final SCTConcept target;
    
    private final int relationshipGroup;
    
    private final int characteristicType;

    public AttributeRelationship(SCTConcept relationshipType, SCTConcept target, int relationshipGroup, int characteristicType) {
        
        this.relationshipType = relationshipType;
        this.target = target;
        this.relationshipGroup = relationshipGroup;
        this.characteristicType = characteristicType;
    }
    
    public SCTConcept getRelationshipType() {
        return relationshipType;
    }
    
    public SCTConcept getTarget() {
        return target;
    }
    
    public int getRelationshipGroup() {
        return relationshipGroup;
    }

    public int getCharacteristicType() {
        return characteristicType;
    }
}
