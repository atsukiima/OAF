package edu.njit.cs.saboc.blu.sno.graph.pareataxonomy;

import SnomedShared.pareataxonomy.Region;
import edu.njit.cs.saboc.blu.core.graph.BluGraph;
import edu.njit.cs.saboc.blu.core.graph.nodes.GenericPartitionEntry;
import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JLabel;


/**
 *
 * @author Chris
 */
public class BluRegion extends GenericPartitionEntry implements MouseListener {

    public BluRegion(Region region, String regionName,
            int width, int height, BluGraph g, BluArea p, Color c, boolean treatAsArea) {
        
        super(region, regionName, width, height, g, p, c, treatAsArea);
    }
    
    public BluRegion(Region region, String regionName,
            int width, int height, BluGraph g, BluArea p, Color c, boolean treatAsArea, JLabel label) {
        
        this(region, regionName, width, height, g, p, c, treatAsArea);
        
        this.remove(partitionLabel);
        
        this.partitionLabel = label;
        
        this.add(label);
    }

    public Region getRegion() {
        return (Region) partition;
    }

    public void mouseClicked(MouseEvent e) {
//        super.mouseClicked(e);
//        
//        if (e.getButton() == MouseEvent.BUTTON1) {
//            if (e.getClickCount() == 2) {
//                new PartitionConceptDialog(((Region)partition), ((PAreaBluGraph)graph).getPAreaHierarchyData(), treatAsContainer);
//            }
//        }
    }
}
