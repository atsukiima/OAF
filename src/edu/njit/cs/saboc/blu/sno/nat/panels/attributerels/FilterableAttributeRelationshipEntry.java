
package edu.njit.cs.saboc.blu.sno.nat.panels.attributerels;

import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.InheritableProperty;
import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.InheritableProperty.InheritanceType;
import edu.njit.cs.saboc.blu.core.utils.filterable.list.Filterable;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.SCTInheritableProperty;
import edu.njit.cs.saboc.blu.sno.localdatasource.concept.AttributeRelationship;
import edu.njit.cs.saboc.blu.sno.localdatasource.concept.SCTConcept;
import edu.njit.cs.saboc.nat.generic.NATBrowserPanel;
import edu.njit.cs.saboc.nat.generic.errorreport.AuditSet;
import edu.njit.cs.saboc.nat.generic.errorreport.error.semanticrel.IncorrectSemanticRelationshipError;
import edu.njit.cs.saboc.nat.generic.errorreport.error.semanticrel.SemanticRelationshipError;
import java.util.List;

/**
 *
 * @author Chris O
 */
public class FilterableAttributeRelationshipEntry extends Filterable<AttributeRelationship> {
    
    private final AttributeRelationship rel;
    
    private final NATBrowserPanel<SCTConcept> mainPanel;
    
    public FilterableAttributeRelationshipEntry(
            NATBrowserPanel<SCTConcept> mainPanel,
            AttributeRelationship rel) {
        
        this.mainPanel = mainPanel;
        
        this.rel = rel;
    }

    @Override
    public boolean containsFilter(String filter) {
        return rel.getType().getName().toLowerCase().contains(filter) || 
                rel.getTarget().getName().toLowerCase().contains(filter);
    }

    @Override
    public AttributeRelationship getObject() {
        return rel;
    }

    @Override
    public String getToolTipText() {
        String relName = SemanticRelationshipError.generateStyledRelationshipText(rel.getType().getName(), rel.getTarget().getName());

        String text = String.format("<html><font size = '4'>%s</font>", relName);

        if (mainPanel.getAuditDatabase().getLoadedAuditSet().isPresent()) {
            AuditSet<SCTConcept> auditSet = mainPanel.getAuditDatabase().getLoadedAuditSet().get();
            SCTConcept focusConcept = mainPanel.getFocusConceptManager().getActiveFocusConcept();


            List<IncorrectSemanticRelationshipError<SCTConcept, InheritableProperty>> relatedErrors = auditSet.getRelatedSemanticRelationshipErrors(
                    focusConcept,
                    new SCTInheritableProperty(rel.getType(), InheritanceType.Introduced),
                    rel.getTarget());

            if (!relatedErrors.isEmpty()) {
                text += String.format("<p><p><font size = '4'><b>Reported Errors (%d):</b></font><br>", relatedErrors.size());

                for (IncorrectSemanticRelationshipError<SCTConcept, InheritableProperty> error : relatedErrors) {
                    text += error.getTooltipText();
                    text += "<p>";
                }
            }
        }

        return text;
    }

    @Override
    public String getClipboardText() {
        
        return String.format("%s\t%s\t%s", 
                rel.getType().getName(),
                rel.getTarget().getName(),
                rel.getTarget().getIDAsString());
    }
}
