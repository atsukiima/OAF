package edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local;

import edu.njit.cs.saboc.blu.core.abn.GroupHierarchy;
import edu.njit.cs.saboc.blu.core.abn.reduced.AggregateAbNGenerator;
import java.util.HashSet;

/**
 *
 * @author Chris O
 */
public class ReducedSCTPAreaTaxonomyGenerator extends AggregateAbNGenerator<SCTPArea, ReducedSCTPArea> {
    protected ReducedSCTPArea createReducedGroup(SCTPArea parea, HashSet<Integer> reducedParentIds, GroupHierarchy<SCTPArea> reducedGroupHierarchy) {
        ReducedSCTPArea reducedPArea = new ReducedSCTPArea(parea, reducedParentIds, reducedGroupHierarchy);

        return reducedPArea;
    }
}
