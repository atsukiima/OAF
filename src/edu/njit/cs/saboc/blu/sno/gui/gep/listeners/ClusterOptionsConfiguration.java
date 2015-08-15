package edu.njit.cs.saboc.blu.sno.gui.gep.listeners;

import edu.njit.cs.saboc.blu.core.gui.gep.panels.BLUGraphConfiguration;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.AbstractGroupPanel;
import edu.njit.cs.saboc.blu.sno.abn.tan.TribalAbstractionNetwork;
import edu.njit.cs.saboc.blu.sno.gui.abnselection.SCTDisplayFrameListener;
import edu.njit.cs.saboc.blu.sno.gui.graphframe.ClusterInternalGraphFrame;
import javax.swing.JFrame;

/**
 *
 * @author Chris
 */
public class ClusterOptionsConfiguration extends BLUGraphConfiguration {

    public ClusterOptionsConfiguration(
            final JFrame parentFrame, 
            final ClusterInternalGraphFrame graphFrame,
            final TribalAbstractionNetwork tan, 
            final SCTDisplayFrameListener displayListener) {

    }
    
    @Override
    public boolean hasGroupDetailsPanel() {
        return false;
    }

    @Override
    public AbstractGroupPanel createGroupDetailsPanel() {
        return null;
    }
}
