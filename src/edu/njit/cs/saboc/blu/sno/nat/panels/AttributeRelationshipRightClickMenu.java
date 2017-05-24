package edu.njit.cs.saboc.blu.sno.nat.panels;

import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.InheritableProperty.InheritanceType;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.SCTInheritableProperty;
import edu.njit.cs.saboc.blu.sno.localdatasource.concept.AttributeRelationship;
import edu.njit.cs.saboc.blu.sno.localdatasource.concept.SCTConcept;
import edu.njit.cs.saboc.nat.generic.NATBrowserPanel;
import edu.njit.cs.saboc.nat.generic.errorreport.AuditSet;
import edu.njit.cs.saboc.nat.generic.errorreport.error.semanticrel.SemanticRelationshipError;
import edu.njit.cs.saboc.nat.generic.gui.panels.errorreporting.errorreport.dialog.ErrorReportDialog;
import edu.njit.cs.saboc.nat.generic.gui.panels.focusconcept.rightclickmenu.AuditReportRightClickMenu;
import java.awt.BorderLayout;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSeparator;

/**
 *
 * @author Chris O
 */
public class AttributeRelationshipRightClickMenu extends AuditReportRightClickMenu<SCTConcept, AttributeRelationship> {
    
    private final NATBrowserPanel<SCTConcept> mainPanel;
    
    public AttributeRelationshipRightClickMenu(NATBrowserPanel<SCTConcept> mainPanel) {
        this.mainPanel = mainPanel;
    }

    @Override
    public ArrayList<JComponent> buildRightClickMenuFor(AttributeRelationship item) {
        
        
        if (mainPanel.getAuditDatabase().getLoadedAuditSet().isPresent()) {

            AuditSet<SCTConcept> auditSet = mainPanel.getAuditDatabase().getLoadedAuditSet().get();
            SCTConcept focusConcept = mainPanel.getFocusConceptManager().getActiveFocusConcept();
            
            ArrayList<JComponent> components = new ArrayList<>();

            JPanel namePanel = new JPanel(new BorderLayout());
            namePanel.add(Box.createHorizontalStrut(8), BorderLayout.WEST);
            namePanel.add(Box.createHorizontalStrut(8), BorderLayout.EAST);

            JLabel nameLabel = new JLabel(
                    SemanticRelationshipError.generateStyledRelationshipText(
                            item.getType().getName(), 
                            item.getTarget().getName()));
                    
            
            nameLabel.setFont(nameLabel.getFont().deriveFont(16.0f));

            namePanel.setBackground(Color.WHITE);
            namePanel.setOpaque(true);

            namePanel.add(nameLabel, BorderLayout.CENTER);

            components.add(namePanel);

            components.add(new JSeparator());
            
            JMenuItem removeRelBtn = new JMenuItem("Remove erroneous attribute relationship");
            removeRelBtn.setFont(removeRelBtn.getFont().deriveFont(14.0f));
            removeRelBtn.addActionListener((ae) -> {
                ErrorReportDialog.displayErroneousSemanticRelationshipDialog(
                        mainPanel,
                        focusConcept,
                        new SCTInheritableProperty(item.getType(), InheritanceType.Introduced), 
                        item.getTarget());
            });
            
            JMenuItem changeTargetBtn = new JMenuItem("Change attribute relationship target");
            changeTargetBtn.setFont(changeTargetBtn.getFont().deriveFont(14.0f));
            changeTargetBtn.addActionListener((ae) -> {
                ErrorReportDialog.displayReplaceTargetDialog(
                        mainPanel, 
                        focusConcept,
                        new SCTInheritableProperty(item.getType(), InheritanceType.Introduced), 
                        item.getTarget());
            });
            
            JMenuItem replaceRelBtn = new JMenuItem("Replace attribute relationship");
            replaceRelBtn.setFont(replaceRelBtn.getFont().deriveFont(14.0f));
            replaceRelBtn.addActionListener((ae) -> {
                ErrorReportDialog.displayReplaceSemanticRelationshipDialog(
                        mainPanel, 
                        focusConcept,
                        new SCTInheritableProperty(item.getType(), InheritanceType.Introduced), 
                        item.getTarget());
            });
            
            JMenuItem otherErrorBtn = new JMenuItem("Other error with attribute relationship");
            otherErrorBtn.setFont(otherErrorBtn.getFont().deriveFont(14.0f));
            otherErrorBtn.addActionListener((ae) -> {
                ErrorReportDialog.displayOtherSemanticRelationshipErrorDialog(
                        mainPanel,
                        focusConcept,
                        new SCTInheritableProperty(item.getType(), InheritanceType.Introduced),
                        item.getTarget());
            });
            
            components.add(removeRelBtn);
            components.add(changeTargetBtn);
            components.add(replaceRelBtn);
            components.add(otherErrorBtn);

            components.add(new JSeparator());
            
            JMenuItem reportMissingRel = new JMenuItem("Report missing attribute relationship");
            reportMissingRel.setFont(reportMissingRel.getFont().deriveFont(14.0f));
            reportMissingRel.addActionListener((ae) -> {
                ErrorReportDialog.displayMissingSemanticRelationshipDialog(mainPanel, focusConcept);
            });
            
            components.add(reportMissingRel);
            
            List<SemanticRelationshipError<SCTConcept>> errors = auditSet.getSemanticRelationshipErrors(focusConcept);
            
            if(!errors.isEmpty()) {
                components.add(new JSeparator());
                
                components.add(super.generateRemoveErrorMenu(auditSet, focusConcept, errors));
            }
            
            return components;

        }

        return new ArrayList<>();
    }

    @Override
    public ArrayList<JComponent> buildEmptyListRightClickMenu() {
        
        if (mainPanel.getAuditDatabase().getLoadedAuditSet().isPresent()) {

            AuditSet<SCTConcept> auditSet = mainPanel.getAuditDatabase().getLoadedAuditSet().get();
            SCTConcept focusConcept = mainPanel.getFocusConceptManager().getActiveFocusConcept();
            
            JMenuItem reportMissingRel = new JMenuItem("Report missing attribute relationship");
            reportMissingRel.setFont(reportMissingRel.getFont().deriveFont(14.0f));
            reportMissingRel.addActionListener((ae) -> {
                ErrorReportDialog.displayMissingSemanticRelationshipDialog(mainPanel, focusConcept);
            });
            
            ArrayList<JComponent> components = new ArrayList<>();
            
            components.add(reportMissingRel);
            
            List<SemanticRelationshipError<SCTConcept>> errors = auditSet.getSemanticRelationshipErrors(focusConcept);
            
            if(!errors.isEmpty()) {
                components.add(new JSeparator());
                
                components.add(super.generateRemoveErrorMenu(auditSet, focusConcept, errors));
            }
            
            return components;
        }
        
        return new ArrayList<>();
    }
}
