package edu.njit.cs.saboc.blu.sno.graph.pareataxonomy;

import edu.njit.cs.saboc.blu.core.graph.BluGraph;
import edu.njit.cs.saboc.blu.core.graph.edges.GraphEdge;
import edu.njit.cs.saboc.blu.core.graph.edges.GraphGroupLevel;
import edu.njit.cs.saboc.blu.core.graph.pareataxonomy.GenericBluPArea;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTPArea;
import java.util.ArrayList;


/**
 *
 * @author
 */
public class BluPArea extends GenericBluPArea<SCTPArea, BluRegion> {
    public BluPArea(SCTPArea summary, BluGraph g, BluRegion r, int pX, GraphGroupLevel parent, ArrayList<GraphEdge> ie) {

        super(summary, g, r, pX, parent, ie);
    }
}
