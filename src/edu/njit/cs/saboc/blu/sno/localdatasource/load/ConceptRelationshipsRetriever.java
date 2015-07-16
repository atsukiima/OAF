package edu.njit.cs.saboc.blu.sno.localdatasource.load;

import SnomedShared.Concept;
import edu.njit.cs.saboc.blu.sno.datastructure.hierarchy.SCTConceptHierarchy;
import java.util.HashMap;
import java.util.HashSet;

/**
 *
 * @author cro3
 */
public interface ConceptRelationshipsRetriever {
    public HashMap<Concept, HashSet<Long>> getDefiningRelationships(SCTConceptHierarchy hierarchy);
}
