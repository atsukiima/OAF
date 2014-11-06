package edu.njit.cs.saboc.blu.sno.localdatasource.concept;

import java.util.ArrayList;

/**
 *
 * @author Chris
 */
public class LocalSCTConceptStated extends LocalSnomedConcept {
    private ArrayList<LocalLateralRelationship> statedAttributeRelationships;
    
    public LocalSCTConceptStated(long id, String name, boolean isPrimitive) {
        super(id, name, isPrimitive);
    }
    
    public void setStatedRelationships(ArrayList<LocalLateralRelationship> statedAttributeRelationships) {
        this.statedAttributeRelationships = statedAttributeRelationships;
    }
    
    public ArrayList<LocalLateralRelationship> getStatedRelationships() {
        return statedAttributeRelationships;
    }
}
