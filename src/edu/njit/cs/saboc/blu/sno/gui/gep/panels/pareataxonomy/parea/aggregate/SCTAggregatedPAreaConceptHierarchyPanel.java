package edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy.parea.aggregate;

import SnomedShared.Concept;
import edu.njit.cs.saboc.blu.core.gui.dialogs.concepthierarchy.HierarchyPanelClickListener;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.pareataxonomy.PAreaConceptHierarchyPanel;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTAggregatePArea;
import edu.njit.cs.saboc.blu.sno.datastructure.hierarchy.SCTConceptHierarchy;
import edu.njit.cs.saboc.blu.sno.gui.dialogs.panels.concepthierarchy.SCTConceptHierarchyViewPanel;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy.configuration.SCTPAreaTaxonomyConfiguration;

/**
 *
 * @author cro3
 */
public class SCTAggregatedPAreaConceptHierarchyPanel extends PAreaConceptHierarchyPanel<Concept, SCTAggregatePArea, SCTConceptHierarchy> {

    public SCTAggregatedPAreaConceptHierarchyPanel(SCTPAreaTaxonomyConfiguration config) {
        
        super(new SCTConceptHierarchyViewPanel(
                config.getDataConfiguration().getPAreaTaxonomy(),
                "Partial-area",
                new HierarchyPanelClickListener<Concept>() {
                    public void conceptDoubleClicked(Concept c) {
                        config.getUIConfiguration().getListenerConfiguration().getGroupConceptListListener().entityDoubleClicked(c);
                    }
                }), config);
    }
}
