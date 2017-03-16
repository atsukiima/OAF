package edu.njit.cs.saboc.blu.sno.descriptivedelta.derivation.editingoperations;

import edu.njit.cs.saboc.blu.sno.localdatasource.concept.SCTConcept;
import java.util.Set;


/**
 * Represents a change in an attribute relationship's relationship group.
 * The attribute relationship went from one group to one of the 
 * potential new groups.
 * 
 * @author Chris O
 */
public class RelationshipGroupChange {
    
    private final SCTConcept relType;
    private final SCTConcept target;
    
    private final int originalRelGroup;
    private final Set<Integer> potentialNewGroups;
    
    public RelationshipGroupChange(
            SCTConcept relType, 
            SCTConcept target, 
            int originalRelGroup, 
            Set<Integer> potentialNewGroups) {
        
        this.relType = relType;
        this.target = target;
        this.originalRelGroup = originalRelGroup;
        this.potentialNewGroups = potentialNewGroups;
    }
    
    public SCTConcept getRelType() {
        return relType;
    }

    public SCTConcept getTarget() {
        return target;
    }

    public int getOriginalRelGroup() {
        return originalRelGroup;
    }

    public Set<Integer> getPotentialNewRelGroups() {
        return potentialNewGroups;
    }
}
