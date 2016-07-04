package edu.njit.cs.saboc.blu.sno.gui.gep.panels.tan.configuration;

import edu.njit.cs.saboc.blu.core.gui.dialogs.concepthierarchy.ConceptPainter;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.AbstractNodeOptionsPanel;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.tan.TANUIConfiguration;
import edu.njit.cs.saboc.blu.sno.gui.abnselection.SCTDisplayFrameListener;
import edu.njit.cs.saboc.blu.sno.gui.dialogs.panels.concepthierarchy.SCTConceptPainter;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.tan.SCTBandOptionsPanel;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.tan.SCTClusterOptionsPanel;
/**
 *
 * @author Chris O
 */
public class SCTTANUIConfiguration extends TANUIConfiguration {

    private final SCTDisplayFrameListener displayListener;
    private final SCTTANConfiguration config;
    
            
    public SCTTANUIConfiguration(SCTTANConfiguration config, SCTDisplayFrameListener displayListener) {
        super(config, new SCTTANListenerConfiguration(config));

        this.config = config;
        this.displayListener = displayListener;
    }
    
    public SCTDisplayFrameListener getDisplayFrameListener() {
        return displayListener;
    }

    @Override
    public AbstractNodeOptionsPanel getPartitionedNodeOptionsPanel() {        
        return new SCTBandOptionsPanel(config);
    }

    @Override
    public AbstractNodeOptionsPanel getNodeOptionsPanel() {
        return new SCTClusterOptionsPanel(config);
    }

    @Override
    public ConceptPainter getConceptHierarchyPainter() {
        return new SCTConceptPainter();
    }
}
