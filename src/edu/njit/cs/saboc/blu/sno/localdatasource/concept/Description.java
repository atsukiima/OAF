package edu.njit.cs.saboc.blu.sno.localdatasource.concept;

/**
 *
 * @author Chris
 */
public class Description {
    
    private final String term;

    private final int descriptionType;

    public Description(String term, int descriptionType) {
        this.term = term;
        this.descriptionType = descriptionType;
    }

    public String getTerm() {        
        return term;
    }
    
    public int getDescriptionType() {
        return descriptionType;
    }
}
