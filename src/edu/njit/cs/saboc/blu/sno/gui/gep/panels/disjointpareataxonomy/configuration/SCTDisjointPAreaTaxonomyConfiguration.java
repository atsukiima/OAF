package edu.njit.cs.saboc.blu.sno.gui.gep.panels.disjointpareataxonomy.configuration;

import edu.njit.cs.saboc.blu.core.abn.disjoint.DisjointAbstractionNetwork;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.disjointabn.pareataxonomy.DisjointPAreaTaxonomyConfiguration;

/**
 *
 * @author Chris O
 */
public class SCTDisjointPAreaTaxonomyConfiguration extends DisjointPAreaTaxonomyConfiguration {
    
    public SCTDisjointPAreaTaxonomyConfiguration(DisjointAbstractionNetwork disjointAbN) {
        super(disjointAbN);
    }
    
    public void setUIConfiguration(SCTDisjointPAreaTaxonomyUIConfiguration uiConfiguation) {
        super.setUIConfiguration(uiConfiguation);
    }
    
    public void setTextConfiguration(SCTDisjointPAreaTaxonomyTextConfiguration uiConfiguation) {
        super.setTextConfiguration(uiConfiguation);
    }
    
    public SCTDisjointPAreaTaxonomyUIConfiguration getUIConfiguration() {
        return (SCTDisjointPAreaTaxonomyUIConfiguration)super.getUIConfiguration();
    }
    
    public SCTDisjointPAreaTaxonomyTextConfiguration getTextConfiguration() {
        return (SCTDisjointPAreaTaxonomyTextConfiguration)super.getTextConfiguration();
    }
}
