package edu.njit.cs.saboc.blu.sno.graph.layout;

import edu.njit.cs.saboc.blu.core.graph.BluGraph;
import edu.njit.cs.saboc.blu.core.graph.disjointabn.GenericDisjointAbNLayout;
import edu.njit.cs.saboc.blu.core.graph.edges.GraphEdge;
import edu.njit.cs.saboc.blu.core.graph.edges.GraphGroupLevel;
import edu.njit.cs.saboc.blu.core.graph.nodes.EmptyContainerPartitionEntry;
import edu.njit.cs.saboc.blu.sno.abn.disjointpareataxonomy.DisjointPAreaTaxonomy;
import edu.njit.cs.saboc.blu.sno.abn.disjointpareataxonomy.DisjointPartialArea;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTPArea;
import edu.njit.cs.saboc.blu.sno.graph.pareataxonomy.disjoint.BluDisjointPartialArea;
import java.awt.Color;
import java.util.ArrayList;

/**
 *
 * @author Chris O
 */
public class DisjointPAreaTaxonomyLayout extends GenericDisjointAbNLayout<SCTPArea, DisjointPAreaTaxonomy, DisjointPartialArea, BluDisjointPartialArea> {


    public DisjointPAreaTaxonomyLayout(BluGraph graph, DisjointPAreaTaxonomy taxonomy) {
        super(graph, taxonomy);
    }
    
    @Override
    protected BluDisjointPartialArea createGroupEntry(DisjointPartialArea p, BluGraph graph, int groupX, EmptyContainerPartitionEntry partition, GraphGroupLevel clusterLevel, ArrayList<GraphEdge> edges, Color[] colors) {
        return new BluDisjointPartialArea(p, graph, partition, groupX, clusterLevel, edges, colors);
    }
  
}
