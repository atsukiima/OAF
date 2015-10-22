package edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy.reports;

import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTArea;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTPAreaTaxonomy;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy.configuration.SCTPAreaTaxonomyConfiguration;
import java.util.ArrayList;
import javax.swing.JDialog;
import javax.swing.JTabbedPane;

/**
 *
 * @author Chris O
 */
public class SCTPAreaTaxonomyReportDialog extends JDialog {

    private final SCTPAreaTaxonomyOverlappingConceptReportPanel overlappingConceptPanel;
    
    private final SCTPAreaTaxonomyImportConceptReport importedConceptPanel;
    
    private final JTabbedPane tabbedPane;
    
    private final SCTPAreaTaxonomyConfiguration config;
    
    public SCTPAreaTaxonomyReportDialog(SCTPAreaTaxonomyConfiguration config) {
        
        this.config = config;

        overlappingConceptPanel = new SCTPAreaTaxonomyOverlappingConceptReportPanel(config);
        
        importedConceptPanel = new SCTPAreaTaxonomyImportConceptReport(config);
        
        tabbedPane = new JTabbedPane();
;
        tabbedPane.addTab("Overlapping Concepts", overlappingConceptPanel);
        tabbedPane.addTab("Imported Concepts Report", importedConceptPanel);
        
        this.add(tabbedPane);
        
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        
        this.setTitle("SNOMED CT Partial-area Taxonomy Reports");
        
        this.setSize(600, 600);
    }
    
    public void showReports(SCTPAreaTaxonomy taxonomy) {
        ArrayList<SCTArea> areas = taxonomy.getContainers();
        
        boolean hasOverlapping = false;
        
        for(SCTArea area : areas) {
            if(!config.getDataConfiguration().getContainerOverlappingResults(area).isEmpty()) {
                hasOverlapping = true;
                break;
            }
        }
        
        if(hasOverlapping) {
            overlappingConceptPanel.displayAbNReport(taxonomy);
        } else {
            tabbedPane.setEnabledAt(0, false);
        }
    }
}