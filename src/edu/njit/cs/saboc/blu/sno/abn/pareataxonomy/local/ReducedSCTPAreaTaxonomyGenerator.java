package edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local;

import SnomedShared.Concept;
import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.GenericParentPAreaInfo;
import edu.njit.cs.saboc.blu.core.abn.reduced.ReducedAbNGenerator;
import edu.njit.cs.saboc.blu.core.datastructure.hierarchy.ConceptGroupHierarchy;
import java.util.HashSet;

/**
 *
 * @author Chris O
 */
public class ReducedSCTPAreaTaxonomyGenerator extends ReducedAbNGenerator<SCTPArea> {
    protected SCTPArea createReducedGroup(SCTPArea parea, HashSet<Integer> reducedParentIds, ConceptGroupHierarchy<SCTPArea> reducedGroupHierarchy) {
        ReducedSCTPArea reducedPArea = new ReducedSCTPArea(parea, reducedParentIds, reducedGroupHierarchy);
        
        reducedPArea.setParentPAreaInfo(new HashSet<GenericParentPAreaInfo<Concept, SCTPArea>>());

        return reducedPArea;
    }
}
