package edu.njit.cs.saboc.blu.sno.gui.gep.panels.disjointpareataxonomy.configuration;

import edu.njit.cs.saboc.blu.core.abn.disjoint.DisjointAbstractionNetwork;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.disjointabn.pareataxonomy.DisjointPAreaTaxonomyConfiguration;
import edu.njit.cs.saboc.blu.sno.sctdatasource.SCTRelease;

/**
 *
 * @author Chris O
 */
public class SCTDisjointPAreaTaxonomyConfiguration extends DisjointPAreaTaxonomyConfiguration {
    
    private final SCTRelease release;
    
    public SCTDisjointPAreaTaxonomyConfiguration(
            SCTRelease release, 
            DisjointAbstractionNetwork disjointAbN) {
        
        super(disjointAbN);
        
        this.release = release;
    }
    
    public SCTRelease getRelease() {
        return release;
    }
    
    public void setUIConfiguration(SCTDisjointPAreaTaxonomyUIConfiguration uiConfiguation) {
        super.setUIConfiguration(uiConfiguation);
    }
    
    public void setTextConfiguration(SCTDisjointPAreaTaxonomyTextConfiguration uiConfiguation) {
        super.setTextConfiguration(uiConfiguation);
    }
    
    @Override
    public SCTDisjointPAreaTaxonomyUIConfiguration getUIConfiguration() {
        return (SCTDisjointPAreaTaxonomyUIConfiguration)super.getUIConfiguration();
    }
    
    @Override
    public SCTDisjointPAreaTaxonomyTextConfiguration getTextConfiguration() {
        return (SCTDisjointPAreaTaxonomyTextConfiguration)super.getTextConfiguration();
    }
}
