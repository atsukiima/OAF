package edu.njit.cs.saboc.blu.sno.abn.disjointpareataxonomy;

import edu.njit.cs.saboc.blu.core.datastructure.hierarchy.MultiRootedHierarchy;
import edu.njit.cs.saboc.blu.core.datastructure.hierarchy.SingleRootedHierarchy;
import java.util.HashSet;

/**
 *
 * @author Chris
 */
public class DisjointPAreaHierarchy extends MultiRootedHierarchy<DisjointPartialArea> {

    public DisjointPAreaHierarchy(HashSet<DisjointPartialArea> basisDPAreas) {
        super(basisDPAreas);
    }
    
    public SingleRootedHierarchy<DisjointPartialArea> getSubhierarchyRootedAt(DisjointPartialArea dparea) {
        return null;
    }
}

