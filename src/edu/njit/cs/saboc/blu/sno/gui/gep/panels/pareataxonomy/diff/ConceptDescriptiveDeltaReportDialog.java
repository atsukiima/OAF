package edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy.diff;

import edu.njit.cs.saboc.blu.core.datastructure.hierarchy.Hierarchy;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.label.DetailsPanelLabel;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.diffpareataxonomy.SCTDescriptiveDiffPAreaTaxonomy;
import edu.njit.cs.saboc.blu.sno.descriptivedelta.DescriptiveDelta;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy.diff.configuration.SCTDiffPAreaTaxonomyConfiguration;
import edu.njit.cs.saboc.blu.sno.localdatasource.concept.SCTConcept;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

/**
 *
 * @author Chris O
 */
public class ConceptDescriptiveDeltaReportDialog extends JPanel {
    
    private final DetailsPanelLabel titleLabel;
    
    private final ConceptDescriptiveDeltaReportPanel reportPanel;
    
    private final DescriptiveDelta sourceDelta;
    
    private final DescriptiveDeltaConceptList ancestorChangeList;
    
    public ConceptDescriptiveDeltaReportDialog(SCTDiffPAreaTaxonomyConfiguration configuration) {
        this.sourceDelta = ((SCTDescriptiveDiffPAreaTaxonomy)configuration.getPAreaTaxonomy()).getDescriptiveDelta();
        
        this.setLayout(new BorderLayout());
        
        this.titleLabel = new DetailsPanelLabel(" ");
        
        this.add(titleLabel, BorderLayout.NORTH);
        
        this.reportPanel = new ConceptDescriptiveDeltaReportPanel(sourceDelta);
        this.reportPanel.setBorder(BorderFactory.createTitledBorder("Changes to Selected Concept"));
        this.reportPanel.setPreferredSize(new Dimension(500, -1));
        
        this.ancestorChangeList = new DescriptiveDeltaConceptList(configuration);
        this.ancestorChangeList.setBorder(BorderFactory.createTitledBorder("Changes to Selected Concept's Ancestors"));
        
        JPanel contentPanel = new JPanel(new BorderLayout());
        
        contentPanel.add(reportPanel, BorderLayout.WEST);
        contentPanel.add(ancestorChangeList, BorderLayout.CENTER);
        
        this.add(contentPanel, BorderLayout.CENTER);
    }
    
    public void setContent(SCTConcept concept) {
        titleLabel.setText(concept.getName());
        
        this.reportPanel.setContents(concept);
        
        Hierarchy<SCTConcept> subhierarchy = sourceDelta.getToRelease().getConceptHierarchy().getSubhierarchyRootedAt(sourceDelta.getSubhierarchyRoot());
        
        Hierarchy<SCTConcept> conceptAncestorHierarchy = subhierarchy.getAncestorHierarchy(concept);

        ArrayList<SCTConcept> ancestorsInSubhierarchy = conceptAncestorHierarchy.getTopologicalOrdering();
        ancestorsInSubhierarchy.remove(concept);

        this.ancestorChangeList.setContents(ancestorsInSubhierarchy);
    }
}
