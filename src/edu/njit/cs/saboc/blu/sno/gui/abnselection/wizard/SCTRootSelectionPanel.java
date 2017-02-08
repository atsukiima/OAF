package edu.njit.cs.saboc.blu.sno.gui.abnselection.wizard;

import edu.njit.cs.saboc.blu.core.gui.gep.panels.configuration.AbNConfiguration;
import edu.njit.cs.saboc.blu.core.gui.panels.abnderivationwizard.rootselection.BaseRootSelectionOptionsPanel;
import edu.njit.cs.saboc.blu.core.gui.panels.abnderivationwizard.rootselection.SearchForRootPanel;
import edu.njit.cs.saboc.blu.core.gui.panels.abnderivationwizard.rootselection.SelectOntologyRootPanel;

/**
 *
 * @author Chris O
 */
public class SCTRootSelectionPanel extends BaseRootSelectionOptionsPanel {
    
    private final SelectOntologyRootPanel selectRoot;
    private final SearchForRootPanel searchForRoot;
    
    public SCTRootSelectionPanel(AbNConfiguration config) {
        this.selectRoot = new SelectOntologyRootPanel(config);
        this.searchForRoot = new SearchForRootPanel(config);
        
        super.addRootSelectionOption(selectRoot);
        super.addRootSelectionOption(searchForRoot);
    }
}
