package edu.njit.cs.saboc.blu.sno.sctdatasource;

import edu.njit.cs.saboc.blu.core.datastructure.hierarchy.Hierarchy;
import edu.njit.cs.saboc.blu.sno.localdatasource.concept.SCTConcept;
import java.util.Set;

/**
 *
 * @author Chris
 */
public class SCTReleaseWithStated extends SCTRelease {
    
    private final Hierarchy<SCTConcept> statedHierarchy;
    
    public SCTReleaseWithStated(
            SCTReleaseInfo releaseInfo,
           Hierarchy<SCTConcept> activeConceptHierarchy,
           Set<SCTConcept> allConcepts,
           Hierarchy<SCTConcept> statedHierarchy) {
        
        super(releaseInfo, activeConceptHierarchy, allConcepts);
        
        this.statedHierarchy = statedHierarchy;
    }
    
    public Hierarchy<SCTConcept> getStatedHierarchy() {
        return statedHierarchy;
    }
    
    @Override
    public boolean supportsStatedRelationships() {
        return true;
    }
    
    public SCTRelease asSCTRelease() {
        return new SCTRelease(
                this.getReleaseInfo(), 
                this.getStatedHierarchy(), 
                this.getAllConcepts());
    }
}
