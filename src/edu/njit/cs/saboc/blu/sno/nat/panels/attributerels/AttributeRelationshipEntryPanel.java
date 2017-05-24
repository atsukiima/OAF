package edu.njit.cs.saboc.blu.sno.nat.panels.attributerels;

import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.InheritableProperty;
import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.InheritableProperty.InheritanceType;
import edu.njit.cs.saboc.blu.core.utils.filterable.list.Filterable;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.SCTInheritableProperty;
import edu.njit.cs.saboc.blu.sno.localdatasource.concept.AttributeRelationship;
import edu.njit.cs.saboc.blu.sno.localdatasource.concept.SCTConcept;
import edu.njit.cs.saboc.blu.sno.nat.SCTConceptBrowserDataSource;
import edu.njit.cs.saboc.nat.generic.NATBrowserPanel;
import edu.njit.cs.saboc.nat.generic.errorreport.AuditSet;
import edu.njit.cs.saboc.nat.generic.errorreport.error.semanticrel.IncorrectSemanticRelationshipError;
import edu.njit.cs.saboc.nat.generic.gui.filterable.nestedlist.FilterableEntryPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.List;
import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author Chris O
 */
public class AttributeRelationshipEntryPanel extends FilterableEntryPanel<FilterableAttributeRelationshipEntry> {

    private final JLabel attributeRelTypeLabel;
    private final JLabel targetConceptNameLabel;
    private final JLabel targetConceptIdLabel;
    private final JLabel errorDetailsLabel;

    public AttributeRelationshipEntryPanel(
            NATBrowserPanel<SCTConcept> mainPanel,
            FilterableAttributeRelationshipEntry relEntry) {
        
        super(relEntry, relEntry.getCurrentFilter());

        this.setOpaque(false);

        this.setLayout(new BorderLayout());

        this.attributeRelTypeLabel = new JLabel();
        this.targetConceptNameLabel = new JLabel();
        this.targetConceptIdLabel = new JLabel();
        this.errorDetailsLabel = new JLabel();

        this.attributeRelTypeLabel.setFont(this.attributeRelTypeLabel.getFont().deriveFont(Font.BOLD, 16));
        this.targetConceptNameLabel.setFont(this.targetConceptNameLabel.getFont().deriveFont(Font.PLAIN, 16));
        this.targetConceptIdLabel.setFont(this.targetConceptIdLabel.getFont().deriveFont(Font.PLAIN, 10));
        this.errorDetailsLabel.setFont(this.errorDetailsLabel.getFont().deriveFont(Font.BOLD, 10));

        this.targetConceptIdLabel.setForeground(Color.BLUE);
        this.errorDetailsLabel.setForeground(Color.RED);

        this.targetConceptNameLabel.setOpaque(false);
        this.targetConceptIdLabel.setOpaque(false);
        this.errorDetailsLabel.setOpaque(false);

        JPanel relNamePanel = new JPanel();
        relNamePanel.setOpaque(false);

        JLabel relStartLabel = new JLabel(" == ");
        relStartLabel.setFont(relStartLabel.getFont().deriveFont(Font.BOLD, 16));

        JLabel relEndLabel = new JLabel(" ==> ");
        relEndLabel.setFont(relEndLabel.getFont().deriveFont(Font.BOLD, 16));

        relStartLabel.setOpaque(false);
        relEndLabel.setOpaque(false);

        relStartLabel.setForeground(Color.RED);
        relEndLabel.setForeground(Color.RED);

        relNamePanel.add(relStartLabel);
        relNamePanel.add(attributeRelTypeLabel);
        relNamePanel.add(relEndLabel);

        JPanel targetPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        targetPanel.add(targetConceptNameLabel);
        targetPanel.add(Box.createHorizontalStrut(8));
        targetPanel.add(targetConceptIdLabel);

        targetPanel.setOpaque(false);

        JPanel entryPanel = new JPanel();
        entryPanel.setOpaque(false);

        entryPanel.add(relNamePanel);
        entryPanel.add(targetPanel);
        entryPanel.add(Box.createHorizontalStrut(10));
        entryPanel.add(errorDetailsLabel);

        this.add(entryPanel, BorderLayout.WEST);
        
        AttributeRelationship rel = relEntry.getObject();

        String relNameStr = rel.getType().getName();
        String conceptNameStr = rel.getTarget().getName();
        
        if(relEntry.getCurrentFilter().isPresent()) {
            relNameStr = Filterable.filter(relNameStr, relEntry.getCurrentFilter().get());
            conceptNameStr = Filterable.filter(conceptNameStr, relEntry.getCurrentFilter().get());
        }
        
        String conceptIdStr = rel.getTarget().getIDAsString();

        this.attributeRelTypeLabel.setText(relNameStr);
        this.targetConceptNameLabel.setText(conceptNameStr);
        this.targetConceptIdLabel.setText(conceptIdStr);
        
        this.errorDetailsLabel.setText("");
        
        if (mainPanel.getAuditDatabase().getLoadedAuditSet().isPresent()) {
            AuditSet<SCTConcept> auditSet = mainPanel.getAuditDatabase().getLoadedAuditSet().get();

            List<IncorrectSemanticRelationshipError<SCTConcept, InheritableProperty>> errors
                    = auditSet.getRelatedSemanticRelationshipErrors(
                            mainPanel.getFocusConceptManager().getActiveFocusConcept(),
                            new SCTInheritableProperty(rel.getType(), InheritanceType.Introduced),
                            rel.getTarget());
            
            if (!errors.isEmpty()) {
                if (errors.size() == 1) {
                    errorDetailsLabel.setText("Error");
                } else {
                    errorDetailsLabel.setText(String.format("Errors (%d)", errors.size()));
                }
            }
        }
        
        this.setPreferredSize(new Dimension(-1, 40));
    }
}
