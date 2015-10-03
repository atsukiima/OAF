package edu.njit.cs.saboc.blu.sno.gui.graphframe;

import SnomedShared.Concept;
import SnomedShared.overlapping.ClusterSummary;
import SnomedShared.overlapping.CommonOverlapSet;
import edu.njit.cs.saboc.blu.core.graph.BluGraph;
import edu.njit.cs.saboc.blu.core.graph.options.GraphOptions;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.exportabn.GenericExportPartitionedAbNButton;
import edu.njit.cs.saboc.blu.core.gui.gep.utils.drawing.AbNPainter;
import edu.njit.cs.saboc.blu.core.gui.gep.utils.drawing.GroupEntryLabelCreator;
import edu.njit.cs.saboc.blu.core.gui.graphframe.GenericInternalGraphFrame;
import edu.njit.cs.saboc.blu.sno.abn.tan.local.SCTCluster;
import edu.njit.cs.saboc.blu.sno.abn.tan.local.SCTTribalAbstractionNetwork;
import edu.njit.cs.saboc.blu.sno.graph.ClusterBluGraph;
import edu.njit.cs.saboc.blu.sno.gui.abnselection.SCTDisplayFrameListener;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.tan.configuration.SCTTANConfiguration;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.tan.configuration.SCTTANConfigurationFactory;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.tan.reports.SCTTANReportDialog;
import edu.njit.cs.saboc.blu.sno.gui.graphframe.buttons.search.TANInternalSearchButton;
import edu.njit.cs.saboc.blu.sno.utils.UtilityMethods;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JFrame;

public class ClusterInternalGraphFrame extends GenericInternalGraphFrame {
    
    private final TANInternalSearchButton searchButton;
    
    private final SCTDisplayFrameListener displayListener;
    
    private SCTTANConfiguration currentConfiguration;
    
    private GenericExportPartitionedAbNButton<Concept, SCTCluster, CommonOverlapSet> exportBtn;
    
    private final JButton openReportsBtn;

    public ClusterInternalGraphFrame(
            final JFrame parentFrame, 
            final SCTTribalAbstractionNetwork data, 
            final boolean setGraph, 
            final boolean conceptCounts, 
            final SCTDisplayFrameListener displayListener) {
        
        super(parentFrame, "SNOMED Tribal Abstraction Network");
        
        this.displayListener = displayListener;

        String frameTitle = UtilityMethods.getPrintableVersionName(data.getSCTVersion());
        this.setTitle(frameTitle);

        openReportsBtn = new JButton("TAN Reports and Metrics");
        openReportsBtn.addActionListener((ActionEvent ae) -> {
            SCTTANReportDialog reportDialog = new SCTTANReportDialog(currentConfiguration);
            reportDialog.showReports(currentConfiguration.getDataConfiguration().getTribalAbstractionNetwork());
            reportDialog.setModal(true);
            
            reportDialog.setVisible(true);
        });
        
        addReportButtonToMenu(openReportsBtn);

        searchButton = new TANInternalSearchButton(parentFrame, this);
        
        replaceInternalFrameDataWith(data, setGraph, conceptCounts, null);
        
        addToggleableButtonToMenu(searchButton);
    }

    private void updateHierarchyInfoLabel(SCTTribalAbstractionNetwork data) {
        ArrayList<SCTCluster> clusters = new ArrayList<>();

        int clusterCount = 0;
        int setCount = 0;

        ArrayList<SCTCluster> patriarchs = data.getHierarchyEntryPoints();

        for (SCTCluster s : patriarchs) {
            clusters.add(s);
        }

        for (CommonOverlapSet a : data.getBands()) {
            for (ClusterSummary cluster : a.getAllClusters()) {
                clusters.add((SCTCluster)cluster);

                clusterCount++;
            }

            setCount++;
        }

        int conceptCount = data.getDataSource().getConceptCountInClusterHierarchy(data, clusters);     
        
        setHierarchyInfoText(String.format("Common Overlap Sets: %d | Clusters: %d | Concepts: %d",
                setCount, clusterCount, conceptCount));
    }

    public void replaceInternalFrameDataWith(SCTTribalAbstractionNetwork data,
            boolean areaGraph, boolean conceptCountLabels, GraphOptions options) {
        
        GroupEntryLabelCreator labelCreator = new GroupEntryLabelCreator<ClusterSummary>() {
            public String getRootNameStr(ClusterSummary cluster) {
                return cluster.getRoot().getName().substring(0, cluster.getRoot().getName().lastIndexOf("(") - 1);
            }
        };

        BluGraph graph = new ClusterBluGraph(parentFrame, data, displayListener, labelCreator);
        
        searchButton.setGraph(graph);
        
        SCTTANConfigurationFactory factory = new SCTTANConfigurationFactory();

        currentConfiguration = factory.createConfiguration(data, displayListener);
    
        initializeGraphTabs(graph, new AbNPainter(), currentConfiguration);
        
        if(exportBtn != null) {
            removeReportButtonFromMenu(exportBtn);
        }
        
        exportBtn = new GenericExportPartitionedAbNButton<>(data, currentConfiguration);

        addReportButtonToMenu(exportBtn);
        
        updateHierarchyInfoLabel(data);
    }
}
