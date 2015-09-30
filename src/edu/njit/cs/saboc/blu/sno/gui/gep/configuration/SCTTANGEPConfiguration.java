package edu.njit.cs.saboc.blu.sno.gui.gep.configuration;

import edu.njit.cs.saboc.blu.core.gui.gep.panels.BLUGraphConfiguration;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.AbstractNodePanel;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.abn.AbstractAbNDetailsPanel;
import edu.njit.cs.saboc.blu.sno.abn.tan.local.SCTTribalAbstractionNetwork;
import edu.njit.cs.saboc.blu.sno.gui.abnselection.SCTDisplayFrameListener;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.SCTTANConfiguration;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.SCTTANDetailsPanel;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.tan.band.SCTBandPanel;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.tan.cluster.SCTClusterPanel;
import edu.njit.cs.saboc.blu.sno.gui.graphframe.ClusterInternalGraphFrame;
import javax.swing.JFrame;

/**
 *
 * @author Chris
 */
public class SCTTANGEPConfiguration extends BLUGraphConfiguration {
    
    private final SCTTANConfiguration config;

    public SCTTANGEPConfiguration(
            final JFrame parentFrame, 
            final ClusterInternalGraphFrame graphFrame,
            final SCTTribalAbstractionNetwork tan, 
            final SCTDisplayFrameListener displayListener) {
        
        super("Tribal Abstraction Network");

        this.config = new SCTTANConfiguration(tan, displayListener, this);
    }
    
    public SCTTANConfiguration getConfiguration() {
        return config;
    }
    
    @Override
    public AbstractAbNDetailsPanel createAbNDetailsPanel() {
        return new SCTTANDetailsPanel(config);
    }
    
    @Override
    public boolean hasGroupDetailsPanel() {
        return true;
    }

    @Override
    public AbstractNodePanel createGroupDetailsPanel() {
        return new SCTClusterPanel(config);
    }
    
    @Override
    public boolean hasContainerDetailsPanel() {
        return true;
    }

    @Override
    public AbstractNodePanel createContainerDetailsPanel() {
        return new SCTBandPanel(config);
    }
}
