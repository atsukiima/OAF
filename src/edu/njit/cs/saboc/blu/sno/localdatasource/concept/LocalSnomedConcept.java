package edu.njit.cs.saboc.blu.sno.localdatasource.concept;

import java.util.ArrayList;

/**
 *
 * @author Chris
 */
public class LocalSnomedConcept extends SnomedShared.Concept {

    private ArrayList<Description> descList = new ArrayList<>();
    
    private ArrayList<LocalLateralRelationship> lateralRelationships = new ArrayList<>();
    
    private final boolean isActive;

    public LocalSnomedConcept(long id, boolean isPrimitive, boolean isActive) {
        super(id, "", isPrimitive);
        
        this.isActive = isActive;
    }
    
    public boolean isActive() {
        return isActive;
    }

    public void setLateralRelationships(ArrayList<LocalLateralRelationship> rels) {
        this.lateralRelationships = rels;
    }

    public void setDescriptions(ArrayList<Description> descriptions) {
        this.descList = descriptions;
    }

    public ArrayList<LocalLateralRelationship> getAttributeRelationships() {
        return lateralRelationships;
    }
    
    public ArrayList<Description> getDescriptions() {
        return descList;
    }
    
    @Override
    public String getName() {
        for(Description d : descList) {
            if(d.getDescriptionType() == 3) {
                return d.getTerm();
            }
        }
        
        return "NAME ERROR";
    }
}
