package edu.njit.cs.saboc.blu.sno.utils.filterable.entry;

import SnomedShared.Concept;
import edu.njit.cs.saboc.blu.core.utils.filterable.list.Filterable;

/**
 *
 * @author Chris
 */
public class FilterableSynonymEntry extends Filterable {
    private String synonym;

    public FilterableSynonymEntry(String synonym) {
        this.synonym = synonym;
    }

    public Concept getNavigableConcept() {
        return null;
    }

    public String getInitialText() {
        return synonym;
    }

    public String getFilterText(String filter) {
        return getInitialText();
    }
}
