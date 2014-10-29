package edu.njit.cs.saboc.blu.sno.gui.dialogs.disjointpareataxonomy;

import edu.njit.cs.saboc.blu.core.gui.disjointabn.DisjointAbNGraphActionListener;
import edu.njit.cs.saboc.blu.core.gui.disjointabn.DisjointAbstractionNetworkGraph;
import edu.njit.cs.saboc.blu.core.gui.disjointabn.DisjointGroupEntry;
import edu.njit.cs.saboc.blu.sno.abn.disjointpareataxonomy.DisjointPAreaTaxonomy;
import edu.njit.cs.saboc.blu.sno.abn.disjointpareataxonomy.DisjointPartialArea;
import edu.njit.cs.saboc.blu.sno.gui.abnselection.SCTDisplayFrameListener;
import edu.njit.cs.saboc.blu.sno.gui.dialogs.DisjointPAreaDetailsDialog;

/**
 *
 * @author Chris
 */
public class DisjointPartialAreaTaxonomyPanel extends DisjointAbstractionNetworkGraph<DisjointPAreaTaxonomy, DisjointPartialArea, BluDisjointPartialArea> { //implements MouseListener {

    public DisjointPartialAreaTaxonomyPanel(final DisjointPAreaTaxonomy djpaTaxonomy, final SCTDisplayFrameListener displayFrameListener) {
        super(djpaTaxonomy, new DisjointAbNGraphActionListener() {
            public void disjointEntryDoubleClicked(DisjointGroupEntry entry) {
                BluDisjointPartialArea disjointEntry = (BluDisjointPartialArea)entry;
                
                new DisjointPAreaDetailsDialog(djpaTaxonomy.getSourcePAreaTaxonomy(), 
                                    disjointEntry.getDisjointPArea(),
                                    djpaTaxonomy,
                                    displayFrameListener);
            }
        });
    }
    
    protected BluDisjointPartialArea createDisjointGroupEntry(DisjointPartialArea djpa) {
        return new BluDisjointPartialArea(djpa);
    }
    
    public DisjointPAreaTaxonomy getDisjointPAreaTaxonomy() {
        return super.getDisjointAbstractionNetwork();
    }
}
