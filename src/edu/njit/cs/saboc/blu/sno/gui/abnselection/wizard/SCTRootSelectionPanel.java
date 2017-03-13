package edu.njit.cs.saboc.blu.sno.gui.abnselection.wizard;

import edu.njit.cs.saboc.blu.core.gui.gep.panels.configuration.AbNConfiguration;
import edu.njit.cs.saboc.blu.core.gui.panels.abnderivationwizard.rootselection.BaseRootSelectionOptionsPanel;
import edu.njit.cs.saboc.blu.core.gui.panels.abnderivationwizard.rootselection.SearchForRootPanel;
import edu.njit.cs.saboc.blu.core.gui.panels.abnderivationwizard.rootselection.SelectOntologyRootPanel;

/**
 * A panel for selecting a top-level SNOMED CT hierarchy or searching
 * for a specific concept
 * 
 * @author Chris O
 */
public class SCTRootSelectionPanel extends BaseRootSelectionOptionsPanel {
    
    private final SelectOntologyRootPanel selectRoot;
    private final SearchForRootPanel searchForRoot;
    
    public SCTRootSelectionPanel(AbNConfiguration config) {
        this.selectRoot = new SelectOntologyRootPanel("Select SNOMED CT Subhierarchy", config);
        
        this.searchForRoot = new SearchForRootPanel(config);
        
        super.addRootSelectionOption(selectRoot);
        super.addRootSelectionOption(searchForRoot);
    }
}
