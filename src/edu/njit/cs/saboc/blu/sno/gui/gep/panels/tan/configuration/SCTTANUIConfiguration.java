package edu.njit.cs.saboc.blu.sno.gui.gep.panels.tan.configuration;

import edu.njit.cs.saboc.blu.core.gui.dialogs.concepthierarchy.ConceptPainter;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.NodeOptionsPanel;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.tan.TANUIConfiguration;
import edu.njit.cs.saboc.blu.core.gui.graphframe.AbNDisplayManager;
import edu.njit.cs.saboc.blu.sno.gui.abnselection.SCTAbNFrameManager;
import edu.njit.cs.saboc.blu.sno.gui.dialogs.panels.concepthierarchy.SCTConceptPainter;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.tan.SCTBandOptionsPanel;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.tan.SCTClusterOptionsPanel;
/**
 *
 * @author Chris O
 */
public class SCTTANUIConfiguration extends TANUIConfiguration {

    private final SCTTANConfiguration config;
            
    public SCTTANUIConfiguration(SCTTANConfiguration config, AbNDisplayManager displayListener) {
        super(config, displayListener, new SCTTANListenerConfiguration(config));

        this.config = config;
    }

    @Override
    public NodeOptionsPanel getPartitionedNodeOptionsPanel() {        
        return new SCTBandOptionsPanel(config, config.getAbstractionNetwork().isAggregated());
    }

    @Override
    public NodeOptionsPanel getNodeOptionsPanel() {
        return new SCTClusterOptionsPanel(config, config.getAbstractionNetwork().isAggregated());
    }

    @Override
    public ConceptPainter getConceptHierarchyPainter() {
        return new SCTConceptPainter();
    }
}
