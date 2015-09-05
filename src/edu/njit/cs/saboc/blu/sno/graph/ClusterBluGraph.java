package edu.njit.cs.saboc.blu.sno.graph;

import SnomedShared.overlapping.ClusterSummary;
import edu.njit.cs.saboc.blu.core.graph.BluGraph;
import edu.njit.cs.saboc.blu.core.graph.ShowHideGroupEntryListener;
import edu.njit.cs.saboc.blu.core.graph.options.GraphOptions;
import edu.njit.cs.saboc.blu.core.gui.dialogs.ContainerResize;
import edu.njit.cs.saboc.blu.core.gui.gep.utils.drawing.GroupEntryLabelCreator;
import edu.njit.cs.saboc.blu.sno.abn.tan.TribalAbstractionNetwork;
import edu.njit.cs.saboc.blu.sno.abn.tan.local.SCTTribalAbstractionNetwork;
import edu.njit.cs.saboc.blu.sno.graph.layout.ClusterNoPartitionLayout;
import edu.njit.cs.saboc.blu.sno.graph.layout.SCTGraphLayoutFactory;
import edu.njit.cs.saboc.blu.sno.graph.tan.BluCluster;
import edu.njit.cs.saboc.blu.sno.graph.tan.BluCommonOverlapSet;
import edu.njit.cs.saboc.blu.sno.gui.abnselection.SCTDisplayFrameListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

/**
 *
 * @author Chris
 */
public class ClusterBluGraph extends BluGraph {

    private SCTDisplayFrameListener displayListener;
    
    public ClusterBluGraph(final JFrame parentFrame, 
            final SCTTribalAbstractionNetwork hierarchyData, 
            final SCTDisplayFrameListener displayListener,
            GroupEntryLabelCreator<ClusterSummary> labelCreator) {
        
        super(hierarchyData, true, true, labelCreator);
        
        this.displayListener = displayListener;

        layout = SCTGraphLayoutFactory.createClusterNoPartitionLayout(this);
        ((ClusterNoPartitionLayout) layout).doLayout();
    }

    public TribalAbstractionNetwork getClusterHierarchyData() {
        return (TribalAbstractionNetwork)getAbstractionNetwork();
    }

    public BluCluster getHierarchyRootCluster() {
        HashMap<Integer, BluCommonOverlapSet>  overlapSets = layout.getContainerEntries();

        return (BluCluster)overlapSets.get(-2).getPartitionEntries().get(0).getVisibleGroups().get(0);
    }
    
    public SCTDisplayFrameListener getDisplayFrameListener() {
        return displayListener;
    }
}
