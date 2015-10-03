package edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy.reports;

import SnomedShared.Concept;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.reports.GenericAbNLevelReportPanel;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTArea;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTPArea;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTPAreaTaxonomy;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy.configuration.SCTPAreaTaxonomyConfiguration;

/**
 *
 * @author Chris O
 */
public class SCTPAreaTaxonomyLevelReportPanel extends GenericAbNLevelReportPanel<Concept, SCTPAreaTaxonomy, SCTArea, SCTPArea> {
    public SCTPAreaTaxonomyLevelReportPanel(SCTPAreaTaxonomyConfiguration config) {
        super(config);
    }
}
