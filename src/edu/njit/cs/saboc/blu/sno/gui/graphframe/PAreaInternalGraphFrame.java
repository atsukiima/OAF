package edu.njit.cs.saboc.blu.sno.gui.graphframe;

import edu.njit.cs.saboc.blu.core.abn.node.Node;
import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.AggregatePArea;
import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.PArea;
import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.PAreaTaxonomy;
import edu.njit.cs.saboc.blu.core.graph.BluGraph;
import edu.njit.cs.saboc.blu.core.graph.pareataxonomy.PAreaBluGraph;
import edu.njit.cs.saboc.blu.core.gui.gep.utils.drawing.AbNPainter;
import edu.njit.cs.saboc.blu.core.gui.gep.utils.drawing.SinglyRootedNodeLabelCreator;
import edu.njit.cs.saboc.blu.core.gui.graphframe.GenericInternalGraphFrame;
import edu.njit.cs.saboc.blu.core.gui.graphframe.buttons.search.PartitionedAbNSearchButton;
import edu.njit.cs.saboc.blu.sno.gui.abnselection.SCTDisplayFrameListener;
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

public class PAreaInternalGraphFrame extends GenericInternalGraphFrame {
    
    private final JButton openReportsBtn;
    
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

        super.setContainerAbNCheckboxText("Show Area Taxonomy");

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

        addReportButtonToMenu(openReportsBtn);
        
        optionsButton = new GraphOptionsButton(parentFrame, this, taxonomy);

        searchButton = new PartitionedAbNSearchButton(parentFrame, new SCTPAreaTaxonomyTextConfiguration(null));

        replaceInternalFrameDataWith(taxonomy);

        optionsButton.setToolTipText("Click to open the options menu for this graph.");
        searchButton.setToolTipText("Click to search within this graph.");
        
        addToggleableButtonToMenu(optionsButton);
        addToggleableButtonToMenu(searchButton);
    }

    public PAreaBluGraph getGraph() {
        return (PAreaBluGraph)super.getGraph();
    }

    private void updateHierarchyInfoLabel(PAreaTaxonomy taxonomy) {

        setHierarchyInfoText(String.format("Areas: %d | Partial-areas: %d | Concepts: %d",
                taxonomy.getAreaTaxonomy().getAreas().size(), 
                taxonomy.getPAreas().size(),
                taxonomy.getSourceHierarchy().size()));
    }

    public final void replaceInternalFrameDataWith(PAreaTaxonomy taxonomy) {
        
        this.currentTaxonomy = taxonomy;
        
        Thread loadThread = new Thread(() -> {
            gep.showLoading();
            
            SinglyRootedNodeLabelCreator labelCreator;

            AbNPainter abnPainter;

            if (taxonomy.isAggregated()) {
                abnPainter = new SCTAggregateTaxonomyPainter();

                labelCreator = new SinglyRootedNodeLabelCreator() {
                    public String getRootNameStr(Node node) {
                        PArea parea = (PArea)node;
                        
                        int lastIndex = parea.getRoot().getName().lastIndexOf(" (");

                        if (lastIndex == -1) {
                            return parea.getRoot().getName();
                        } else {
                            return parea.getRoot().getName().substring(0, lastIndex);
                        }
                    }

                    public String getCountStr(Node node) {
                        AggregatePArea aggregatePArea = (AggregatePArea) node;

                        if (aggregatePArea.getAggregatedNodes().isEmpty()) {
                            return super.getCountStr(aggregatePArea);
                        }

                        return String.format("(%d) [%d]", 
                                aggregatePArea.getConcepts().size(), 
                                aggregatePArea.getAggregatedNodes().size());
                    }
                };
            } else {
                abnPainter = new SCTTaxonomyPainter();

                labelCreator = new SinglyRootedNodeLabelCreator() {
                    public String getRootNameStr(Node node) {
                        PArea parea = (PArea)node;
                        
                        int lastIndex = parea.getRoot().getName().lastIndexOf(" (");

                        if (lastIndex == -1) {
                            return parea.getRoot().getName();
                        } else {
                            return parea.getRoot().getName().substring(0, lastIndex);
                        }
                    }
                };
            }

            SCTPAreaTaxonomyConfigurationFactory factory = new SCTPAreaTaxonomyConfigurationFactory();

            currentConfiguration = factory.createConfiguration(taxonomy, displayListener);

            BluGraph graph = new PAreaBluGraph(parentFrame, taxonomy, labelCreator, currentConfiguration);
            
            searchButton.initialize(currentConfiguration);

            SwingUtilities.invokeLater(() -> {
                displayAbstractionNetwork(graph, abnPainter, currentConfiguration);

                updateHierarchyInfoLabel(taxonomy);
            });
        });

        loadThread.start();
    }
}
