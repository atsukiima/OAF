package edu.njit.cs.saboc.blu.sno.gui.graphframe;

import SnomedShared.Concept;
import edu.njit.cs.saboc.blu.core.abn.reduced.ReducingGroup;
import edu.njit.cs.saboc.blu.core.graph.BluGraph;
import edu.njit.cs.saboc.blu.core.graph.options.GraphOptions;
import edu.njit.cs.saboc.blu.core.gui.gep.utils.drawing.GroupEntryLabelCreator;
import edu.njit.cs.saboc.blu.core.gui.graphframe.GenericInternalGraphFrame;
import edu.njit.cs.saboc.blu.core.gui.hierarchypainter.HierarchyPainterPanel;
import edu.njit.cs.saboc.blu.sno.abn.export.ExportAbN;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTArea;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTPArea;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTPAreaTaxonomy;
import edu.njit.cs.saboc.blu.sno.graph.PAreaBluGraph;
import edu.njit.cs.saboc.blu.sno.gui.abnselection.SCTDisplayFrameListener;
import edu.njit.cs.saboc.blu.sno.gui.dialogs.AreaReportDialog;
import edu.njit.cs.saboc.blu.sno.gui.dialogs.LevelReportDialog;
import edu.njit.cs.saboc.blu.sno.gui.gep.listeners.PAreaOptionsConfiguration;
import edu.njit.cs.saboc.blu.sno.gui.gep.listeners.PAreaTaxonomyGEPListener;
import edu.njit.cs.saboc.blu.sno.gui.gep.listeners.ReducedPAreaOptionsConfiguration;
import edu.njit.cs.saboc.blu.sno.gui.gep.painter.SCTTaxonomyPainter;
import edu.njit.cs.saboc.blu.sno.gui.graphframe.buttons.GraphOptionsButton;
import edu.njit.cs.saboc.blu.sno.gui.graphframe.buttons.RelationshipSelectionButton;
import edu.njit.cs.saboc.blu.sno.gui.graphframe.buttons.search.PAreaInternalSearchButton;
import edu.njit.cs.saboc.blu.sno.gui.graphframe.textbrowser.TextBrowserPanel;
import edu.njit.cs.saboc.blu.sno.sctdatasource.SCTDataSource;
import edu.njit.cs.saboc.blu.sno.utils.UtilityMethods;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.JButton;
import javax.swing.JFrame;

public class PAreaInternalGraphFrame extends GenericInternalGraphFrame {

    private TextBrowserPanel tbp;
    
    private HierarchyPainterPanel<Concept> hierarchyPanel;
    
    private PAreaInternalSearchButton searchButton;
    
    private GraphOptionsButton optionsButton;
    
    private SCTDisplayFrameListener displayListener;

