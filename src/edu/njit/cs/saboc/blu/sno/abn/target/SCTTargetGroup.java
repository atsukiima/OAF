package edu.njit.cs.saboc.blu.sno.abn.target;

import SnomedShared.Concept;
import edu.njit.cs.saboc.blu.core.abn.targetbased.TargetGroup;
import edu.njit.cs.saboc.blu.sno.datastructure.hierarchy.SCTConceptHierarchy;
import java.util.HashMap;
import java.util.HashSet;

/**
 *
 * @author Chris O
 */
public class SCTTargetGroup extends TargetGroup<Concept> {
    
    public SCTTargetGroup(int id, 
            Concept root, 
            HashSet<Integer> parents, 
            SCTConceptHierarchy groupHierarchy, 
            HashMap<Concept, HashSet<Concept>> incomingRelSources) {
        
        super(id, root, parents, groupHierarchy, incomingRelSources);
    }
    
    public SCTConceptHierarchy getGroupSCTConceptHierarchy() {
        return (SCTConceptHierarchy)super.getGroupHierarchy();
    }
}
