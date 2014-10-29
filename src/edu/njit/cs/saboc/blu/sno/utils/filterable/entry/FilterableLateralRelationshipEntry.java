package edu.njit.cs.saboc.blu.sno.utils.filterable.entry;

import SnomedShared.Concept;
import SnomedShared.OutgoingLateralRelationship;
import edu.njit.cs.saboc.blu.core.utils.filterable.list.Filterable;

/**
 *
 * @author Chris
 */
public class FilterableLateralRelationshipEntry extends Filterable {
    private OutgoingLateralRelationship relationship;

    public FilterableLateralRelationshipEntry(OutgoingLateralRelationship olr) {
        this.relationship = olr;
    }

    public Concept getNavigableConcept() {
        return relationship.getTarget();
    }
    
    public String getInitialText() {
        return String.format("<html>(<font color='green'>%s</font>) <font color='blue'>%s</font> => %s",
                Integer.toString(relationship.getRelationshipGroup()),
                relationship.getRelationship().getName(),
                relationship.getTarget().getName());
    }

    public String getFilterText(String filter) {
        return String.format("<html>(<font color='green'>%s</font>) <font color='blue'>%s</font> => %s",
                filter(Integer.toString(relationship.getRelationshipGroup()), filter),
                filter(relationship.getRelationship().getName(), filter),
                filter(relationship.getTarget().getName(), filter));
    }

}
