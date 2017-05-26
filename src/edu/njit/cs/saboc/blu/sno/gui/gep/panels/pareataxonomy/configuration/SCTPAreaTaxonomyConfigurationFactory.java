package edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy.configuration;

import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.PAreaTaxonomy;
import edu.njit.cs.saboc.blu.core.gui.graphframe.AbNDisplayManager;
import edu.njit.cs.saboc.blu.sno.gui.abnselection.SCTAbNFrameManager;
import edu.njit.cs.saboc.blu.sno.sctdatasource.SCTRelease;


/**
 *
 * @author Chris O
 */
public class SCTPAreaTaxonomyConfigurationFactory {
    
    public SCTPAreaTaxonomyConfiguration createConfiguration(
            SCTRelease release,
            PAreaTaxonomy taxonomy, 
            AbNDisplayManager displayListener, 
            SCTAbNFrameManager frameManager,
            boolean showingAreaTaxonomy) {
        
        SCTPAreaTaxonomyConfiguration pareaTaxonomyConfiguration = new SCTPAreaTaxonomyConfiguration(release, taxonomy);
        
        pareaTaxonomyConfiguration.setUIConfiguration(
                new SCTPAreaTaxonomyUIConfiguration(
                        pareaTaxonomyConfiguration, 
                        displayListener, 
                        frameManager,
                        showingAreaTaxonomy));
        
        pareaTaxonomyConfiguration.setTextConfiguration(new SCTPAreaTaxonomyTextConfiguration(taxonomy));
        
        return pareaTaxonomyConfiguration;
    }
}
