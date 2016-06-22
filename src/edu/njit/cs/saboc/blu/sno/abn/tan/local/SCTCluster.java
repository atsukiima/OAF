
package edu.njit.cs.saboc.blu.sno.abn.tan.local;

import SnomedShared.Concept;
import edu.njit.cs.saboc.blu.core.abn.tan.Cluster;
import edu.njit.cs.saboc.blu.sno.datastructure.hierarchy.SCTConceptHierarchy;
import java.util.HashSet;

/**
 *
 * @author Chris
 */
public class SCTCluster extends Cluster<Concept, SCTConceptHierarchy, SCTCluster> {
    public SCTCluster (
            int id, 
            SCTConceptHierarchy conceptHierarchy, 
            HashSet<Integer> parentClusters, 
            HashSet<Concept> patriarchs) {
        
        super(id, conceptHierarchy.getRoot(), conceptHierarchy, parentClusters, patriarchs);
    }
}
