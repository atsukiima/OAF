package edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy.parea.aggregate;

import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.models.BLUAbstractChildGroupTableModel;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTAggregatePArea;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy.configuration.SCTPAreaTaxonomyConfiguration;

/**
 *
 * @author Chris O
 */
public class SCTAggregateChildPAreaTableModel extends BLUAbstractChildGroupTableModel<SCTAggregatePArea> {

    private final SCTPAreaTaxonomyConfiguration config;
    
    public SCTAggregateChildPAreaTableModel(SCTPAreaTaxonomyConfiguration config) {
        super(new String[] {
            "Child Partial-area", 
            "# Concepts",
            "# Aggregate Partial-areas",
            "Area"
        });
        
        this.config = config;
    }
    

    @Override
    protected Object[] createRow(SCTAggregatePArea parea) {
        String areaName = config.getTextConfiguration().getGroupsContainerName(parea).replaceAll(", ", "\n");
        
        return new Object[] {
            config.getTextConfiguration().getGroupName(parea),
            parea.getAllGroupsConcepts().size(),
            parea.getAggregatedGroups().size(),
            areaName
        };
    }
}
