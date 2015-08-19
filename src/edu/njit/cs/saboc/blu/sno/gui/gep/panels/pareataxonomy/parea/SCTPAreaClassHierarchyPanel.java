package edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy.parea;

import SnomedShared.Concept;
import edu.njit.cs.saboc.blu.core.gui.dialogs.concepthierarchy.HierarchyPanelClickListener;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.pareataxonomy.PAreaConceptHierarchyPanel;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTPArea;
import edu.njit.cs.saboc.blu.sno.datastructure.hierarchy.SCTConceptHierarchy;
import edu.njit.cs.saboc.blu.sno.gui.dialogs.panels.concepthierarchy.SCTConceptHierarchyViewPanel;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.SCTPAreaTaxonomyConfiguration;

/**
 *
 * @author Chris O
 */
public class SCTPAreaClassHierarchyPanel extends PAreaConceptHierarchyPanel<Concept, SCTPArea, SCTConceptHierarchy> {

    public SCTPAreaClassHierarchyPanel(SCTPAreaTaxonomyConfiguration config) {
        
        super(new SCTConceptHierarchyViewPanel(
                config.getPAreaTaxonomy(),
                "Partial-area",
                new HierarchyPanelClickListener<Concept>() {
                    public void conceptDoubleClicked(Concept c) {
                       
                    }
                }), config);
    }
}
