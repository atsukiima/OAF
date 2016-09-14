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
           Hierarchy<SCTConcept> activeConceptHierarchy,
           Set<SCTConcept> allConcepts,
           Hierarchy<SCTConcept> statedHierarchy) {
        
        super(activeConceptHierarchy, allConcepts);
        
        this.statedHierarchy = statedHierarchy;
    }
    
    public Hierarchy<SCTConcept> getStatedHierarchy() {
        return statedHierarchy;
    }
    
    public boolean supportsStatedRelationships() {
        return true;
    }
}
