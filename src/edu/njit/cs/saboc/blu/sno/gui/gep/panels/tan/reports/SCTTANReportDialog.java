
package edu.njit.cs.saboc.blu.sno.gui.gep.panels.tan.reports;

import edu.njit.cs.saboc.blu.core.abn.tan.Band;
import edu.njit.cs.saboc.blu.core.abn.tan.ClusterTribalAbstractionNetwork;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.reports.AbNConceptLocationReportPanel;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.reports.OverlappingConceptReportPanel;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.reports.models.PartitionedAbNImportReportTableModel;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.reports.SCTConceptLocationDataFactory;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.tan.configuration.SCTTANConfiguration;
import java.util.Set;
import javax.swing.JDialog;
import javax.swing.JTabbedPane;

/**
 *
 * @author Chris O
 */
public class SCTTANReportDialog extends JDialog {
    
    private final OverlappingConceptReportPanel overlappingConceptPanel;
    
    private final AbNConceptLocationReportPanel importConceptsPanel;
    
    private final JTabbedPane tabbedPane;
    
    public SCTTANReportDialog(SCTTANConfiguration config) {

        overlappingConceptPanel = new OverlappingConceptReportPanel(config);
        
        importConceptsPanel = new AbNConceptLocationReportPanel(config, 
                new SCTConceptLocationDataFactory(config.getAbstractionNetwork().getSourceHierarchy().getNodes()), 
                new PartitionedAbNImportReportTableModel(config));

        
        tabbedPane = new JTabbedPane();
        
        tabbedPane.addTab("Overlapping Concepts", overlappingConceptPanel);
        tabbedPane.add("Import Concept List", importConceptsPanel);
        
        this.add(tabbedPane);
        
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        
        this.setTitle("SNOMED CT Tribal Abstraction Network Reports");
        
        this.setSize(600, 600);
    }
    
    public void showReports(ClusterTribalAbstractionNetwork tan) {
        Set<Band> bands = tan.getBands();
        
        boolean hasOverlapping = false;
        
        for(Band band : bands) {
            if(band.hasOverlappingConcepts()) {
                hasOverlapping = true;
                break;
            }
        }
        
        if(hasOverlapping) {
            overlappingConceptPanel.displayAbNReport(tan);
        } else {
            tabbedPane.setEnabledAt(0, false);
        }
    }
}
