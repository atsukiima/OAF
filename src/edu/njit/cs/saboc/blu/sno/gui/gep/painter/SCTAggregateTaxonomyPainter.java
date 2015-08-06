package edu.njit.cs.saboc.blu.sno.gui.gep.painter;

import edu.njit.cs.saboc.blu.core.graph.nodes.GenericContainerEntry;
import edu.njit.cs.saboc.blu.core.gui.gep.utils.drawing.ReducedAbNPainter;
import java.awt.Graphics2D;
import java.awt.Point;

/**
 *
 * @author Chris O
 */
public class SCTAggregateTaxonomyPainter extends ReducedAbNPainter {
    public void paintContainerAtPoint(Graphics2D g2d, GenericContainerEntry entry, Point p, double scale) {
        SCTTaxonomyPainter painter = new SCTTaxonomyPainter();
        painter.paintContainerAtPoint(g2d, entry, p, scale);
    }
}
