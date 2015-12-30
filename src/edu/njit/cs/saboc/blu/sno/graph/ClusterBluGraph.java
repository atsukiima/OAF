package edu.njit.cs.saboc.blu.sno.graph;

import edu.njit.cs.saboc.blu.core.graph.BluGraph;
import edu.njit.cs.saboc.blu.core.gui.gep.utils.drawing.GroupEntryLabelCreator;
import edu.njit.cs.saboc.blu.sno.abn.tan.local.SCTCluster;
import edu.njit.cs.saboc.blu.sno.abn.tan.local.SCTTribalAbstractionNetwork;
import edu.njit.cs.saboc.blu.sno.graph.layout.ClusterNoPartitionLayout;
import edu.njit.cs.saboc.blu.sno.graph.layout.SCTGraphLayoutFactory;
import edu.njit.cs.saboc.blu.sno.graph.tan.SCTBluCluster;
import edu.njit.cs.saboc.blu.sno.graph.tan.SCTBluBand;
import edu.njit.cs.saboc.blu.sno.gui.abnselection.SCTDisplayFrameListener;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.tan.configuration.SCTTANConfiguration;
import java.util.HashMap;
import javax.swing.JFrame;

/**
 *
 * @author Chris
 */
public class ClusterBluGraph extends BluGraph {

    private SCTDisplayFrameListener displayListener;
    
    private SCTTANConfiguration config;
    
    public ClusterBluGraph(final JFrame parentFrame, 
            final SCTTribalAbstractionNetwork hierarchyData, 
            final SCTDisplayFrameListener displayListener,
            GroupEntryLabelCreator<SCTCluster> labelCreator,
            SCTTANConfiguration config) {
        
        super(hierarchyData, true, true, labelCreator);
        
        this.displayListener = displayListener;
        
        this.config = config;

        layout = SCTGraphLayoutFactory.createClusterNoPartitionLayout(this);
        
        ((ClusterNoPartitionLayout) layout).doLayout();
    }

    public SCTTribalAbstractionNetwork getClusterHierarchyData() {
        return (SCTTribalAbstractionNetwork)getAbstractionNetwork();
    }

    public SCTBluCluster getHierarchyRootCluster() {
        HashMap<Integer, SCTBluBand>  overlapSets = layout.getContainerEntries();

        return (SCTBluCluster)overlapSets.get(-2).getPartitionEntries().get(0).getVisibleGroups().get(0);
    }
    
    public SCTDisplayFrameListener getDisplayFrameListener() {
        return displayListener;
    }
    
    public SCTTANConfiguration getConfiguration() {
        return config;
    }
}
