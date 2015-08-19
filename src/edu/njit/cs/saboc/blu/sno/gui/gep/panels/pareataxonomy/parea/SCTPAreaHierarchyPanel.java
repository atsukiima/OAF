package edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy.parea;

import SnomedShared.Concept;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.pareataxonomy.GenericPAreaHierarchyPanel;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTPArea;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.SCTPAreaTaxonomyConfiguration;

/**
 *
 * @author Chris O
 */
public class SCTPAreaHierarchyPanel extends GenericPAreaHierarchyPanel<Concept, SCTPArea> {

    public SCTPAreaHierarchyPanel(SCTPAreaTaxonomyConfiguration config) {
        super(
                new SCTParentPAreaTableModel(),
                new SCTChildPAreaTableModel(),
                config.getPAreaTaxonomy(), config);
    }
}
