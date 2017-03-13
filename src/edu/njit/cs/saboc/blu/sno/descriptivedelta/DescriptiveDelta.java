
package edu.njit.cs.saboc.blu.sno.descriptivedelta;

import edu.njit.cs.saboc.blu.sno.localdatasource.concept.SCTConcept;
import edu.njit.cs.saboc.blu.sno.sctdatasource.SCTRelease;
import java.util.Map;
import java.util.Set;

/**
 * Stores the set of editing operations obtained by applying the descriptive delta process on a
 * set of delta relationships.
 * 
 * @author Chris O
 */
public class DescriptiveDelta {
    
    private final SCTRelease fromRelease;
    private final SCTRelease toRelease;
    
    private final SCTConcept subhierarchyRoot;
    
    private final Set<SCTConcept> retiredConcepts;
    private final Set<SCTConcept> activatedConcepts;
    
    private final Set<SCTConcept> conceptsRemovedFromSubhierarchy;
    private final Set<SCTConcept> conceptsAddedToSubhierarchy;
    
    private final int statedDeltaEntryCount;
    private final int inferredDeltaEntryCount;
    
    private final Map<SCTConcept, EditingOperationReport> statedEditingOperations;
    
    private final Map<SCTConcept, EditingOperationReport> inferredChanges;
    
    public DescriptiveDelta(
            SCTRelease fromRelease, 
            SCTRelease toRelease, 
            SCTConcept subhierarchyRoot,
            Set<SCTConcept> retiredConcepts,
            Set<SCTConcept> activatedConcepts,
            Set<SCTConcept> conceptsRemovedFromSubhierarchy,
            Set<SCTConcept> conceptsAddedToSubhierarchy,
            int statedDeltaEntryCount,
            int inferredDeltaEntryCount, 
            Map<SCTConcept, EditingOperationReport> statedEditingOperations, 
            Map<SCTConcept, EditingOperationReport> inferredChanges) {
        
        this.fromRelease = fromRelease;
        this.toRelease = toRelease;
        
        this.subhierarchyRoot = subhierarchyRoot;
        
        this.retiredConcepts = retiredConcepts;
        this.activatedConcepts = activatedConcepts;
        
        this.conceptsRemovedFromSubhierarchy = conceptsRemovedFromSubhierarchy;
        this.conceptsAddedToSubhierarchy = conceptsAddedToSubhierarchy;
        
        this.statedDeltaEntryCount = statedDeltaEntryCount;
        this.inferredDeltaEntryCount = inferredDeltaEntryCount;
        
        this.statedEditingOperations = statedEditingOperations;
        this.inferredChanges = inferredChanges;
    }

    public SCTRelease getFromRelease() {
        return fromRelease;
    }

    public SCTRelease getToRelease() {
        return toRelease;
    }

    public SCTConcept getSubhierarchyRoot() {
        return subhierarchyRoot;
    }

    public Set<SCTConcept> getRetiredConcepts() {
        return retiredConcepts;
    }

    public Set<SCTConcept> getActivatedConcepts() {
        return activatedConcepts;
    }

    public Set<SCTConcept> getConceptsRemovedFromSubhierarchy() {
        return conceptsRemovedFromSubhierarchy;
    }

    public Set<SCTConcept> getConceptsAddedToSubhierarchy() {
        return conceptsAddedToSubhierarchy;
    }

    public int getStatedDeltaEntryCount() {
        return statedDeltaEntryCount;
    }

    public int getInferredDeltaEntryCount() {
        return inferredDeltaEntryCount;
    }

    public Map<SCTConcept, EditingOperationReport> getStatedEditingOperations() {
        return statedEditingOperations;
    }

    public Map<SCTConcept, EditingOperationReport> getInferredChanges() {
        return inferredChanges;
    }
}
