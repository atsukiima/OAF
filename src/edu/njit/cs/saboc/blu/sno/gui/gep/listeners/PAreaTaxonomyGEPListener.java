package edu.njit.cs.saboc.blu.sno.gui.gep.listeners;

import SnomedShared.generic.GenericConceptGroup;
import SnomedShared.generic.GenericContainerPartition;
import edu.njit.cs.saboc.blu.core.abn.AbstractionNetwork;
import edu.njit.cs.saboc.blu.core.gui.gep.utils.GEPActionListener;
import edu.njit.cs.saboc.blu.sno.abn.SCTAbstractionNetwork;
import edu.njit.cs.saboc.blu.sno.gui.abnselection.SCTDisplayFrameListener;
import edu.njit.cs.saboc.blu.sno.gui.dialogs.ConceptGroupDetailsDialog;
import edu.njit.cs.saboc.blu.sno.gui.dialogs.PartitionConceptDialog;
import javax.swing.JFrame;

/**
 *
 * @author Chris
 */
public class PAreaTaxonomyGEPListener implements GEPActionListener {

    private final JFrame parentFrame;
    private final SCTDisplayFrameListener displayListener;
    
    public PAreaTaxonomyGEPListener(JFrame parentFrame, SCTDisplayFrameListener displayListener) {
        this.parentFrame = parentFrame;
        this.displayListener = displayListener;
    }
    
    public void containerPartitionSelected(GenericContainerPartition partition, boolean treatedAsContainer, AbstractionNetwork abn) {
        PartitionConceptDialog dialog = new PartitionConceptDialog(parentFrame, partition, (SCTAbstractionNetwork) abn,
                treatedAsContainer, displayListener);
    }

    public void groupSelected(GenericConceptGroup group, AbstractionNetwork abn) {
        ConceptGroupDetailsDialog dialog = new ConceptGroupDetailsDialog(group, (SCTAbstractionNetwork) abn,
                ConceptGroupDetailsDialog.DialogType.PartialArea, displayListener);
    }
}
