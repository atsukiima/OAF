package edu.njit.cs.saboc.blu.sno.abn.target;

import edu.njit.cs.saboc.blu.core.abn.GroupHierarchy;
import edu.njit.cs.saboc.blu.core.abn.aggregate.AggregateAbNGenerator;

import java.util.HashSet;

/**
 *
 * @author Chris O
 */
public class SCTAggregateTargetAbNGenerator extends AggregateAbNGenerator<SCTTargetGroup, SCTAggregateTargetGroup> {
    
    protected SCTAggregateTargetGroup createReducedGroup(SCTTargetGroup targetGroup, 
            HashSet<Integer> reducedParentIds, 
            GroupHierarchy<SCTTargetGroup> reducedGroups) {
        
        return new SCTAggregateTargetGroup(targetGroup, reducedParentIds, reducedGroups);
    }
}
