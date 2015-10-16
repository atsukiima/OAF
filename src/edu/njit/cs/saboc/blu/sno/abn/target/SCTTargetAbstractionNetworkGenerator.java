package edu.njit.cs.saboc.blu.sno.abn.target;

import SnomedShared.Concept;
import edu.njit.cs.saboc.blu.core.abn.GroupHierarchy;
import edu.njit.cs.saboc.blu.core.abn.targetbased.GenericRelationship;
import edu.njit.cs.saboc.blu.core.abn.targetbased.TargetAbstractionNetworkGenerator;
import edu.njit.cs.saboc.blu.sno.datastructure.hierarchy.SCTConceptHierarchy;
import edu.njit.cs.saboc.blu.sno.localdatasource.concept.LocalLateralRelationship;
import edu.njit.cs.saboc.blu.sno.localdatasource.concept.LocalSnomedConcept;
import edu.njit.cs.saboc.blu.sno.sctdatasource.SCTLocalDataSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 *
 * @author Chris O
 */
public class SCTTargetAbstractionNetworkGenerator extends TargetAbstractionNetworkGenerator<Concept, Concept, SCTConceptHierarchy, SCTTargetGroup, 
        SCTTargetAbstractionNetwork> {

    private final SCTLocalDataSource dataSource;
    
    public SCTTargetAbstractionNetworkGenerator(SCTLocalDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public HashSet<GenericRelationship<Concept, Concept>> getConceptRelationships(Concept concept) {
        LocalSnomedConcept c = (LocalSnomedConcept)concept;
        
        ArrayList<LocalLateralRelationship> rels = c.getAttributeRelationships();
        
        HashSet<GenericRelationship<Concept, Concept>> convertedRels = new HashSet<>();
        
        rels.forEach( (LocalLateralRelationship rel) -> {
            if(rel.getCharacteristicType() == 0) {
                convertedRels.add(new GenericRelationship<>(rel.getRelationship(), rel.getTarget()));
            }
        });
        
        return convertedRels;
    }

    public SCTConceptHierarchy getTargetHierarchy(Concept root) {
        return dataSource.getConceptHierarchy().getSubhierarchyRootedAt(root);
    }

    public SCTTargetGroup createGroup(int id, 
            Concept root, 
            HashSet<Integer> parentIds, 
            SCTConceptHierarchy groupHierarchy, 
            HashMap<Concept, HashSet<Concept>> incomingRelSources) {
        
        return new SCTTargetGroup(id, root, parentIds, groupHierarchy, incomingRelSources);
    }

    protected SCTTargetAbstractionNetwork createTargetAbstractionNetwork(
            SCTTargetGroup targetGroup, 
            HashMap<Integer, SCTTargetGroup> groups, 
            GroupHierarchy<SCTTargetGroup> groupHierarchy) {    
        
        return new SCTTargetAbstractionNetwork(targetGroup, groups, groupHierarchy);
    }
    
    protected SCTConceptHierarchy createGroupHierarchy(Concept root) {
        return new SCTConceptHierarchy(root);
    }
    
}
