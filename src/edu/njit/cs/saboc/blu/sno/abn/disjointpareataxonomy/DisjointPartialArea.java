/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.njit.cs.saboc.blu.sno.abn.disjointpareataxonomy;

import SnomedShared.Concept;
import edu.njit.cs.saboc.blu.core.abn.disjoint.nodes.DisjointGenericConceptGroup;
import edu.njit.cs.saboc.blu.core.datastructure.hierarchy.SingleRootedHierarchy;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTPArea;
import edu.njit.cs.saboc.blu.sno.datastructure.hierarchy.SCTConceptHierarchy;
import edu.njit.cs.saboc.blu.sno.utils.comparators.ConceptNameComparator;
import java.util.Comparator;
import java.util.HashSet;

/**
 *
 * @author Chris
 */
public class DisjointPartialArea extends DisjointGenericConceptGroup<SCTPArea, Concept, DisjointPartialArea> {
    
    public DisjointPartialArea(Concept root, HashSet<SCTPArea> overlapsIn) {
        super(root, overlapsIn);
    }
    
    protected Comparator<Concept> getConceptComparator() {
        return new ConceptNameComparator();
    }
    
    protected SingleRootedHierarchy<Concept> createGroupHierarchy(Concept root) {
        return new SCTConceptHierarchy(root);
    }
}
