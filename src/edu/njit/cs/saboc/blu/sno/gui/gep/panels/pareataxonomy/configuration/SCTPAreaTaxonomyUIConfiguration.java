
package edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy.configuration;

import edu.njit.cs.saboc.blu.core.gui.dialogs.concepthierarchy.ConceptPainter;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.NodeOptionsPanel;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.pareataxonomy.configuration.PAreaTaxonomyUIConfiguration;
import edu.njit.cs.saboc.blu.core.gui.graphframe.AbNDisplayManager;
import edu.njit.cs.saboc.blu.sno.gui.abnselection.SCTAbNFrameManager;
import edu.njit.cs.saboc.blu.sno.gui.dialogs.panels.concepthierarchy.SCTConceptPainter;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy.SCTAreaOptionsPanel;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy.SCTPAreaOptionsPanel;

/**
 *
 * @author Chris O
 */
public class SCTPAreaTaxonomyUIConfiguration extends PAreaTaxonomyUIConfiguration {
    
    private final SCTAbNFrameManager frameManager;
    
    public SCTPAreaTaxonomyUIConfiguration(
            SCTPAreaTaxonomyConfiguration config, 
            AbNDisplayManager displayListener, 
            SCTAbNFrameManager frameManager) {
        
        super(config, displayListener, new SCTPAreaTaxonomyListenerConfiguration(config));
        
        this.frameManager = frameManager;
    }
    
    public SCTAbNFrameManager getFrameManager() {
        return frameManager;
    }
    
    @Override
    public SCTPAreaTaxonomyConfiguration getConfiguration() {
        return (SCTPAreaTaxonomyConfiguration)super.getConfiguration();
    }

    @Override
    public NodeOptionsPanel getPartitionedNodeOptionsPanel() {
        return new SCTAreaOptionsPanel(getConfiguration(), getConfiguration().getPAreaTaxonomy().isAggregated());
    }

    @Override
    public NodeOptionsPanel getNodeOptionsPanel() {
        return new SCTPAreaOptionsPanel(getConfiguration(), getConfiguration().getPAreaTaxonomy().isAggregated());
    }

    @Override
    public ConceptPainter getConceptHierarchyPainter() {
        return new SCTConceptPainter();
    }
}
