package edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy.area.aggregate;

import SnomedShared.Concept;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.pareataxonomy.GenericAreaAggregatedPAreaListModel;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTAggregatePArea;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTPArea;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy.configuration.SCTPAreaTaxonomyConfiguration;

/**
 *
 * @author Chris O
 */
public class SCTAreaAggregatedPAreasTableModel extends GenericAreaAggregatedPAreaListModel<Concept, SCTPArea, SCTAggregatePArea> {

    public SCTAreaAggregatedPAreasTableModel(SCTPAreaTaxonomyConfiguration config) {
        super(config);
    }
    
    @Override
    protected String getAggregatedPAreaAreaName(SCTPArea aggregatedPArea) {
        SCTPAreaTaxonomyConfiguration config = (SCTPAreaTaxonomyConfiguration)configuration;

        return config.getTextConfiguration().getGroupsContainerName(aggregatedPArea).replaceAll(", ", "\n");
    }
}
