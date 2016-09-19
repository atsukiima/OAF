package edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy.diff;

import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.pareataxonomy.diff.DiffPAreaPanel;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy.diff.configuration.SCTDiffPAreaTaxonomyConfiguration;

/**
 *
 * @author Chris O
 */
public class SCTDescriptiveDiffPAreaPanel extends DiffPAreaPanel {
    
    private final DescriptiveDeltaConceptList descriptiveDeltaConceptList;
    
    public SCTDescriptiveDiffPAreaPanel(SCTDiffPAreaTaxonomyConfiguration configuration) {
        super(configuration);
        
        this.descriptiveDeltaConceptList = new DescriptiveDeltaConceptList(configuration);

        super.addInformationTab(descriptiveDeltaConceptList, "Descriptive Delta Details");
    }
}
