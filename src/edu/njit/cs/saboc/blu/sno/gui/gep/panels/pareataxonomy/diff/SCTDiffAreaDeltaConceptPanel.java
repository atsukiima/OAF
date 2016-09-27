
package edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy.diff;

import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.diff.DiffArea;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.BaseNodeInformationPanel;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy.diff.configuration.SCTDiffPAreaTaxonomyConfiguration;
import edu.njit.cs.saboc.blu.sno.localdatasource.concept.SCTConcept;
import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.Set;

/**
 *
 * @author Chris O
 */
public class SCTDiffAreaDeltaConceptPanel extends BaseNodeInformationPanel<DiffArea> {
    
    private final DescriptiveDeltaConceptList ddConceptList;
    
    public SCTDiffAreaDeltaConceptPanel(SCTDiffPAreaTaxonomyConfiguration config) {
        this.setLayout(new BorderLayout());
        
        this.ddConceptList = new DescriptiveDeltaConceptList(config);
        
        this.add(ddConceptList, BorderLayout.CENTER);
    }

    @Override
    public void setContents(DiffArea node) {
        
        Set<SCTConcept> concepts = (Set<SCTConcept>)(Set<?>)node.getConcepts();
        
        ArrayList<SCTConcept> sortedConcepts = new ArrayList<>(concepts);
        
        sortedConcepts.sort((a,b) -> {
            return a.getName().compareToIgnoreCase(b.getName());
        }); 
        
        ddConceptList.setContents(sortedConcepts);
    }

    @Override
    public void clearContents() {
        ddConceptList.clearContents();
    }
}