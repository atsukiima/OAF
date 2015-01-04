package edu.njit.cs.saboc.blu.sno.graph.layout;

import edu.njit.cs.saboc.blu.core.graph.BluGraph;
import edu.njit.cs.saboc.blu.core.graph.edges.GraphEdge;
import edu.njit.cs.saboc.blu.core.graph.edges.GraphGroupLevel;
import edu.njit.cs.saboc.blu.core.graph.edges.GraphLevel;
import edu.njit.cs.saboc.blu.core.graph.nodes.GenericGroupEntry;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTArea;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTPArea;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTRegion;
import edu.njit.cs.saboc.blu.sno.graph.pareataxonomy.BluArea;
import edu.njit.cs.saboc.blu.sno.graph.pareataxonomy.BluPArea;
import edu.njit.cs.saboc.blu.sno.graph.pareataxonomy.BluRegion;
import java.awt.Color;
import java.awt.Rectangle;
import java.util.ArrayList;
import javax.swing.JLabel;

/**
 *
 * @author Chris O
 */
public class PAreaTaxonomyLayoutUtils {

    public static BluPArea createPAreaPanel(BluGraph graph, SCTPArea p, BluRegion parent, int x, int y, int pAreaX, GraphGroupLevel pAreaLevel) {
        BluPArea pAreaPanel = new BluPArea(p, graph, parent, pAreaX, pAreaLevel, new ArrayList<GraphEdge>());

        //Make sure this panel dimensions will fit on the graph, stretch the graph if necessary
        graph.stretchGraphToFitPanel(x, y, GenericGroupEntry.ENTRY_WIDTH, GenericGroupEntry.ENTRY_HEIGHT);

        //Setup the panel's dimensions, etc.
        pAreaPanel.setBounds(x, y, GenericGroupEntry.ENTRY_WIDTH, GenericGroupEntry.ENTRY_HEIGHT);

        parent.add(pAreaPanel, 0);

        return pAreaPanel;
    }
    
    public static BluArea createAreaPanel(BluGraph graph, SCTArea a, int x, int y, int width, int height, Color c, int areaX, GraphLevel parentLevel) {
        BluArea areaPanel = new BluArea(a, graph, areaX, parentLevel, new Rectangle(x, y, width, height));

        graph.stretchGraphToFitPanel(x, y, width, height);

        areaPanel.setBounds(x, y, width, height);
        areaPanel.setBackground(c);

        graph.add(areaPanel, 0);

        return areaPanel;
    }
    
     public static BluRegion createRegionPanel(BluGraph graph, SCTRegion region, String regionName, 
            BluArea ap, int x, int y, int width, int height, Color c, boolean treatRegionAsArea, JLabel regionLabel) {

        BluRegion regionPanel = new BluRegion(region, regionName,
                width, height, graph, ap, c, treatRegionAsArea, regionLabel);

        graph.stretchGraphToFitPanel(x, y, width, height);

        regionPanel.setBounds(x, y, width, height);

        ap.add(regionPanel, 0);

        return regionPanel;
    }
}
