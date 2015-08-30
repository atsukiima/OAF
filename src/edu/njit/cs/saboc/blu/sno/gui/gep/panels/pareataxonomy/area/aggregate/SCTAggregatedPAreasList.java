package edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy.area.aggregate;

import SnomedShared.Concept;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.AbstractEntityList;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.entry.AggregatedGroupEntry;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTAggregatePArea;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTPArea;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.SCTPAreaTaxonomyConfiguration;
import java.util.ArrayList;
import java.util.Optional;

/**
 *
 * @author Chris O
 */
public class SCTAggregatedPAreasList extends AbstractEntityList<AggregatedGroupEntry<Concept, SCTPArea, SCTAggregatePArea>> {

    public SCTAggregatedPAreasList(SCTPAreaTaxonomyConfiguration config) {
        super(new SCTAreaAggregatedPAreasTableModel(config));
    }
    
    @Override
    protected String getBorderText(Optional<ArrayList<AggregatedGroupEntry<Concept, SCTPArea, SCTAggregatePArea>>> entities) {
        if(entities.isPresent()) {
            return String.format("Aggregated Partial-areas (%d)", entities.get().size());
        } else {
            return "Aggregated Partial-areas";
        }
    }
}
