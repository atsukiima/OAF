package edu.njit.cs.saboc.blu.sno.utils.filterable.entry;

import SnomedShared.Concept;

/**
 *
 * @author Chris
 */
public class IndentedConceptEntry extends FilterableConceptEntry {
    public IndentedConceptEntry(Concept c) {
        super(c);
    }
    
    public String getInitialText() {
        return "<html>&nbsp&nbsp&nbsp&nbsp " + super.getInitialText();
    }

    public String getFilterText(String filter) {
        return "<html>&nbsp&nbsp&nbsp&nbsp " + super.getFilterText(filter);
    }
}
