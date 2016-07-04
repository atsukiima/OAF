
package edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy.configuration;

import edu.njit.cs.saboc.blu.core.gui.dialogs.concepthierarchy.ConceptPainter;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.AbstractNodeOptionsPanel;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.pareataxonomy.configuration.PAreaTaxonomyUIConfiguration;
import edu.njit.cs.saboc.blu.sno.gui.abnselection.SCTDisplayFrameListener;
import edu.njit.cs.saboc.blu.sno.gui.dialogs.panels.concepthierarchy.SCTConceptPainter;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy.SCTAggregateAreaOptionsPanel;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy.SCTAggregatePAreaOptionsPanel;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy.SCTAreaOptionsPanel;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy.SCTPAreaOptionsPanel;

/**
 *
 * @author Chris O
 */
public class SCTPAreaTaxonomyUIConfiguration extends PAreaTaxonomyUIConfiguration {
    
    private final SCTPAreaTaxonomyConfiguration config;
    
    private final SCTDisplayFrameListener displayListener;
    
    public SCTPAreaTaxonomyUIConfiguration(SCTPAreaTaxonomyConfiguration config, SCTDisplayFrameListener displayListener) {
        super(config, new SCTPAreaTaxonomyListenerConfiguration(config));
        
        this.config = config;
        this.displayListener = displayListener;
    }
    
    public SCTDisplayFrameListener getDisplayFrameListener() {
        return displayListener;
    }

    @Override
    public AbstractNodeOptionsPanel getPartitionedNodeOptionsPanel() {
        
        if(config.getPAreaTaxonomy().isAggregated()) {
            return new SCTAggregateAreaOptionsPanel(config);
        } else {
            return new SCTAreaOptionsPanel(config);
        }
    }

    @Override
    public AbstractNodeOptionsPanel getNodeOptionsPanel() {
        if(config.getPAreaTaxonomy().isAggregated()) {
            return new SCTAggregatePAreaOptionsPanel(config);
        } else {
            return new SCTPAreaOptionsPanel(config);
        }
    }

    @Override
    public ConceptPainter getConceptHierarchyPainter() {
        return new SCTConceptPainter();
    }
}
