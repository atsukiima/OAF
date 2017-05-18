
package edu.njit.cs.saboc.blu.sno.gui.gep.panels.targetabn.configuration;

import edu.njit.cs.saboc.blu.core.gui.dialogs.concepthierarchy.ConceptPainter;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.AbNOptionsPanel;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.NodeOptionsPanel;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.targetbased.configuration.TargetAbNUIConfiguration;
import edu.njit.cs.saboc.blu.core.gui.graphframe.AbNDisplayManager;
import edu.njit.cs.saboc.blu.sno.gui.abnselection.SCTAbNFrameManager;
import edu.njit.cs.saboc.blu.sno.gui.dialogs.panels.concepthierarchy.SCTConceptPainter;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.targetabn.SCTTargetAbNOptionsPanel;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.targetabn.SCTTargetGroupOptionsPanel;

/**
 *
 * @author Chris O
 */
public class SCTTargetAbNUIConfiguration extends TargetAbNUIConfiguration {
        
    private final SCTAbNFrameManager frameManager;
    
    public SCTTargetAbNUIConfiguration(
            SCTTargetAbNConfiguration config, 
            SCTTargetAbNListenerConfiguration listenerConfig,
            AbNDisplayManager displayListener,
            SCTAbNFrameManager frameManager) {

        super(config, displayListener, listenerConfig);
        
        this.frameManager = frameManager;
    }
    
    public SCTTargetAbNUIConfiguration(
            SCTTargetAbNConfiguration config, 
            AbNDisplayManager displayListener, 
            SCTAbNFrameManager frameManager) {
        
        this(config, 
                new SCTTargetAbNListenerConfiguration(config), 
                displayListener, 
                frameManager);
    }
    
    public SCTAbNFrameManager getFrameManager() {
        return frameManager;
    }
    
    @Override
    public SCTTargetAbNConfiguration getConfiguration() {
        return (SCTTargetAbNConfiguration)super.getConfiguration();
    }
    
    @Override
    public NodeOptionsPanel getNodeOptionsPanel() {
        return new SCTTargetGroupOptionsPanel(this.getConfiguration());
    }
    
    
    @Override
    public AbNOptionsPanel getAbNOptionsPanel() {
        return new SCTTargetAbNOptionsPanel(getConfiguration());
    }

    @Override
    public ConceptPainter getConceptHierarchyPainter() {
        return new SCTConceptPainter();
    }
}
