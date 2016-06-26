package edu.njit.cs.saboc.blu.sno.gui.gep.panels.tan.configuration;

import SnomedShared.Concept;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.NodeDashboardPanel;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.abn.AbstractAbNDetailsPanel;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.tan.TANUIConfiguration;
import edu.njit.cs.saboc.blu.sno.abn.tan.local.SCTBand;
import edu.njit.cs.saboc.blu.sno.abn.tan.local.SCTCluster;
import edu.njit.cs.saboc.blu.sno.abn.tan.local.SCTTribalAbstractionNetwork;
import edu.njit.cs.saboc.blu.sno.gui.abnselection.SCTDisplayFrameListener;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.SCTTANDetailsPanel;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.tan.band.SCTBandPanel;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.tan.cluster.SCTClusterPanel;

/**
 *
 * @author Chris O
 */
public class SCTTANUIConfiguration extends TANUIConfiguration<SCTTribalAbstractionNetwork, SCTBand, SCTCluster, Concept, SCTTANListenerConfiguration> {

    private final SCTTANConfiguration config;
    
    private final SCTDisplayFrameListener displayListener;
            
    public SCTTANUIConfiguration(SCTTANConfiguration config, SCTDisplayFrameListener displayListener) {
        super(new SCTTANListenerConfiguration(config));
        
        this.config = config;
        
        this.displayListener = displayListener;
    }
    
    public SCTDisplayFrameListener getDisplayFrameListener() {
        return displayListener;
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
    public NodeDashboardPanel createGroupDetailsPanel() {
        return new SCTClusterPanel(config);
    }
    
    @Override
    public boolean hasContainerDetailsPanel() {
        return true;
    }

    @Override
    public NodeDashboardPanel createContainerDetailsPanel() {
        return new SCTBandPanel(config);
    }
}
