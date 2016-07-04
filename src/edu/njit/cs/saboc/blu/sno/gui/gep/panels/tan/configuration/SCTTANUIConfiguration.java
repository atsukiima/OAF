package edu.njit.cs.saboc.blu.sno.gui.gep.panels.tan.configuration;

import edu.njit.cs.saboc.blu.core.gui.dialogs.concepthierarchy.ConceptPainter;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.AbstractNodeOptionsPanel;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.tan.TANUIConfiguration;
import edu.njit.cs.saboc.blu.sno.gui.abnselection.SCTDisplayFrameListener;
import edu.njit.cs.saboc.blu.sno.gui.dialogs.panels.concepthierarchy.SCTConceptPainter;
/**
 *
 * @author Chris O
 */
public class SCTTANUIConfiguration extends TANUIConfiguration {

    private final SCTDisplayFrameListener displayListener;
            
    public SCTTANUIConfiguration(SCTTANConfiguration config, SCTDisplayFrameListener displayListener) {
        super(config, new SCTTANListenerConfiguration(config));

        this.displayListener = displayListener;
    }
    
    public SCTDisplayFrameListener getDisplayFrameListener() {
        return displayListener;
    }

    @Override
    public AbstractNodeOptionsPanel getPartitionedNodeOptionsPanel() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public AbstractNodeOptionsPanel getNodeOptionsPanel() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ConceptPainter getConceptHierarchyPainter() {
        return new SCTConceptPainter();
    }
}
