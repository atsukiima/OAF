package edu.njit.cs.saboc.blu.sno.graph.pareataxonomy;

import edu.njit.cs.saboc.blu.core.graph.BluGraph;
import edu.njit.cs.saboc.blu.core.graph.edges.GraphLevel;
import edu.njit.cs.saboc.blu.core.graph.pareataxonomy.AreaEntry;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTArea;
import java.awt.Rectangle;

/**
 *
 * @author
 */
public class BluArea extends AreaEntry<SCTArea> {

    public BluArea(SCTArea area, BluGraph g, int aX, GraphLevel parent, Rectangle prefBounds) {
        super(area, g, aX, parent, prefBounds);
    }

}
