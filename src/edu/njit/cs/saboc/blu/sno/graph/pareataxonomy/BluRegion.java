package edu.njit.cs.saboc.blu.sno.graph.pareataxonomy;

import edu.njit.cs.saboc.blu.core.graph.BluGraph;
import edu.njit.cs.saboc.blu.core.graph.pareataxonomy.RegionEntry;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTRegion;
import java.awt.Color;
import javax.swing.JLabel;


/**
 *
 * @author Chris
 */
public class BluRegion extends RegionEntry<SCTRegion, BluArea> {

    public BluRegion(SCTRegion region, String regionName,
            int width, int height, BluGraph g, BluArea p, Color c, boolean treatAsArea) {
        
        super(region, regionName, width, height, g, p, c, treatAsArea);
    }
    
    public BluRegion(SCTRegion region, String regionName,
            int width, int height, BluGraph g, BluArea p, Color c, boolean treatAsArea, JLabel label) {
        
        super(region, regionName, width, height, g, p, c, treatAsArea, label);
    }
}
