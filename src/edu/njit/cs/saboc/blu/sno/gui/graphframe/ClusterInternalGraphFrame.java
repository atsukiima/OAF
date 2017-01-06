package edu.njit.cs.saboc.blu.sno.gui.graphframe;

import edu.njit.cs.saboc.blu.core.abn.tan.Cluster;
import edu.njit.cs.saboc.blu.core.abn.tan.ClusterTribalAbstractionNetwork;
import edu.njit.cs.saboc.blu.core.graph.AbstractionNetworkGraph;
import edu.njit.cs.saboc.blu.core.graph.tan.BandTANGraph;
import edu.njit.cs.saboc.blu.core.graph.tan.ClusterTANGraph;
import edu.njit.cs.saboc.blu.core.gui.gep.AggregateableAbNInitializer;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.exportabn.ExportPartitionedAbNButton;
import edu.njit.cs.saboc.blu.core.gui.gep.utils.drawing.AbNPainter;
import edu.njit.cs.saboc.blu.core.gui.gep.utils.drawing.AggregateSinglyRootedNodeLabelCreator;
import edu.njit.cs.saboc.blu.core.gui.gep.utils.drawing.PartitionedAbNPainter;
import edu.njit.cs.saboc.blu.core.gui.gep.utils.drawing.SinglyRootedNodeLabelCreator;
import edu.njit.cs.saboc.blu.core.gui.gep.utils.drawing.tan.AggregateTANPainter;
import edu.njit.cs.saboc.blu.core.gui.gep.utils.drawing.tan.TANPainter;
import edu.njit.cs.saboc.blu.core.gui.graphframe.GenericInternalGraphFrame;
import edu.njit.cs.saboc.blu.core.gui.graphframe.buttons.PartitionedAbNSelectionPanel;
import edu.njit.cs.saboc.blu.core.gui.graphframe.buttons.search.PartitionedAbNSearchButton;
import edu.njit.cs.saboc.blu.sno.gui.abnselection.SCTDisplayFrameListener;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.tan.configuration.SCTTANConfiguration;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.tan.configuration.SCTTANConfigurationFactory;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.tan.configuration.SCTTANTextConfiguration;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.tan.reports.SCTTANReportDialog;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class ClusterInternalGraphFrame extends GenericInternalGraphFrame<ClusterTribalAbstractionNetwork> {
    
    private final PartitionedAbNSearchButton searchButton;
    
    private final SCTDisplayFrameListener displayListener;
    
    private final JButton openReportsBtn;
    private final ExportPartitionedAbNButton exportBtn;
    
    private SCTTANConfiguration currentConfiguration;
    
    private final PartitionedAbNSelectionPanel abnTypeSelectionPanel;

    public ClusterInternalGraphFrame(
            JFrame parentFrame, 
            SCTDisplayFrameListener displayListener) {
        
        super(parentFrame, "SNOMED CT Tribal Abstraction Network");
        
        this.displayListener = displayListener;
        
        openReportsBtn = new JButton("Reports and Metrics");
        openReportsBtn.addActionListener( (ae) -> {
            SCTTANReportDialog reportDialog = new SCTTANReportDialog(currentConfiguration);
            reportDialog.showReports(currentConfiguration.getAbstractionNetwork());
            reportDialog.setModal(true);

            reportDialog.setVisible(true);
        });
        
        exportBtn = new ExportPartitionedAbNButton();

        addReportButtonToMenu(openReportsBtn);
        addReportButtonToMenu(exportBtn);

        searchButton = new PartitionedAbNSearchButton(parentFrame, new SCTTANTextConfiguration(null));
        
        addToggleableButtonToMenu(searchButton);
        
        this.abnTypeSelectionPanel = new PartitionedAbNSelectionPanel() {

            @Override
            public void showFullClicked() {
                displayClusterTAN(currentConfiguration.getTribalAbstractionNetwork());
            }

            @Override
            public void showBaseClicked() {
                displayBandTAN(currentConfiguration.getTribalAbstractionNetwork());
            }
        };

        this.addOtherOptionsComponent(abnTypeSelectionPanel);
    }

    private void updateHierarchyInfoLabel(ClusterTribalAbstractionNetwork tan) {
        int setCount = tan.getBands().size();
        int clusterCount = tan.getClusters().size();
        int conceptCount = tan.getSourceHierarchy().size();
        
        setHierarchyInfoText(String.format("Bands: %d | Clusters: %d | Concepts: %d",
                setCount, clusterCount, conceptCount));
    }

    public void displayClusterTAN(ClusterTribalAbstractionNetwork tan) {
        getAbNExplorationPanel().showLoading();
        
        Thread loadThread = new Thread(() -> {

            AbNPainter painter;
            SinglyRootedNodeLabelCreator<Cluster> labelCreator;
            
            if(tan.isAggregated()) {
                painter = new AggregateTANPainter();
                labelCreator = new AggregateSinglyRootedNodeLabelCreator<>();
            } else {
                painter = new TANPainter();
                labelCreator = new SinglyRootedNodeLabelCreator<>();
            }

            SCTTANConfigurationFactory factory = new SCTTANConfigurationFactory();
            SCTTANConfiguration config = factory.createConfiguration(tan, displayListener);

            AbstractionNetworkGraph newGraph = new ClusterTANGraph(tan, labelCreator, config);
            
            displayTAN(config, newGraph, painter, true);
        });
        
        loadThread.start();
    }
    
    public final void displayBandTAN(ClusterTribalAbstractionNetwork tan) {
        getAbNExplorationPanel().showLoading();
        
        Thread loadThread = new Thread(() -> {
            PartitionedAbNPainter abnPainter = new PartitionedAbNPainter();
            
            SCTTANConfigurationFactory factory = new SCTTANConfigurationFactory();
            SCTTANConfiguration config = factory.createConfiguration(tan, displayListener);

            abnPainter.initialize(tan);

            BandTANGraph newGraph = new BandTANGraph(tan, new SinglyRootedNodeLabelCreator<>(), config);

            displayTAN(config, newGraph, abnPainter, false);
        });

        loadThread.start();
    }
    
    private void displayTAN(
            SCTTANConfiguration config,
            AbstractionNetworkGraph<ClusterTribalAbstractionNetwork> graph,
            AbNPainter painter,
            boolean showClusterTAN) {
        
        this.currentConfiguration = config;

        abnTypeSelectionPanel.initialize(config, showClusterTAN);
        exportBtn.initialize(config);
        searchButton.initialize(config);

        SwingUtilities.invokeLater(() -> {
            displayAbstractionNetwork(graph,
                    painter,
                    currentConfiguration,
                    new AggregateableAbNInitializer((bound) -> {
                        ClusterTribalAbstractionNetwork aggregateTAN = currentConfiguration.getAbstractionNetwork().getAggregated(bound);

                        displayClusterTAN(aggregateTAN);
                    }));

            updateHierarchyInfoLabel(config.getTribalAbstractionNetwork());
        });
    }
}
