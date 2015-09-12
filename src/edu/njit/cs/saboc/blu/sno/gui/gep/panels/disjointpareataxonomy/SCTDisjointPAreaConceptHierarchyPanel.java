package edu.njit.cs.saboc.blu.sno.gui.gep.panels.disjointpareataxonomy;

import SnomedShared.Concept;
import edu.njit.cs.saboc.blu.core.gui.dialogs.concepthierarchy.HierarchyPanelClickListener;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.disjointabn.GenericDisjointGroupConceptHierarchyPanel;
import edu.njit.cs.saboc.blu.sno.abn.disjointpareataxonomy.DisjointPartialArea;
import edu.njit.cs.saboc.blu.sno.datastructure.hierarchy.SCTConceptHierarchy;
import edu.njit.cs.saboc.blu.sno.gui.dialogs.panels.concepthierarchy.SCTConceptHierarchyViewPanel;

/**
 *
 * @author Chris O
 */
public class SCTDisjointPAreaConceptHierarchyPanel extends GenericDisjointGroupConceptHierarchyPanel<Concept, DisjointPartialArea, SCTConceptHierarchy> {
    
    public SCTDisjointPAreaConceptHierarchyPanel(SCTDisjointPAreaTaxonomyConfiguration config) {
        super(
            new SCTConceptHierarchyViewPanel(
                config.getDisjointPAreaTaxonomy(),
                "Disjoint Partial-area",
                new HierarchyPanelClickListener<Concept>() {
                    public void conceptDoubleClicked(Concept c) {
                        config.getGroupConceptListListener().entityDoubleClicked(c);
                    }
                }), 
                config);
        
    }
}
