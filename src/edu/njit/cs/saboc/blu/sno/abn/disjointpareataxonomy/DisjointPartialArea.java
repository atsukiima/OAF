package edu.njit.cs.saboc.blu.sno.abn.disjointpareataxonomy;

import SnomedShared.Concept;
import edu.njit.cs.saboc.blu.core.abn.disjoint.nodes.DisjointGenericConceptGroup;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTPArea;
import edu.njit.cs.saboc.blu.sno.datastructure.hierarchy.SCTConceptHierarchy;
import edu.njit.cs.saboc.blu.sno.utils.comparators.ConceptNameComparator;
import java.util.Comparator;
import java.util.HashSet;

/**
 *
 * @author Chris
 */
public class DisjointPartialArea extends DisjointGenericConceptGroup<SCTPArea, Concept, SCTConceptHierarchy, DisjointPartialArea> {
    
    public DisjointPartialArea(int id, 
            Concept root, 
            SCTConceptHierarchy conceptHierarchy, 
            HashSet<Integer> parentIds,
            HashSet<SCTPArea> overlapsIn) {
        
        super(id, root, conceptHierarchy, parentIds, overlapsIn);
    }
    
    protected Comparator<Concept> getConceptComparator() {
        return new ConceptNameComparator();
    }
}
