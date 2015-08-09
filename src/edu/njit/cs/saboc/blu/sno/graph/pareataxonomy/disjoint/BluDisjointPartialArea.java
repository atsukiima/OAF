
package edu.njit.cs.saboc.blu.sno.graph.pareataxonomy.disjoint;

import edu.njit.cs.saboc.blu.core.graph.BluGraph;
import edu.njit.cs.saboc.blu.core.graph.edges.GraphEdge;
import edu.njit.cs.saboc.blu.core.graph.edges.GraphGroupLevel;
import edu.njit.cs.saboc.blu.core.graph.disjointabn.BluDisjointGroupEntry;
import edu.njit.cs.saboc.blu.core.graph.nodes.EmptyContainerPartitionEntry;
import edu.njit.cs.saboc.blu.sno.abn.disjointpareataxonomy.DisjointPartialArea;
import java.awt.Color;
import java.util.ArrayList;

/**
 *
 * @author Chris
 */
public class BluDisjointPartialArea extends BluDisjointGroupEntry<DisjointPartialArea> {
    public BluDisjointPartialArea(DisjointPartialArea disjointPartialArea, 
            BluGraph g, 
            EmptyContainerPartitionEntry r, 
            int pX, 
            GraphGroupLevel parent, 
            ArrayList<GraphEdge> ie, 
            Color [] colors) {
        
        super(disjointPartialArea, g, r, pX, parent, ie, colors);
    }
}
