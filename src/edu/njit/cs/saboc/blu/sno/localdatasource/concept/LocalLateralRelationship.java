package edu.njit.cs.saboc.blu.sno.localdatasource.concept;

import SnomedShared.Concept;
import SnomedShared.OutgoingLateralRelationship;

/**
 *
 * @author Chris
 */
public class LocalLateralRelationship extends OutgoingLateralRelationship {

    private int characteristicType;

    public LocalLateralRelationship(Concept relationship, Concept target, int relationshipGroup, int charType) {
        super(relationship, target, relationshipGroup);
        characteristicType = charType;
    }

    public int getCharacteristicType() {
        return characteristicType;
    }
}
