package edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy.configuration;

import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTPAreaTaxonomy;
import edu.njit.cs.saboc.blu.sno.gui.abnselection.SCTDisplayFrameListener;


/**
 *
 * @author Chris O
 */
public class SCTPAreaTaxonomyConfigurationFactory {
    public SCTPAreaTaxonomyConfiguration createConfiguration(SCTPAreaTaxonomy taxonomy, SCTDisplayFrameListener displayListener) {
        
        SCTPAreaTaxonomyConfiguration pareaTaxonomyConfiguration = new SCTPAreaTaxonomyConfiguration();
        pareaTaxonomyConfiguration.setDataConfiguration(new SCTPAreaTaxonomyDataConfiguration(taxonomy));
        pareaTaxonomyConfiguration.setUIConfiguration(new SCTPAreaTaxonomyUIConfiguration(pareaTaxonomyConfiguration, displayListener));
        pareaTaxonomyConfiguration.setTextConfiguration(new SCTPAreaTaxonomyTextConfiguration(taxonomy));
        
        return pareaTaxonomyConfiguration;
    }
}
