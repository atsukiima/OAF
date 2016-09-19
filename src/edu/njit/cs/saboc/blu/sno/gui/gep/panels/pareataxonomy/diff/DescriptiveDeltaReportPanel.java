package edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy.diff;

import edu.njit.cs.saboc.blu.sno.descriptivedelta.DescriptiveDelta;
import edu.njit.cs.saboc.blu.sno.descriptivedelta.EditingOperationReport;
import edu.njit.cs.saboc.blu.sno.descriptivedelta.EditingOperationReport.EditingOperationType;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.util.Optional;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author Chris O
 */
public class DescriptiveDeltaReportPanel extends JPanel {
    
    private class DescriptiveDeltaEntry extends JPanel {
        
        private final JLabel iconLabel;
        
        private final JEditorPane descriptionPane;
        
        public DescriptiveDeltaEntry(EditingOperationType type, String text) {
            this.setLayout(new BorderLayout());
            this.setBorder(new EmptyBorder(4, 4, 4, 4));
            
            this.setBackground(Color.WHITE);
            
            this.iconLabel = new JLabel(DescriptiveDeltaGUIUtils.getIconForEditingOperation(type));
            
            JPanel descriptionPanel = new JPanel(new BorderLayout());
            
            this.descriptionPane = new JEditorPane();
            this.descriptionPane.setEditable(false);
            this.descriptionPane.setContentType("text/html");
 

            this.descriptionPane.setText("<html>" + text);
            
            JLabel typeLabel = new JLabel(type.toString());
            typeLabel.setOpaque(true);
            typeLabel.setBackground(DescriptiveDeltaGUIUtils.getOperationColor(type));
            
            descriptionPanel.add(typeLabel, BorderLayout.NORTH);
            descriptionPanel.add(descriptionPane, BorderLayout.CENTER);
            
            JPanel contentPanel = new JPanel(new BorderLayout());
            contentPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            contentPanel.setOpaque(false);
            
            contentPanel.add(iconLabel, BorderLayout.WEST);
            contentPanel.add(descriptionPanel, BorderLayout.CENTER);
            
            this.add(contentPanel, BorderLayout.CENTER);
            
            this.setMinimumSize(new Dimension(Integer.MAX_VALUE, 100));
            this.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        }
    }
    
    
    private final JPanel contentPanel;
    
    private final JScrollPane scroller;
    
    private final DescriptiveDelta descriptiveDelta;
    
    private Optional<EditingOperationReport> currentEditingOperations = Optional.empty();
    
    public DescriptiveDeltaReportPanel(DescriptiveDelta delta) {
        this.descriptiveDelta = delta;
        
        this.setLayout(new BorderLayout());
        
        contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(Color.WHITE);
        
        scroller = new JScrollPane(contentPanel);
        
        
        this.add(scroller, BorderLayout.CENTER);
    }
    
    public void setCurrentEditingOperationReport(EditingOperationReport report) {
        this.clearEntries();
        
        this.currentEditingOperations = Optional.of(report);
        
        createEntriesFor(report);
        
        SwingUtilities.invokeLater( () -> {
            scroller.getVerticalScrollBar().setValue(0);
        });
    }
    
    private void createEntriesFor(EditingOperationReport report) {
        
        if(!report.getAddedParents().isEmpty()) {
            report.getAddedParents().forEach( (addedParent) -> {
                contentPanel.add(
                        new DescriptiveDeltaEntry(
                                EditingOperationType.AddedParent, 
                                DescriptiveDeltaGUIUtils.getParentAddedText(descriptiveDelta, addedParent)
                        )
                );
            });
        }
        
        if (!report.getRemovedParents().isEmpty()) {
            report.getRemovedParents().forEach((removedParent) -> {
                contentPanel.add(
                        new DescriptiveDeltaEntry(
                                EditingOperationType.RemovedParent, 
                                DescriptiveDeltaGUIUtils.getParentRemovedText(descriptiveDelta, removedParent)
                        )
                );
            });
        }
        
        if(!report.getChangedParents().isEmpty()) {
            report.getChangedParents().forEach( (changedParent) -> {
                contentPanel.add(
                        new DescriptiveDeltaEntry(
                                EditingOperationType.ChangedParent, 
                                DescriptiveDeltaGUIUtils.getParentChangedText(descriptiveDelta, changedParent)
                        )
                );
            });
        }
        
        if(!report.getLessRefinedParents().isEmpty()) {
            report.getLessRefinedParents().forEach( (lessRefinedParent) -> {
                contentPanel.add(new DescriptiveDeltaEntry(EditingOperationType.ParentLessRefined, "Less refined parent"));
            });
        }
        
        if(!report.getMoreRefinedParents().isEmpty()) {
            report.getMoreRefinedParents().forEach( (moreRefinedParent) -> {
                contentPanel.add(new DescriptiveDeltaEntry(EditingOperationType.ParentMoreRefined, "More refined parent"));
            });
        }
        
        if(!report.getAddedRelationships().isEmpty()) {
            report.getAddedRelationships().forEach( (addedRel) -> {
                contentPanel.add(new DescriptiveDeltaEntry(EditingOperationType.AddedAttributeRelationship, "Added attribute relationship"));
            });
        }
        
        if(!report.getRemovedRelationships().isEmpty()) {
            report.getRemovedRelationships().forEach( (removedRel) -> {
                contentPanel.add(new DescriptiveDeltaEntry(EditingOperationType.RemovedAttributeRelationship, "Removed attribute relationship"));
            });
        }
        
        if(!report.getChangedRelationships().isEmpty()) {
            report.getChangedRelationships().forEach((changedRel) -> {
                contentPanel.add(new DescriptiveDeltaEntry(EditingOperationType.ChangedAttributeRelationship, "Changed attribute relationship target"));
            });
        }
        
        if(!report.getLessRefinedRelationships().isEmpty()) {
            report.getLessRefinedRelationships().forEach((lessRefinedRel) -> {
                contentPanel.add(new DescriptiveDeltaEntry(EditingOperationType.AttributeRelationshipLessRefined,
                        "Attribute relationship target less refined"));
            });
        }
        
        if (!report.getMoreRefinedRelationships().isEmpty()) {
            report.getMoreRefinedRelationships().forEach((moreRefinedRel) -> {
                contentPanel.add(new DescriptiveDeltaEntry(EditingOperationType.AttributeRelationshipMoreRefined,
                        "Attribute relationship target more refined"));
            });
        }
        
        if (!report.getRelGroupChangedRelationships().isEmpty()) {
            report.getRelGroupChangedRelationships().forEach((relGroupChange) -> {
                contentPanel.add(new DescriptiveDeltaEntry(EditingOperationType.RelationshipGroupChanged,
                        DescriptiveDeltaGUIUtils.getGroupChangeText(relGroupChange)));
            });
        }
        
        contentPanel.validate();
        contentPanel.repaint();
    }
    
    public void clearEntries() {
        currentEditingOperations = Optional.empty();
        
        contentPanel.removeAll();
    }

    
}
