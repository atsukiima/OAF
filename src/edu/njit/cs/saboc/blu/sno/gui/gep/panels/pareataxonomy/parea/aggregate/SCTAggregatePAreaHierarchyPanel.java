package edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy.parea.aggregate;

import SnomedShared.Concept;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.pareataxonomy.GenericPAreaHierarchyPanel;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTAggregatePArea;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.SCTPAreaTaxonomyConfiguration;

/**
 *
 * @author Chris O
 */
public class SCTAggregatePAreaHierarchyPanel extends GenericPAreaHierarchyPanel<Concept, SCTAggregatePArea> {

    public SCTAggregatePAreaHierarchyPanel(SCTPAreaTaxonomyConfiguration config) {
        super(
                new SCTAggregateParentPAreaTableModel(config),
                new SCTAggregateChildPAreaTableModel(config),
                config.getPAreaTaxonomy(), 
                config);
    }
}

