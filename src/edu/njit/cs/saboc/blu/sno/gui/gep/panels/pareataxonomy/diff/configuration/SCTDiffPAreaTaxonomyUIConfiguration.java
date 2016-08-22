
package edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy.diff.configuration;

import edu.njit.cs.saboc.blu.core.gui.dialogs.concepthierarchy.ConceptPainter;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.NodeOptionsPanel;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.pareataxonomy.diff.configuration.DiffPAreaTaxonomyUIConfiguration;
import edu.njit.cs.saboc.blu.sno.gui.abnselection.SCTDisplayFrameListener;

/**
 *
 * @author Chris O
 */
public class SCTDiffPAreaTaxonomyUIConfiguration extends DiffPAreaTaxonomyUIConfiguration {
    
    private final SCTDisplayFrameListener displayListener;
    
    public SCTDiffPAreaTaxonomyUIConfiguration(
            SCTDiffPAreaTaxonomyConfiguration config, 
            SCTDiffPAreaTaxonomyListenerConfiguration listenerConfig,
            SCTDisplayFrameListener displayListener) {

        super(config, listenerConfig);
        
        this.displayListener = displayListener;
    }
    
    public SCTDiffPAreaTaxonomyUIConfiguration(SCTDiffPAreaTaxonomyConfiguration config, 
            SCTDisplayFrameListener displayListener) {
        
        this(config, new SCTDiffPAreaTaxonomyListenerConfiguration(config), displayListener);
    }
    
    public SCTDiffPAreaTaxonomyConfiguration getConfiguration() {
        return (SCTDiffPAreaTaxonomyConfiguration)super.getConfiguration();
    }
    
    public SCTDisplayFrameListener getDisplayFrameListener() {
        return displayListener;
    }

    @Override
    public NodeOptionsPanel getPartitionedNodeOptionsPanel() {
        return new NodeOptionsPanel();
    }

    @Override
    public NodeOptionsPanel getNodeOptionsPanel() {
        return new NodeOptionsPanel();
    }

    @Override
    public ConceptPainter getConceptHierarchyPainter() {
        return new ConceptPainter();
    }
}
