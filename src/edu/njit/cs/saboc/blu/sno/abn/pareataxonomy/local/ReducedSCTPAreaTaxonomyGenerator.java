package edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local;

import edu.njit.cs.saboc.blu.core.abn.reduced.ReducedAbNGenerator;
import java.util.HashSet;

/**
 *
 * @author Chris O
 */
public class ReducedSCTPAreaTaxonomyGenerator extends ReducedAbNGenerator<SCTPArea> {
    protected SCTPArea createReducedGroup(SCTPArea targetGroup, HashSet<Integer> reducedParentIds, HashSet<SCTPArea> reducedGroups) {
        return new ReducedSCTPArea(targetGroup, reducedParentIds, reducedGroups);
    }
}
