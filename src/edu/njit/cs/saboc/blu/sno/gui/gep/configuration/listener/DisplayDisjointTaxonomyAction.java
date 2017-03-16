package edu.njit.cs.saboc.blu.sno.gui.gep.configuration.listener;

import edu.njit.cs.saboc.blu.core.abn.disjoint.DisjointAbstractionNetwork;
import edu.njit.cs.saboc.blu.core.abn.disjoint.DisjointNode;
import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.PArea;
import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.PAreaTaxonomy;
import edu.njit.cs.saboc.blu.core.gui.graphframe.AbNDisplayManager;
import edu.njit.cs.saboc.blu.core.gui.listener.DisplayAbNAction;

/**
 * Action for displaying a disjoint partial-area taxonomy
 * 
 * @author Chris O
 */
public class DisplayDisjointTaxonomyAction implements DisplayAbNAction<DisjointAbstractionNetwork> {
    
    private final AbNDisplayManager displayListener;
    
    public DisplayDisjointTaxonomyAction(AbNDisplayManager displayListener) {
        this.displayListener = displayListener;
    }

    @Override
    public void displayAbstractionNetwork(DisjointAbstractionNetwork abstractionNetwork) {
        DisjointAbstractionNetwork<DisjointNode<PArea>, PAreaTaxonomy<PArea>, PArea> disjointAbN = 
                (DisjointAbstractionNetwork<DisjointNode<PArea>, PAreaTaxonomy<PArea>, PArea>)abstractionNetwork;
        
        displayListener.displayDisjointPAreaTaxonomy(disjointAbN);
    }
}
