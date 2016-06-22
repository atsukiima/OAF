package edu.njit.cs.saboc.blu.sno.abn.disjointpareataxonomy;

import edu.njit.cs.saboc.blu.core.datastructure.hierarchy.Hierarchy;
import java.util.HashSet;

/**
 *
 * @author Chris
 */
public class DisjointPAreaHierarchy extends Hierarchy<DisjointPartialArea> {

    public DisjointPAreaHierarchy(HashSet<DisjointPartialArea> basisDPAreas) {
        super(basisDPAreas);
    }
}

