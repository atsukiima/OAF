package edu.njit.cs.saboc.blu.sno.utils.comparators;

import SnomedShared.SearchResult;
import java.util.Comparator;

/**
 *
 * @author Chris
 */
public class SearchResultComparator implements Comparator<SearchResult> {

    public int compare(SearchResult a, SearchResult b) {
        if (a.getFullySpecifiedName().equals(b.getFullySpecifiedName())) {
            return a.getTerm().compareToIgnoreCase(b.getTerm());
        } else {
            return a.getFullySpecifiedName().compareToIgnoreCase(b.getFullySpecifiedName());
        }
    }
}
