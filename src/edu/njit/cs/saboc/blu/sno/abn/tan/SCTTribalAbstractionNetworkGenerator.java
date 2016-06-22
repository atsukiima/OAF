package edu.njit.cs.saboc.blu.sno.abn.tan;

import SnomedShared.Concept;
import edu.njit.cs.saboc.blu.core.abn.GroupHierarchy;
import edu.njit.cs.saboc.blu.core.abn.tan.TribalAbstractionNetworkGenerator;
import edu.njit.cs.saboc.blu.core.datastructure.hierarchy.Hierarchy;
import edu.njit.cs.saboc.blu.sno.abn.tan.local.SCTBand;
import edu.njit.cs.saboc.blu.sno.abn.tan.local.SCTCluster;
import edu.njit.cs.saboc.blu.sno.abn.tan.local.SCTTribalAbstractionNetwork;
import edu.njit.cs.saboc.blu.sno.datastructure.hierarchy.SCTConceptHierarchy;
import edu.njit.cs.saboc.blu.sno.sctdatasource.SCTLocalDataSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;


/**
 *
 * @author Chris
 */
public class SCTTribalAbstractionNetworkGenerator extends TribalAbstractionNetworkGenerator<Concept, SCTConceptHierarchy, SCTTribalAbstractionNetwork, SCTBand, SCTCluster> {
    
    private final String tanName;
    private final SCTLocalDataSource dataSource;
    
    public SCTTribalAbstractionNetworkGenerator(String tanName, SCTLocalDataSource dataSource) {
        this.tanName = tanName;
        this.dataSource = dataSource;
    }
    
    @Override
    protected SCTConceptHierarchy createHierarchy(Concept root) {
        return new SCTConceptHierarchy(root);
    }

    @Override
    protected SCTTribalAbstractionNetwork createTribalAbstractionNetwork(ArrayList<SCTBand> bands, HashMap<Integer, SCTCluster> clusters, GroupHierarchy<SCTCluster> clusterHierarchy, ArrayList<SCTCluster> patriarchs, Hierarchy<Concept> sourceHierarchy) {
        return new SCTTribalAbstractionNetwork(tanName, bands, clusters, clusterHierarchy, patriarchs, sourceHierarchy, dataSource);
    }

    @Override
    protected SCTBand createBand(int id, HashSet<Concept> patriarchs) {
        return new SCTBand(id, patriarchs);
    }

    @Override
    protected SCTCluster createCluster(int id, SCTConceptHierarchy conceptHierarchy, HashSet<Integer> parentClusters, HashSet<Concept> patriarchs) {
        return new SCTCluster(id, conceptHierarchy, parentClusters, patriarchs);
    }
}
