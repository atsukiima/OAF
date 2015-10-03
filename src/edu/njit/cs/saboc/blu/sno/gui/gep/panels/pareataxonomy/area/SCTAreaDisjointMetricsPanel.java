package edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy.area;

import SnomedShared.Concept;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.AbstractDisjointAbNMetricsPanel;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.models.BLUAbstractOverlappingDetailsTableModel;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.models.BLUAbstractOverlappingGroupTableModel;
import edu.njit.cs.saboc.blu.sno.abn.disjointpareataxonomy.DisjointPAreaTaxonomy;
import edu.njit.cs.saboc.blu.sno.abn.disjointpareataxonomy.DisjointPartialArea;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTArea;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTPArea;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy.configuration.SCTPAreaTaxonomyConfiguration;

/**
 *
 * @author Chris O
 */
public class SCTAreaDisjointMetricsPanel extends AbstractDisjointAbNMetricsPanel<
        SCTArea,
        DisjointPAreaTaxonomy, 
        SCTPArea,
        DisjointPartialArea,
        Concept> {

    public SCTAreaDisjointMetricsPanel(SCTPAreaTaxonomyConfiguration config) {
        super(new BLUAbstractOverlappingGroupTableModel<SCTPArea, Concept>(config),
              new BLUAbstractOverlappingDetailsTableModel<SCTPArea, DisjointPartialArea, Concept>(config),
              config);
    }
}
