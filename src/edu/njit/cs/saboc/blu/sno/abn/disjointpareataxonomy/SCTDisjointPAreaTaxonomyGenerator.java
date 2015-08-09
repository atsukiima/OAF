package edu.njit.cs.saboc.blu.sno.abn.disjointpareataxonomy;

import SnomedShared.Concept;
import edu.njit.cs.saboc.blu.core.abn.disjoint.DisjointAbNGenerator;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTPArea;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTPAreaTaxonomy;
import edu.njit.cs.saboc.blu.sno.datastructure.hierarchy.SCTConceptHierarchy;
import java.util.HashMap;
import java.util.HashSet;

/**
 *
 * @author Chris O
 */
public class SCTDisjointPAreaTaxonomyGenerator extends DisjointAbNGenerator<SCTPAreaTaxonomy, SCTPArea, 
        Concept, SCTConceptHierarchy, DisjointPartialArea, DisjointPAreaTaxonomy> {

    @Override
    protected SCTConceptHierarchy createConceptHierarchy(Concept root) {
        return new SCTConceptHierarchy(root);
    }

    @Override
    protected Concept getDisjointGroupRoot(DisjointPartialArea group) {
        return group.getRoot();
    }

    @Override
    protected Concept getGroupRoot(SCTPArea group) {
        return group.getRoot();
    }

    @Override
    protected DisjointPartialArea createDisjointGroup(int id, SCTConceptHierarchy conceptHierarchy, HashSet<Integer> parentIds, HashSet<SCTPArea> overlapsIn) {
        return new DisjointPartialArea(id, conceptHierarchy.getRoot(), conceptHierarchy, parentIds, overlapsIn);
    }

    @Override
    protected DisjointPAreaTaxonomy createDisjointAbN(SCTPAreaTaxonomy abstractionNetwork, HashMap<Integer, DisjointPartialArea> disjointGroups, HashMap<Integer, HashSet<Integer>> groupHierarchy, int levels, HashSet<SCTPArea> allGroups, HashSet<SCTPArea> overlappingGroups) {
        return new DisjointPAreaTaxonomy(abstractionNetwork, disjointGroups, groupHierarchy, levels, allGroups, overlappingGroups);
    }
}
