package edu.njit.cs.saboc.blu.sno.gui.gep.configuration.listener;

import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.PAreaTaxonomy;
import edu.njit.cs.saboc.blu.core.gui.graphframe.AbNDisplayManager;
import edu.njit.cs.saboc.blu.core.gui.listener.DisplayAbNAction;

/**
 * Action for displaying a partial-area taxonomy
 * 
 * @author Chris O
 */
public class DisplayPAreaTaxonomyAction implements DisplayAbNAction<PAreaTaxonomy> {
    
    private final AbNDisplayManager listener;
    
    public DisplayPAreaTaxonomyAction(AbNDisplayManager listener) {
        this.listener = listener;
    }

    @Override
    public void displayAbstractionNetwork(PAreaTaxonomy taxonomy) {
        listener.displayPAreaTaxonomy(taxonomy);
    }
}