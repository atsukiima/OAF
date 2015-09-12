package edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy.reports;

import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTArea;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTPAreaTaxonomy;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.SCTPAreaTaxonomyConfiguration;
import java.util.ArrayList;
import javax.swing.JDialog;
import javax.swing.JTabbedPane;

/**
 *
 * @author Chris O
 */
public class SCTPAreaTaxonomyReportDialog extends JDialog {
    
    private final SCTPAreaTaxonomyLevelReportPanel levelReportPanel;
    
    private final SCTPAreaTaxonomyAreaReportPanel areaReportPanel;
    
    private final SCTPAreaTaxonomyOverlappingConceptReportPanel overlappingClassPanel;
    
    private final JTabbedPane tabbedPane;
    
    public SCTPAreaTaxonomyReportDialog(SCTPAreaTaxonomyConfiguration config) {
        
        levelReportPanel = new SCTPAreaTaxonomyLevelReportPanel(config);
        areaReportPanel = new SCTPAreaTaxonomyAreaReportPanel(config);
        overlappingClassPanel = new SCTPAreaTaxonomyOverlappingConceptReportPanel(config);
        
        tabbedPane = new JTabbedPane();
        
        tabbedPane.addTab("Partial-area Taxonomy Levels", levelReportPanel);
        tabbedPane.addTab("Areas in Partial-area Taxonomy", areaReportPanel);
        tabbedPane.addTab("Overlapping Concepts", overlappingClassPanel);
        
        this.add(tabbedPane);
        
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        
        this.setTitle("SNOMED CT Partial-area Taxonomy Reports");
        
        this.setSize(600, 600);
    }
    
    public void showReports(SCTPAreaTaxonomy taxonomy) {
        ArrayList<SCTArea> areas = taxonomy.getContainers();
        
        boolean hasOverlapping = false;
        
        for(SCTArea area : areas) {
            if(area.hasOverlappingConcepts()) {
                hasOverlapping = true;
                break;
            }
        }
        
        if(hasOverlapping) {
            overlappingClassPanel.displayAbNReport(taxonomy);
        } else {
            tabbedPane.setEnabledAt(2, false);
        }
        
        levelReportPanel.displayAbNReport(taxonomy);
        areaReportPanel.displayAbNReport(taxonomy);
    }
}
