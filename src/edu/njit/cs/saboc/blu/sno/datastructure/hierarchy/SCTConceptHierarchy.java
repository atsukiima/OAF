package edu.njit.cs.saboc.blu.sno.datastructure.hierarchy;

import SnomedShared.Concept;
import edu.njit.cs.saboc.blu.core.datastructure.hierarchy.SingleRootedHierarchy;
import java.util.HashMap;
import java.util.HashSet;

/**
 *
 * @author Chris
 */
public class SCTConceptHierarchy extends SingleRootedHierarchy<Concept> {
       
    public SCTConceptHierarchy(Concept root) {
        super(root);
    }
    
    public SCTConceptHierarchy(Concept root, HashMap<Concept, HashSet<Concept>> conceptHierarchy) {
        super(root, conceptHierarchy);
    }
    
    public SCTConceptHierarchy(Concept root, SCTConceptHierarchy hierarchy) {
        super(root, hierarchy.children);
    }
    
    public SingleRootedHierarchy<Concept> getSubhierarchyRootedAt(Concept root) {
        return new SCTConceptHierarchy(root, this.children);
    }
    
    public HashSet<Concept> getConceptsInHierarchy() {
        return super.getNodesInHierarchy();
    }
}
