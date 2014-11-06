package edu.njit.cs.saboc.blu.sno.localdatasource.load;

import SnomedShared.Concept;
import edu.njit.cs.saboc.blu.sno.datastructure.hierarchy.SCTConceptHierarchy;
import edu.njit.cs.saboc.blu.sno.localdatasource.concept.LocalLateralRelationship;
import edu.njit.cs.saboc.blu.sno.localdatasource.concept.LocalSnomedConcept;
import edu.njit.cs.saboc.blu.sno.sctdatasource.SCTLocalDataSource;
import java.util.HashMap;
import java.util.HashSet;

/**
 *
 * @author Chris
 */
public class InferredRelationshipsRetriever implements ConceptRelationshipsRetriever {
    
    public HashMap<Concept, HashSet<Long>> getDefiningRelationships(SCTConceptHierarchy hierarchy) {
        HashMap<Concept, HashSet<Long>> definingAttributeRels = new HashMap<Concept, HashSet<Long>>();
        
        HashSet<Concept> hierarchyConcepts = hierarchy.getConceptsInHierarchy();
        
        for (Concept concept : hierarchyConcepts) {
            definingAttributeRels.put(concept, new HashSet<Long>());
            
            LocalSnomedConcept localConcept = (LocalSnomedConcept)concept;
            
            for (LocalLateralRelationship lr : localConcept.getAttributeRelationships()) {
                if (lr.getCharacteristicType() == 0) {       
                    definingAttributeRels.get(concept).add(lr.getRelationship().getId());
                }
            }

        }

        return definingAttributeRels;
    }
    
    public SCTConceptHierarchy getConceptHierarchy(SCTLocalDataSource dataSource, Concept root) {
        return (SCTConceptHierarchy)dataSource.getConceptHierarchy().getSubhierarchyRootedAt(root);
    }
}
