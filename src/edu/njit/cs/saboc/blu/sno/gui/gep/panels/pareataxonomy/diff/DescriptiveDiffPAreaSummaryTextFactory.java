package edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy.diff;

import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.PArea;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.pareataxonomy.diff.DiffPAreaSummaryTextFactory;
import edu.njit.cs.saboc.blu.core.ontology.Concept;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.diffpareataxonomy.SCTDescriptiveDiffPAreaTaxonomy;
import edu.njit.cs.saboc.blu.sno.descriptivedelta.DescriptiveDelta;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy.diff.configuration.SCTDiffPAreaTaxonomyConfiguration;
import edu.njit.cs.saboc.blu.sno.localdatasource.concept.SCTConcept;

/**
 *
 * @author Chris O
 */
public class DescriptiveDiffPAreaSummaryTextFactory extends DiffPAreaSummaryTextFactory {
    
    public DescriptiveDiffPAreaSummaryTextFactory(SCTDiffPAreaTaxonomyConfiguration config) {
        super(config);
    }

    @Override
    public String createNodeSummaryText(PArea parea) {
        String primaryDescription = super.createNodeSummaryText(parea);
        
        SCTDescriptiveDiffPAreaTaxonomy diffTaxonomy = (SCTDescriptiveDiffPAreaTaxonomy)super.getConfiguration().getPAreaTaxonomy();
        
        int editedCount = 0;
        int inferredChangeCount = 0;
        
        DescriptiveDelta delta = diffTaxonomy.getDescriptiveDelta();
        
        for(Concept concept : parea.getConcepts()) {
            SCTConcept sctConcept = (SCTConcept)concept;
            
            if(delta.getStatedEditingOperations().containsKey(sctConcept)) {
                editedCount++;
            }
            
            if(delta.getInferredChanges().containsKey(sctConcept)) {
                inferredChangeCount++;
            }
        }
        
        String descriptiveMetrics = String.format("<b>Descriptive delta metrics:</b><br>"
                + "# Concepts with stated editing operation: %d<br>"
                + "# Concepts with inferred change: %d<br>",
                editedCount,
                inferredChangeCount);
        
        if(delta.getStatedEditingOperations().containsKey((SCTConcept)parea.getRoot())) {
            descriptiveMetrics += "<p>The root concept had stated editing operations applied.";
        }
        
        if(delta.getInferredChanges().containsKey((SCTConcept)parea.getRoot())) {
            descriptiveMetrics += "<p>The root concept's inferred relationships were changed.";
        }
        
        return String.format("%s<p><p>%s", primaryDescription, descriptiveMetrics);
    }
    
    
}
