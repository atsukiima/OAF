package edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy.reports;

import SnomedShared.Concept;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.reports.GenericOverlappingConceptReportPanel;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTArea;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTPArea;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTPAreaTaxonomy;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.SCTPAreaTaxonomyConfiguration;

/**
 *
 * @author Chris O
 */
public class SCTPAreaTaxonomyOverlappingClassReportPanel extends GenericOverlappingConceptReportPanel<Concept, SCTPAreaTaxonomy, SCTArea, SCTPArea>{
    public SCTPAreaTaxonomyOverlappingClassReportPanel(SCTPAreaTaxonomyConfiguration config) {
        super(config);
    }
}
