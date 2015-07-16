package edu.njit.cs.saboc.blu.sno.localdatasource.load;

import SnomedShared.Concept;
import edu.njit.cs.saboc.blu.sno.datastructure.hierarchy.SCTConceptHierarchy;
import edu.njit.cs.saboc.blu.sno.localdatasource.concept.LocalLateralRelationship;
import edu.njit.cs.saboc.blu.sno.localdatasource.concept.LocalSCTConceptStated;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;

/**
 *
 * @author Chris
 */
public class StatedRelationshipsRetriever implements ConceptRelationshipsRetriever {

    public HashMap<Concept, HashSet<Long>> getDefiningRelationships(SCTConceptHierarchy hierarchy) {
        HashMap<Concept, HashSet<Long>> definingAttributeRels = new HashMap<Concept, HashSet<Long>>();

        HashSet<Concept> hierarchyConcepts = hierarchy.getConceptsInHierarchy();
        
        HashMap<Concept, Integer> parentCounts = new HashMap<Concept, Integer>();
        
        Concept root = hierarchy.getRoot();
        
        for(Concept concept : hierarchyConcepts) {
            parentCounts.put(concept, hierarchy.getParents(concept).size());
            definingAttributeRels.put(concept, new HashSet<Long>());
        }
        
        Queue<Concept> queue = new LinkedList<Concept>();
        queue.add(root);
        
        while(!queue.isEmpty()) {
            LocalSCTConceptStated concept = (LocalSCTConceptStated)queue.remove();
            
            ArrayList<LocalLateralRelationship> statedRelationships = concept.getStatedRelationships();
            
            for (LocalLateralRelationship lr : statedRelationships) {
                if (lr.getCharacteristicType() == 0) {       
                    definingAttributeRels.get(concept).add(lr.getRelationship().getId());
                }
            }
            
            HashSet<Concept> parents = hierarchy.getParents(concept);
            
            for(Concept parent : parents) {
                HashSet<Long> parentRelationships = definingAttributeRels.get(parent);
                definingAttributeRels.get(concept).addAll(parentRelationships);
            }
            
            HashSet<Concept> children = hierarchy.getChildren(concept);
            
            for(Concept child : children) {
                int childParentCount = parentCounts.get(child);
                
                if(childParentCount - 1 == 0) {
                    queue.add(child);
                } else {
                    parentCounts.put(child, childParentCount - 1);
                }
            }
        }

        return definingAttributeRels;
    }
}
