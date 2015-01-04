package edu.njit.cs.saboc.blu.sno.gui.graphframe;

import SnomedShared.Concept;
import SnomedShared.overlapping.ClusterSummary;
import SnomedShared.overlapping.CommonOverlapSet;
import edu.njit.cs.saboc.blu.core.graph.BluGraph;
import edu.njit.cs.saboc.blu.core.graph.options.GraphOptions;
import edu.njit.cs.saboc.blu.core.gui.gep.utils.drawing.AbNPainter;
import edu.njit.cs.saboc.blu.core.gui.graphframe.GenericInternalGraphFrame;
import edu.njit.cs.saboc.blu.sno.abn.export.ExportAbN;
import edu.njit.cs.saboc.blu.sno.abn.tan.TribalAbstractionNetwork;
import edu.njit.cs.saboc.blu.sno.graph.ClusterBluGraph;
import edu.njit.cs.saboc.blu.sno.gui.abnselection.SCTDisplayFrameListener;
import edu.njit.cs.saboc.blu.sno.gui.dialogs.LevelReportDialog;
import edu.njit.cs.saboc.blu.sno.gui.gep.listeners.ClusterOptionsConfiguration;
import edu.njit.cs.saboc.blu.sno.gui.gep.listeners.TribalGEPListener;
import edu.njit.cs.saboc.blu.sno.gui.graphframe.buttons.search.TANInternalSearchButton;
import edu.njit.cs.saboc.blu.sno.sctdatasource.SCTDataSource;
import edu.njit.cs.saboc.blu.sno.utils.UtilityMethods;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.JButton;
import javax.swing.JFrame;

public class ClusterInternalGraphFrame extends GenericInternalGraphFrame {
    
    private TANInternalSearchButton searchButton;
    
    private SCTDisplayFrameListener displayListener;

    public ClusterInternalGraphFrame(final JFrame parentFrame, final TribalAbstractionNetwork data, final boolean setGraph, 
            final boolean conceptCounts, final SCTDisplayFrameListener displayListener) {
        
        super(parentFrame, "SNOMED Tribal Abstraction Network");
        
        this.displayListener = displayListener;

        String frameTitle = UtilityMethods.getPrintableVersionName(data.getSCTVersion()) + " | Hierarchy: " + data.getSCTRootConcept().getName();
        this.setTitle(frameTitle);

        JButton tribalBandReportBtn = new JButton("Common Overlap Set Report");
        tribalBandReportBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                
            }
        });

        tribalBandReportBtn.setToolTipText("Display a selectable list of Clusters within this hierarchy.");

        JButton levelReportBtn = new JButton("Level Report");
        levelReportBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                new LevelReportDialog(graph);
            }
        });
        
        JButton exportBtn = new JButton("Export TAN");
        exportBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                exportTANCSV();
            }
        });

        levelReportBtn.setToolTipText("Display a list of detailed information about each level of this Tribal AN.");

        searchButton = new TANInternalSearchButton(parentFrame, this);

        addReportButtonToMenu(tribalBandReportBtn);
        addReportButtonToMenu(levelReportBtn);
        addReportButtonToMenu(exportBtn);
        
        replaceInternalFrameDataWith(data, setGraph, conceptCounts, null);
        
        addToggleableButtonToMenu(searchButton);
    }

    private void updateHierarchyInfoLabel(TribalAbstractionNetwork data) {
        ArrayList<ClusterSummary> clusters = new ArrayList<ClusterSummary>();

        int clusterCount = 0;
        int setCount = 0;

        ArrayList<ClusterSummary> patriarchs = data.getHierarchyEntryPoints();

        for (ClusterSummary s : patriarchs) {
            clusters.add(s);
        }

        for (CommonOverlapSet a : data.getBands()) {
            for (ClusterSummary cluster : a.getAllClusters()) {
                clusters.add(cluster);

                clusterCount++;
            }

            setCount++;
        }

        int conceptCount = data.getDataSource().getConceptCountInClusterHierarchy(
                (TribalAbstractionNetwork)data, clusters);     
        
        setHierarchyInfoText(String.format("Common Overlap Sets: %d | Clusters: %d | Concepts: %d",
                setCount, clusterCount, conceptCount));
    }
    
    private void exportTANCSV() {
        TribalAbstractionNetwork tan = (TribalAbstractionNetwork) graph.getAbstractionNetwork();

        SCTDataSource dataSource = tan.getDataSource();

        ArrayList<CommonOverlapSet> bands = tan.getBands();

        HashMap<Long, ArrayList<Concept>> clusterConcepts = new HashMap<Long, ArrayList<Concept>>();
        
        clusterConcepts.putAll(dataSource.getConceptsInClusterSet(tan, tan.getHierarchyEntryPoints()));

        for (CommonOverlapSet band : bands) {
            ArrayList<ClusterSummary> areaPAreas = band.getAllClusters();
            clusterConcepts.putAll(dataSource.getConceptsInClusterSet(tan, areaPAreas));
        }

        ExportAbN.exportAbNGroups(clusterConcepts, "CLUSTER");
    }

    public void replaceInternalFrameDataWith(TribalAbstractionNetwork data,
            boolean areaGraph, boolean conceptCountLabels, GraphOptions options) {

        BluGraph graph = new ClusterBluGraph(parentFrame, data, areaGraph, conceptCountLabels, options, displayListener);
        
        searchButton.setGraph(graph);
    
        initializeGraphTabs(graph, new AbNPainter(), 
                new TribalGEPListener(parentFrame, displayListener), 
                new ClusterOptionsConfiguration(parentFrame, this, data, displayListener));
        
        updateHierarchyInfoLabel(data);
    }
}
