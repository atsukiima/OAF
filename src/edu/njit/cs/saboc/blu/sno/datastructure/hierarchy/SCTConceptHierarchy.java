package edu.njit.cs.saboc.blu.sno.datastructure.hierarchy;

import SnomedShared.Concept;
import edu.njit.cs.saboc.blu.core.datastructure.hierarchy.MultiRootedHierarchy;
import edu.njit.cs.saboc.blu.core.datastructure.hierarchy.SingleRootedHierarchy;
import java.util.Arrays;
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
    
    public SCTConceptHierarchy(MultiRootedHierarchy<Concept> clses) {
        super(clses.getRoots().iterator().next(), clses.getAllChildRelationships());
    }
    
    public SCTConceptHierarchy getAncestorHierarchy(Concept source) {
        return this.getAncestorHierarchy(new HashSet<>(Arrays.asList(source)));
    }
    
    public SCTConceptHierarchy getAncestorHierarchy(HashSet<Concept> sources) {
        return new SCTConceptHierarchy(super.getAncestorHierarchy(sources));
    }
    
    public SCTConceptHierarchy getDescendantHierarchyWithinDistance(Concept cls, int maxDistance) {
        return new SCTConceptHierarchy(super.getDescendantHierarchyWithinDistance(cls, maxDistance));
    }
    
    public SCTConceptHierarchy getSubhierarchyRootedAt(Concept root) {
        return new SCTConceptHierarchy(root, this.children);
    }
    
    public HashSet<Concept> getConceptsInHierarchy() {
        return super.getNodesInHierarchy();
    }
}
