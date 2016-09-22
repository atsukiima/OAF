package edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy.diff;

import edu.njit.cs.saboc.blu.sno.descriptivedelta.DescriptiveDelta;
import edu.njit.cs.saboc.blu.sno.descriptivedelta.EditingOperationReport;
import edu.njit.cs.saboc.blu.sno.descriptivedelta.EditingOperationReport.EditingOperationType;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Optional;
import javax.swing.BorderFactory;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.Scrollable;
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
 
            this.descriptionPane.setText(String.format("<html><table><tr><td>%s</td></tr></table>", text));
            
            
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
        }
    }
    
    /**
     * A JPanel that will maintain its width within a JSCrollPane
     */
    private class EntryContentPanel extends JPanel implements Scrollable {
        
        public EntryContentPanel() {
            super(new BorderLayout());
        }

        @Override
        public Dimension getPreferredScrollableViewportSize() {
            return getPreferredSize();
        }

        @Override
        public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
            return 10;
        }

        @Override
        public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
            return 50;
        }

        @Override
        public boolean getScrollableTracksViewportWidth() {
            return true;
        }

        @Override
        public boolean getScrollableTracksViewportHeight() {
            Container parent = getParent();

            if (parent instanceof JViewport) {
                JViewport viewport = (JViewport)parent;
                
                return viewport.getHeight() > getPreferredSize().height;
            }

            return false;
        }
    }
    
    private final EntryContentPanel contentPanel;
    
    private final JScrollPane scroller;
    
    private final DescriptiveDelta descriptiveDelta;
    
    private final ArrayList<DescriptiveDeltaEntry> entries = new ArrayList<>();
    
    private Optional<EditingOperationReport> currentEditingOperations = Optional.empty();
    
    public DescriptiveDeltaReportPanel(DescriptiveDelta delta) {
        this.descriptiveDelta = delta;
        
        this.setLayout(new BorderLayout());
        
        contentPanel = new EntryContentPanel();
        
        contentPanel.setBackground(Color.WHITE);
        
        scroller = new JScrollPane(contentPanel);
                
        this.add(scroller, BorderLayout.CENTER);
    }
    
    public void setCurrentEditingOperationReport(EditingOperationReport report) {
        this.clearContent();
        
        this.currentEditingOperations = Optional.of(report);
        
        createEntriesFor(report);
        
        SwingUtilities.invokeLater( () -> {
            scroller.getVerticalScrollBar().setValue(0);
        });
    }
    
    private void createEntriesFor(EditingOperationReport report) {
        
        if(!report.getAddedParents().isEmpty()) {
            report.getAddedParents().forEach( (addedParent) -> {
                addEntry(
                        new DescriptiveDeltaEntry(
                                EditingOperationType.AddedParent, 
                                DescriptiveDeltaGUIUtils.getParentAddedText(descriptiveDelta, addedParent)
                        )
                );
            });
        }
        
        if (!report.getRemovedParents().isEmpty()) {
            report.getRemovedParents().forEach((removedParent) -> {
                addEntry(
                        new DescriptiveDeltaEntry(
                                EditingOperationType.RemovedParent, 
                                DescriptiveDeltaGUIUtils.getParentRemovedText(descriptiveDelta, removedParent)
                        )
                );
            });
        }
        
        if(!report.getChangedParents().isEmpty()) {
            report.getChangedParents().forEach( (changedParent) -> {
                addEntry(
                        new DescriptiveDeltaEntry(
                                EditingOperationType.ChangedParent, 
                                DescriptiveDeltaGUIUtils.getParentChangedText(descriptiveDelta, changedParent)
                        )
                );
            });
        }
        
        if(!report.getLessRefinedParents().isEmpty()) {
            report.getLessRefinedParents().forEach( (lessRefinedParent) -> {
                addEntry(
                        new DescriptiveDeltaEntry(
                        EditingOperationType.ParentLessRefined, 
                        DescriptiveDeltaGUIUtils.getParentLessRefinedText(descriptiveDelta, lessRefinedParent)));
            });
        }
        
        if(!report.getMoreRefinedParents().isEmpty()) {
            report.getMoreRefinedParents().forEach( (moreRefinedParent) -> {
                addEntry(
                        new DescriptiveDeltaEntry(EditingOperationType.ParentMoreRefined, 
                            DescriptiveDeltaGUIUtils.getParentMoreRefinedText(descriptiveDelta, moreRefinedParent)));
            });
        }
        
        if(!report.getAddedRelationships().isEmpty()) {
            report.getAddedRelationships().forEach( (addedRel) -> {
                addEntry(
                        new DescriptiveDeltaEntry(
                                EditingOperationType.AddedAttributeRelationship, 
                                DescriptiveDeltaGUIUtils.getAttributeRelAddedText(addedRel)));
            });
        }
        
        if(!report.getRemovedRelationships().isEmpty()) {
            report.getRemovedRelationships().forEach( (removedRel) -> {
                addEntry(new DescriptiveDeltaEntry(EditingOperationType.RemovedAttributeRelationship, 
                        DescriptiveDeltaGUIUtils.getAttributeRelRemovedText(removedRel)));
            });
        }
        
        if(!report.getChangedRelationships().isEmpty()) {
            report.getChangedRelationships().forEach((changedRel) -> {
                addEntry(new DescriptiveDeltaEntry(EditingOperationType.ChangedAttributeRelationship, 
                        DescriptiveDeltaGUIUtils.getAttributeRelTargetChangedText(changedRel)));
            });
        }
        
        
        
        
        if(!report.getLessRefinedRelationships().isEmpty()) {
            report.getLessRefinedRelationships().forEach((lessRefinedRel) -> {
                addEntry(new DescriptiveDeltaEntry(EditingOperationType.AttributeRelationshipLessRefined,
                        DescriptiveDeltaGUIUtils.getAttributeRelLessRefinedText(lessRefinedRel)));
            });
        }
        
        if (!report.getMoreRefinedRelationships().isEmpty()) {
            report.getMoreRefinedRelationships().forEach((moreRefinedRel) -> {
                addEntry(new DescriptiveDeltaEntry(EditingOperationType.AttributeRelationshipMoreRefined,
                        DescriptiveDeltaGUIUtils.getAttributeRelMoreRefinedText(moreRefinedRel)));
            });
        }
        
        if (!report.getRelGroupChangedRelationships().isEmpty()) {
            report.getRelGroupChangedRelationships().forEach((relGroupChange) -> {
                addEntry(new DescriptiveDeltaEntry(EditingOperationType.RelationshipGroupChanged,
                        DescriptiveDeltaGUIUtils.getGroupChangeText(relGroupChange)));
            });
        }
        
        contentPanel.validate();
        contentPanel.repaint();
    }
    
    private void addEntry(DescriptiveDeltaEntry entry) {
        JPanel internalPanel = new JPanel(new BorderLayout());
        
        if(contentPanel.getComponentCount() > 0) {
            internalPanel.add(contentPanel.getComponent(0), BorderLayout.NORTH);
        }
        
        
        internalPanel.add(entry, BorderLayout.CENTER);
        
        entries.add(entry);

        contentPanel.add(internalPanel, BorderLayout.CENTER);
    }
    
    
    public void clearContent() {
        currentEditingOperations = Optional.empty();
        
        contentPanel.removeAll();
        
        entries.clear();
    }

    
}
