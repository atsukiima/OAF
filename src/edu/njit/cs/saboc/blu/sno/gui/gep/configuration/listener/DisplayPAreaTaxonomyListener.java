package edu.njit.cs.saboc.blu.sno.gui.gep.configuration.listener;

import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.PAreaTaxonomy;
import edu.njit.cs.saboc.blu.core.gui.listener.DisplayAbNListener;
import edu.njit.cs.saboc.blu.sno.gui.abnselection.SCTDisplayFrameListener;

/**
 *
 * @author Chris O
 */
public class DisplayPAreaTaxonomyListener implements DisplayAbNListener<PAreaTaxonomy> {
    
    private final SCTDisplayFrameListener listener;
    
    public DisplayPAreaTaxonomyListener(SCTDisplayFrameListener listener) {
        this.listener = listener;
    }

    @Override
    public void displayAbstractionNetwork(PAreaTaxonomy taxonomy) {
        listener.addNewPAreaGraphFrame(taxonomy);
    }
}