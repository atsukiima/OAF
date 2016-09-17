package edu.njit.cs.saboc.blu.sno.descriptivedelta;

import edu.njit.cs.saboc.blu.sno.descriptivedelta.derivation.editingoperations.FuzzyParentChange;
import edu.njit.cs.saboc.blu.sno.descriptivedelta.derivation.editingoperations.FuzzyRelationshipChange;
import edu.njit.cs.saboc.blu.sno.descriptivedelta.derivation.editingoperations.RelationshipGroupChange;
import edu.njit.cs.saboc.blu.sno.descriptivedelta.derivation.editingoperations.SimpleParentChange;
import edu.njit.cs.saboc.blu.sno.descriptivedelta.derivation.editingoperations.SimpleRelationshipChange;
import edu.njit.cs.saboc.blu.sno.localdatasource.concept.SCTConcept;
import java.util.HashSet;
import java.util.Set;


/**
 *
 * @author Chris O
 */
public class EditingOperationReport {
    
    private final Set<SCTConcept> addedParents = new HashSet<>();
    private final Set<SCTConcept> removedParents = new HashSet<>();
    
    private final Set<SimpleParentChange> moreRefinedParents = new HashSet<>();
    private final Set<SimpleParentChange> lessRefinedParents = new HashSet<>();
    
    private final Set<FuzzyParentChange> changedParents = new HashSet<>();
    
    private final Set<DeltaRelationship> addedRelationships = new HashSet<>();
    private final Set<DeltaRelationship> removedRelationships = new HashSet<>();
    
    private final Set<SimpleRelationshipChange> moreRefinedRelationships = new HashSet<>();
    private final Set<SimpleRelationshipChange> lessRefinedRelationships = new HashSet<>();
    
    private final Set<FuzzyRelationshipChange> changedRelationships = new HashSet<>();
    
    private final Set<RelationshipGroupChange> relGroupChangedRelationships = new HashSet<>();
    
    private final SCTConcept concept;
    
    
    public EditingOperationReport(SCTConcept concept) {
        this.concept = concept;
    }
    
    public void addNewParent(SCTConcept parent) {
        addedParents.add(parent);
    }
    
    public void addRemovedParent(SCTConcept parent) {
        removedParents.add(parent);
    }
    
    public void addMoreRefinedParent(SimpleParentChange parent) {
        moreRefinedParents.add(parent);
    }
    
    public void addLessRefinedParent(SimpleParentChange parent) {
        lessRefinedParents.add(parent);
    }
    
    public void addChangedParent(FuzzyParentChange parent) {
        changedParents.add(parent);
    }
    
    public void addNewRelationships(DeltaRelationship rel) {
        addedRelationships.add(rel);
    }
    
    public void addRemovedRelationship(DeltaRelationship rel) {
        removedRelationships.add(rel);
    }
    
    public void addMoreRefinedRelationship(SimpleRelationshipChange rel) {
        moreRefinedRelationships.add(rel);
    }
    
    public void addLessRefinedRelationship(SimpleRelationshipChange rel) {
        lessRefinedRelationships.add(rel);
    }
    
    public void addChangedRelationship(FuzzyRelationshipChange rel) {
        changedRelationships.add(rel);
    }
    
    public void addRelGroupChangedRelationship(RelationshipGroupChange rel) {
        relGroupChangedRelationships.add(rel);
    }

    public Set<SCTConcept> getAddedParents() {
        return addedParents;
    }

    public Set<SCTConcept> getRemovedParents() {
        return removedParents;
    }

    public Set<SimpleParentChange> getMoreRefinedParents() {
        return moreRefinedParents;
    }

    public Set<SimpleParentChange> getLessRefinedParents() {
        return lessRefinedParents;
    }
    
    public Set<FuzzyParentChange> getChangedParents() {
        return changedParents;
    }

    public Set<DeltaRelationship> getAddedRelationships() {
        return addedRelationships;
    }

    public Set<DeltaRelationship> getRemovedRelationships() {
        return removedRelationships;
    }

    public Set<SimpleRelationshipChange> getMoreRefinedRelationships() {
        return moreRefinedRelationships;
    }

    public Set<SimpleRelationshipChange> getLessRefinedRelationships() {
        return lessRefinedRelationships;
    }
    
    public Set<FuzzyRelationshipChange> getChangedRelationships() {
        return changedRelationships;
    }

    public Set<RelationshipGroupChange> getRelGroupChangedRelationships() {
        return relGroupChangedRelationships;
    }

    public SCTConcept getConcept() {
        return concept;
    }
}
