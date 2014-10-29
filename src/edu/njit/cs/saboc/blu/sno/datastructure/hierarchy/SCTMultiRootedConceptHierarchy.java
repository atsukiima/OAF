package edu.njit.cs.saboc.blu.sno.datastructure.hierarchy;

import SnomedShared.Concept;
import edu.njit.cs.saboc.blu.core.datastructure.hierarchy.MultiRootedHierarchy;
import edu.njit.cs.saboc.blu.core.datastructure.hierarchy.SingleRootedHierarchy;
import java.util.HashMap;
import java.util.HashSet;

/**
 *
 * @author Chris
 */
public class SCTMultiRootedConceptHierarchy extends MultiRootedHierarchy<Concept> {
    public SCTMultiRootedConceptHierarchy(HashSet<Concept> roots) {
        super(roots);
    }
    
    public SCTMultiRootedConceptHierarchy(HashSet<Concept> roots, HashMap<Concept, HashSet<Concept>> conceptHierarchy) {
        super(roots, conceptHierarchy);
    }
    
    public SCTMultiRootedConceptHierarchy(HashSet<Concept> roots, SCTMultiRootedConceptHierarchy hierarchy) {
        this(roots, hierarchy.children);
    }
    
    public SingleRootedHierarchy<Concept> getSubhierarchyRootedAt(Concept root) {
        return new SCTConceptHierarchy(root, this.children);
    }

    public HashSet<Concept> getConceptsInHierarchy() {
        return super.getNodesInHierarchy();
    }
}
