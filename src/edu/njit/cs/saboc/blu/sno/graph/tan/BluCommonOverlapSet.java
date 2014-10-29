/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.njit.cs.saboc.blu.sno.graph.tan;

import SnomedShared.overlapping.CommonOverlapSet;
import edu.njit.cs.saboc.blu.core.graph.BluGraph;
import edu.njit.cs.saboc.blu.core.graph.edges.GraphLevel;
import edu.njit.cs.saboc.blu.core.graph.nodes.GenericContainerEntry;
import java.awt.Rectangle;

/**
 *
 * @author Chris
 */
public class BluCommonOverlapSet extends GenericContainerEntry {
    public BluCommonOverlapSet(CommonOverlapSet overlapSet, BluGraph g, int aX, GraphLevel parent, Rectangle prefBounds) {
        super(overlapSet, g, aX, parent, prefBounds);
    }

    public CommonOverlapSet getOverlapSet() {
        return (CommonOverlapSet)container;
    }
}
