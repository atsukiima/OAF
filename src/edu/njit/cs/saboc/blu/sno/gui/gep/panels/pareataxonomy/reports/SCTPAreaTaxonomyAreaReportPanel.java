package edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy.reports;

import SnomedShared.Concept;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.reports.AbstractAbNContainerReportPanel;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTArea;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTPArea;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTPAreaTaxonomy;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy.configuration.SCTPAreaTaxonomyConfiguration;

/**
 *
 * @author Chris O
 */
public class SCTPAreaTaxonomyAreaReportPanel extends AbstractAbNContainerReportPanel<SCTPAreaTaxonomy, SCTArea, SCTPArea, Concept> {
    public SCTPAreaTaxonomyAreaReportPanel(SCTPAreaTaxonomyConfiguration config) {
        super(config);
    }
}
