package edu.njit.cs.saboc.blu.sno.gui.gep.panels.disjointpareataxonomy;

import SnomedShared.Concept;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.disjointabn.ChildDisjointNodeTableModel;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.disjointabn.GenericDisjointGroupHierarchyPanel;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.disjointabn.GenericParentDisjointGroupTableModel;
import edu.njit.cs.saboc.blu.sno.abn.disjointpareataxonomy.DisjointPartialArea;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTPArea;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.disjointpareataxonomy.configuration.SCTDisjointPAreaTaxonomyConfiguration;

/**
 *
 * @author Chris O
 */
public class SCTDisjointPAreaHierarchyPanel extends GenericDisjointGroupHierarchyPanel<Concept, DisjointPartialArea, SCTDisjointPAreaTaxonomyConfiguration> {
    public SCTDisjointPAreaHierarchyPanel(SCTDisjointPAreaTaxonomyConfiguration config) {
        super(new GenericParentDisjointGroupTableModel<Concept, DisjointPartialArea, SCTPArea>(config), 
                new GenericChildDisjointGroupTableModel<DisjointPartialArea, SCTPArea>(config), 
                config.getDataConfiguration().getDisjointPAreaTaxonomy(), config);
    }
}
