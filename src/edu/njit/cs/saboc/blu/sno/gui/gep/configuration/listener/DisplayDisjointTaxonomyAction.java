package edu.njit.cs.saboc.blu.sno.gui.gep.configuration.listener;

import edu.njit.cs.saboc.blu.core.abn.disjoint.DisjointAbstractionNetwork;
import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.PArea;
import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.PAreaTaxonomy;
import edu.njit.cs.saboc.blu.core.gui.listener.DisplayAbNAction;
import edu.njit.cs.saboc.blu.sno.gui.abnselection.SCTDisplayFrameListener;

/**
 *
 * @author Chris O
 */
public class DisplayDisjointTaxonomyAction implements DisplayAbNAction<DisjointAbstractionNetwork> {
    
    private final SCTDisplayFrameListener displayListener;
    
    public DisplayDisjointTaxonomyAction(SCTDisplayFrameListener displayListener) {
        this.displayListener = displayListener;
    }

    @Override
    public void displayAbstractionNetwork(DisjointAbstractionNetwork abstractionNetwork) {
        DisjointAbstractionNetwork<PAreaTaxonomy<PArea>, PArea> disjointAbN = (DisjointAbstractionNetwork<PAreaTaxonomy<PArea>, PArea>)abstractionNetwork;
        
        displayListener.addNewDisjointPAreaTaxonomyGraphFrame(disjointAbN);
    }
}
