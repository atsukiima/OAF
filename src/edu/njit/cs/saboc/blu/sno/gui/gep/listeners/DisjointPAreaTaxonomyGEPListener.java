package edu.njit.cs.saboc.blu.sno.gui.gep.listeners;

import edu.njit.cs.saboc.blu.core.abn.AbstractionNetwork;
import edu.njit.cs.saboc.blu.core.abn.node.PartitionedNode;
import edu.njit.cs.saboc.blu.core.abn.node.SinglyRootedNode;
import edu.njit.cs.saboc.blu.core.gui.gep.utils.GEPActionListener;
import edu.njit.cs.saboc.blu.sno.gui.abnselection.SCTDisplayFrameListener;
import javax.swing.JFrame;

/**
 *
 * @author Chris O
 */
public class DisjointPAreaTaxonomyGEPListener implements GEPActionListener {

    private final JFrame parentFrame;
    private final SCTDisplayFrameListener displayListener;
    
    public DisjointPAreaTaxonomyGEPListener(JFrame parentFrame, SCTDisplayFrameListener displayListener) {
        this.parentFrame = parentFrame;
        this.displayListener = displayListener;
    }
    
    public void containerPartitionSelected(PartitionedNode partition, boolean treatedAsContainer, AbstractionNetwork abn) {
        System.out.println("HEREEEEEEEEEE.........");
    }

    public void groupSelected(SinglyRootedNode group, AbstractionNetwork abn) {

    }
}
