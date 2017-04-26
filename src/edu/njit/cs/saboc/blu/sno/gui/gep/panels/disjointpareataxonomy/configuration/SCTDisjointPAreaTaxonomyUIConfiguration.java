package edu.njit.cs.saboc.blu.sno.gui.gep.panels.disjointpareataxonomy.configuration;

import edu.njit.cs.saboc.blu.core.abn.disjoint.DisjointNode;
import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.PArea;
import edu.njit.cs.saboc.blu.core.gui.dialogs.concepthierarchy.ConceptPainter;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.AbNOptionsPanel;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.NodeOptionsPanel;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.disjointabn.pareataxonomy.DisjointPAreaTaxonomyUIConfiguration;
import edu.njit.cs.saboc.blu.core.gui.graphframe.AbNDisplayManager;
import edu.njit.cs.saboc.blu.sno.gui.abnselection.SCTAbNFrameManager;
import edu.njit.cs.saboc.blu.sno.gui.dialogs.panels.concepthierarchy.SCTConceptPainter;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.disjointpareataxonomy.SCTDisjointPAreaOptionsPanel;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.disjointpareataxonomy.SCTDisjointPAreaTaxonomyOptionsPanel;

/**
 *
 * @author Chris O
 */
public class SCTDisjointPAreaTaxonomyUIConfiguration extends DisjointPAreaTaxonomyUIConfiguration {

    private final SCTAbNFrameManager frameManager;
    
    public SCTDisjointPAreaTaxonomyUIConfiguration(
            SCTDisjointPAreaTaxonomyConfiguration config,
            AbNDisplayManager displayListener,
            SCTAbNFrameManager frameManager) {
        
        super(config, displayListener, new SCTDisjointPAreaTaxonomyListenerConfiguration(config));
        
        this.frameManager = frameManager;
    }
    
    public SCTAbNFrameManager getFrameManager() {
        return frameManager;
    }

    @Override
    public SCTDisjointPAreaTaxonomyConfiguration getConfiguration() {
        return (SCTDisjointPAreaTaxonomyConfiguration)super.getConfiguration();
    }
    
    @Override
    public NodeOptionsPanel<DisjointNode<PArea>> getNodeOptionsPanel() {
        return new SCTDisjointPAreaOptionsPanel(getConfiguration(), getConfiguration().getAbstractionNetwork().isAggregated());
    }

    @Override
    public AbNOptionsPanel getAbNOptionsPanel() {
        return new SCTDisjointPAreaTaxonomyOptionsPanel(getConfiguration());
    }

    @Override
    public ConceptPainter getConceptHierarchyPainter() {
        return new SCTConceptPainter();
    }
}
