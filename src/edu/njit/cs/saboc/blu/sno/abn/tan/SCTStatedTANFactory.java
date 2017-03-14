package edu.njit.cs.saboc.blu.sno.abn.tan;

import edu.njit.cs.saboc.blu.core.abn.tan.BandTribalAbstractionNetwork;
import edu.njit.cs.saboc.blu.core.abn.tan.Cluster;
import edu.njit.cs.saboc.blu.core.abn.tan.ClusterTribalAbstractionNetwork;
import edu.njit.cs.saboc.blu.core.abn.tan.TANFactory;
import edu.njit.cs.saboc.blu.core.datastructure.hierarchy.Hierarchy;
import edu.njit.cs.saboc.blu.core.ontology.Concept;
import edu.njit.cs.saboc.blu.sno.sctdatasource.SCTReleaseWithStated;

/**
 * A factory for creating a TAN from the stated hierarchical relationships
 * of concepts.
 * 
 * @author Chris O
 */
public class SCTStatedTANFactory extends TANFactory {
    
    public SCTStatedTANFactory(SCTReleaseWithStated statedRelease) {
        super(statedRelease);
    }

    @Override
    public <T extends Cluster> ClusterTribalAbstractionNetwork createClusterTAN(
            BandTribalAbstractionNetwork bandTAN, 
            Hierarchy<T> clusterHierarchy, 
            Hierarchy<Concept> sourceHierarchy) {
        
        return new SCTStatedTAN(super.createClusterTAN(bandTAN, clusterHierarchy, sourceHierarchy));
    }
}
