package edu.njit.cs.saboc.blu.sno.localdatasource.conceptdata;

import SnomedShared.Concept;
import java.util.ArrayList;

/**
 *
 * @author Chris
 */
public class HierarchyMetrics {
    private ArrayList<Concept> hierarchies;
    
    private int ancestorCount;
    private int descendantCount;
    
    private int parentCount;
    private int childCount;
    
    private int siblingCount;
    
    private Concept focusConcept;
    
    public HierarchyMetrics(Concept focusConcept, ArrayList<Concept> hierarchies, int ancestorCount, int descendantCount, int parentCount, int childCount, int siblingCount) {
        this.focusConcept = focusConcept;
        this.hierarchies = hierarchies;
        this.ancestorCount = ancestorCount;
        this.descendantCount = descendantCount;
        this.parentCount = parentCount;
        this.childCount = childCount;
        this.siblingCount = siblingCount;
    }

    public ArrayList<Concept> getHierarchies() {
        return hierarchies;
    }

    public int getAncestorCount() {
        return ancestorCount;
    }

    public int getDescendantCount() {
        return descendantCount;
    }

    public int getParentCount() {
        return parentCount;
    }

    public int getChildCount() {
        return childCount;
    }

    public int getSiblingCount() {
        return siblingCount;
    }

    public Concept getFocusConcept() {
        return focusConcept;
    }
}
