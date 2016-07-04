package edu.njit.cs.saboc.blu.sno.sctdatasource;

import edu.njit.cs.saboc.blu.core.datastructure.hierarchy.Hierarchy;
import edu.njit.cs.saboc.blu.core.ontology.ConceptHierarchy;
import edu.njit.cs.saboc.blu.sno.localdatasource.concept.SCTConcept;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Chris O
 */
public class SCTConceptHierarchy extends ConceptHierarchy<SCTConcept> {
    public SCTConceptHierarchy(Set<SCTConcept> roots) {
        super(roots);
    }
    
    public SCTConceptHierarchy(SCTConcept root) {
        this(Collections.singleton(root));
    }
    
    public SCTConceptHierarchy(Set<SCTConcept> roots, Map<SCTConcept, Set<SCTConcept>> hierarchy) {
        super(roots, hierarchy);
    }
    
    public SCTConceptHierarchy(SCTConcept root, Map<SCTConcept, Set<SCTConcept>> hierarchy) {
        this(Collections.singleton(root), hierarchy);
    }
    
    public SCTConceptHierarchy(Hierarchy<SCTConcept> hierarchy) {
        super(hierarchy.getRoots(), hierarchy.getAllChildRelationships());
    }
}
