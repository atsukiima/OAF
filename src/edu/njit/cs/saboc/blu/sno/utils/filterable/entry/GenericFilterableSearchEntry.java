package edu.njit.cs.saboc.blu.sno.utils.filterable.entry;

import SnomedShared.Concept;
import SnomedShared.SearchResult;
import edu.njit.cs.saboc.blu.core.utils.filterable.list.Filterable;
import edu.njit.cs.saboc.blu.sno.conceptbrowser.Options;
/**
 *
 * @author harsh
 */
public class GenericFilterableSearchEntry extends Filterable<SearchResult> implements NavigableEntry {

    private SearchResult entry;
    private Options options;

    public GenericFilterableSearchEntry(SearchResult entry) {
        this.entry = entry;
    }
    
    public Concept getNavigableConcept() {
        return options.getDataSource().getConceptFromId(entry.getConceptId());
    }
    
    public Concept getNavigateConcept() {
        return getNavigableConcept();
    }
    
    public SearchResult getObject() {
        return entry;
    }

    public String getInitialText() {
        return String.format("<html>%s &nbsp;<font color='blue'>{%s}</font> " +
                "<font color='purple'>--%s</font>", entry.getTerm(),
                entry.getFullySpecifiedName(), Long.toString(entry.getConceptId()));
    }

    public String getFilterText(String filter) {
        return String.format("<html>%s &nbsp; <font color='blue'>{%s}</font> " +
                "<font color='purple'>--%s</font>", filter(entry.getTerm(), filter),
                filter(entry.getFullySpecifiedName(), filter),
                filter(Long.toString(entry.getConceptId()), filter));
    }
    
    public boolean containsFilter(String filter) {
        return entry.getTerm().toLowerCase().contains(filter) ||
                entry.getFullySpecifiedName().toLowerCase().contains(filter) ||
                Long.toString(entry.getConceptId()).contains(filter);
    }
    
    @Override
    public String getClipboardText() {
        return String.format("%s\t%s\t%s", entry.getTerm(), entry.getConceptId(), entry.getFullySpecifiedName());
    }
}
