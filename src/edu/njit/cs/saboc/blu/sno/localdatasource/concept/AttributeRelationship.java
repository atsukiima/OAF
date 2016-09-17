package edu.njit.cs.saboc.blu.sno.localdatasource.concept;

/**
 *
 * @author Chris
 */
public class AttributeRelationship {

    private final SCTConcept relationshipType;
    
    private final SCTConcept target;
    
    private final int relationshipGroup;
    
    private final long characteristicType;

    public AttributeRelationship(SCTConcept relationshipType, SCTConcept target, int relationshipGroup, long characteristicType) {
        this.relationshipType = relationshipType;
        this.target = target;
        this.relationshipGroup = relationshipGroup;
        this.characteristicType = characteristicType;
    }
    
    public boolean isDefining() {
        return characteristicType == 900000000000011006l || characteristicType == 900000000000010007l;
    }
    
    public SCTConcept getType() {
        return relationshipType;
    }
    
    public SCTConcept getTarget() {
        return target;
    }
    
    public int getGroup() {
        return relationshipGroup;
    }

    public long getCharacteristicType() {
        return characteristicType;
    }
}
