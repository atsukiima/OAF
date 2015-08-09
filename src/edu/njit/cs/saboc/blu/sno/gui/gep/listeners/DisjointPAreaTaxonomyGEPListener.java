package edu.njit.cs.saboc.blu.sno.gui.gep.listeners;

import SnomedShared.generic.GenericConceptGroup;
import SnomedShared.generic.GenericContainerPartition;
import edu.njit.cs.saboc.blu.core.abn.AbstractionNetwork;
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
    
    public void containerPartitionSelected(GenericContainerPartition partition, boolean treatedAsContainer, AbstractionNetwork abn) {
        System.out.println("HEREEEEEEEEEE.........");
    }

    public void groupSelected(GenericConceptGroup group, AbstractionNetwork abn) {

    }
}
