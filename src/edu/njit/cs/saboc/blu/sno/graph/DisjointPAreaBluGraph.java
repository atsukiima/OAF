package edu.njit.cs.saboc.blu.sno.graph;

import edu.njit.cs.saboc.blu.core.graph.BluGraph;
import edu.njit.cs.saboc.blu.core.graph.options.GraphOptions;
import edu.njit.cs.saboc.blu.core.gui.gep.utils.drawing.GroupEntryLabelCreator;
import edu.njit.cs.saboc.blu.sno.abn.disjointpareataxonomy.DisjointPAreaTaxonomy;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTPArea;
import edu.njit.cs.saboc.blu.sno.graph.layout.DisjointPAreaTaxonomyLayout;
import edu.njit.cs.saboc.blu.sno.gui.abnselection.SCTDisplayFrameListener;
import javax.swing.JFrame;

/**
 *
 * @author Chris O
 */
public class DisjointPAreaBluGraph extends BluGraph {
    
    private SCTDisplayFrameListener displayListener;

    public DisjointPAreaBluGraph(final JFrame parentFrame, 
            final DisjointPAreaTaxonomy disjointTaxonomy, 
            final SCTDisplayFrameListener displayListener, 
            GroupEntryLabelCreator<SCTPArea> labelCreator) {
        
        super(disjointTaxonomy, true, false, labelCreator);
        
        this.displayListener = displayListener;
        
        layout = new DisjointPAreaTaxonomyLayout(this, disjointTaxonomy);
         ((DisjointPAreaTaxonomyLayout) layout).doLayout();
    }

    public DisjointPAreaTaxonomy getDisjointPAreaTaxonomy() {
        return (DisjointPAreaTaxonomy)getAbstractionNetwork();
    }
    
    public SCTDisplayFrameListener getDisplayFrameListener() {
        return displayListener;
    }
}
