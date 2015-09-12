package edu.njit.cs.saboc.blu.sno.gui.gep.configuration;

import edu.njit.cs.saboc.blu.core.gui.gep.panels.BLUGraphConfiguration;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.AbstractNodePanel;
import edu.njit.cs.saboc.blu.sno.abn.disjointpareataxonomy.DisjointPAreaTaxonomy;
import edu.njit.cs.saboc.blu.sno.gui.abnselection.SCTDisplayFrameListener;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.SCTPAreaTaxonomyConfiguration;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.disjointpareataxonomy.SCTDisjointPAreaPanel;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.disjointpareataxonomy.SCTDisjointPAreaTaxonomyConfiguration;
import javax.swing.JFrame;

/**
 *
 * @author Chris O
 */
public class SCTDisjointPAreaTaxonomyGEPConfiguration extends BLUGraphConfiguration {

    private final JFrame parentFrame;
    private final SCTDisplayFrameListener displayListener;
    
    private final SCTDisjointPAreaTaxonomyConfiguration uiConfiguration;
    
    public SCTDisjointPAreaTaxonomyGEPConfiguration(JFrame parentFrame, 
            DisjointPAreaTaxonomy taxonomy, 
            SCTDisplayFrameListener displayListener) {
        
        super("Disjoint Partial-area Taxonomy");

        this.parentFrame = parentFrame;
        this.displayListener = displayListener;
        
        this.uiConfiguration = new SCTDisjointPAreaTaxonomyConfiguration(taxonomy, displayListener, this);
    }
    
    public SCTDisjointPAreaTaxonomyConfiguration getConfiguration() {
        return uiConfiguration;
    }
    
    @Override
    public boolean hasGroupDetailsPanel() {
        return true;
    }

    @Override
    public AbstractNodePanel createGroupDetailsPanel() {
        return new SCTDisjointPAreaPanel(uiConfiguration);
    }

    @Override
    public boolean hasContainerDetailsPanel() {
        return false;
    }

    @Override
    public AbstractNodePanel createContainerDetailsPanel() {
        return null;
    }
}
