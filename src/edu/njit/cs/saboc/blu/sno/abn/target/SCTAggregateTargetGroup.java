
package edu.njit.cs.saboc.blu.sno.abn.target;

import SnomedShared.Concept;
import edu.njit.cs.saboc.blu.core.abn.reduced.ReducingGroup;
import edu.njit.cs.saboc.blu.core.datastructure.hierarchy.ConceptGroupHierarchy;
import java.util.HashSet;

/**
 *
 * @author Chris O
 */
public class SCTAggregateTargetGroup extends SCTTargetGroup implements ReducingGroup<Concept, SCTTargetGroup> {

    private ConceptGroupHierarchy<SCTTargetGroup> reducedGroupHierarchy;
    
    public SCTAggregateTargetGroup(SCTTargetGroup group, HashSet<Integer> parentIds, ConceptGroupHierarchy<SCTTargetGroup> reducedGroupHierarchy) {
        
        super(group.getId(), group.getRoot(), parentIds, group.getGroupSCTConceptHierarchy(), group.getGroupIncomingRelSources());
        
        this.reducedGroupHierarchy = reducedGroupHierarchy;
    }
    
    public ConceptGroupHierarchy<SCTTargetGroup> getReducedGroupHierarchy() {
        return reducedGroupHierarchy;
    }
    
    public HashSet<SCTTargetGroup> getReducedGroups() {
        return new HashSet(reducedGroupHierarchy.getNodesInHierarchy());
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
