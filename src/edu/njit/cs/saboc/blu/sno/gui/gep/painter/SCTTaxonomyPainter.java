package edu.njit.cs.saboc.blu.sno.gui.gep.painter;

import edu.njit.cs.saboc.blu.core.graph.nodes.PartitionedNodeEntry;
import edu.njit.cs.saboc.blu.core.graph.pareataxonomy.AreaEntry;
import edu.njit.cs.saboc.blu.core.gui.gep.utils.drawing.AbNPainter;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;

/**
 *
 * @author Chris O
 */
public class SCTTaxonomyPainter extends AbNPainter {
    public void paintContainerAtPoint(Graphics2D g2d, PartitionedNodeEntry entry, Point p, double scale) {
        AreaEntry area = (AreaEntry)entry;
        
        Stroke savedStroke = g2d.getStroke();

        g2d.setPaint(entry.getBackground());
        
        g2d.fillRect(p.x, p.y, (int)(entry.getWidth() * scale), (int)(entry.getHeight() * scale));
        
        g2d.setStroke(new BasicStroke(2));
        g2d.setPaint(Color.BLACK);
        g2d.drawRect(p.x, p.y, (int)(entry.getWidth() * scale), (int)(entry.getHeight() * scale));
        
        g2d.setStroke(savedStroke);
    }
}
