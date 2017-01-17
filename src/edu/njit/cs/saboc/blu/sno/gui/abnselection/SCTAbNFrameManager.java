package edu.njit.cs.saboc.blu.sno.gui.abnselection;

import edu.njit.cs.saboc.blu.core.abn.disjoint.DisjointAbstractionNetwork;
import edu.njit.cs.saboc.blu.core.abn.disjoint.DisjointNode;
import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.DisjointPArea;
import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.PArea;
import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.PAreaTaxonomy;
import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.diff.DiffPAreaTaxonomy;
import edu.njit.cs.saboc.blu.core.abn.tan.Cluster;
import edu.njit.cs.saboc.blu.core.abn.tan.ClusterTribalAbstractionNetwork;
import edu.njit.cs.saboc.blu.core.abn.targetbased.TargetAbstractionNetwork;
import edu.njit.cs.saboc.blu.core.graph.tan.DisjointCluster;
import edu.njit.cs.saboc.blu.core.gui.graphframe.AbNDisplayManager;
import edu.njit.cs.saboc.blu.core.gui.graphframe.FrameCreationAction;
import edu.njit.cs.saboc.blu.core.gui.graphframe.multiabn.MultiAbNGraphFrame;
import edu.njit.cs.saboc.blu.sno.gui.graphframe.SCTDiffPAreaTaxonomyGraphFrame;
import edu.njit.cs.saboc.blu.sno.gui.graphframe.initializers.SCTMultiAbNGraphFrameInitializers;
import javax.swing.JFrame;

/**
 *
 * @author Chris
 */
public class SCTAbNFrameManager extends AbNDisplayManager  {
    
        
    private final JFrame mainFrame;
    
    public SCTAbNFrameManager(JFrame mainFrame, FrameCreationAction fca) {
        super(fca);

        this.mainFrame = mainFrame;
    }
    
    @Override
    public void displayPAreaTaxonomy(PAreaTaxonomy taxonomy) {
        MultiAbNGraphFrame graphFrame = new MultiAbNGraphFrame(mainFrame, new SCTMultiAbNGraphFrameInitializers());
        graphFrame.displayPAreaTaxonomy(taxonomy);

        this.getFrameCreationAction().displayFrame(graphFrame);
    }
        
    
    @Override
    public void displayTribalAbstractionNetwork(ClusterTribalAbstractionNetwork tan) {
        MultiAbNGraphFrame graphFrame = new MultiAbNGraphFrame(mainFrame, new SCTMultiAbNGraphFrameInitializers());
        graphFrame.displayTAN(tan);

        this.getFrameCreationAction().displayFrame(graphFrame);
    }

    @Override
    public void displayDisjointPAreaTaxonomy(
            DisjointAbstractionNetwork<DisjointNode<PArea>, PAreaTaxonomy<PArea>, PArea> disjointTaxonomy) {

        MultiAbNGraphFrame graphFrame = new MultiAbNGraphFrame(mainFrame, new SCTMultiAbNGraphFrameInitializers());
        graphFrame.displayDisjointPAreaTaxonomy(
                (DisjointAbstractionNetwork<DisjointPArea, PAreaTaxonomy<PArea>, PArea>)(DisjointAbstractionNetwork<?, ?, ?>)disjointTaxonomy);

        this.getFrameCreationAction().displayFrame(graphFrame);
    }
    
    @Override
    public void displayDisjointTribalAbstractionNetwork(
            DisjointAbstractionNetwork<DisjointNode<Cluster>, ClusterTribalAbstractionNetwork<Cluster>, Cluster> disjointTAN) {
        
        MultiAbNGraphFrame graphFrame = new MultiAbNGraphFrame(mainFrame, new SCTMultiAbNGraphFrameInitializers());
        graphFrame.displayDisjointTAN(
                (DisjointAbstractionNetwork<DisjointCluster, ClusterTribalAbstractionNetwork<Cluster>, Cluster>)(DisjointAbstractionNetwork<?, ?, ?>)
                        disjointTAN);

        this.getFrameCreationAction().displayFrame(graphFrame);
    }

    @Override
    public void displayTargetAbstractionNetwork(TargetAbstractionNetwork targetAbN) {
        MultiAbNGraphFrame graphFrame = new MultiAbNGraphFrame(mainFrame, new SCTMultiAbNGraphFrameInitializers());
        graphFrame.displayTargetAbstractionNewtork(targetAbN);

        this.getFrameCreationAction().displayFrame(graphFrame);
    }

    @Override
    public void displayAreaTaxonomy(PAreaTaxonomy taxonomy) {
        MultiAbNGraphFrame graphFrame = new MultiAbNGraphFrame(mainFrame, new SCTMultiAbNGraphFrameInitializers());
        graphFrame.displayAreaTaxonomy(taxonomy);

        this.getFrameCreationAction().displayFrame(graphFrame);
    }

    @Override
    public void displayBandTribalAbstractionNetwork(ClusterTribalAbstractionNetwork tan) {
        MultiAbNGraphFrame graphFrame = new MultiAbNGraphFrame(mainFrame, new SCTMultiAbNGraphFrameInitializers());
        graphFrame.displayBandTAN(tan);

        this.getFrameCreationAction().displayFrame(graphFrame);
    }
    
    
    @Override
    public void displayDiffPAreaTaxonomy(DiffPAreaTaxonomy diffTaxonomy) {
        SCTDiffPAreaTaxonomyGraphFrame frame = new SCTDiffPAreaTaxonomyGraphFrame(mainFrame, this);
        frame.displayDiffPAreaTaxonomy(diffTaxonomy);
        
        super.getFrameCreationAction().displayFrame(frame);
    }
}
