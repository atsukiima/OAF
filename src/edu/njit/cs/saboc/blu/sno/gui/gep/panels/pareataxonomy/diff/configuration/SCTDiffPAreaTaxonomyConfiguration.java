package edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy.diff.configuration;

import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.diff.DiffPAreaTaxonomy;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.pareataxonomy.diff.configuration.DiffPAreaTaxonomyConfiguration;

/**
 *
 * @author Chris O
 */
public class SCTDiffPAreaTaxonomyConfiguration extends DiffPAreaTaxonomyConfiguration {

    public SCTDiffPAreaTaxonomyConfiguration(DiffPAreaTaxonomy taxonomy) {
        super(taxonomy);
    }
  
    public void setUIConfiguration(SCTDiffPAreaTaxonomyUIConfiguration config) {
        super.setUIConfiguration(config);
    }
    
    public void setTextConfiguration(SCTDiffPAreaTaxonomyTextConfiguration config) {
        super.setTextConfiguration(config);
    }
    
    public SCTDiffPAreaTaxonomyUIConfiguration getUIConfiguration() {
        return (SCTDiffPAreaTaxonomyUIConfiguration)super.getUIConfiguration();
    }
    
    public SCTDiffPAreaTaxonomyTextConfiguration getTextConfiguration() {
        return (SCTDiffPAreaTaxonomyTextConfiguration)super.getTextConfiguration();
    }
}
