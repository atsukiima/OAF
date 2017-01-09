package edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy.diff.configuration;

import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.diff.DiffPAreaTaxonomy;
import edu.njit.cs.saboc.blu.core.gui.graphframe.AbNDisplayManager;

/**
 *
 * @author Chris O
 */
public class SCTDiffPAreaTaxonomyConfigurationFactory {
    
    public SCTDiffPAreaTaxonomyConfiguration createConfiguration(
            DiffPAreaTaxonomy taxonomy, 
            AbNDisplayManager displayListener) {
        
        SCTDiffPAreaTaxonomyConfiguration pareaTaxonomyConfiguration = new SCTDiffPAreaTaxonomyConfiguration(taxonomy);
        pareaTaxonomyConfiguration.setUIConfiguration(new SCTDiffPAreaTaxonomyUIConfiguration(pareaTaxonomyConfiguration, displayListener));
        
        pareaTaxonomyConfiguration.setTextConfiguration(new SCTDiffPAreaTaxonomyTextConfiguration(taxonomy));
        
        return pareaTaxonomyConfiguration;
    }
}
