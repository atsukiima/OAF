package edu.njit.cs.saboc.blu.sno.gui.graphframe;

import edu.njit.cs.saboc.blu.core.abn.tan.Cluster;
import edu.njit.cs.saboc.blu.core.abn.tan.ClusterTribalAbstractionNetwork;
import edu.njit.cs.saboc.blu.core.graph.AbstractionNetworkGraph;
import edu.njit.cs.saboc.blu.core.graph.tan.ClusterTANGraph;
import edu.njit.cs.saboc.blu.core.gui.gep.AggregateableAbNInitializer;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.exportabn.ExportPartitionedAbNButton;
import edu.njit.cs.saboc.blu.core.gui.gep.utils.drawing.AbNPainter;
import edu.njit.cs.saboc.blu.core.gui.gep.utils.drawing.AggregateSinglyRootedNodeLabelCreator;
import edu.njit.cs.saboc.blu.core.gui.gep.utils.drawing.SinglyRootedNodeLabelCreator;
import edu.njit.cs.saboc.blu.core.gui.gep.utils.drawing.tan.AggregateTANPainter;
import edu.njit.cs.saboc.blu.core.gui.gep.utils.drawing.tan.TANPainter;
import edu.njit.cs.saboc.blu.core.gui.graphframe.GenericInternalGraphFrame;
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

    public ClusterInternalGraphFrame(
            JFrame parentFrame, 
            ClusterTribalAbstractionNetwork data, 
            SCTDisplayFrameListener displayListener) {
        
        super(parentFrame, "SNOMED Tribal Abstraction Network");
        
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
        
        replaceInternalFrameDataWith(data);
        
        addToggleableButtonToMenu(searchButton);
    }

    private void updateHierarchyInfoLabel(ClusterTribalAbstractionNetwork tan) {
        int setCount = tan.getBands().size();
        int clusterCount = tan.getClusters().size();
        int conceptCount = tan.getSourceHierarchy().size();
        
        setHierarchyInfoText(String.format("Bands: %d | Clusters: %d | Concepts: %d",
                setCount, clusterCount, conceptCount));
    }

    public final void replaceInternalFrameDataWith(ClusterTribalAbstractionNetwork<Cluster> tan) {
        
        Thread loadThread = new Thread(() -> {
            getAbNExplorationPanel().showLoading();
            
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
            currentConfiguration = factory.createConfiguration(tan, displayListener);

            AbstractionNetworkGraph newGraph = new ClusterTANGraph(tan, labelCreator, currentConfiguration);
            
            exportBtn.initialize(currentConfiguration);
            searchButton.initialize(currentConfiguration);
           
            SwingUtilities.invokeLater(() -> {
                displayAbstractionNetwork(newGraph, 
                        painter, 
                        currentConfiguration,
                        new AggregateableAbNInitializer( (bound) -> {
                            ClusterTribalAbstractionNetwork aggregateTAN = currentConfiguration.getTribalAbstractionNetwork().getAggregated(bound);
                            replaceInternalFrameDataWith(aggregateTAN);
                        }));
                
                updateHierarchyInfoLabel(tan);
            });
        });
        
        loadThread.start();
    }
}
