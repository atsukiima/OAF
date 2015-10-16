package edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy.area.aggregate;

import SnomedShared.Concept;
import SnomedShared.pareataxonomy.InheritedRelationship;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.pareataxonomy.GenericAggregateAreaSummaryPanel;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTArea;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTPAreaTaxonomy;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy.area.SCTAreaRelationshipsDetailsPanel;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy.configuration.SCTPAreaTaxonomyConfiguration;

/**
 *
 * @author Chris O
 */
public class SCTAggregateAreaSummaryPanel extends GenericAggregateAreaSummaryPanel<Concept, InheritedRelationship, SCTPAreaTaxonomy, SCTArea> {
    public SCTAggregateAreaSummaryPanel(SCTPAreaTaxonomyConfiguration config) {
        super(new SCTAreaRelationshipsDetailsPanel(config), config);
    }
}
