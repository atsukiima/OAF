package edu.njit.cs.saboc.blu.sno.graph;

import SnomedShared.pareataxonomy.Region;
import edu.njit.cs.saboc.blu.core.graph.BluGraph;
import edu.njit.cs.saboc.blu.core.graph.ShowHideGroupEntryListener;
import edu.njit.cs.saboc.blu.core.gui.dialogs.ContainerResize;
import edu.njit.cs.saboc.blu.sno.abn.tan.TribalAbstractionNetwork;
import edu.njit.cs.saboc.blu.sno.graph.layout.ClusterNoPartitionLayout;
import edu.njit.cs.saboc.blu.sno.graph.layout.ClusterPartitionLayout;
import edu.njit.cs.saboc.blu.sno.graph.layout.SCTGraphLayoutFactory;
import edu.njit.cs.saboc.blu.sno.graph.options.GraphOptions;
import edu.njit.cs.saboc.blu.sno.graph.tan.BluCluster;
import edu.njit.cs.saboc.blu.sno.graph.tan.BluCommonOverlapSet;
import edu.njit.cs.saboc.blu.sno.gui.abnselection.SCTDisplayFrameListener;
import edu.njit.cs.saboc.blu.sno.gui.dialogs.PartitionConceptDialog;
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
    
    public ClusterBluGraph(final JFrame parentFrame, final TribalAbstractionNetwork hierarchyData, 
            boolean setGraph, boolean conceptLabels, GraphOptions options, final SCTDisplayFrameListener displayListener) {
        
        super(hierarchyData, setGraph, conceptLabels);
        
        this.displayListener = displayListener;

        if (setGraph) {
            layout = SCTGraphLayoutFactory.createClusterNoPartitionLayout(this);
            ((ClusterNoPartitionLayout)layout).doLayout(options, showConceptCountLabels);

        } else {
            layout = SCTGraphLayoutFactory.createClusterPartitionLayout(this);
            ((ClusterPartitionLayout) layout).doLayout(options, showConceptCountLabels);
        }

        partitionMenu = new JPopupMenu();
        partitionMenu.setFocusable(true);

        JMenuItem menuItem2 = new JMenuItem("Select Clusters to Show / Hide");
        menuItem2.addActionListener(new ShowHideGroupEntryListener(this));

        JMenuItem menuItem3 = new JMenuItem("Show Concepts in Region");
        menuItem3.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                Region region = (Region)getCurrentPartitionEntry().getPartition();

                partitionMenu.setVisible(false);

                new PartitionConceptDialog(parentFrame, region, hierarchyData,
                        getCurrentPartitionEntry().getPartitionTreatedAsContainer(), displayListener);
            }
        });

        JMenuItem menuItem5 = new JMenuItem("Resize Layout");

        menuItem5.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                new ContainerResize(parentFrame, currentPartition, ClusterBluGraph.this);
                partitionMenu.setVisible(false);
            }
        });

        partitionMenu.add(new JPopupMenu.Separator());
        partitionMenu.add(menuItem3);
        partitionMenu.add(new JPopupMenu.Separator());
        partitionMenu.add(menuItem2);
        partitionMenu.add(menuItem5);
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
