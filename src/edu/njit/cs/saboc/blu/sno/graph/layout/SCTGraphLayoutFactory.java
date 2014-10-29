
package edu.njit.cs.saboc.blu.sno.graph.layout;

import edu.njit.cs.saboc.blu.sno.graph.ClusterBluGraph;
import edu.njit.cs.saboc.blu.sno.graph.PAreaBluGraph;

/**
 *
 * @author Chris
 */
public class SCTGraphLayoutFactory {

    private SCTGraphLayoutFactory() {
        
    }

    public static NoRegionsLayout createNoRegionsPAreaLayout(PAreaBluGraph graph) {
        return new NoRegionsLayout(graph, graph.getPAreaTaxonomy());
    }

    public static RegionsLayout createRegionsPAreaLayout(PAreaBluGraph graph) {
        return new RegionsLayout(graph, graph.getPAreaTaxonomy());
    }

    public static ClusterPartitionLayout createClusterPartitionLayout(ClusterBluGraph graph) {
        return new ClusterPartitionLayout(graph);
    }

    public static ClusterNoPartitionLayout createClusterNoPartitionLayout(ClusterBluGraph graph) {
        return new ClusterNoPartitionLayout(graph);
    }
}