    public PAreaInternalGraphFrame(final JFrame parentFrame, final SCTPAreaTaxonomy data, 
            boolean areaGraph, boolean conceptCounts, SCTDisplayFrameListener displayListener) {
        
        super(parentFrame, "SNOMED CT Partial-area Taxonomy");
        
        this.displayListener = displayListener;

        String frameTitle = UtilityMethods.getPrintableVersionName(data.getSCTVersion()) + " | Hierarchy: " + data.getSCTRootConcept().getName();

        if (!data.getSCTRootConcept().equals(data.getRootPArea().getRoot())) {
            frameTitle += " | Subhierarchy Rooted At: " + data.getRootPArea().getRoot().getName();
        }

        this.setTitle(frameTitle);

        JButton areaReportBtn = new JButton("Area Report");
        areaReportBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                AreaReportDialog dialog = new AreaReportDialog((PAreaBluGraph)graph);
            }
        });
        
        areaReportBtn.setToolTipText("Display a selectable list of Areas within this hierarchy.");

        JButton levelReportBtn = new JButton("Level Report");
        levelReportBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                LevelReportDialog dialog = new LevelReportDialog(graph);
            }
        });
        
        levelReportBtn.setToolTipText("Display a list of detailed information about each level of this hierarchy.");
        
        JButton exportBtn = new JButton("Export Taxonomy");
        exportBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                exportPAreaCSV();
            }
        });
        
        exportBtn.setToolTipText("Exports the taxonomy as a tab-delimited CSV file.");
        
        addReportButtonToMenu(areaReportBtn);
        addReportButtonToMenu(levelReportBtn);
        addReportButtonToMenu(exportBtn);
        
        final RelationshipSelectionButton filterRelationships = new RelationshipSelectionButton(parentFrame, this, data);

        optionsButton = new GraphOptionsButton(parentFrame, this, data);

        searchButton = new PAreaInternalSearchButton(parentFrame, this);

        replaceInternalFrameDataWith(data, areaGraph, conceptCounts, null);

        optionsButton.setToolTipText("Click to open the options menu for this graph.");
        filterRelationships.setToolTipText("Click to open the menu to filter relationships in this graph");
        searchButton.setToolTipText("Click to search within this graph.");
        
        addToggleableButtonToMenu(optionsButton);
        addToggleableButtonToMenu(filterRelationships);
        addToggleableButtonToMenu(searchButton);
    }
    
    private void exportPAreaCSV() {
        SCTPAreaTaxonomy taxonomy = (SCTPAreaTaxonomy) graph.getAbstractionNetwork();

        if (taxonomy.isReduced()) {
            ExportAbN.exportAggregateTaxonomy(taxonomy);
        } else {

            SCTDataSource dataSource = taxonomy.getDataSource();

            ArrayList<SCTArea> areas = taxonomy.getHierarchyAreas();

            HashMap<Long, ArrayList<Concept>> pareaConcepts = new HashMap<Long, ArrayList<Concept>>();

            for (SCTArea a : areas) {
                ArrayList<SCTPArea> areaPAreas = a.getAllPAreas();
                pareaConcepts.putAll(dataSource.getConceptsInPAreaSet(taxonomy, areaPAreas));
            }

            ExportAbN.exportAbNGroups(pareaConcepts, "PARTIALAREA");
        }

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

    public void replaceInternalFrameDataWith(final SCTPAreaTaxonomy data,
            final boolean areaGraph, final boolean conceptCountLabels, final GraphOptions options) {
        
        GroupEntryLabelCreator labelCreator;
        
        if (data.isReduced()) {
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
                    ReducingGroup reduced = (ReducingGroup)parea;
                    
                    if(reduced.getReducedGroups().size() - 1 == 0) {
                        return super.getCountStr(parea);
                    }
                    
                    return String.format("(%d) [%d]", reduced.getAllGroupsConcepts().size(), reduced.getReducedGroups().size());
                }
            };
        } else {
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
        
        BluGraph graph = new PAreaBluGraph(parentFrame, data, areaGraph, conceptCountLabels, options, displayListener, labelCreator);
        
        PAreaOptionsConfiguration optionsConfig;
        
        if(data.isReduced()) {
            optionsConfig = new ReducedPAreaOptionsConfiguration(parentFrame, this, data, displayListener);
        } else {
            optionsConfig = new PAreaOptionsConfiguration(parentFrame, this, data, displayListener);
        }

        initializeGraphTabs(graph, new SCTTaxonomyPainter(), 
                new PAreaTaxonomyGEPListener(parentFrame, displayListener), 
                optionsConfig);
        
        tbp = null;

        updateHierarchyInfoLabel((SCTPAreaTaxonomy) data);

        searchButton.setGraph(graph);
        optionsButton.setGraph(graph);

        tabbedPane.addTab("Text-Diagram Hybrid View", tbp = new TextBrowserPanel((PAreaBluGraph)graph));

        tabbedPane.setToolTipTextAt(2, "<html><b>Hybrid Graph View</b> allows you to quickly obtain informatin <br>"
                + "about Areas and PAreas in a text/diagram hybrid view.");
        
        tabbedPane.validate();
        tabbedPane.repaint();
        
        /*
        if (data.getAbstractionNetwork().getDataSource().isLocalDataSource() && data.getAbstractionNetwork().getConceptHierarchy().getNodesInHierarchy().size() < 3000) {
            hierarchyPanel = new HierarchyPainterPanel<Concept>();

            tabbedPane.add("Complete Concept Hierarchy", hierarchyPanel);

            Thread painterThread = new Thread(new Runnable() {
                public void run() {
                    hierarchyPanel.paintHierarchy(data.getConceptHierarchy());
                }
            });

            painterThread.run();
        }
        */

    }

    public void viewInTextBrowser(SCTPArea parea) {
        tbp.navigateTo(parea);
        tabbedPane.setSelectedIndex(2);
    }
}
