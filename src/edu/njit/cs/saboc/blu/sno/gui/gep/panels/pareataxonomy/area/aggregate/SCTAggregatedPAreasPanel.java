package edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy.area.aggregate;

import SnomedShared.Concept;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.pareataxonomy.GenericAreaAggregatedPAreaPanel;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTAggregatePArea;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTArea;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTPArea;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.SCTConceptList;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy.configuration.SCTPAreaTaxonomyConfiguration;

/**
 *
 * @author Chris O
 */
public class SCTAggregatedPAreasPanel extends GenericAreaAggregatedPAreaPanel<Concept, SCTArea, SCTPArea, SCTAggregatePArea> {
    public SCTAggregatedPAreasPanel(SCTPAreaTaxonomyConfiguration config) {
        super(new SCTAggregatedPAreasList(config), 
                new SCTConceptList(), 
                config);
    }
}
