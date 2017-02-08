
package edu.njit.cs.saboc.blu.sno.gui.gep.panels.targetabn.configuration;

import edu.njit.cs.saboc.blu.core.gui.dialogs.concepthierarchy.ConceptPainter;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.NodeOptionsPanel;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.targetbased.configuration.TargetAbNUIConfiguration;
import edu.njit.cs.saboc.blu.core.gui.graphframe.AbNDisplayManager;
import edu.njit.cs.saboc.blu.sno.gui.dialogs.panels.concepthierarchy.SCTConceptPainter;

/**
 *
 * @author Chris O
 */
public class SCTTargetAbNUIConfiguration extends TargetAbNUIConfiguration {
        
    public SCTTargetAbNUIConfiguration(
            SCTTargetAbNConfiguration config, 
            SCTTargetAbNListenerConfiguration listenerConfig,
            AbNDisplayManager displayListener) {

        super(config, displayListener, listenerConfig);
    }
    
    public SCTTargetAbNUIConfiguration(
            SCTTargetAbNConfiguration config, 
            AbNDisplayManager displayListener) {
        
        this(config, new SCTTargetAbNListenerConfiguration(config), displayListener);
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
    public ConceptPainter getConceptHierarchyPainter() {
        return new SCTConceptPainter();
    }
}
