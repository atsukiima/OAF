package edu.njit.cs.saboc.blu.sno.gui.dialogs.disjointpareataxonomy;

import SnomedShared.Concept;
import SnomedShared.pareataxonomy.PAreaSummary;
import edu.njit.cs.saboc.blu.sno.abn.disjointpareataxonomy.DisjointPAreaTaxonomy;
import edu.njit.cs.saboc.blu.sno.abn.disjointpareataxonomy.DisjointPartialArea;
import edu.njit.cs.saboc.blu.sno.abn.export.ExportAbN;
import edu.njit.cs.saboc.blu.sno.gui.abnselection.SCTDisplayFrameListener;
import edu.njit.cs.saboc.blu.sno.gui.dialogs.PAreaOverlapMetricsPanel;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;


/**
 *
 * @author Den
 */
public class DisjointPAreaTaxonomyGraphPanel extends JPanel {
    
    private DisjointPartialAreaTaxonomyPanel taxonomyPanel;
    
    public DisjointPAreaTaxonomyGraphPanel(JFrame parentFrame, final DisjointPAreaTaxonomy taxonomy, SCTDisplayFrameListener displayFrameListener) {
        super(new BorderLayout());
        
        this.taxonomyPanel = new DisjointPartialAreaTaxonomyPanel(taxonomy, displayFrameListener);
        
        JPanel optionsPanel = new JPanel();
        
        JButton exportBtn = new JButton("Export Taxonomy");
        exportBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                exportDisjointPAreaCSV();
            }
        });
        
        optionsPanel.add(exportBtn);
        
        optionsPanel.add(new DisjointPAreaSearchButton(parentFrame, taxonomyPanel));
                
        this.add(optionsPanel, BorderLayout.NORTH);
        
        JTabbedPane disjointTabs = new JTabbedPane();
        
        disjointTabs.addTab("Disjoint Partial-area Taxonomy", new JScrollPane(taxonomyPanel));
        disjointTabs.addTab("Overlapping Concept Metrics", new PAreaOverlapMetricsPanel(taxonomy));
        
        this.add(disjointTabs, BorderLayout.CENTER);
    }
    
    /**
     * Exports the disjoint parea taxonomy. 
     * 
     * TODO: There are issues if the disjoint taxonomy is a root subtaxonomy.
     */
    private void exportDisjointPAreaCSV() {
        DisjointPAreaTaxonomy taxonomy = taxonomyPanel.getDisjointPAreaTaxonomy();
        
        HashSet<DisjointPartialArea> disjointPAreas = taxonomy.getDisjointPAreas();
        
        HashMap<Long, ArrayList<Concept>> pareaConcepts = new HashMap<Long, ArrayList<Concept>>();
        
        for(DisjointPartialArea parea : disjointPAreas) {
            ArrayList<Concept> concepts = parea.getConceptsAsList();
            
            pareaConcepts.put(parea.getRoot().getId(), concepts);
        }
        
        HashSet<PAreaSummary> nonOverlapPAreas = taxonomy.getPAreasWithNoOverlap();
        
        for(PAreaSummary parea : nonOverlapPAreas) {
            pareaConcepts.put(parea.getRoot().getId(), 
                    taxonomy.getSourcePAreaTaxonomy().getSCTDataSource().getConceptsInPArea(
                        taxonomy.getSourcePAreaTaxonomy(), 
                        parea));
        }

        ExportAbN.exportAbNGroups(pareaConcepts, "DISJOINTPAREA");
    }

}
