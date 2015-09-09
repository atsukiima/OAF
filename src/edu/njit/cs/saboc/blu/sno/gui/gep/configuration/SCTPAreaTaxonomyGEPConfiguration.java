package edu.njit.cs.saboc.blu.sno.gui.gep.configuration;

import edu.njit.cs.saboc.blu.core.gui.gep.panels.BLUGraphConfiguration;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.AbstractNodePanel;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTPAreaTaxonomy;
import edu.njit.cs.saboc.blu.sno.gui.abnselection.SCTDisplayFrameListener;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.SCTPAreaTaxonomyConfiguration;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy.area.SCTAreaPanel;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy.area.aggregate.SCTAggregateAreaPanel;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy.parea.SCTPAreaPanel;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy.parea.aggregate.SCTAggregatePAreaPanel;
import edu.njit.cs.saboc.blu.sno.gui.graphframe.PAreaInternalGraphFrame;
import javax.swing.JFrame;

/**
 *
 * @author Chris
 */
public class SCTPAreaTaxonomyGEPConfiguration extends BLUGraphConfiguration {
    
    private final SCTPAreaTaxonomy taxonomy;
    
    private final SCTDisplayFrameListener displayListener;
    
    private final SCTPAreaTaxonomyConfiguration uiConfiguration;
    
    public SCTPAreaTaxonomyGEPConfiguration(
            final JFrame parentFrame, 
            final PAreaInternalGraphFrame graphFrame,
            final SCTPAreaTaxonomy taxonomy, 
            final SCTDisplayFrameListener displayListener) {
        
        super("Partial-area Taxonomy");
        
        this.taxonomy = taxonomy;
        this.displayListener = displayListener;
        
        this.uiConfiguration = new SCTPAreaTaxonomyConfiguration(taxonomy, displayListener, this);
    }
    
    public SCTPAreaTaxonomyConfiguration getConfiguration() {
        return uiConfiguration;
    }
    
    @Override
    public boolean hasGroupDetailsPanel() {
        return true;
    }

    @Override
    public AbstractNodePanel createGroupDetailsPanel() {
        if(taxonomy.isReduced()) {
            return new SCTAggregatePAreaPanel(uiConfiguration);
        } else {
            return new SCTPAreaPanel(uiConfiguration);
        }
    }
    
    @Override
    public boolean hasContainerDetailsPanel() {
        return true;
    }

    @Override
    public AbstractNodePanel createContainerDetailsPanel() {
        
        if(taxonomy.isReduced()) {
            return new SCTAggregateAreaPanel(uiConfiguration);
        } else {
            return new SCTAreaPanel(uiConfiguration);
        }
    }
    
}
