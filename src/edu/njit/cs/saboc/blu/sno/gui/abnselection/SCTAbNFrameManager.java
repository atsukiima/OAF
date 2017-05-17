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
import edu.njit.cs.saboc.blu.core.gui.frame.OAFMainFrame;
import edu.njit.cs.saboc.blu.core.gui.graphframe.AbNDisplayManager;
import edu.njit.cs.saboc.blu.core.gui.graphframe.FrameCreationAction;
import edu.njit.cs.saboc.blu.core.gui.graphframe.multiabn.MultiAbNGraphFrame;
import edu.njit.cs.saboc.blu.core.utils.toolstate.OAFStateFileManager;
import edu.njit.cs.saboc.blu.sno.abnhistory.SCTDerivationParser;
import edu.njit.cs.saboc.blu.sno.gui.graphframe.SCTDiffPAreaTaxonomyGraphFrame;
import edu.njit.cs.saboc.blu.sno.gui.graphframe.initializers.SCTMultiAbNGraphFrameInitializers;
import edu.njit.cs.saboc.blu.sno.localdatasource.concept.SCTConcept;
import edu.njit.cs.saboc.blu.sno.nat.SCTConceptBrowserDataSource;
import edu.njit.cs.saboc.blu.sno.nat.SCTNATLayout;
import edu.njit.cs.saboc.blu.sno.sctdatasource.SCTRelease;
import edu.njit.cs.saboc.nat.generic.NATBrowserFrame;
import java.util.Optional;

/**
 * A class for creating and displaying new abstraction networks and NAT
 * frames (and any other kind of frame) in the OAF
 * 
 * @author Chris
 */
public class SCTAbNFrameManager extends AbNDisplayManager  {
    
    private final OAFMainFrame mainFrame;
    
    private final OAFStateFileManager stateFileManager;
    
    public SCTAbNFrameManager(
            OAFMainFrame mainFrame, 
            FrameCreationAction fca, 
            OAFStateFileManager stateFileManager) {
        
        super(fca);

        this.stateFileManager = stateFileManager;
        this.mainFrame = mainFrame;
    }
    
    public OAFMainFrame getMainFrame() {
        return mainFrame;
    }
    
    @Override
    public void displayPAreaTaxonomy(PAreaTaxonomy taxonomy) {
        
        SCTRelease release = (SCTRelease)taxonomy.getDerivation().getSourceOntology();
        
        MultiAbNGraphFrame graphFrame = new MultiAbNGraphFrame(
                mainFrame, 
                new SCTMultiAbNGraphFrameInitializers(release, stateFileManager, this), 
                new SCTDerivationParser(release));

        graphFrame.displayPAreaTaxonomy(taxonomy);

        this.getFrameCreationAction().displayFrame(graphFrame);
    }
        
    
    @Override
    public void displayTribalAbstractionNetwork(ClusterTribalAbstractionNetwork tan) {
        
        SCTRelease release = (SCTRelease)tan.getDerivation().getSourceOntology();
        
        MultiAbNGraphFrame graphFrame = new MultiAbNGraphFrame(
                mainFrame, 
                new SCTMultiAbNGraphFrameInitializers(release, stateFileManager, this), 
                new SCTDerivationParser(release));
        
        graphFrame.displayTAN(tan);

        this.getFrameCreationAction().displayFrame(graphFrame);
    }

    @Override
    public void displayDisjointPAreaTaxonomy(
            DisjointAbstractionNetwork<DisjointNode<PArea>, PAreaTaxonomy<PArea>, PArea> disjointTaxonomy) {
        
        SCTRelease release = (SCTRelease)disjointTaxonomy.getDerivation().getSourceOntology();

        MultiAbNGraphFrame graphFrame = new MultiAbNGraphFrame(
                mainFrame, 
                new SCTMultiAbNGraphFrameInitializers(release, stateFileManager, this), 
                new SCTDerivationParser(release));
        
        graphFrame.displayDisjointPAreaTaxonomy(
                (DisjointAbstractionNetwork<DisjointPArea, PAreaTaxonomy<PArea>, PArea>)(DisjointAbstractionNetwork<?, ?, ?>)disjointTaxonomy);

        this.getFrameCreationAction().displayFrame(graphFrame);
    }
    
