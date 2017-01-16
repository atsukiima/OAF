package edu.njit.cs.saboc.blu.sno.gui.abnselection;

import edu.njit.cs.saboc.blu.core.abn.disjoint.DisjointAbstractionNetwork;
import edu.njit.cs.saboc.blu.core.abn.disjoint.DisjointNode;
import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.PArea;
import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.PAreaTaxonomy;
import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.diff.DiffPAreaTaxonomy;
import edu.njit.cs.saboc.blu.core.abn.tan.Cluster;
import edu.njit.cs.saboc.blu.core.abn.tan.ClusterTribalAbstractionNetwork;
import edu.njit.cs.saboc.blu.core.abn.targetbased.TargetAbstractionNetwork;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.configuration.DisjointAbNConfiguration;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.configuration.PartitionedAbNConfiguration;
import edu.njit.cs.saboc.blu.core.gui.graphframe.AbNDisplayManager;
import edu.njit.cs.saboc.blu.core.gui.graphframe.FrameCreationAction;
import edu.njit.cs.saboc.blu.core.gui.graphframe.ClusterInternalGraphFrame;
import edu.njit.cs.saboc.blu.core.gui.graphframe.DisjointAbNInternalGraphFrame;
import edu.njit.cs.saboc.blu.core.gui.graphframe.DisjointAbNInternalGraphFrame.DisjointAbNConfigurationConfiguration;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.disjointpareataxonomy.configuration.SCTDisjointPAreaTaxonomyConfigurationFactory;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.disjointtan.configuration.SCTDisjointTANConfigurationFactory;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy.configuration.SCTPAreaTaxonomyConfigurationFactory;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.tan.configuration.SCTTANConfiguration;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.tan.configuration.SCTTANConfigurationFactory;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.tan.reports.SCTTANReportDialog;
import edu.njit.cs.saboc.blu.sno.gui.graphframe.PAreaInternalGraphFrame;
import edu.njit.cs.saboc.blu.sno.gui.graphframe.SCTDiffPAreaTaxonomyGraphFrame;
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
    public void displayPAreaTaxonomy(PAreaTaxonomy data) {
        PAreaInternalGraphFrame igf = new PAreaInternalGraphFrame(mainFrame, data, this);

        super.getFrameCreationAction().displayFrame(igf);
    }

    @Override
    public void displayTribalAbstractionNetwork(ClusterTribalAbstractionNetwork tan) {
        
        ClusterInternalGraphFrame cigf = new ClusterInternalGraphFrame(
                mainFrame,
                
                (clusterTAN, displayManager) -> {
                    return new SCTTANConfigurationFactory().createConfiguration(clusterTAN, displayManager);
                },
                
                (config) -> {

                    SCTTANReportDialog reportDialog = new SCTTANReportDialog((SCTTANConfiguration)config);
                    reportDialog.showReports(config.getAbstractionNetwork());
                    reportDialog.setModal(true);

                    reportDialog.setVisible(true);
                }, 
                
                this);

        cigf.displayClusterTAN(tan);
        
        super.getFrameCreationAction().displayFrame(cigf);
    }
    
    
    @Override
    public void displayDisjointPAreaTaxonomy(
            DisjointAbstractionNetwork<DisjointNode<PArea>, PAreaTaxonomy<PArea>, PArea> disjointTaxonomy) {
        
        DisjointAbNInternalGraphFrame frame = new DisjointAbNInternalGraphFrame(
                mainFrame,
                new DisjointAbNConfigurationConfiguration() {

                    @Override
                    public DisjointAbNConfiguration getDisjointAbNConfiguration(DisjointAbstractionNetwork disjointAbN, AbNDisplayManager displayListener) {
                        return new SCTDisjointPAreaTaxonomyConfigurationFactory().createConfiguration(disjointAbN, displayListener);
                    }

                    @Override
                    public PartitionedAbNConfiguration getParentAbNConfiguration(DisjointAbstractionNetwork disjointAbN, AbNDisplayManager displayListener) {
                        return new SCTPAreaTaxonomyConfigurationFactory().createConfiguration((PAreaTaxonomy)disjointAbN.getParentAbstractionNetwork(), displayListener);
                    }

                }, 
                this);

        frame.displayDisjointAbN(disjointTaxonomy);
        
        super.getFrameCreationAction().displayFrame(frame);
    }
    
    @Override
    public void displayDisjointTribalAbstractionNetwork(
            DisjointAbstractionNetwork<DisjointNode<Cluster>, ClusterTribalAbstractionNetwork<Cluster>, Cluster> disjointTAN) {
        
        DisjointAbNInternalGraphFrame frame = new DisjointAbNInternalGraphFrame(
                mainFrame,
                new DisjointAbNConfigurationConfiguration() {

                    @Override
                    public DisjointAbNConfiguration getDisjointAbNConfiguration(DisjointAbstractionNetwork disjointAbN, AbNDisplayManager displayListener) {
                        return new SCTDisjointTANConfigurationFactory().createConfiguration(disjointAbN, displayListener);
                    }

                    @Override
                    public PartitionedAbNConfiguration getParentAbNConfiguration(DisjointAbstractionNetwork disjointAbN, AbNDisplayManager displayListener) {
                        return new SCTTANConfigurationFactory().createConfiguration((ClusterTribalAbstractionNetwork)disjointAbN.getParentAbstractionNetwork(), displayListener);
                    }
                }, 
                this);
        
        frame.displayDisjointAbN(disjointTAN);
        
        super.getFrameCreationAction().displayFrame(frame);
    }
    
    @Override
    public void displayDiffPAreaTaxonomy(DiffPAreaTaxonomy diffTaxonomy) {
        SCTDiffPAreaTaxonomyGraphFrame frame = new SCTDiffPAreaTaxonomyGraphFrame(mainFrame, this);
        frame.displayDiffPAreaTaxonomy(diffTaxonomy);
        
        super.getFrameCreationAction().displayFrame(frame);
    }

    @Override
    public void displayTargetAbstractionNetwork(TargetAbstractionNetwork targetAbN) {
        
    }

    @Override
    public void displayAreaTaxonomy(PAreaTaxonomy taxonomy) {
        
    }

    @Override
    public void displayBandTribalAbstractionNetwork(ClusterTribalAbstractionNetwork tan) {
        
    }
}
