package edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy.parea;

import SnomedShared.Concept;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.pareataxonomy.GenericPAreaPanel;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTPArea;
import edu.njit.cs.saboc.blu.sno.datastructure.hierarchy.SCTConceptHierarchy;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.SCTPAreaTaxonomyConfiguration;

/**
 *
 * @author Chris O
 */
public class SCTPAreaPanel extends GenericPAreaPanel<Concept, SCTPArea, SCTConceptHierarchy> {
    public SCTPAreaPanel(SCTPAreaTaxonomyConfiguration config) {
        super(new SCTPAreaDetailsPanel(config), 
                new SCTPAreaHierarchyPanel(config), 
                new SCTPAreaConceptHierarchyPanel(config), 
                config);
    }
}
