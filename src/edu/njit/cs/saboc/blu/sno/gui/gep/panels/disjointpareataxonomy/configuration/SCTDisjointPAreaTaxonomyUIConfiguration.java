package edu.njit.cs.saboc.blu.sno.gui.gep.panels.disjointpareataxonomy.configuration;

import SnomedShared.Concept;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.configuration.ui.BLUAbNUIConfiguration;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.NodeInformationPanel;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.abn.AbstractAbNDetailsPanel;
import edu.njit.cs.saboc.blu.sno.abn.disjointpareataxonomy.DisjointPAreaTaxonomy;
import edu.njit.cs.saboc.blu.sno.abn.disjointpareataxonomy.DisjointPartialArea;
import edu.njit.cs.saboc.blu.sno.gui.abnselection.SCTDisplayFrameListener;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.disjointpareataxonomy.SCTDisjointPAreaPanel;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.disjointpareataxonomy.SCTDisjointPAreaTaxonomyDetailsPanel;

/**
 *
 * @author Chris O
 */
public class SCTDisjointPAreaTaxonomyUIConfiguration extends BLUAbNUIConfiguration<DisjointPAreaTaxonomy, DisjointPartialArea, Concept,
        SCTDisjointPAreaTaxonomyConfiguration, SCTDisjointPAreaTaxonomyListenerConfiguration> {
    
    private final SCTDisjointPAreaTaxonomyConfiguration config;
    
    private final SCTDisplayFrameListener displayListener;
    
    public SCTDisjointPAreaTaxonomyUIConfiguration(SCTDisjointPAreaTaxonomyConfiguration config, SCTDisplayFrameListener displayListener) {
        super(new SCTDisjointPAreaTaxonomyListenerConfiguration(config));
        
        this.config = config;
        this.displayListener = displayListener;
    }
    
    public SCTDisplayFrameListener getDisplayListener() {
        return displayListener;
    }

    @Override
    public AbstractAbNDetailsPanel createAbNDetailsPanel() {
        return new SCTDisjointPAreaTaxonomyDetailsPanel(config);
    }

    @Override
    public boolean hasGroupDetailsPanel() {
        return true;
    }

    @Override
    public NodeInformationPanel createGroupDetailsPanel() {
        return new SCTDisjointPAreaPanel(config);
    }
}
