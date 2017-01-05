package edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy.diff;

import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.diff.DiffPArea;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.models.AbstractNodeEntityTableModel;
import edu.njit.cs.saboc.blu.core.ontology.Concept;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.diffpareataxonomy.SCTDescriptiveDiffPAreaTaxonomy;
import edu.njit.cs.saboc.blu.sno.descriptivedelta.DescriptiveDelta;
import edu.njit.cs.saboc.blu.sno.descriptivedelta.EditingOperationReport;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy.diff.configuration.SCTDiffPAreaTaxonomyConfiguration;
import edu.njit.cs.saboc.blu.sno.localdatasource.concept.SCTConcept;

/**
 *
 * @author Chris O
 */
public class SCTDescriptiveDeltaConceptListModel extends AbstractNodeEntityTableModel<Concept, DiffPArea> {
    
    private final SCTDiffPAreaTaxonomyConfiguration config;
    
    public SCTDescriptiveDeltaConceptListModel(SCTDiffPAreaTaxonomyConfiguration config) {
        super(new String[] {
            "Concept",
            "ID",
            "Stated Editing Operations",
            "Changes to Inferred Relationships"
        });
        
        this.config = config;
    }

    @Override
    protected Object[] createRow(Concept item) {
        
        SCTConcept c = (SCTConcept)item;
        
        SCTDescriptiveDiffPAreaTaxonomy diffTaxonomy = (SCTDescriptiveDiffPAreaTaxonomy)config.getPAreaTaxonomy();
        
        DescriptiveDelta descriptiveDelta = diffTaxonomy.getDescriptiveDelta();
        
        EditingOperationReport statedReport = descriptiveDelta.getStatedEditingOperations().getOrDefault(c, new EditingOperationReport(c));
        EditingOperationReport inferredReport = descriptiveDelta.getInferredChanges().getOrDefault(c, new EditingOperationReport(c));
        
        return new Object [] {
            c.getName(),
            c.getIDAsString(), 
            statedReport, 
            inferredReport
        };
    }
}
