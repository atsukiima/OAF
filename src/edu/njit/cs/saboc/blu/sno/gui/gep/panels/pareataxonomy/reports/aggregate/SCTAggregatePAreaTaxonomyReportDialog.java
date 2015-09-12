package edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy.reports.aggregate;

import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTPAreaTaxonomy;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.SCTPAreaTaxonomyConfiguration;
import javax.swing.JDialog;
import javax.swing.JTabbedPane;

/**
 *
 * @author Chris O
 */
public class SCTAggregatePAreaTaxonomyReportDialog extends JDialog {
    
    private final SCTAggregatePAreaTaxonomyLevelReportPanel levelReportPanel;
    
    private final SCTAggregatePAreaTaxonomyAreaReportPanel areaReportPanel;
    
    private final JTabbedPane tabbedPane;
    
    public SCTAggregatePAreaTaxonomyReportDialog(SCTPAreaTaxonomyConfiguration config) {
        
        levelReportPanel = new SCTAggregatePAreaTaxonomyLevelReportPanel(config);
        areaReportPanel = new SCTAggregatePAreaTaxonomyAreaReportPanel(config);
        
        tabbedPane = new JTabbedPane();
        
        tabbedPane.addTab("Aggregate Partial-area Taxonomy Levels", levelReportPanel);
        tabbedPane.addTab("Areas in Aggregate Partial-area Taxonomy", areaReportPanel);
        
        this.add(tabbedPane);
        
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        
        this.setTitle("SNOMED CT Aggregate Partial-area Taxonomy Reports");
        
        this.setSize(600, 600);
    }
    
    public void showReports(SCTPAreaTaxonomy taxonomy) {        
        levelReportPanel.displayAbNReport(taxonomy);
        areaReportPanel.displayAbNReport(taxonomy);
    }
}
