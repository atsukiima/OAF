package edu.njit.cs.saboc.blu.sno.gui.abnselection;

import SnomedShared.Concept;
import edu.njit.cs.saboc.blu.core.gui.graphframe.FrameCreationListener;
import edu.njit.cs.saboc.blu.sno.abn.disjointpareataxonomy.DisjointPAreaTaxonomy;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTPAreaTaxonomy;
import edu.njit.cs.saboc.blu.sno.abn.tan.TribalAbstractionNetwork;
import edu.njit.cs.saboc.blu.sno.conceptbrowser.InternalConceptBrowserFrame;
import edu.njit.cs.saboc.blu.sno.gui.graphframe.ClusterInternalGraphFrame;
import edu.njit.cs.saboc.blu.sno.gui.graphframe.DisjointPAreaInternalGraphFrame;
import edu.njit.cs.saboc.blu.sno.gui.graphframe.PAreaInternalGraphFrame;
import edu.njit.cs.saboc.blu.sno.sctdatasource.SCTDataSource;
import edu.njit.cs.saboc.blu.sno.sctdatasource.SCTLocalDataSource;
import edu.njit.cs.saboc.blu.sno.sctdatasource.SCTRemoteDataSource;
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
     * @param areaGraph False if taxonomy should be partitioned into regions.
     * True if only areas are used.
     * @return The newly created internal graph frame.
     */
    public PAreaInternalGraphFrame addNewPAreaGraphFrame(SCTPAreaTaxonomy data, boolean areaGraph) {
        
        PAreaInternalGraphFrame igf = new PAreaInternalGraphFrame(mainFrame, data, areaGraph, this);

        this.displayFrame(igf);

        return igf;
    }

    /**
     * Creates and displays a new SNOMED CT Tribal Abstraction Network graph
     * frame.
     *
     * @param data The tribal abstraction network data used to create the graph.
     * @param setGraph False if tribal bands should be partitioned based on
     * inheritance. True otherwise.
     * @param conceptCount True if tribal bands labels should indicate total
     * number of concepts. False for displaying total number of clusters.
     * @return The newly created internal graph frame.
     */
    public ClusterInternalGraphFrame addNewClusterGraphFrame(TribalAbstractionNetwork data, boolean setGraph, boolean conceptCount) {
        
        ClusterInternalGraphFrame cigf = new ClusterInternalGraphFrame(mainFrame, data, true, false, this);

        this.displayFrame(cigf);

        return cigf;
    }
    
    public DisjointPAreaInternalGraphFrame addNewDisjointPAreaTaxonomyGraphFrame(DisjointPAreaTaxonomy taxonomy) {
        DisjointPAreaInternalGraphFrame frame = new DisjointPAreaInternalGraphFrame(mainFrame, taxonomy, this);
        
        this.displayFrame(frame);
        
        return frame;
    }

    /**
     * *
     * Displays a new concept-centric browser internal frame.
     *
     * @return The newly created browser frame.
     */
    public InternalConceptBrowserFrame addNewBrowserFrame(SCTDataSource dataSource) {
        
        SCTDataSource browserDataSource; 
        
        if(dataSource instanceof SCTLocalDataSource) {
            browserDataSource = dataSource;
        } else { 
            // Need to make a copy so the version can be changed in the browser frame without 
            // changing the original data source
            browserDataSource = new SCTRemoteDataSource(dataSource.getSelectedVersion());
        }

        InternalConceptBrowserFrame ibf = new InternalConceptBrowserFrame(mainFrame, browserDataSource, this);

        this.displayFrame(ibf);

        ibf.setBounds(ibf.getX(), 50, ibf.getWidth(), ibf.getHeight());

        return ibf;
    }

    /**
     * Displays a new concept-centric browser internal frame and focuses it on
     * the specified concept.
     *
     * @param c The concept which will become the focus concept of the browser
     * frame.
     * @return The newly created browser frame.
     */
    public InternalConceptBrowserFrame addNewBrowserFrame(Concept c, SCTDataSource dataSource) {
        return addNewBrowserFrame(c.getId(), dataSource);
    }

    /**
     * Displays a new concept-centric browser internal frame and focuses it on
     * the concept with the specified conceptid.
     *
     * @param conceptId The id of the concept which will become the focus
     * concept of the browser frame.
     * @return The newly created browser frame.
     */
    public InternalConceptBrowserFrame addNewBrowserFrame(long conceptId, SCTDataSource dataSource) {
        InternalConceptBrowserFrame ibf = addNewBrowserFrame(dataSource);
        ibf.navigateTo(conceptId);

        return ibf;
    }
}
