package edu.njit.cs.saboc.blu.sno.gui.graphframe;

import SnomedShared.Concept;
import edu.njit.cs.saboc.blu.core.abn.aggregate.AggregateableConceptGroup;
import edu.njit.cs.saboc.blu.core.graph.BluGraph;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.exportabn.GenericExportPartitionedAbNButton;
import edu.njit.cs.saboc.blu.core.gui.gep.utils.drawing.AbNPainter;
import edu.njit.cs.saboc.blu.core.gui.gep.utils.drawing.GroupEntryLabelCreator;
import edu.njit.cs.saboc.blu.core.gui.graphframe.GenericInternalGraphFrame;
import edu.njit.cs.saboc.blu.core.gui.hierarchypainter.HierarchyPainterPanel;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTArea;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTPArea;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTPAreaTaxonomy;
import edu.njit.cs.saboc.blu.sno.graph.PAreaBluGraph;
import edu.njit.cs.saboc.blu.sno.gui.abnselection.SCTDisplayFrameListener;
import edu.njit.cs.saboc.blu.sno.gui.gep.configuration.SCTPAreaTaxonomyGEPConfiguration;
import edu.njit.cs.saboc.blu.sno.gui.gep.painter.SCTAggregateTaxonomyPainter;
import edu.njit.cs.saboc.blu.sno.gui.gep.painter.SCTTaxonomyPainter;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy.reports.SCTPAreaTaxonomyReportDialog;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy.reports.aggregate.SCTAggregatePAreaTaxonomyReportDialog;
import edu.njit.cs.saboc.blu.sno.gui.graphframe.buttons.GraphOptionsButton;
import edu.njit.cs.saboc.blu.sno.gui.graphframe.buttons.RelationshipSelectionButton;
import edu.njit.cs.saboc.blu.sno.gui.graphframe.buttons.search.PAreaInternalSearchButton;
import edu.njit.cs.saboc.blu.sno.gui.graphframe.textbrowser.TextBrowserPanel;
import edu.njit.cs.saboc.blu.sno.utils.UtilityMethods;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JFrame;

public class PAreaInternalGraphFrame extends GenericInternalGraphFrame {

    private TextBrowserPanel tbp;
    
    private HierarchyPainterPanel<Concept> hierarchyPanel;
    
    private final JButton openReportsBtn;
    
    private GenericExportPartitionedAbNButton<Concept, SCTPArea, SCTArea> exportBtn;
    
    private final PAreaInternalSearchButton searchButton;
    
    private final GraphOptionsButton optionsButton;
    
    private final SCTDisplayFrameListener displayListener;
    
    private SCTPAreaTaxonomyGEPConfiguration currentConfiguration;
    
    private SCTPAreaTaxonomy currentTaxonomy;

    public PAreaInternalGraphFrame(
            final JFrame parentFrame, 
            final SCTPAreaTaxonomy taxonomy, 
            boolean areaGraph, 
            SCTDisplayFrameListener displayListener) {
        
        super(parentFrame, "SNOMED CT Partial-area Taxonomy");
        
        this.displayListener = displayListener;
        
        this.currentTaxonomy = taxonomy;

        String frameTitle = UtilityMethods.getPrintableVersionName(taxonomy.getSCTVersion()) + " | Hierarchy: " + taxonomy.getSCTRootConcept().getName();

        if (!taxonomy.getSCTRootConcept().equals(taxonomy.getRootPArea().getRoot())) {
            frameTitle += " | Subhierarchy Rooted At: " + taxonomy.getRootPArea().getRoot().getName();
        }

        this.setTitle(frameTitle);

        openReportsBtn = new JButton("Partial-area Taxonomy Reports and Metrics");
        openReportsBtn.addActionListener((ActionEvent ae) -> {
            
            if (currentTaxonomy.isReduced()) {

            } else {
                SCTPAreaTaxonomyReportDialog reportDialog = new SCTPAreaTaxonomyReportDialog(currentConfiguration.getConfiguration());
                reportDialog.showReports(currentConfiguration.getConfiguration().getPAreaTaxonomy());
                reportDialog.setModal(true);

                reportDialog.setVisible(true);
            }
        });

        addReportButtonToMenu(openReportsBtn);
        
        
        final RelationshipSelectionButton filterRelationships = new RelationshipSelectionButton(parentFrame, this, taxonomy);

        optionsButton = new GraphOptionsButton(parentFrame, this, taxonomy);

        searchButton = new PAreaInternalSearchButton(parentFrame, this);

        replaceInternalFrameDataWith(taxonomy, areaGraph);

        optionsButton.setToolTipText("Click to open the options menu for this graph.");
        filterRelationships.setToolTipText("Click to open the menu to filter relationships in this graph");
        searchButton.setToolTipText("Click to search within this graph.");
        
        addToggleableButtonToMenu(optionsButton);
        addToggleableButtonToMenu(filterRelationships);
        addToggleableButtonToMenu(searchButton);
    }

