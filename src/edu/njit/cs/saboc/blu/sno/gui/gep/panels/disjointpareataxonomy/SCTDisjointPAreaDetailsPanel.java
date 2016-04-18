package edu.njit.cs.saboc.blu.sno.gui.gep.panels.disjointpareataxonomy;

import SnomedShared.Concept;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.AbstractNodeDetailsPanel;
import edu.njit.cs.saboc.blu.sno.abn.disjointpareataxonomy.DisjointPartialArea;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.SCTAbNNodeConceptList;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.disjointpareataxonomy.configuration.SCTDisjointPAreaTaxonomyConfiguration;
import java.util.ArrayList;

/**
 *
 * @author Chris O
 */
public class SCTDisjointPAreaDetailsPanel extends AbstractNodeDetailsPanel<DisjointPartialArea, Concept> {

    public SCTDisjointPAreaDetailsPanel(SCTDisjointPAreaTaxonomyConfiguration config) {
        super(new SCTDisjointPAreaSummaryPanel(config), 
                new SCTDisjointPAreaOptionsPanel(config), 
                new SCTAbNNodeConceptList());
        
        getConceptList().addEntitySelectionListener(config.getUIConfiguration().getListenerConfiguration().getGroupConceptListListener());
    }
    
    @Override
    protected ArrayList<Concept> getSortedConceptList(DisjointPartialArea conceptGroup) {
        return conceptGroup.getConceptsAsList();
    }
}
