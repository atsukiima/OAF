package edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy.reports.aggregate;

import SnomedShared.Concept;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.reports.aggregate.AggregateAbNLevelReportPanel;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTArea;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTPArea;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTPAreaTaxonomy;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy.configuration.SCTPAreaTaxonomyConfiguration;

/**
 *
 * @author Chris O
 */
public class SCTAggregatePAreaTaxonomyLevelReportPanel extends AggregateAbNLevelReportPanel<Concept, SCTPAreaTaxonomy, SCTPArea, SCTArea> {

    public SCTAggregatePAreaTaxonomyLevelReportPanel(SCTPAreaTaxonomyConfiguration config) {
        super(config);
    }
}
