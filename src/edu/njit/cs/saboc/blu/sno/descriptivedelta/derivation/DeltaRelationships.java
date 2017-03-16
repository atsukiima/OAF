package edu.njit.cs.saboc.blu.sno.descriptivedelta.derivation;

import edu.njit.cs.saboc.blu.sno.descriptivedelta.DeltaRelationship;
import edu.njit.cs.saboc.blu.sno.localdatasource.concept.SCTConcept;
import java.util.Map;
import java.util.Set;

/**
 * The set of delta relationships found in the stated and inferred relationships
 * delta files
 * 
 * @author Chris O
 */
public class DeltaRelationships {
    private final Map<SCTConcept, Set<DeltaRelationship>> statedDeltaRelationships;
    private final Map<SCTConcept, Set<DeltaRelationship>> inferredDeltaRelationships;
    
    public DeltaRelationships(
            Map<SCTConcept, Set<DeltaRelationship>> statedDeltaRelationships, 
            Map<SCTConcept, Set<DeltaRelationship>> inferredDeltaRelationships) {
        
        this.statedDeltaRelationships = statedDeltaRelationships;
        this.inferredDeltaRelationships = inferredDeltaRelationships;
    }

    public Map<SCTConcept, Set<DeltaRelationship>> getStatedDeltaRelationships() {
        return statedDeltaRelationships;
    }

    public Map<SCTConcept, Set<DeltaRelationship>> getInferredDeltaRelationships() {
        return inferredDeltaRelationships;
    }
}
