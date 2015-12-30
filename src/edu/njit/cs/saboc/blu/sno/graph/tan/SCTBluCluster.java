
package edu.njit.cs.saboc.blu.sno.graph.tan;

import edu.njit.cs.saboc.blu.core.graph.BluGraph;
import edu.njit.cs.saboc.blu.core.graph.edges.GraphEdge;
import edu.njit.cs.saboc.blu.core.graph.edges.GraphGroupLevel;
import edu.njit.cs.saboc.blu.core.graph.tan.GenericBluBandPartition;
import edu.njit.cs.saboc.blu.core.graph.tan.GenericBluCluster;
import edu.njit.cs.saboc.blu.sno.abn.tan.local.SCTCluster;
import java.util.ArrayList;

/**
 *
 * @author Chris
 */
public class SCTBluCluster extends GenericBluCluster<SCTCluster> {

    public SCTBluCluster(SCTCluster cluster, 
            BluGraph graph, 
            GenericBluBandPartition<SCTBluBand> r,
            int pX, 
            GraphGroupLevel parent, 
            ArrayList<GraphEdge> ie) {
        
        super(cluster, graph, r, pX, parent, ie);
    }
}
