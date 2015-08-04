package edu.njit.cs.saboc.blu.sno.localdatasource.concept;

import java.util.ArrayList;

/**
 *
 * @author Chris
 */
public class LocalSCTConceptStated extends LocalSnomedConcept {
    private ArrayList<LocalLateralRelationship> statedAttributeRelationships;
    
    public LocalSCTConceptStated(long id, boolean isPrimitive, boolean isActive) {
        super(id, isPrimitive, isActive);
    }
    
    public void setStatedRelationships(ArrayList<LocalLateralRelationship> statedAttributeRelationships) {
        this.statedAttributeRelationships = statedAttributeRelationships;
    }
    
    public ArrayList<LocalLateralRelationship> getStatedRelationships() {
        return statedAttributeRelationships;
    }
}
