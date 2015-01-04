package edu.njit.cs.saboc.blu.sno.graph;

import SnomedShared.pareataxonomy.InheritedRelationship;
import SnomedShared.pareataxonomy.Region;
import edu.njit.cs.saboc.blu.core.graph.BluGraph;
import edu.njit.cs.saboc.blu.core.graph.ShowHideGroupEntryListener;
import edu.njit.cs.saboc.blu.core.graph.options.GraphOptions;
import edu.njit.cs.saboc.blu.core.gui.dialogs.ContainerResize;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTPAreaTaxonomy;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTRegion;
import edu.njit.cs.saboc.blu.sno.graph.layout.NoRegionsLayout;
import edu.njit.cs.saboc.blu.sno.graph.layout.RegionsLayout;
import edu.njit.cs.saboc.blu.sno.graph.layout.SCTGraphLayoutFactory;
import edu.njit.cs.saboc.blu.sno.gui.abnselection.SCTDisplayFrameListener;
import edu.njit.cs.saboc.blu.sno.gui.dialogs.PartitionConceptDialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

/**
 *
 * @author Chris
 */
public class PAreaBluGraph extends BluGraph {
    
    private SCTDisplayFrameListener displayListener;

    public PAreaBluGraph(final JFrame parentFrame, final SCTPAreaTaxonomy hierarchyData, boolean areaGraph, 
            boolean conceptLabels, GraphOptions options, final SCTDisplayFrameListener displayListener) {
        
        super(hierarchyData, areaGraph, conceptLabels);
        
        this.displayListener = displayListener;

        if (areaGraph) {
            layout = SCTGraphLayoutFactory.createNoRegionsPAreaLayout(this);
            ((NoRegionsLayout) layout).doLayout(options, showConceptCountLabels);

        } else {
            layout = SCTGraphLayoutFactory.createRegionsPAreaLayout(this);
            ((RegionsLayout) layout).doLayout(options, showConceptCountLabels);
        }

        partitionMenu = new JPopupMenu();
        partitionMenu.setFocusable(true);

        JMenuItem menuItem1 = new JMenuItem("Create Subtaxonomy From Relationships");

        menuItem1.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                SCTRegion region = (SCTRegion)getCurrentPartitionEntry().getPartition();

                ArrayList<Long> relTypes = new ArrayList<Long>();
                ArrayList<InheritedRelationship> regionRels = new ArrayList<InheritedRelationship>(region.getRelationships());

                for (InheritedRelationship ir : regionRels) {
                    relTypes.add(ir.getRelationshipTypeId());
                }

                SCTPAreaTaxonomy data = ((SCTPAreaTaxonomy)hierarchyData).getRelationshipSubtaxonomy(relTypes);

                displayListener.addNewPAreaGraphFrame(data, true, false);

                partitionMenu.setVisible(false);
            }
        });

        JMenuItem menuItem2 = new JMenuItem("Select PAreas to Show / Hide");
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

        JMenuItem menuItem5 = new JMenuItem("Resize Region");

        menuItem5.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                new ContainerResize(parentFrame, currentPartition, PAreaBluGraph.this);
                partitionMenu.setVisible(false);
            }
        });

        partitionMenu.add(menuItem1);
        partitionMenu.add(new JPopupMenu.Separator());
        partitionMenu.add(menuItem3);
        partitionMenu.add(new JPopupMenu.Separator());
        partitionMenu.add(menuItem2);
        partitionMenu.add(menuItem5);
    }

    public SCTPAreaTaxonomy getPAreaTaxonomy() {
        return (SCTPAreaTaxonomy)getAbstractionNetwork();
    }
    
    public SCTDisplayFrameListener getDisplayFrameListener() {
        return displayListener;
    }
}
