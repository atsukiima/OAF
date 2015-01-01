package edu.njit.cs.saboc.blu.sno.graph.pareataxonomy;

import SnomedShared.pareataxonomy.PAreaSummary;
import edu.njit.cs.saboc.blu.core.graph.BluGraph;
import edu.njit.cs.saboc.blu.core.graph.edges.GraphEdge;
import edu.njit.cs.saboc.blu.core.graph.edges.GraphGroupLevel;
import edu.njit.cs.saboc.blu.core.graph.nodes.GenericGroupEntry;
import java.util.ArrayList;
import javax.swing.event.MouseInputListener;


/**
 *
 * @author
 */
public class BluPArea extends GenericGroupEntry implements MouseInputListener {
    public BluPArea(PAreaSummary summary, BluGraph g, BluRegion r, int pX, GraphGroupLevel parent, ArrayList<GraphEdge> ie) {

        super(summary, g, r, pX, parent, ie, false);
        
        this.addMouseListener(this);
    }

    public PAreaSummary getSummary() {
        return (PAreaSummary)getGroup();
    }
}
