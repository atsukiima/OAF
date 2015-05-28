package edu.njit.cs.saboc.blu.sno.utils.filterable.entry;

import SnomedShared.Concept;
import edu.njit.cs.saboc.blu.core.utils.filterable.list.Filterable;


/**
 *
 * @author Chris
 */
public class FilterableConceptEntry extends Filterable<Concept> {

    private Concept concept;

    private boolean showConceptIds = true;

    public FilterableConceptEntry(Concept c) {
        this.concept = c;
    }
    
    public void setShowConceptIds(boolean showConceptIds) {
    	this.showConceptIds = showConceptIds;
    }
    
    public Concept getObject() {
        return concept;
    }
    
    public boolean containsFilter(String filter) {
        return concept.getName().toLowerCase().contains(filter) ||
                Long.toString(concept.getId()).contains(filter);
    }

    public Concept getNavigableConcept() {
        if(concept.getId() != -1) {
            return concept;
        }

        return null;
    }
    
    protected String createEntryStr(String conceptName, String conceptId) {
        if (showConceptIds) {
            return String.format(
                    "<html>%s <font color='blue'>(%s)</font>",
                    conceptName, conceptId);
        } else {
            return String.format(
                    "<html>%s",
                    conceptName);
        }
    }
    
    protected String createEntryStrWithPrimitive(String conceptName, String conceptId) {
        if (showConceptIds) {
            return String.format(
                    "<html>%s <font color ='purple'>[primitive]</font> <font color='blue'>(%s)</font>",
                    conceptName, conceptId);
        } else {
            return String.format(
                    "<html>%s <font color ='purple'>[primitive]</font>",
                    conceptName);
        }
    }

    public String getInitialText() {
        if(concept.primitiveSet() && concept.isPrimitive()) {
            return createEntryStrWithPrimitive(concept.getName(), Long.toString(concept.getId()));
        } else {
            return createEntryStr(concept.getName(), Long.toString(concept.getId()));
        }
    }
    
    public String getFilterText(String filter) {
        if (!filter.isEmpty()) {
            if (concept.primitiveSet() && concept.isPrimitive()) {
                return createEntryStrWithPrimitive(filter(concept.getName(), filter), filter(Long.toString(concept.getId()), filter));
            } else {
                return createEntryStr(filter(concept.getName(), filter), filter(Long.toString(concept.getId()), filter));
            }

        } else {
            return getInitialText();
        }
    }
}
