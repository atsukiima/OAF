package edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy.diff;

import edu.njit.cs.saboc.blu.sno.descriptivedelta.DescriptiveDelta;
import edu.njit.cs.saboc.blu.sno.descriptivedelta.EditingOperationReport;
import edu.njit.cs.saboc.blu.sno.localdatasource.concept.SCTConcept;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

/**
 *
 * @author Chris O
 */
public class ConceptDescriptiveDeltaReportPanel extends JPanel {
    
    private final DescriptiveDeltaReportPanel statedReportPanel;
    private final DescriptiveDeltaReportPanel inferredReportPanel;
    
    private final DescriptiveDelta sourceDescriptiveDelta;
    
    private final JTabbedPane editReportTabs;
    
    public ConceptDescriptiveDeltaReportPanel(DescriptiveDelta sourceDescriptiveDelta) {
        this.setLayout(new BorderLayout());
        
        this.sourceDescriptiveDelta = sourceDescriptiveDelta;
        
        this.statedReportPanel = new DescriptiveDeltaReportPanel(sourceDescriptiveDelta); 
        this.inferredReportPanel = new DescriptiveDeltaReportPanel(sourceDescriptiveDelta);
        
        this.editReportTabs = new JTabbedPane();
        
        this.editReportTabs.addTab("Stated Editing Operations", statedReportPanel);
        this.editReportTabs.addTab("Inferred Relationship Changes", inferredReportPanel);
        
        this.add(editReportTabs, BorderLayout.CENTER);
    }
    
    public void setContents(SCTConcept concept) {
        if (sourceDescriptiveDelta.getStatedEditingOperations().containsKey(concept)) {
            statedReportPanel.setCurrentEditingOperationReport(
                    sourceDescriptiveDelta.getStatedEditingOperations().get(concept));

            editReportTabs.setEnabledAt(0, true);
        } else {
            editReportTabs.setEnabledAt(0, false);
            statedReportPanel.setCurrentEditingOperationReport(new EditingOperationReport(concept));
        }

        if (sourceDescriptiveDelta.getInferredChanges().containsKey(concept)) {
            inferredReportPanel.setCurrentEditingOperationReport(
                    sourceDescriptiveDelta.getInferredChanges().get(concept));

            editReportTabs.setEnabledAt(1, true);
        } else {
            editReportTabs.setEnabledAt(1, false);
            inferredReportPanel.setCurrentEditingOperationReport(new EditingOperationReport(concept));
        }
    }
    
    public void clearContents() {
        statedReportPanel.setCurrentEditingOperationReport(new EditingOperationReport(null));
        inferredReportPanel.setCurrentEditingOperationReport(new EditingOperationReport(null));
        
        editReportTabs.setEnabledAt(0, false);
        editReportTabs.setEnabledAt(1, false);
    }
}
