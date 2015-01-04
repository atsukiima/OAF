package edu.njit.cs.saboc.blu.sno.gui.gep.painter;

import edu.njit.cs.saboc.blu.core.graph.nodes.GenericContainerEntry;
import edu.njit.cs.saboc.blu.core.gui.gep.utils.drawing.AbNPainter;
import edu.njit.cs.saboc.blu.sno.graph.pareataxonomy.BluArea;
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
    public void paintContainerAtPoint(Graphics2D g2d, GenericContainerEntry entry, Point p, double scale) {
        BluArea area = (BluArea)entry;
        
        Stroke savedStroke = g2d.getStroke();
        
        if(area.getArea().isImplicit()) {
            Color color = entry.getBackground();
            
            int r = (int)Math.min(color.getRed() * 0.6, 255);
            int g = (int)Math.min(color.getGreen() * 0.6, 255);
            int b = (int)Math.min(color.getBlue() * 0.6, 255);
            
            g2d.setPaint(new Color(r, g, b));
        } else {
            g2d.setPaint(entry.getBackground());
        }
        
        g2d.fillRect(p.x, p.y, (int)(entry.getWidth() * scale), (int)(entry.getHeight() * scale));
        
        g2d.setStroke(new BasicStroke(2));
        g2d.setPaint(Color.BLACK);
        g2d.drawRect(p.x, p.y, (int)(entry.getWidth() * scale), (int)(entry.getHeight() * scale));
        
        g2d.setStroke(savedStroke);
    }
}
