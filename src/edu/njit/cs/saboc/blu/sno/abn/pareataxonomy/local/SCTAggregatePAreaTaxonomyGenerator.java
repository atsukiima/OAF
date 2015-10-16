package edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local;

import edu.njit.cs.saboc.blu.core.abn.GroupHierarchy;
import edu.njit.cs.saboc.blu.core.abn.aggregate.AggregateAbNGenerator;
import java.util.HashSet;

/**
 *
 * @author Chris O
 */
public class SCTAggregatePAreaTaxonomyGenerator extends AggregateAbNGenerator<SCTPArea, SCTAggregatePArea> {
    protected SCTAggregatePArea createReducedGroup(SCTPArea parea, HashSet<Integer> reducedParentIds, GroupHierarchy<SCTPArea> reducedGroupHierarchy) {
        SCTAggregatePArea reducedPArea = new SCTAggregatePArea(parea, reducedParentIds, reducedGroupHierarchy);

        return reducedPArea;
    }
}
