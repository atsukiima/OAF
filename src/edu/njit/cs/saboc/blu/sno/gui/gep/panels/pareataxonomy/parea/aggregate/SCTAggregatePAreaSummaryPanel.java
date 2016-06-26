package edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy.parea.aggregate;

import SnomedShared.Concept;
import SnomedShared.pareataxonomy.InheritedRelationship;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.pareataxonomy.AggregatePAreaSummaryPanel;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTAggregatePArea;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTPArea;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTPAreaTaxonomy;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy.configuration.SCTPAreaTaxonomyConfiguration;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy.parea.SCTPAreaRelationshipsDetailsPanel;

/**
 *
 * @author Chris O
 */
public class SCTAggregatePAreaSummaryPanel extends 
        AggregatePAreaSummaryPanel<Concept, InheritedRelationship, SCTPAreaTaxonomy, SCTPArea, SCTAggregatePArea> {

    public SCTAggregatePAreaSummaryPanel(SCTPAreaTaxonomyConfiguration config) {
        super(new SCTPAreaRelationshipsDetailsPanel(config.getDataConfiguration().getPAreaTaxonomy()), 
                config.getDataConfiguration().getPAreaTaxonomy(), config);
    }
}