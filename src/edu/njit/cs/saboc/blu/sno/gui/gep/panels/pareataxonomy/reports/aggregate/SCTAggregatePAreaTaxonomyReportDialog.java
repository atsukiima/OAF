package edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy.reports.aggregate;

import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTPAreaTaxonomy;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy.configuration.SCTPAreaTaxonomyConfiguration;
import javax.swing.JDialog;
import javax.swing.JTabbedPane;

/**
 *
 * @author Chris O
 */
public class SCTAggregatePAreaTaxonomyReportDialog extends JDialog {
    

    
    private final JTabbedPane tabbedPane;
    
    public SCTAggregatePAreaTaxonomyReportDialog(SCTPAreaTaxonomyConfiguration config) {
        
        tabbedPane = new JTabbedPane();
        
        this.add(tabbedPane);
        
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        
        this.setTitle("SNOMED CT Aggregate Partial-area Taxonomy Reports");
        
        this.setSize(600, 600);
    }
    
    public void showReports(SCTPAreaTaxonomy taxonomy) {        

    }
}
