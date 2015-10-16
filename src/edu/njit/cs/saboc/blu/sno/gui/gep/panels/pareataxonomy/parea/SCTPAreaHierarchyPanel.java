package edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy.parea;

import SnomedShared.Concept;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.pareataxonomy.GenericPAreaHierarchyPanel;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTPArea;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy.configuration.SCTPAreaTaxonomyConfiguration;

/**
 *
 * @author Chris O
 */
public class SCTPAreaHierarchyPanel extends GenericPAreaHierarchyPanel<Concept, SCTPArea, SCTPAreaTaxonomyConfiguration> {

    public SCTPAreaHierarchyPanel(SCTPAreaTaxonomyConfiguration config) {
        super(
                new SCTParentPAreaTableModel(config),
                new SCTChildPAreaTableModel(config),
                config.getDataConfiguration().getPAreaTaxonomy(), config);
    }
}
