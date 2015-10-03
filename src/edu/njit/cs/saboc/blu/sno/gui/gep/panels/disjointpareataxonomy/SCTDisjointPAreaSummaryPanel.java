package edu.njit.cs.saboc.blu.sno.gui.gep.panels.disjointpareataxonomy;

import SnomedShared.Concept;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.disjointabn.GenericDisjointGroupSummaryPanel;
import edu.njit.cs.saboc.blu.sno.abn.disjointpareataxonomy.DisjointPAreaTaxonomy;
import edu.njit.cs.saboc.blu.sno.abn.disjointpareataxonomy.DisjointPartialArea;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTPArea;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.disjointpareataxonomy.configuration.SCTDisjointPAreaTaxonomyConfiguration;

/**
 *
 * @author Chris O
 */
public class SCTDisjointPAreaSummaryPanel extends GenericDisjointGroupSummaryPanel <Concept, SCTPArea, DisjointPAreaTaxonomy, DisjointPartialArea> {
    
    public SCTDisjointPAreaSummaryPanel(SCTDisjointPAreaTaxonomyConfiguration config) {
        super(
                new SCTOverlappingPAreaList(config), 
                config.getDataConfiguration().getDisjointPAreaTaxonomy(), 
                config
        );
    }
}
