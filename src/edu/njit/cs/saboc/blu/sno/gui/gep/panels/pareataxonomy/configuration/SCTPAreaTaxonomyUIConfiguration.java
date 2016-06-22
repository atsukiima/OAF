
package edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy.configuration;

import SnomedShared.Concept;
import SnomedShared.pareataxonomy.InheritedRelationship;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.NodeDashboardPanel;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.abn.AbstractAbNDetailsPanel;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.pareataxonomy.configuration.BLUGenericPAreaTaxonomyUIConfiguration;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTArea;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTPArea;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTPAreaTaxonomy;
import edu.njit.cs.saboc.blu.sno.gui.abnselection.SCTDisplayFrameListener;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.SCTPAreaTaxonomyDetailsPanel;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy.area.SCTAreaPanel;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy.area.aggregate.SCTAggregateAreaPanel;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy.parea.SCTPAreaPanel;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy.parea.aggregate.SCTAggregatePAreaPanel;

/**
 *
 * @author Chris O
 */
public class SCTPAreaTaxonomyUIConfiguration extends BLUGenericPAreaTaxonomyUIConfiguration<SCTPAreaTaxonomy, 
        SCTArea, SCTPArea, Concept, InheritedRelationship, SCTPAreaTaxonomyListenerConfiguration> {
    
    private final SCTPAreaTaxonomyConfiguration config;
    
    private final SCTDisplayFrameListener displayListener;
    
    public SCTPAreaTaxonomyUIConfiguration(SCTPAreaTaxonomyConfiguration config, SCTDisplayFrameListener displayListener) {
        super(new SCTPAreaTaxonomyListenerConfiguration(config));
        
        this.config = config;
        this.displayListener = displayListener;
    }
    
    public SCTDisplayFrameListener getDisplayFrameListener() {
        return displayListener;
    }
    
    @Override
    public AbstractAbNDetailsPanel createAbNDetailsPanel() {
        return new SCTPAreaTaxonomyDetailsPanel(config);
    }
    
    @Override
    public boolean hasGroupDetailsPanel() {
        return true;
    }

    @Override
    public NodeDashboardPanel createGroupDetailsPanel() {
        if(config.getDataConfiguration().getPAreaTaxonomy().isReduced()) {
            return new SCTAggregatePAreaPanel(config);
        } else {
            return new SCTPAreaPanel(config);
        }
    }
    
    @Override
    public boolean hasContainerDetailsPanel() {
        return true;
    }

    @Override
    public NodeDashboardPanel createContainerDetailsPanel() {
        
        if(config.getDataConfiguration().getPAreaTaxonomy().isReduced()) {
            return new SCTAggregateAreaPanel(config);
        } else {
            return new SCTAreaPanel(config);
        }
    }
}
