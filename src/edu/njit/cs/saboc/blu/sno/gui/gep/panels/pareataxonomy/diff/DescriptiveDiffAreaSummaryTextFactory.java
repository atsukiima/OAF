package edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy.diff;

import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.Area;
import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.diff.DiffArea;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.pareataxonomy.diff.DiffAreaSummaryTextFactory;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.diffpareataxonomy.SCTDescriptiveDiffPAreaTaxonomy;
import edu.njit.cs.saboc.blu.sno.descriptivedelta.DescriptiveDelta;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy.diff.configuration.SCTDiffPAreaTaxonomyConfiguration;
import edu.njit.cs.saboc.blu.sno.localdatasource.concept.SCTConcept;

/**
 *
 * @author Chris O
 */
public class DescriptiveDiffAreaSummaryTextFactory extends DiffAreaSummaryTextFactory {
    
    public DescriptiveDiffAreaSummaryTextFactory(SCTDiffPAreaTaxonomyConfiguration config) {
        super(config);
    }

    @Override
    public String createNodeSummaryText(Area area) {
        String primaryDescription = super.createNodeSummaryText(area);
        
        DiffArea diffArea = (DiffArea)area;
        
        SCTDescriptiveDiffPAreaTaxonomy diffTaxonomy = (SCTDescriptiveDiffPAreaTaxonomy)super.getConfiguration().getPAreaTaxonomy();
        
        int editedCount = 0;
        int inferredChangeCount = 0;
        
        DescriptiveDelta delta = diffTaxonomy.getDescriptiveDelta();
        
        for(Object concept : diffArea.getConcepts()) { // Why is the area returning a set of objects?
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
        
        
        return String.format("%s<p><p>%s", primaryDescription, descriptiveMetrics);
    }
}
