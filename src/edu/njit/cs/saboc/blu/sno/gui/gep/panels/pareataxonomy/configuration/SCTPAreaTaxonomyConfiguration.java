package edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy.configuration;

import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.PAreaTaxonomy;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.pareataxonomy.configuration.PAreaTaxonomyConfiguration;
import edu.njit.cs.saboc.blu.sno.sctdatasource.SCTRelease;


/**
 *
 * @author Chris O
 */
public class SCTPAreaTaxonomyConfiguration extends PAreaTaxonomyConfiguration {
    
    private final SCTRelease release;
    
    public SCTPAreaTaxonomyConfiguration(SCTRelease release, PAreaTaxonomy taxonomy) {
        super(taxonomy);
        
        this.release = release;
    }
    
    public SCTRelease getRelease() {
        return release;
    }
    
    public void setUIConfiguration(SCTPAreaTaxonomyUIConfiguration config) {
        super.setUIConfiguration(config);
    }
    
    public void setTextConfiguration(SCTPAreaTaxonomyTextConfiguration config) {
        super.setTextConfiguration(config);
    }
    
    public SCTPAreaTaxonomyUIConfiguration getUIConfiguration() {
        return (SCTPAreaTaxonomyUIConfiguration)super.getUIConfiguration();
    }
    
    public SCTPAreaTaxonomyTextConfiguration getTextConfiguration() {
        return (SCTPAreaTaxonomyTextConfiguration)super.getTextConfiguration();
    }
    
}
