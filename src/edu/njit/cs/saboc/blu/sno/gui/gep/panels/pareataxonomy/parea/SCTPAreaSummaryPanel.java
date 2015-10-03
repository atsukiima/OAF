package edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy.parea;

import SnomedShared.Concept;
import SnomedShared.pareataxonomy.InheritedRelationship;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.pareataxonomy.GenericPAreaSummaryPanel;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTPArea;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTPAreaTaxonomy;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy.configuration.SCTPAreaTaxonomyConfiguration;

/**
 *
 * @author Chris O
 */
public class SCTPAreaSummaryPanel extends GenericPAreaSummaryPanel<Concept, InheritedRelationship, SCTPAreaTaxonomy, SCTPArea> {

    public SCTPAreaSummaryPanel(SCTPAreaTaxonomyConfiguration config) {
        super(new SCTPAreaRelationshipsDetailsPanel(config.getDataConfiguration().getPAreaTaxonomy()), 
                config.getDataConfiguration().getPAreaTaxonomy(), config);
    }
}
