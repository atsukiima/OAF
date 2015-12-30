package edu.njit.cs.saboc.blu.sno.graph.tan;

import edu.njit.cs.saboc.blu.core.graph.BluGraph;
import edu.njit.cs.saboc.blu.core.graph.edges.GraphLevel;
import edu.njit.cs.saboc.blu.core.graph.tan.GenericBluBand;
import edu.njit.cs.saboc.blu.sno.abn.tan.local.SCTBand;
import java.awt.Rectangle;

/**
 *
 * @author Chris
 */
public class SCTBluBand extends GenericBluBand<SCTBand> {
    public SCTBluBand(SCTBand overlapSet, BluGraph g, int aX, GraphLevel parent, Rectangle prefBounds) {
        super(overlapSet, g, aX, parent, prefBounds);
    }
}
