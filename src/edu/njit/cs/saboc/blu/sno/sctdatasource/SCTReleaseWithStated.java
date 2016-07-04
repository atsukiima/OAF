package edu.njit.cs.saboc.blu.sno.sctdatasource;

/**
 *
 * @author Chris
 */
public class SCTReleaseWithStated extends SCTRelease {
    
    private final SCTConceptHierarchy statedHierarchy;
    
    public SCTReleaseWithStated(
            SCTConceptHierarchy conceptHierarchy,
           SCTConceptHierarchy statedHierarchy) {
        
        super(conceptHierarchy);
        
        this.statedHierarchy = statedHierarchy;
    }
    
    public SCTConceptHierarchy getStatedHierarchy() {
        return statedHierarchy;
    }
    
    public boolean supportsStatedRelationships() {
        return true;
    }
}
