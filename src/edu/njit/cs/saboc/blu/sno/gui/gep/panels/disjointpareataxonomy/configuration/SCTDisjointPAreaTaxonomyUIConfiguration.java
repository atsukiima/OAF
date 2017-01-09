package edu.njit.cs.saboc.blu.sno.gui.gep.panels.disjointpareataxonomy.configuration;

import edu.njit.cs.saboc.blu.core.abn.disjoint.DisjointNode;
import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.PArea;
import edu.njit.cs.saboc.blu.core.gui.dialogs.concepthierarchy.ConceptPainter;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.NodeOptionsPanel;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.disjointabn.pareataxonomy.DisjointPAreaTaxonomyUIConfiguration;
import edu.njit.cs.saboc.blu.core.gui.graphframe.AbNDisplayManager;
import edu.njit.cs.saboc.blu.sno.gui.abnselection.SCTAbNFrameManager;
import edu.njit.cs.saboc.blu.sno.gui.dialogs.panels.concepthierarchy.SCTConceptPainter;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.disjointpareataxonomy.SCTDisjointPAreaOptionsPanel;

/**
 *
 * @author Chris O
 */
public class SCTDisjointPAreaTaxonomyUIConfiguration extends DisjointPAreaTaxonomyUIConfiguration {
    
    private final SCTDisjointPAreaTaxonomyConfiguration config;

    public SCTDisjointPAreaTaxonomyUIConfiguration(
            SCTDisjointPAreaTaxonomyConfiguration config,
            AbNDisplayManager displayListener) {
        
        super(config, displayListener, new SCTDisjointPAreaTaxonomyListenerConfiguration(config));
        
        this.config = config;
    }

    @Override
    public NodeOptionsPanel<DisjointNode<PArea>> getNodeOptionsPanel() {
        return new SCTDisjointPAreaOptionsPanel(config, config.getAbstractionNetwork().isAggregated());
    }

    @Override
    public ConceptPainter getConceptHierarchyPainter() {
        return new SCTConceptPainter();
    }
}
