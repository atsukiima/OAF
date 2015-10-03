package edu.njit.cs.saboc.blu.sno.gui.gep.panels.tan.reports;

import SnomedShared.overlapping.CommonOverlapSet;
import edu.njit.cs.saboc.blu.sno.abn.tan.local.SCTTribalAbstractionNetwork;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.tan.configuration.SCTTANConfiguration;
import java.util.ArrayList;
import javax.swing.JDialog;
import javax.swing.JTabbedPane;

/**
 *
 * @author Chris O
 */
public class SCTTANReportDialog extends JDialog {
        
    private final SCTTANOverlappingConceptReportPanel overlappingClassPanel;
    
    private final JTabbedPane tabbedPane;
    
    private final SCTTANConfiguration config;
    
    public SCTTANReportDialog(SCTTANConfiguration config) {
        this.config = config;
        
        overlappingClassPanel = new SCTTANOverlappingConceptReportPanel(config);
        
        tabbedPane = new JTabbedPane();
        
        tabbedPane.addTab("Overlapping Concepts", overlappingClassPanel);
        
        this.add(tabbedPane);
        
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        
        this.setTitle("SNOMED CT Tribal Abstraction Network Reports");
        
        this.setSize(600, 600);
    }
    
    public void showReports(SCTTribalAbstractionNetwork tan) {
        ArrayList<CommonOverlapSet> bands = tan.getContainers();
        
        boolean hasOverlapping = false;
        
        for(CommonOverlapSet band : bands) {
            if(!config.getDataConfiguration().getContainerOverlappingConcepts(band).isEmpty()) {
                hasOverlapping = true;
                break;
            }
        }
        
        if(hasOverlapping) {
            overlappingClassPanel.displayAbNReport(tan);
        } else {
            tabbedPane.setEnabledAt(0, false);
        }
    }
}
