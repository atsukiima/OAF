package edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy.area;

import SnomedShared.Concept;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.DisjointAbNMetricsPanel;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.models.OverlappingCombinationsTableModel;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.models.OverlappingDetailsTableModel;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.models.OverlappingNodeTableModel;
import edu.njit.cs.saboc.blu.sno.abn.disjointpareataxonomy.DisjointPAreaTaxonomy;
import edu.njit.cs.saboc.blu.sno.abn.disjointpareataxonomy.DisjointPartialArea;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTArea;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTPArea;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy.configuration.SCTPAreaTaxonomyConfiguration;

/**
 *
 * @author Chris O
 */
public class SCTAreaDisjointMetricsPanel extends DisjointAbNMetricsPanel<
        SCTArea,
        DisjointPAreaTaxonomy, 
        SCTPArea,
        DisjointPartialArea,
        Concept> {

    public SCTAreaDisjointMetricsPanel(SCTPAreaTaxonomyConfiguration config) {
        super(new OverlappingNodeTableModel<SCTPArea, Concept>(config),
              new OverlappingDetailsTableModel<SCTPArea, DisjointPartialArea, Concept>(config),
              new BLUAbstractOverlappingCombinationsTableModel<SCTPArea, DisjointPartialArea, Concept>(config),
              config);
    }
}
