package edu.njit.cs.saboc.blu.sno.graph.layout;

import SnomedShared.Concept;
import edu.njit.cs.saboc.blu.core.graph.BluGraph;
import edu.njit.cs.saboc.blu.core.graph.edges.GraphEdge;
import edu.njit.cs.saboc.blu.core.graph.edges.GraphGroupLevel;
import edu.njit.cs.saboc.blu.core.graph.edges.GraphLevel;
import edu.njit.cs.saboc.blu.core.graph.tan.GenericBluBandPartition;
import edu.njit.cs.saboc.blu.core.graph.tan.TANLayout;
import edu.njit.cs.saboc.blu.sno.abn.tan.local.SCTBand;
import edu.njit.cs.saboc.blu.sno.abn.tan.local.SCTCluster;
import edu.njit.cs.saboc.blu.sno.abn.tan.local.SCTTribalAbstractionNetwork;
import edu.njit.cs.saboc.blu.sno.graph.tan.SCTBluCluster;
import edu.njit.cs.saboc.blu.sno.graph.tan.SCTBluBand;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.tan.configuration.SCTTANConfiguration;
import java.awt.Rectangle;
import java.util.ArrayList;

/**
 *
 * @author Chris
 */
public class ClusterNoPartitionLayout extends TANLayout<Concept, SCTBand, SCTCluster, SCTBluBand, SCTBluCluster> {

    public ClusterNoPartitionLayout(BluGraph graph, SCTTribalAbstractionNetwork tan, SCTTANConfiguration config) {
        super(graph, tan, config);
    }
    
    @Override
    protected SCTBluBand makeBandNode(SCTBand band, BluGraph g, int aX, GraphLevel parent, Rectangle prefBounds) {
        return new SCTBluBand(band, g, aX, parent, prefBounds);
    }

    @Override
    protected SCTBluCluster makeClusterNode(SCTCluster cluster, BluGraph graph, GenericBluBandPartition r, int pX, GraphGroupLevel parent, ArrayList<GraphEdge> ie) {
        return new SCTBluCluster(cluster, graph, r, pX, parent, ie);
    }
}
