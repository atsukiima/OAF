package edu.njit.cs.saboc.blu.sno.gui.gep.panels;

import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.abn.AbstractAbNDetailsPanel;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTPAreaTaxonomy;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy.configuration.SCTPAreaTaxonomyConfiguration;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy.reports.SCTPAreaTaxonomyAreaReportPanel;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy.reports.SCTPAreaTaxonomyLevelReportPanel;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy.reports.aggregate.SCTAggregatePAreaTaxonomyAreaReportPanel;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy.reports.aggregate.SCTAggregatePAreaTaxonomyLevelReportPanel;

/**
 *
 * @author Chris O
 */
public class SCTPAreaTaxonomyDetailsPanel extends AbstractAbNDetailsPanel<SCTPAreaTaxonomy> {
    
    public SCTPAreaTaxonomyDetailsPanel(SCTPAreaTaxonomyConfiguration config) {
        super(config);

        if (config.getDataConfiguration().getPAreaTaxonomy().isReduced()) {
            SCTAggregatePAreaTaxonomyLevelReportPanel levelReportPanel = new SCTAggregatePAreaTaxonomyLevelReportPanel(config);
            levelReportPanel.displayAbNReport(config.getDataConfiguration().getPAreaTaxonomy());

            SCTAggregatePAreaTaxonomyAreaReportPanel areaReportPanel = new SCTAggregatePAreaTaxonomyAreaReportPanel(config);
            areaReportPanel.displayAbNReport(config.getDataConfiguration().getPAreaTaxonomy());

            super.addDetailsTab("Aggregate Partial-area Taxonomy Levels", levelReportPanel);
            super.addDetailsTab("Areas in Aggregate Partial-area Taxonomy", areaReportPanel);
        } else {
            SCTPAreaTaxonomyLevelReportPanel levelReportPanel = new SCTPAreaTaxonomyLevelReportPanel(config);
            levelReportPanel.displayAbNReport(config.getDataConfiguration().getPAreaTaxonomy());

            SCTPAreaTaxonomyAreaReportPanel areaReportPanel = new SCTPAreaTaxonomyAreaReportPanel(config);
            areaReportPanel.displayAbNReport(config.getDataConfiguration().getPAreaTaxonomy());

            super.addDetailsTab("Partial-area Taxonomy Levels", levelReportPanel);
            super.addDetailsTab("Areas in Partial-area Taxonomy", areaReportPanel);
        }
    }
}
