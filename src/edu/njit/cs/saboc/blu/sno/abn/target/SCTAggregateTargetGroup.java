
package edu.njit.cs.saboc.blu.sno.abn.target;

import SnomedShared.Concept;
import edu.njit.cs.saboc.blu.core.abn.GroupHierarchy;
import edu.njit.cs.saboc.blu.core.abn.aggregate.AggregateableConceptGroup;

import java.util.HashSet;

/**
 *
 * @author Chris O
 */
public class SCTAggregateTargetGroup extends SCTTargetGroup implements AggregateableConceptGroup<Concept, SCTTargetGroup> {


    private GroupHierarchy<SCTTargetGroup> reducedGroupHierarchy;
    
    public SCTAggregateTargetGroup(SCTTargetGroup group, HashSet<Integer> parentIds, GroupHierarchy<SCTTargetGroup> reducedGroupHierarchy) {
        
        super(group.getId(), group.getRoot(), parentIds, group.getGroupSCTConceptHierarchy(), group.getGroupIncomingRelSources());
        
        this.reducedGroupHierarchy = reducedGroupHierarchy;
    }
    
    public GroupHierarchy<SCTTargetGroup> getAggregatedGroupHierarchy() {
        return reducedGroupHierarchy;
    }
    
    public HashSet<SCTTargetGroup> getAggregatedGroups() {
        HashSet<SCTTargetGroup> aggregatedGroups = new HashSet(reducedGroupHierarchy.getNodesInHierarchy());
        aggregatedGroups.removeAll(reducedGroupHierarchy.getRoots());
        
        return aggregatedGroups;
    }
    
    public HashSet<Concept> getAllGroupsConcepts() {
        HashSet<Concept> allConcepts = new HashSet<>();
        allConcepts.addAll(this.getGroupIncomingRelSources().keySet());
        
        for(SCTTargetGroup reducedGroup : reducedGroupHierarchy.getNodesInHierarchy()) {
            allConcepts.addAll(reducedGroup.getGroupIncomingRelSources().keySet());
        }
        
        return allConcepts;
    }

    public HashSet<Concept> getAllGroupsSourceConcepts() {
        HashSet<Concept> allConcepts = new HashSet<Concept>();
        allConcepts.addAll(this.getSourceConcepts());

        for (SCTTargetGroup reducedGroup : reducedGroupHierarchy.getNodesInHierarchy()) {
            allConcepts.addAll(reducedGroup.getSourceConcepts());
        }

        return allConcepts;
    }
    
}
