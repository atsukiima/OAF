package edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy.area.aggregate;

import SnomedShared.Concept;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.pareataxonomy.GenericAggregateAreaPanel;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTAggregatePArea;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTArea;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTPArea;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.SCTPAreaTaxonomyConfiguration;

/**
 *
 * @author Chris O
 */
public class SCTAggregateAreaPanel extends GenericAggregateAreaPanel<SCTArea, SCTPArea, SCTAggregatePArea, Concept, Concept> {
    
    private final SCTPAreaTaxonomyConfiguration config;
    
    public SCTAggregateAreaPanel(SCTPAreaTaxonomyConfiguration config) {
        super(new SCTAggregateAreaDetailsPanel(config), 
                new SCTAreaAggregatedPAreaPanel(config), 
                new SCTAggregatedPAreasPanel(config),
                config);
        
        
        this.config = config;
    }
    
    @Override
    protected String getNodeTitle(SCTArea node) {
        if(node.getRelationships().isEmpty()) {
            return "root area (no properties)";
        } else {
            String areaName = config.getContainerName(node);
        
            return String.format("{%s}", areaName);
        }
    }
}
