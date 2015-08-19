package edu.njit.cs.saboc.blu.sno.gui.gep.listeners;

import edu.njit.cs.saboc.blu.core.gui.gep.panels.BLUGraphConfiguration;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.AbstractNodePanel;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTPAreaTaxonomy;
import edu.njit.cs.saboc.blu.sno.gui.abnselection.SCTDisplayFrameListener;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.SCTPAreaTaxonomyConfiguration;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy.parea.SCTPAreaPanel;
import edu.njit.cs.saboc.blu.sno.gui.graphframe.PAreaInternalGraphFrame;
import javax.swing.JFrame;

/**
 *
 * @author Chris
 */
public class PAreaOptionsConfiguration extends BLUGraphConfiguration {
    
    private final SCTPAreaTaxonomy taxonomy;
    
    private final SCTDisplayFrameListener displayListener;
    
    private final SCTPAreaTaxonomyConfiguration uiConfiguration;
    
    public PAreaOptionsConfiguration(
            final JFrame parentFrame, 
            final PAreaInternalGraphFrame graphFrame,
            final SCTPAreaTaxonomy taxonomy, 
            final SCTDisplayFrameListener displayListener) {
        
        this.taxonomy = taxonomy;
        this.displayListener = displayListener;
        
        this.uiConfiguration = new SCTPAreaTaxonomyConfiguration(taxonomy, displayListener);
        
    }
    
    @Override
    public boolean hasGroupDetailsPanel() {
        return true;
    }

    @Override
    public AbstractNodePanel createGroupDetailsPanel() {
        return new SCTPAreaPanel(uiConfiguration);
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
