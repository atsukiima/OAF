package edu.njit.cs.saboc.blu.sno.gui.abnselection;

import edu.njit.cs.saboc.blu.core.abn.disjoint.DisjointAbstractionNetwork;
import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.PArea;
import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.PAreaTaxonomy;
import edu.njit.cs.saboc.blu.core.abn.tan.ClusterTribalAbstractionNetwork;
import edu.njit.cs.saboc.blu.core.gui.graphframe.FrameCreationListener;
import edu.njit.cs.saboc.blu.sno.gui.graphframe.ClusterInternalGraphFrame;
import edu.njit.cs.saboc.blu.sno.gui.graphframe.DisjointPAreaInternalGraphFrame;
import edu.njit.cs.saboc.blu.sno.gui.graphframe.PAreaInternalGraphFrame;
import javax.swing.JFrame;

/**
 *
 * @author Chris
 */
public abstract class SCTDisplayFrameListener implements FrameCreationListener {
    
    private final JFrame mainFrame;
    
    public SCTDisplayFrameListener(JFrame mainFrame) {
        this.mainFrame = mainFrame;
    }
    
    /**
     * *
     * Creates and displays a new SNOMED CT partial-area taxonomy graph frame.
     *
     * @param data The taxonomy data used to create the graph.
     * @return The newly created internal graph frame.
     */
    public PAreaInternalGraphFrame addNewPAreaGraphFrame(PAreaTaxonomy data) {
        
        PAreaInternalGraphFrame igf = new PAreaInternalGraphFrame(mainFrame, data, this);

        this.displayFrame(igf);

        return igf;
    }

    /**
     * Creates and displays a new SNOMED CT Tribal Abstraction Network graph
     * frame.
     *
     * @param data
     * @return The newly created internal graph frame.
     */
    public ClusterInternalGraphFrame addNewClusterGraphFrame(ClusterTribalAbstractionNetwork data) {
        
        ClusterInternalGraphFrame cigf = new ClusterInternalGraphFrame(mainFrame, data, this);

        this.displayFrame(cigf);

        return cigf;
    }
    
    public DisjointPAreaInternalGraphFrame addNewDisjointPAreaTaxonomyGraphFrame(DisjointAbstractionNetwork<PAreaTaxonomy, PArea> taxonomy) {
        DisjointPAreaInternalGraphFrame frame = new DisjointPAreaInternalGraphFrame(mainFrame, taxonomy, this);
        
        this.displayFrame(frame);
        
        return frame;
    }
}
