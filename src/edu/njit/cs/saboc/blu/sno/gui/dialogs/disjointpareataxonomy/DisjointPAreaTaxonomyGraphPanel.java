package edu.njit.cs.saboc.blu.sno.gui.dialogs.disjointpareataxonomy;

import SnomedShared.Concept;
import edu.njit.cs.saboc.blu.sno.abn.disjointpareataxonomy.DisjointPAreaTaxonomy;
import edu.njit.cs.saboc.blu.sno.abn.disjointpareataxonomy.DisjointPartialArea;
import edu.njit.cs.saboc.blu.sno.abn.export.ExportAbN;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTPArea;
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
import javax.swing.JTabbedPane;


/**
 *
 * @author Den
 */
public class DisjointPAreaTaxonomyGraphPanel extends JPanel {

    private final DisjointPAreaTaxonomy taxonomy;
    
    public DisjointPAreaTaxonomyGraphPanel(JFrame parentFrame, final DisjointPAreaTaxonomy taxonomy, SCTDisplayFrameListener displayFrameListener) {
        super(new BorderLayout());
        
        this.taxonomy = taxonomy;

        JPanel optionsPanel = new JPanel();
        
        JButton displayDisjointBtn = new JButton("Display Disjoint Partial-area Taxonomy");
        displayDisjointBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                displayFrameListener.addNewDisjointPAreaTaxonomyGraphFrame(taxonomy);
            }
        });
        
        optionsPanel.add(displayDisjointBtn);
        
        JButton exportBtn = new JButton("Export Taxonomy");
        exportBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                exportDisjointPAreaCSV();
            }
        });
        
        optionsPanel.add(exportBtn);
                
        this.add(optionsPanel, BorderLayout.NORTH);
        
        JTabbedPane disjointTabs = new JTabbedPane();
        disjointTabs.addTab("Overlapping Concept Metrics", new PAreaOverlapMetricsPanel(taxonomy));
        
        this.add(disjointTabs, BorderLayout.CENTER);
    }
    
    /**
     * Exports the disjoint parea taxonomy. 
     * 
     * TODO: There are issues if the disjoint taxonomy is a root subtaxonomy.
     */
    private void exportDisjointPAreaCSV() {

        HashSet<DisjointPartialArea> disjointPAreas = taxonomy.getDisjointGroups();
        
        HashMap<Long, ArrayList<Concept>> pareaConcepts = new HashMap<Long, ArrayList<Concept>>();
        
        for(DisjointPartialArea parea : disjointPAreas) {
            ArrayList<Concept> concepts = parea.getConceptsAsList();
            
            pareaConcepts.put(parea.getRoot().getId(), concepts);
        }
        
        HashSet<SCTPArea> nonOverlapPAreas = taxonomy.getPAreasWithNoOverlap();
        
        for(SCTPArea parea : nonOverlapPAreas) {
            pareaConcepts.put(parea.getRoot().getId(), 
                    taxonomy.getSourcePAreaTaxonomy().getDataSource().getConceptsInPArea(
                        taxonomy.getSourcePAreaTaxonomy(), 
                        parea));
        }

        ExportAbN.exportAbNGroups(pareaConcepts, "DISJOINTPAREA");
    }

}