    public PAreaBluGraph getGraph() {
        return (PAreaBluGraph)super.getGraph();
    }

    private void updateHierarchyInfoLabel(SCTPAreaTaxonomy data) {
        ArrayList<SCTPArea> pareas = new ArrayList<SCTPArea>();

        int pareaCount = 0;
        int areaCount = 0;

        for (SCTArea a : data.getHierarchyAreas()) {
            if (!a.isImplicit()) {
                for (SCTPArea parea : a.getAllPAreas()) {
                    pareas.add(parea);
                    pareaCount++;
                }

                areaCount++;
            }
        }

        int conceptCount;
        
        conceptCount = data.getDataSource().getConceptCountInPAreaHierarchy(data, pareas);
                
        setHierarchyInfoText(String.format("Areas: %d | Partial-areas: %d | Concepts: %d",
                areaCount, pareaCount, conceptCount));
    }

    public void replaceInternalFrameDataWith(final SCTPAreaTaxonomy taxonomy, final boolean areaGraph) {
        
        this.currentTaxonomy = taxonomy;
        
        GroupEntryLabelCreator labelCreator;
        
        AbNPainter abnPainter;
        
        if (taxonomy.isReduced()) {
            abnPainter = new SCTAggregateTaxonomyPainter();
            
            labelCreator = new GroupEntryLabelCreator<SCTPArea>() {
                public String getRootNameStr(SCTPArea parea) {
                    int lastIndex = parea.getRoot().getName().lastIndexOf(" (");
                    
                    if(lastIndex == -1) {
                        return parea.getRoot().getName();
                    } else {
                        return parea.getRoot().getName().substring(0, lastIndex);
                    }
                }
                
                public String getCountStr(SCTPArea parea) {
                    AggregateableConceptGroup reduced = (AggregateableConceptGroup)parea;
                    
                    if(reduced.getAggregatedGroups().isEmpty()) {
                        return super.getCountStr(parea);
                    }
                    
                    return String.format("(%d) [%d]", reduced.getAllGroupsConcepts().size(), reduced.getAggregatedGroups().size());
                }
            };
        } else {
            abnPainter = new SCTTaxonomyPainter();
            
            labelCreator = new GroupEntryLabelCreator<SCTPArea>() {
                public String getRootNameStr(SCTPArea parea) {
                    int lastIndex = parea.getRoot().getName().lastIndexOf(" (");
                    
                    if(lastIndex == -1) {
                        return parea.getRoot().getName();
                    } else {
                        return parea.getRoot().getName().substring(0, lastIndex);
                    }
                }
            };
        }
        
        SCTPAreaTaxonomyGEPConfiguration optionsConfig = new SCTPAreaTaxonomyGEPConfiguration(parentFrame, this, taxonomy, displayListener);

        BluGraph graph = new PAreaBluGraph(parentFrame, taxonomy, areaGraph, displayListener, labelCreator, optionsConfig);

        currentConfiguration = optionsConfig;

        
        if(exportBtn != null) {
            removeReportButtonFromMenu(exportBtn);
        }
        
        exportBtn = new GenericExportPartitionedAbNButton<>(taxonomy, currentConfiguration.getConfiguration());
        
        if(taxonomy.isReduced()) {
            exportBtn.setEnabled(false);
            
            openReportsBtn.setEnabled(false);
        } else {
            exportBtn.setEnabled(true);
            
            openReportsBtn.setEnabled(true);
        }

        addReportButtonToMenu(exportBtn);
        

        initializeGraphTabs(graph, abnPainter, optionsConfig);
        
        tbp = null;

        updateHierarchyInfoLabel((SCTPAreaTaxonomy) taxonomy);

        searchButton.setGraph(graph);
        optionsButton.setGraph(graph);

        tabbedPane.addTab("Text-Diagram Hybrid View", tbp = new TextBrowserPanel((PAreaBluGraph)graph));

        tabbedPane.setToolTipTextAt(2, "<html><b>Hybrid Graph View</b> allows you to quickly obtain informatin <br>"
                + "about Areas and PAreas in a text/diagram hybrid view.");
        
        tabbedPane.validate();
        tabbedPane.repaint();
    }

    public void viewInTextBrowser(SCTPArea parea) {
        tbp.navigateTo(parea);
        tabbedPane.setSelectedIndex(2);
    }
}
