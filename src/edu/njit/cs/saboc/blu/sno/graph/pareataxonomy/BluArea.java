package edu.njit.cs.saboc.blu.sno.graph.pareataxonomy;

import SnomedShared.pareataxonomy.Area;
import edu.njit.cs.saboc.blu.core.graph.BluGraph;
import edu.njit.cs.saboc.blu.core.graph.edges.GraphLevel;
import edu.njit.cs.saboc.blu.core.graph.nodes.GenericContainerEntry;
import java.awt.Rectangle;

/**
 *
 * @author
 */
public class BluArea extends GenericContainerEntry {

    public BluArea(Area area, BluGraph g, int aX, GraphLevel parent, Rectangle prefBounds) {
        super(area, g, aX, parent, prefBounds);
    }

    public Area getArea() {
        return (Area)container;
    }
}
