package edu.njit.cs.saboc.blu.sno.gui.graphframe;

import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.PArea;
import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.PAreaTaxonomy;
import edu.njit.cs.saboc.blu.core.graph.AbstractionNetworkGraph;
import edu.njit.cs.saboc.blu.core.graph.pareataxonomy.PAreaTaxonomyGraph;
import edu.njit.cs.saboc.blu.core.gui.gep.utils.drawing.AbNPainter;
import edu.njit.cs.saboc.blu.core.gui.gep.utils.drawing.SinglyRootedNodeLabelCreator;
import edu.njit.cs.saboc.blu.core.gui.graphframe.GenericInternalGraphFrame;
import edu.njit.cs.saboc.blu.core.gui.graphframe.buttons.search.PartitionedAbNSearchButton;
import edu.njit.cs.saboc.blu.sno.gui.abnselection.SCTDisplayFrameListener;
import edu.njit.cs.saboc.blu.core.gui.gep.AggregateableAbNInitializer;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.exportabn.ExportPartitionedAbNButton;
import edu.njit.cs.saboc.blu.core.gui.gep.utils.drawing.AggregateSinglyRootedNodeLabelCreator;
import edu.njit.cs.saboc.blu.sno.gui.gep.painter.SCTAggregateTaxonomyPainter;
import edu.njit.cs.saboc.blu.sno.gui.gep.painter.SCTTaxonomyPainter;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy.configuration.SCTPAreaTaxonomyConfiguration;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy.configuration.SCTPAreaTaxonomyConfigurationFactory;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy.configuration.SCTPAreaTaxonomyTextConfiguration;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy.reports.SCTPAreaTaxonomyReportDialog;
import edu.njit.cs.saboc.blu.sno.gui.graphframe.buttons.GraphOptionsButton;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class PAreaInternalGraphFrame extends GenericInternalGraphFrame<PAreaTaxonomy> {
    
    private final JButton openReportsBtn;
    
    private final ExportPartitionedAbNButton exportBtn;
    
    private final PartitionedAbNSearchButton searchButton;
    
    private final GraphOptionsButton optionsButton;
    
    private final SCTDisplayFrameListener displayListener;
    
    
    private SCTPAreaTaxonomyConfiguration currentConfiguration;
    
    private PAreaTaxonomy currentTaxonomy;
    
    public PAreaInternalGraphFrame(
            final JFrame parentFrame, 
            final PAreaTaxonomy taxonomy, 
            SCTDisplayFrameListener displayListener) {
        
        super(parentFrame, "SNOMED CT Partial-area Taxonomy");
        
        this.displayListener = displayListener;
        
        this.currentTaxonomy = taxonomy;

        openReportsBtn = new JButton("Taxonomy Reports and Metrics");
        openReportsBtn.addActionListener( (ae) -> {
            
            if (currentTaxonomy.isAggregated()) {

            } else {
                SCTPAreaTaxonomyReportDialog reportDialog = new SCTPAreaTaxonomyReportDialog(currentConfiguration);
                reportDialog.showReports(taxonomy);

                reportDialog.setModal(true);
                reportDialog.setVisible(true);
            }
        });
        
        this.exportBtn = new ExportPartitionedAbNButton();

        addReportButtonToMenu(openReportsBtn);
        addReportButtonToMenu(exportBtn);
        
        optionsButton = new GraphOptionsButton(parentFrame, this, taxonomy);

        searchButton = new PartitionedAbNSearchButton(parentFrame, new SCTPAreaTaxonomyTextConfiguration(null));

        displayPAreaTaxonomy(taxonomy);

        optionsButton.setToolTipText("Click to open the options menu for this graph.");
        searchButton.setToolTipText("Click to search within this graph.");
        
        addToggleableButtonToMenu(optionsButton);
        addToggleableButtonToMenu(searchButton);
    }

    private void updateHierarchyInfoLabel(PAreaTaxonomy<PArea> taxonomy) {

        setHierarchyInfoText(String.format("Areas: %d | Partial-areas: %d | Concepts: %d",
                taxonomy.getAreaTaxonomy().getAreas().size(), 
                taxonomy.getPAreas().size(),
                taxonomy.getSourceHierarchy().size()));
    }

    public final void displayPAreaTaxonomy(PAreaTaxonomy taxonomy) {
        
        this.currentTaxonomy = taxonomy;
        
        Thread loadThread = new Thread(() -> {
            getAbNExplorationPanel().showLoading();
            
            SinglyRootedNodeLabelCreator<PArea> labelCreator;

            AbNPainter abnPainter;

            if (taxonomy.isAggregated()) {
                abnPainter = new SCTAggregateTaxonomyPainter();
                labelCreator = new AggregateSinglyRootedNodeLabelCreator<>();
            } else {
                abnPainter = new SCTTaxonomyPainter();
                labelCreator = new SinglyRootedNodeLabelCreator<>();
            }

            SCTPAreaTaxonomyConfigurationFactory factory = new SCTPAreaTaxonomyConfigurationFactory();

            currentConfiguration = factory.createConfiguration(currentTaxonomy, displayListener);

            AbstractionNetworkGraph graph = new PAreaTaxonomyGraph(getParentFrame(), currentTaxonomy, labelCreator, currentConfiguration);
            
            exportBtn.initialize(currentConfiguration);
            
            searchButton.initialize(currentConfiguration);
            
            SwingUtilities.invokeLater(() -> {
   
                displayAbstractionNetwork(graph, 
                        abnPainter, 
                        currentConfiguration, 
                        new AggregateableAbNInitializer( (bound) -> {
                            PAreaTaxonomy aggregateTaxonomy = currentTaxonomy.getAggregated(bound);
                            displayPAreaTaxonomy(aggregateTaxonomy);
                        })
                );

                updateHierarchyInfoLabel(currentTaxonomy);
            });
        });

        loadThread.start();
    }
}
