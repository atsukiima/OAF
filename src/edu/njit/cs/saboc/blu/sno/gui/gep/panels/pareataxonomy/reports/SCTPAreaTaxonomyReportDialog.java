
package edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy.reports;

import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.Area;
import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.PAreaTaxonomy;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.reports.AbNConceptLocationReportPanel;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.reports.OverlappingConceptReportPanel;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.reports.models.PartitionedAbNImportReportTableModel;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy.configuration.SCTPAreaTaxonomyConfiguration;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.reports.SCTConceptLocationDataFactory;
import java.util.Set;
import javax.swing.JDialog;
import javax.swing.JTabbedPane;

/**
 *
 * @author Chris O
 */
public class SCTPAreaTaxonomyReportDialog extends JDialog {
    
    private final OverlappingConceptReportPanel overlappingConceptPanel;
    
    private final AbNConceptLocationReportPanel importConceptsPanel;
    
    private final JTabbedPane tabbedPane;
    
    public SCTPAreaTaxonomyReportDialog(SCTPAreaTaxonomyConfiguration config) {

        overlappingConceptPanel = new OverlappingConceptReportPanel(config);
        
        importConceptsPanel = new AbNConceptLocationReportPanel(config, 
                new SCTConceptLocationDataFactory(config.getAbstractionNetwork().getSourceHierarchy().getNodes()), 
                new PartitionedAbNImportReportTableModel(config));

        
        tabbedPane = new JTabbedPane();
        
        tabbedPane.addTab("Overlapping Concepts", overlappingConceptPanel);
        tabbedPane.add("Import Concept List", importConceptsPanel);
        
        this.add(tabbedPane);
        
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        
        this.setTitle("SNOMED CT PArea Taxonomy Reports");
        
        this.setSize(600, 600);
    }
    
    public void showReports(PAreaTaxonomy taxonomy) {
        Set<Area> areas = taxonomy.getAreas();
        
        boolean hasOverlapping = false;
        
        for(Area area : areas) {
            if(area.hasOverlappingConcepts()) {
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