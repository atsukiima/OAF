package edu.njit.cs.saboc.blu.sno.abn.target;

import edu.njit.cs.saboc.blu.core.abn.reduced.ReducedAbNGenerator;
import edu.njit.cs.saboc.blu.core.datastructure.hierarchy.ConceptGroupHierarchy;
import java.util.HashSet;

/**
 *
 * @author Chris O
 */
public class SCTAggregateTargetAbNGenerator extends ReducedAbNGenerator<SCTTargetGroup> {
    protected SCTTargetGroup createReducedGroup(SCTTargetGroup targetGroup, HashSet<Integer> reducedParentIds, ConceptGroupHierarchy<SCTTargetGroup> reducedGroups) {
        return new SCTAggregateTargetGroup(targetGroup, reducedParentIds, reducedGroups);
    }
}
