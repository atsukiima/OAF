package edu.njit.cs.saboc.blu.sno.utils.comparators;

import SnomedShared.Concept;
import java.util.Comparator;

/**
 *
 * @author Chris
 */
public class ConceptNameComparator implements Comparator<Concept> {
    public int compare(Concept a, Concept b) {
        return a.getName().compareToIgnoreCase(b.getName());
    }
}
