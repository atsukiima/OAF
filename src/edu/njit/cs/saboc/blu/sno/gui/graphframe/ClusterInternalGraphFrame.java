package edu.njit.cs.saboc.blu.sno.gui.graphframe;

import edu.njit.cs.saboc.blu.core.abn.node.Node;
import edu.njit.cs.saboc.blu.core.abn.tan.ClusterTribalAbstractionNetwork;
import edu.njit.cs.saboc.blu.core.graph.BluGraph;
import edu.njit.cs.saboc.blu.core.graph.tan.ClusterBluGraph;
import edu.njit.cs.saboc.blu.core.gui.gep.AggregateableAbNInitializer;
import edu.njit.cs.saboc.blu.core.gui.gep.utils.drawing.AbNPainter;
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

public class ClusterInternalGraphFrame extends GenericInternalGraphFrame {
    
    private final PartitionedAbNSearchButton searchButton;
    
    private final SCTDisplayFrameListener displayListener;
    
    private final JButton openReportsBtn;
    
    private SCTTANConfiguration currentConfiguration;

    public ClusterInternalGraphFrame(
            JFrame parentFrame, 
            ClusterTribalAbstractionNetwork data, 
            SCTDisplayFrameListener displayListener) {
        
        super(parentFrame, "SNOMED Tribal Abstraction Network");
        
        this.displayListener = displayListener;

        super.setContainerAbNCheckboxText("Show Band TAN");
        
        openReportsBtn = new JButton("Reports and Metrics");
        openReportsBtn.addActionListener( (ae) -> {
            SCTTANReportDialog reportDialog = new SCTTANReportDialog(currentConfiguration);
            reportDialog.showReports(currentConfiguration.getAbstractionNetwork());
            reportDialog.setModal(true);

            reportDialog.setVisible(true);
        });

        addReportButtonToMenu(openReportsBtn);

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

    public final void replaceInternalFrameDataWith(ClusterTribalAbstractionNetwork tan) {
        
        Thread loadThread = new Thread(() -> {
            gep.showLoading();
            
            AbNPainter painter;
            
            if(tan.isAggregated()) {
                painter = new AggregateTANPainter();
            } else {
                painter = new TANPainter();
            }

            SinglyRootedNodeLabelCreator labelCreator = new SinglyRootedNodeLabelCreator() {
                public String getRootNameStr(Node node) {
                    return node.getName();
                }
            };
            
            SCTTANConfigurationFactory factory = new SCTTANConfigurationFactory();
            currentConfiguration = factory.createConfiguration(tan, displayListener);

            BluGraph graph = new ClusterBluGraph(tan, labelCreator, currentConfiguration);
            
            searchButton.initialize(currentConfiguration);
           
            SwingUtilities.invokeLater(() -> {
                displayAbstractionNetwork(graph, 
                        painter, 
                        currentConfiguration,
                        new AggregateableAbNInitializer( (bound) -> {
                            ClusterTribalAbstractionNetwork aggregateTAN = tan.getAggregated(bound);
                            replaceInternalFrameDataWith(aggregateTAN);
                        }));
                
                updateHierarchyInfoLabel(tan);
            });
        });
        
        loadThread.start();
    }
}