    @Override
    public void displayDisjointTribalAbstractionNetwork(
            DisjointAbstractionNetwork<DisjointNode<Cluster>, ClusterTribalAbstractionNetwork<Cluster>, Cluster> disjointTAN) {
        
        SCTRelease release = (SCTRelease)disjointTAN.getDerivation().getSourceOntology();
        
        MultiAbNGraphFrame graphFrame = new MultiAbNGraphFrame(
                mainFrame, 
                new SCTMultiAbNGraphFrameInitializers(release, stateFileManager, this), 
                new SCTDerivationParser((SCTRelease)disjointTAN.getDerivation().getSourceOntology()));
        
        graphFrame.displayDisjointTAN(
                (DisjointAbstractionNetwork<DisjointCluster, ClusterTribalAbstractionNetwork<Cluster>, Cluster>)(DisjointAbstractionNetwork<?, ?, ?>)
                        disjointTAN);

        this.getFrameCreationAction().displayFrame(graphFrame);
    }

    @Override
    public void displayTargetAbstractionNetwork(TargetAbstractionNetwork targetAbN) {
        
        SCTRelease release = (SCTRelease)targetAbN.getDerivation().getSourceOntology();
        
        MultiAbNGraphFrame graphFrame = new MultiAbNGraphFrame(
                mainFrame, 
                new SCTMultiAbNGraphFrameInitializers(release, stateFileManager, this), 
                new SCTDerivationParser(release));
        
        graphFrame.displayTargetAbstractionNetwork(targetAbN);

        this.getFrameCreationAction().displayFrame(graphFrame);
    }

    @Override
    public void displayAreaTaxonomy(PAreaTaxonomy taxonomy) {
        
        SCTRelease release = (SCTRelease)taxonomy.getDerivation().getSourceOntology();
        
        MultiAbNGraphFrame graphFrame = new MultiAbNGraphFrame(
                mainFrame, 
                new SCTMultiAbNGraphFrameInitializers(release, stateFileManager, this),
                new SCTDerivationParser(release));
        
        graphFrame.displayAreaTaxonomy(taxonomy);

        this.getFrameCreationAction().displayFrame(graphFrame);
    }

    @Override
    public void displayBandTribalAbstractionNetwork(ClusterTribalAbstractionNetwork tan) {
        
        SCTRelease release = (SCTRelease)tan.getDerivation().getSourceOntology();
        
        MultiAbNGraphFrame graphFrame = new MultiAbNGraphFrame(
                mainFrame, 
                new SCTMultiAbNGraphFrameInitializers(release, stateFileManager, this), 
                new SCTDerivationParser(release));
        
        graphFrame.displayBandTAN(tan);

        this.getFrameCreationAction().displayFrame(graphFrame);
    }
    
    @Override
    public void displayDiffPAreaTaxonomy(DiffPAreaTaxonomy diffTaxonomy) {
        SCTDiffPAreaTaxonomyGraphFrame frame = new SCTDiffPAreaTaxonomyGraphFrame(mainFrame, this);
        frame.displayDiffPAreaTaxonomy(diffTaxonomy);
        
        super.getFrameCreationAction().displayFrame(frame);
    }   
    
    public void displayConceptBrowserFrame(SCTConceptBrowserDataSource dataSource, Optional<SCTConcept> focusConcept) {
        
        NATBrowserFrame<SCTConcept> browserFrame = new NATBrowserFrame<>(
            mainFrame,
            dataSource, 
            new SCTNATLayout(dataSource));
        
        if(focusConcept.isPresent()) {
            browserFrame.nagivateTo(focusConcept.get());
        } else {
            browserFrame.nagivateTo(dataSource.getOntology().getConceptHierarchy().getRoot());
        }
        
        super.getFrameCreationAction().displayFrame(browserFrame);
    }
}
