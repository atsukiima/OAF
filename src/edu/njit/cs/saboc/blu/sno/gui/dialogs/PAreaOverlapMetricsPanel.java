package edu.njit.cs.saboc.blu.sno.gui.dialogs;

import SnomedShared.Concept;
import edu.njit.cs.saboc.blu.core.abn.disjoint.nodes.DisjointGenericConceptGroup;
import edu.njit.cs.saboc.blu.core.gui.dialogs.panels.OverlapMetricsPanel;
import edu.njit.cs.saboc.blu.sno.abn.disjointpareataxonomy.DisjointPAreaTaxonomy;

/**
 *
 * @author Chris
 */
public class PAreaOverlapMetricsPanel extends OverlapMetricsPanel<DisjointPAreaTaxonomy> {

    public PAreaOverlapMetricsPanel(DisjointPAreaTaxonomy disjointTaxonomy) {
        super(disjointTaxonomy);
    }

    protected String getDisjointGroupRootName(DisjointGenericConceptGroup group) {
        return ((Concept)group.getRoot()).getName();
    }
    
    protected String [] getOverlapGroupColumnNames() {
        return new String[]{"Overlapping Partial-area", "# Concepts", "# Overlapping Concepts"};
    }
}
