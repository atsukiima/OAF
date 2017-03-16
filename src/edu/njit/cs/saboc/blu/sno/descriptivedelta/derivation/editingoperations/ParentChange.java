package edu.njit.cs.saboc.blu.sno.descriptivedelta.derivation.editingoperations;

import edu.njit.cs.saboc.blu.sno.localdatasource.concept.SCTConcept;

/**
 * Interface for defining a change to a concept's parent
 * 
 * @author Chris O
 */
public interface ParentChange {
    public SCTConcept getOriginalParent();
}
