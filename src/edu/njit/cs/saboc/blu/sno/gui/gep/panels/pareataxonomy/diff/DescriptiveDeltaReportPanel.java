package edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy.diff;

import edu.njit.cs.saboc.blu.sno.descriptivedelta.DescriptiveDelta;
import edu.njit.cs.saboc.blu.sno.descriptivedelta.EditingOperationReport;
import edu.njit.cs.saboc.blu.sno.descriptivedelta.EditingOperationReport.EditingOperationType;
import java.awt.BorderLayout;
import java.util.Optional;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

/**
 *
 * @author Chris O
 */
public class DescriptiveDeltaReportPanel extends JPanel {
    
    private class DescriptiveDeltaEntry extends JPanel {
        
        private final JLabel iconLabel;
        
        private final JTextArea descriptionText;
        
        public DescriptiveDeltaEntry(EditingOperationType type, String text) {
            this.setLayout(new BorderLayout());
            
            this.iconLabel = new JLabel(DescriptiveDeltaGUIUtils.getIconForEditingOperation(type));
            
            this.add(iconLabel, BorderLayout.WEST);
            
            this.descriptionText = new JTextArea();
            this.descriptionText.setText("<html>" + text);
            
            this.add(descriptionText, BorderLayout.CENTER);
        }
    }
    
    
    private final JPanel contentPanel;
    
    private final DescriptiveDelta descriptiveDelta;
    
    private Optional<EditingOperationReport> currentEditingOperations = Optional.empty();
    
    public DescriptiveDeltaReportPanel(DescriptiveDelta delta) {
        this.descriptiveDelta = delta;
        
        this.setLayout(new BorderLayout());
        
        contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        
        
        this.add(contentPanel, BorderLayout.CENTER);
    }
    
    public void setCurrentEditingOperationReport(EditingOperationReport report) {
        this.clearEntries();
        
        this.currentEditingOperations = Optional.of(report);
        
        createEntriesFor(report);
    }
    
    private void createEntriesFor(EditingOperationReport report) {
        
        if(!report.getAddedParents().isEmpty()) {
            report.getAddedParents().forEach( (addedParent) -> {
                contentPanel.add(new DescriptiveDeltaEntry(EditingOperationType.AddedParent, "Added parent"));
            });
        }
        
        if (!report.getRemovedParents().isEmpty()) {
            report.getRemovedParents().forEach((removedParent) -> {
                contentPanel.add(new DescriptiveDeltaEntry(EditingOperationType.RemovedParent, "Removed parent"));
            });
        }
        
        if(!report.getChangedParents().isEmpty()) {
            
        }
        
        if(!report.getLessRefinedParents().isEmpty()) {
            
        }
        
        if(!report.getMoreRefinedParents().isEmpty()) {
            
        }
        
        if(!report.getAddedRelationships().isEmpty()) {
            
        }
        
        if(!report.getRemovedRelationships().isEmpty()) {
            
        }
        
        if(!report.getChangedRelationships().isEmpty()) {
            
        }
        
        if(!report.getLessRefinedRelationships().isEmpty()) {
            
        }
        
        if(!report.getMoreRefinedRelationships().isEmpty()) {
            
        }
        
        if(!report.getRelGroupChangedRelationships().isEmpty()) {
            
        }
        
        contentPanel.validate();
        contentPanel.repaint();
    }
   
    
    public void clearEntries() {
        currentEditingOperations = Optional.empty();
        
        contentPanel.removeAll();
    }
    
    
    
    

}
