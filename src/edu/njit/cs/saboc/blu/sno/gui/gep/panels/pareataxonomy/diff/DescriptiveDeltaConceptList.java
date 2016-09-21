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
import javax.swing.JTabbedPane;

/**
 *
 * @author Chris O
 */
public class DescriptiveDeltaConceptList extends BaseNodeInformationPanel<DiffPArea> {

     private final JSplitPane splitPane;
    
    private final NodeConceptList descriptiveDeltaConceptList;
    
    private final JTabbedPane editReportTabs;
    
    private final DescriptiveDeltaReportPanel statedReportPanel;
    private final DescriptiveDeltaReportPanel inferredReportPanel;

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
                clearEditingOperationTabs();
            }

            @Override
            public void entityClicked(SCTConcept entity) {
                
                clearEditingOperationTabs();

                if(descriptiveDiffTaxonomy.getDescriptiveDelta().getStatedEditingOperations().containsKey(entity)) {
                    statedReportPanel.setCurrentEditingOperationReport(
                        descriptiveDiffTaxonomy.getDescriptiveDelta().getStatedEditingOperations().get(entity));
                    
                    editReportTabs.setEnabledAt(0, true);
                } else {
                    statedReportPanel.setCurrentEditingOperationReport(new EditingOperationReport(entity));
                }
                
                if(descriptiveDiffTaxonomy.getDescriptiveDelta().getInferredChanges().containsKey(entity)) {
                    inferredReportPanel.setCurrentEditingOperationReport(
                        descriptiveDiffTaxonomy.getDescriptiveDelta().getInferredChanges().get(entity));
                    
                    editReportTabs.setEnabledAt(1, true);
                } else {
                    inferredReportPanel.setCurrentEditingOperationReport(new EditingOperationReport(entity));
                }
            }
        });
        
        this.descriptiveDeltaConceptList.setDefaultTableRenderer(EditingOperationReport.class, new DescriptiveDeltaReportRenderer());
        
        this.statedReportPanel = new DescriptiveDeltaReportPanel(descriptiveDiffTaxonomy.getDescriptiveDelta()); 
        this.inferredReportPanel = new DescriptiveDeltaReportPanel(descriptiveDiffTaxonomy.getDescriptiveDelta());
        
        this.editReportTabs = new JTabbedPane();
        
        this.splitPane.setTopComponent(descriptiveDeltaConceptList);
        
        this.editReportTabs.addTab("Stated Editing Operaitons", statedReportPanel);
        this.editReportTabs.addTab("Inferred Relationship Changes", inferredReportPanel);
                
        this.splitPane.setBottomComponent(editReportTabs);
        
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
        
        clearEditingOperationTabs();
    }
    
    private void clearEditingOperationTabs() {
        statedReportPanel.clearContent();
        inferredReportPanel.clearContent();
        
        editReportTabs.setEnabledAt(0, false);
        editReportTabs.setEnabledAt(1, false);
    }
}
