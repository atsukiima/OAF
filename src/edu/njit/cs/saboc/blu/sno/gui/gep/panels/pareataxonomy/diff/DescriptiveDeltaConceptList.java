package edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy.diff;

import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.NodeConceptList;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.NodeDetailsPanel;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.listeners.EntitySelectionAdapter;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.diffpareataxonomy.SCTDescriptiveDiffPAreaTaxonomy;
import edu.njit.cs.saboc.blu.sno.descriptivedelta.DescriptiveDelta;
import edu.njit.cs.saboc.blu.sno.descriptivedelta.EditingOperationReport;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy.diff.configuration.SCTDiffPAreaTaxonomyConfiguration;
import edu.njit.cs.saboc.blu.sno.localdatasource.concept.SCTConcept;
import java.awt.BorderLayout;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

/**
 *
 * @author Chris O
 */
public class DescriptiveDeltaConceptList extends JPanel {

    private final JSplitPane splitPane;
    
    private final NodeConceptList descriptiveDeltaConceptList;
        
    private final ConceptDescriptiveDeltaReportPanel selectedConceptReportPanel;
    
    private final DescriptiveDelta sourceDescriptiveDelta;

    public DescriptiveDeltaConceptList(SCTDiffPAreaTaxonomyConfiguration configuration) {
        
        SCTDescriptiveDiffPAreaTaxonomy descriptiveDiffTaxonomy = (SCTDescriptiveDiffPAreaTaxonomy)configuration.getPAreaTaxonomy();
        
        this.sourceDescriptiveDelta = descriptiveDiffTaxonomy.getDescriptiveDelta();
        
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
                
                selectedConceptReportPanel.setContents(entity);
            }

            @Override
            public void entityDoubleClicked(SCTConcept entity) {
                ConceptDescriptiveDeltaReportDialog panel = new ConceptDescriptiveDeltaReportDialog(configuration);
                
                panel.setContent(entity);
                
                JFrame frame = new JFrame();
                frame.setSize(1200, 600);
                frame.add(panel);
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                
                frame.setVisible(true);
            }
        });
        
        this.descriptiveDeltaConceptList.setDefaultTableRenderer(EditingOperationReport.class, new DescriptiveDeltaReportRenderer());
        
        this.splitPane.setTopComponent(descriptiveDeltaConceptList);
       
        this.selectedConceptReportPanel = new ConceptDescriptiveDeltaReportPanel(sourceDescriptiveDelta);
        
        this.splitPane.setBottomComponent(selectedConceptReportPanel);
        
        this.add(splitPane, BorderLayout.CENTER);
    }

    public void setContents(ArrayList<SCTConcept> concepts) {
        descriptiveDeltaConceptList.setContents(concepts);
    }

    public void clearContents() {
        descriptiveDeltaConceptList.clearContents();
        
        clearEditingOperationTabs();
    }
    
    private void clearEditingOperationTabs() {
        selectedConceptReportPanel.clearContents();
    }
}
