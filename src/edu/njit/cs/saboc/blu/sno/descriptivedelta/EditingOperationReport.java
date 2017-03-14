package edu.njit.cs.saboc.blu.sno.descriptivedelta;

import edu.njit.cs.saboc.blu.sno.descriptivedelta.derivation.editingoperations.FuzzyParentChange;
import edu.njit.cs.saboc.blu.sno.descriptivedelta.derivation.editingoperations.FuzzyRelationshipChange;
import edu.njit.cs.saboc.blu.sno.descriptivedelta.derivation.editingoperations.RelationshipGroupChange;
import edu.njit.cs.saboc.blu.sno.descriptivedelta.derivation.editingoperations.SimpleParentChange;
import edu.njit.cs.saboc.blu.sno.descriptivedelta.derivation.editingoperations.SimpleRelationshipChange;
import edu.njit.cs.saboc.blu.sno.localdatasource.concept.SCTConcept;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;


/**
 * A report on which editing operations where applied on a given concept
 * 
 * @author Chris O
 */
public class EditingOperationReport {
    
    public static enum EditingOperationType {
        AddedParent,
        RemovedParent,
        ChangedParent,
        ParentLessRefined,
        ParentMoreRefined,
        AddedAttributeRelationship,
        RemovedAttributeRelationship,
        ChangedAttributeRelationship,
        AttributeRelationshipMoreRefined,
        AttributeRelationshipLessRefined,
        RelationshipGroupChanged;
        
        @Override
        public String toString() {
            switch (this) {
                case AddedParent:
                    return "Added parent";

                case RemovedParent:
                    return "Removed parent";

                case ChangedParent:
                    return "Changed parent";

                case ParentLessRefined:
                    return "Parent less refined";

                case ParentMoreRefined:
                    return "Parent more refined";

                case AddedAttributeRelationship:
                    return "Added attribute relationship";

                case RemovedAttributeRelationship:
                    return "Removed attribute relationship";

                case ChangedAttributeRelationship:
                    return "Changed attribute relationship target";

                case AttributeRelationshipMoreRefined:
                    return "Attribute relationship target more refined";

                case AttributeRelationshipLessRefined:
                    return "Attribute relationship target less refined";

                case RelationshipGroupChanged:
                    return "Relationship group changed";
                    
                default:
                    return "[UNKNOWN EDITING OPERATION TYPE]";
            }
        }
    }
    
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
    
    public ArrayList<EditingOperationType> getAppliedEditingOperationTypes() {
        ArrayList<EditingOperationType> result = new ArrayList<>();
        
        if(!addedParents.isEmpty()) {
            result.add(EditingOperationType.AddedParent);
        }
        
        if(!removedParents.isEmpty()) {
            result.add(EditingOperationType.RemovedParent);
        }
        
        if(!changedParents.isEmpty()) {
            result.add(EditingOperationType.ChangedParent);
        }
        
        if(!moreRefinedParents.isEmpty()) {
            result.add(EditingOperationType.ParentMoreRefined);
        }
        
        if(!lessRefinedParents.isEmpty()) {
            result.add(EditingOperationType.ParentLessRefined);
        }
        
        if(!addedRelationships.isEmpty()) {
            result.add(EditingOperationType.AddedAttributeRelationship);
        }
        
        if(!removedRelationships.isEmpty()) {
            result.add(EditingOperationType.RemovedAttributeRelationship);
        }
        
        if(!changedRelationships.isEmpty()) {
            result.add(EditingOperationType.ChangedAttributeRelationship);
        }
        
        if(!moreRefinedRelationships.isEmpty()) {
            result.add(EditingOperationType.AttributeRelationshipMoreRefined);
        }
        
        if(!lessRefinedRelationships.isEmpty()) {
            result.add(EditingOperationType.AttributeRelationshipLessRefined);
        }
        
        if(!relGroupChangedRelationships.isEmpty()) {
            result.add(EditingOperationType.RelationshipGroupChanged);
        }
        
        return result;
    }
    
    public int getNumberOfOperationsForType(EditingOperationType type) {
        switch(type) {
            case AddedParent:
                return addedParents.size();
                
            case RemovedParent:
                return removedParents.size();
                
            case ChangedParent:
                return changedParents.size();
                
            case ParentLessRefined:
                return lessRefinedParents.size();
                
            case ParentMoreRefined:
                return moreRefinedParents.size();
                
            case AddedAttributeRelationship:
                return addedRelationships.size();
                
            case RemovedAttributeRelationship:
                return removedRelationships.size();
                
            case ChangedAttributeRelationship:
                return changedRelationships.size();
                
            case AttributeRelationshipMoreRefined:
                return moreRefinedRelationships.size();
                
            case AttributeRelationshipLessRefined:
                return lessRefinedRelationships.size();
                
            case RelationshipGroupChanged:
                return relGroupChangedRelationships.size();
        }
        
        return 0;
    }
    
    public int getNumberOfOperationsApplied() {
        int result = 0;
        
        for(EditingOperationType type : EditingOperationType.values()) {
            result += getNumberOfOperationsForType(type);
        }
        
        return result;
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
