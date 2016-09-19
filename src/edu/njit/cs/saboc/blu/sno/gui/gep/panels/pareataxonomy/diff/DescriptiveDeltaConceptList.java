package edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy.diff;

import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.diff.DiffPArea;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.BaseNodeInformationPanel;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.NodeConceptList;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.NodeDetailsPanel;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.listeners.EntitySelectionAdapter;
import edu.njit.cs.saboc.blu.core.ontology.Concept;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.diffpareataxonomy.SCTDescriptiveDiffPAreaTaxonomy;
import edu.njit.cs.saboc.blu.sno.descriptivedelta.EditingOperationReport;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy.diff.configuration.SCTDiffPAreaTaxonomyConfiguration;
import edu.njit.cs.saboc.blu.sno.localdatasource.concept.SCTConcept;
import java.awt.BorderLayout;
import java.util.ArrayList;
import javax.swing.JSplitPane;

/**
 *
 * @author Chris O
 */
public class DescriptiveDeltaConceptList extends BaseNodeInformationPanel<DiffPArea> {

     private final JSplitPane splitPane;
    
    private final NodeConceptList descriptiveDeltaConceptList;
    
    private final DescriptiveDeltaReportPanel reportPanel;

    public DescriptiveDeltaConceptList(SCTDiffPAreaTaxonomyConfiguration configuration) {
        
        SCTDescriptiveDiffPAreaTaxonomy descriptiveDiffTaxonomy = (SCTDescriptiveDiffPAreaTaxonomy)configuration.getPAreaTaxonomy();
        
        this.setLayout(new BorderLayout());
        
        this.splitPane = NodeDetailsPanel.createStyledSplitPane(JSplitPane.VERTICAL_SPLIT);
        
        this.descriptiveDeltaConceptList = new NodeConceptList(
                new SCTDescriptiveDeltaConceptListModel(configuration),
                configuration);
        
        descriptiveDeltaConceptList.addEntitySelectionListener(new EntitySelectionAdapter<SCTConcept>() {

            @Override
            public void noEntitySelected() {
               reportPanel.clearEntries();
            }

            @Override
            public void entityClicked(SCTConcept entity) {
                 reportPanel.setCurrentEditingOperationReport(
                        descriptiveDiffTaxonomy.getDescriptiveDelta().getInferredChanges().getOrDefault(entity, new EditingOperationReport(entity)));
            }
        });
        
        this.descriptiveDeltaConceptList.setDefaultTableRenderer(EditingOperationReport.class, new DescriptiveDeltaReportRenderer());
        
        this.reportPanel = new DescriptiveDeltaReportPanel(descriptiveDiffTaxonomy.getDescriptiveDelta());
        
        this.splitPane.setTopComponent(descriptiveDeltaConceptList);
        this.splitPane.setBottomComponent(reportPanel);
        
        this.add(splitPane, BorderLayout.CENTER);
    }

    @Override
    public void setContents(DiffPArea node) {
        ArrayList<Concept> concepts = new ArrayList<>(node.getConcepts());
        
        concepts.sort( (a, b) -> {
            return a.getName().compareToIgnoreCase(b.getName());
        });
        
        descriptiveDeltaConceptList.setContents(concepts);
    }

    @Override
    public void clearContents() {
        descriptiveDeltaConceptList.clearContents();
    }
}
