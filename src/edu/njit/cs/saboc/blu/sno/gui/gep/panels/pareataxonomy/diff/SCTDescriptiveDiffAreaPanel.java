package edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy.diff;

import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.pareataxonomy.diff.DiffAreaPanel;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy.diff.configuration.SCTDiffPAreaTaxonomyConfiguration;

/**
 *
 * @author Chris O
 */
public class SCTDescriptiveDiffAreaPanel extends DiffAreaPanel {
    
    private final SCTDiffAreaDeltaConceptPanel descriptiveDeltaConceptPanel;
    
    public SCTDescriptiveDiffAreaPanel(SCTDiffPAreaTaxonomyConfiguration config) {
        super(config, new DescriptiveDiffAreaSummaryTextFactory(config));        
        
        this.descriptiveDeltaConceptPanel = new SCTDiffAreaDeltaConceptPanel(config);

        super.addInformationTab(descriptiveDeltaConceptPanel, "Descriptive Delta Details");
    }
}
