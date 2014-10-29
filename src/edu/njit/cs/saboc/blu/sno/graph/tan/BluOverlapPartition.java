/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.njit.cs.saboc.blu.sno.graph.tan;

import SnomedShared.overlapping.OverlapInheritancePartition;
import edu.njit.cs.saboc.blu.core.graph.BluGraph;
import edu.njit.cs.saboc.blu.core.graph.nodes.GenericPartitionEntry;
import java.awt.Color;
import javax.swing.JLabel;

/**
 *
 * @author Chris
 */
public class BluOverlapPartition extends GenericPartitionEntry {

    public BluOverlapPartition(OverlapInheritancePartition partition, String partitionName,
            int width, int height, BluGraph g, BluCommonOverlapSet p, Color c, boolean treatAsOverlapSet) {

        super(partition, partitionName, width, height, g, p, c, treatAsOverlapSet);
    }
    
    public BluOverlapPartition(OverlapInheritancePartition partition, String partitionName,
            int width, int height, BluGraph g, BluCommonOverlapSet p, Color c, boolean treatAsOverlapSet, JLabel label) {

        this(partition, partitionName, width, height, g, p, c, treatAsOverlapSet);
        
        this.remove(partitionLabel);
        
        this.partitionLabel = label;
        
        this.add(label);
    }

    public OverlapInheritancePartition getOverlapPartition() {
        return (OverlapInheritancePartition) partition;
    }
}
