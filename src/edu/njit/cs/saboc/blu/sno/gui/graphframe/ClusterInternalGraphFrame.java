package edu.njit.cs.saboc.blu.sno.gui.graphframe;

import SnomedShared.Concept;
import edu.njit.cs.saboc.blu.core.graph.BluGraph;
import edu.njit.cs.saboc.blu.core.graph.options.GraphOptions;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.exportabn.GenericExportPartitionedAbNButton;
import edu.njit.cs.saboc.blu.core.gui.gep.utils.drawing.GroupEntryLabelCreator;
import edu.njit.cs.saboc.blu.core.gui.gep.utils.drawing.tan.TANPainter;
import edu.njit.cs.saboc.blu.core.gui.graphframe.GenericInternalGraphFrame;
import edu.njit.cs.saboc.blu.sno.abn.tan.local.SCTBand;
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
import javax.swing.SwingUtilities;

public class ClusterInternalGraphFrame extends GenericInternalGraphFrame {
    
    private final TANInternalSearchButton searchButton;
    
    private final SCTDisplayFrameListener displayListener;
    
    private SCTTANConfiguration currentConfiguration;
    
    private GenericExportPartitionedAbNButton<Concept, SCTCluster, SCTBand> exportBtn;
    
    private final JButton openReportsBtn;

    public ClusterInternalGraphFrame(
            final JFrame parentFrame, 
            final SCTTribalAbstractionNetwork data, 
            final boolean setGraph, 
            final boolean conceptCounts, 
            final SCTDisplayFrameListener displayListener) {
        
        super(parentFrame, "SNOMED Tribal Abstraction Network");
        
        this.displayListener = displayListener;

        String frameTitle = UtilityMethods.getPrintableVersionName(data.getDataSource().getSelectedVersion());
        this.setTitle(frameTitle);
        
        super.setContainerAbNCheckboxText("Show Band TAN");

        openReportsBtn = new JButton("Reports and Metrics");
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

    private void updateHierarchyInfoLabel(SCTTribalAbstractionNetwork tan) {

        int setCount = tan.getBandCount();

        ArrayList<SCTCluster> clusters = new ArrayList<>(tan.getClusters().values());

        int clusterCount = tan.getClusterCount();

        int conceptCount = tan.getDataSource().getConceptCountInClusterHierarchy(tan, clusters);     
        
        setHierarchyInfoText(String.format("Bands: %d | Clusters: %d | Concepts: %d",
                setCount, clusterCount, conceptCount));
    }

    public void replaceInternalFrameDataWith(SCTTribalAbstractionNetwork data,
            boolean areaGraph, boolean conceptCountLabels, GraphOptions options) {
        
        Thread loadThread = new Thread(() -> {
            gep.showLoading();
            
            GroupEntryLabelCreator labelCreator = new GroupEntryLabelCreator<SCTCluster>() {
                public String getRootNameStr(SCTCluster cluster) {
                    return cluster.getRoot().getName().substring(0, cluster.getRoot().getName().lastIndexOf("(") - 1);
                }
            };
            
            SCTTANConfigurationFactory factory = new SCTTANConfigurationFactory();

            currentConfiguration = factory.createConfiguration(data, displayListener);

            BluGraph graph = new ClusterBluGraph(parentFrame, data, displayListener, labelCreator, currentConfiguration);

            searchButton.setGraph(graph);
           
            SwingUtilities.invokeLater(() -> {
                displayAbstractionNetwork(graph, new TANPainter(), currentConfiguration);

                if (exportBtn != null) {
                    removeReportButtonFromMenu(exportBtn);
                }

                exportBtn = new GenericExportPartitionedAbNButton<>(data, currentConfiguration);

                addReportButtonToMenu(exportBtn);

                updateHierarchyInfoLabel(data);
            });
        });
        
        loadThread.start();
    }
}
