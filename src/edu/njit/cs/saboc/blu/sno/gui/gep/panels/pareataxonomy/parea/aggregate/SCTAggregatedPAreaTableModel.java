package edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy.parea.aggregate;

import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.models.BLUAbstractTableModel;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTAggregatePArea;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.SCTPAreaTaxonomyConfiguration;

/**
 *
 * @author Chris O
 */
public class SCTAggregatedPAreaTableModel<AGGREGATEPAREA_T extends SCTAggregatePArea> extends BLUAbstractTableModel<AGGREGATEPAREA_T> {
    
    protected final SCTPAreaTaxonomyConfiguration configuration;
    
    public SCTAggregatedPAreaTableModel(SCTPAreaTaxonomyConfiguration configuration) {
        super(
                new String[] {
                    "Partial-area",
                    "# Concepts",
                    "# Aggregated Partial-areas",
                    "Area"
            });
        
        this.configuration = configuration;
    }

    @Override
    protected Object[] createRow(AGGREGATEPAREA_T group) {
        
        String areaName = configuration.getGroupsContainerName(group);
        
        return new Object [] {
            configuration.getGroupName(group),
            group.getAllGroupsConcepts().size(),
            group.getAggregatedGroups(),
            areaName
        };
    }
    
}
