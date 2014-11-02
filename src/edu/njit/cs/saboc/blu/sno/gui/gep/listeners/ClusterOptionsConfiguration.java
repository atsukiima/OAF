package edu.njit.cs.saboc.blu.sno.gui.gep.listeners;

import SnomedShared.overlapping.ClusterSummary;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.GroupOptionsPanelActionListener;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.GroupOptionsPanelConfiguration;
import edu.njit.cs.saboc.blu.sno.abn.tan.TANGenerator;
import edu.njit.cs.saboc.blu.sno.abn.tan.TribalAbstractionNetwork;
import edu.njit.cs.saboc.blu.sno.datastructure.hierarchy.SCTConceptHierarchy;
import edu.njit.cs.saboc.blu.sno.gui.abnselection.SCTDisplayFrameListener;
import edu.njit.cs.saboc.blu.sno.gui.dialogs.ConceptGroupDetailsDialog;
import edu.njit.cs.saboc.blu.sno.gui.graphframe.ClusterInternalGraphFrame;
import javax.swing.JFrame;

/**
 *
 * @author Chris
 */
public class ClusterOptionsConfiguration extends GroupOptionsPanelConfiguration {

    public ClusterOptionsConfiguration(
            final JFrame parentFrame, 
            final ClusterInternalGraphFrame graphFrame,
            final TribalAbstractionNetwork tan, 
            final SCTDisplayFrameListener displayListener) {
        
        super.enableButtonWithAction(0, new GroupOptionsPanelActionListener<ClusterSummary>() {
            public void actionPerformedOn(ClusterSummary cluster) {
                
               ConceptGroupDetailsDialog dialog = new ConceptGroupDetailsDialog(cluster, tan,
                            ConceptGroupDetailsDialog.DialogType.Cluster, displayListener);
            }
        });
        
        super.enableButtonWithAction(1, new GroupOptionsPanelActionListener<ClusterSummary>() {
            public void actionPerformedOn(ClusterSummary cluster) {
                displayListener.addNewBrowserFrame(cluster.getRoot(), tan.getSCTDataSource());
            }
        });
                
        super.enableButtonWithAction(4, new GroupOptionsPanelActionListener<ClusterSummary>() {
            public void actionPerformedOn(ClusterSummary cluster) {
                SCTConceptHierarchy hierarchy = tan.getSCTDataSource().getClusterConceptHierarchy(tan, cluster);

                TribalAbstractionNetwork chd = TANGenerator.createTANFromConceptHierarchy(
                        cluster.getRoot(),
                        tan.getVersion(),
                        hierarchy);

                displayListener.addNewClusterGraphFrame(chd, true, false);
            }
        });
    }
}
