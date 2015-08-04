/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.njit.cs.saboc.blu.sno.graph.tan;

import SnomedShared.overlapping.ClusterSummary;
import edu.njit.cs.saboc.blu.core.graph.BluGraph;
import edu.njit.cs.saboc.blu.core.graph.edges.GraphEdge;
import edu.njit.cs.saboc.blu.core.graph.edges.GraphGroupLevel;
import edu.njit.cs.saboc.blu.core.graph.nodes.GenericGroupEntry;
import java.util.ArrayList;

/**
 *
 * @author Chris
 */
public class BluCluster extends GenericGroupEntry {

    public BluCluster(ClusterSummary cluster, BluGraph graph, BluOverlapPartition r,
            int pX, GraphGroupLevel parent, ArrayList<GraphEdge> ie) {
        
        super(cluster, graph, r, pX, parent, ie);
    }

    public ClusterSummary getSummary() {
        return (ClusterSummary) getGroup();
    }
}
