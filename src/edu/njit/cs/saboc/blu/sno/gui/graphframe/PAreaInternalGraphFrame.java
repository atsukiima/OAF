package edu.njit.cs.saboc.blu.sno.gui.graphframe;

import SnomedShared.Concept;
import SnomedShared.generic.GenericConceptGroup;
import SnomedShared.generic.GenericContainerPartition;
import SnomedShared.pareataxonomy.Area;
import SnomedShared.pareataxonomy.PAreaSummary;
import edu.njit.cs.saboc.blu.core.abn.AbstractionNetwork;
import edu.njit.cs.saboc.blu.core.graph.BluGraph;
import edu.njit.cs.saboc.blu.core.gui.gep.utils.GEPActionListener;
import edu.njit.cs.saboc.blu.core.gui.gep.utils.drawing.AbNPainter;
import edu.njit.cs.saboc.blu.core.gui.graphframe.GenericInternalGraphFrame;
import edu.njit.cs.saboc.blu.sno.abn.SCTAbstractionNetwork;
import edu.njit.cs.saboc.blu.sno.abn.export.ExportAbN;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.PAreaTaxonomy;
import edu.njit.cs.saboc.blu.sno.graph.PAreaBluGraph;
import edu.njit.cs.saboc.blu.sno.graph.options.GraphOptions;
import edu.njit.cs.saboc.blu.sno.gui.abnselection.SCTDisplayFrameListener;
import edu.njit.cs.saboc.blu.sno.gui.dialogs.AreaReportDialog;
import edu.njit.cs.saboc.blu.sno.gui.dialogs.ConceptGroupDetailsDialog;
import edu.njit.cs.saboc.blu.sno.gui.dialogs.LevelReportDialog;
import edu.njit.cs.saboc.blu.sno.gui.dialogs.PartitionConceptDialog;
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
    
    private PAreaInternalSearchButton searchButton;
    
    private GraphOptionsButton optionsButton;
    
    private SCTDisplayFrameListener displayListener;

    public PAreaInternalGraphFrame(final JFrame parentFrame, final PAreaTaxonomy data, 
            boolean areaGraph, boolean conceptCounts, SCTDisplayFrameListener displayListener) {
        
        super(parentFrame, "SNOMED-CT Partial-area Taxonomy");
        
        this.displayListener = displayListener;

        String frameTitle = UtilityMethods.getPrintableVersionName(data.getVersion()) + " | Hierarchy: " + data.getSNOMEDHierarchyRoot().getName();

        if (!data.getSNOMEDHierarchyRoot().equals(data.getRootPArea().getRoot())) {
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
        PAreaTaxonomy taxonomy = (PAreaTaxonomy)graph.getAbstractionNetwork();
        
        SCTDataSource dataSource = taxonomy.getSCTDataSource();
        
        ArrayList<Area> areas = taxonomy.getHierarchyAreas();
        
        HashMap<Long, ArrayList<Concept>> pareaConcepts = new HashMap<Long, ArrayList<Concept>>();
        
        for(Area a : areas) {
            ArrayList<PAreaSummary> areaPAreas = a.getAllPAreas();
            pareaConcepts.putAll(dataSource.getConceptsInPAreaSet(taxonomy, areaPAreas));
        }
        
        ExportAbN.exportAbNGroups(pareaConcepts, "PARTIALAREA");
    }

    public PAreaBluGraph getGraph() {
        return (PAreaBluGraph)super.getGraph();
    }

    private void updateHierarchyInfoLabel(PAreaTaxonomy data) {
        ArrayList<PAreaSummary> pareas = new ArrayList<PAreaSummary>();

        int pareaCount = 0;
        int areaCount = 0;

        for (Area a : data.getHierarchyAreas()) {
            if (!a.isImplicitArea()) {
                for (PAreaSummary parea : a.getAllPAreas()) {
                    pareas.add(parea);
                    pareaCount++;
                }

                areaCount++;
            }
        }

        int conceptCount;
        
        conceptCount = data.getSCTDataSource().getConceptCountInPAreaHierarchy(data, pareas);
                
        setHierarchyInfoText(String.format("Areas: %d | Partial-areas: %d | Concepts: %d",
                areaCount, pareaCount, conceptCount));
    }

    public void replaceInternalFrameDataWith(PAreaTaxonomy data,
            boolean areaGraph, boolean conceptCountLabels, GraphOptions options) {
        
        BluGraph graph = new PAreaBluGraph(parentFrame, data, areaGraph, conceptCountLabels, options, displayListener);

        initializeGraphTabs(graph, new AbNPainter(), new GEPActionListener() {
                    public void containerPartitionSelected(GenericContainerPartition partition, boolean treatedAsContainer, AbstractionNetwork abn) {
                        PartitionConceptDialog dialog = new PartitionConceptDialog(parentFrame, partition, (SCTAbstractionNetwork)abn,
                                    treatedAsContainer, displayListener);
                    }
                    
                    public void groupSelected(GenericConceptGroup group, AbstractionNetwork abn) {
                        ConceptGroupDetailsDialog dialog = new ConceptGroupDetailsDialog(group, (SCTAbstractionNetwork)abn,
                                    ConceptGroupDetailsDialog.DialogType.PartialArea, displayListener);
                    }
                });
        
        tbp = null;

        updateHierarchyInfoLabel((PAreaTaxonomy) data);

        searchButton.setGraph(graph);
        optionsButton.setGraph(graph);

        tabbedPane.addTab("Text-Diagram Hybrid View", tbp = new TextBrowserPanel((PAreaBluGraph)graph));

        tabbedPane.setToolTipTextAt(2, "<html><b>Hybrid Graph View</b> allows you to quickly obtain informatin <br>"
                + "about Areas and PAreas in a text/diagram hybrid view.");

        tabbedPane.validate();
        tabbedPane.repaint();
    }
    

    public void viewInTextBrowser(PAreaSummary parea) {
        tbp.navigateTo(parea);
        tabbedPane.setSelectedIndex(2);
    }
}
