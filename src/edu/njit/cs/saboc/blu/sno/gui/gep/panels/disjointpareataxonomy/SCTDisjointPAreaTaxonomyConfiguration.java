package edu.njit.cs.saboc.blu.sno.gui.gep.panels.disjointpareataxonomy;

import edu.njit.cs.saboc.blu.sno.abn.disjointpareataxonomy.DisjointPAreaTaxonomy;
import edu.njit.cs.saboc.blu.sno.gui.abnselection.SCTDisplayFrameListener;
import edu.njit.cs.saboc.blu.sno.gui.gep.configuration.SCTDisjointPAreaTaxonomyGEPConfiguration;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.SCTPAreaTaxonomyConfiguration;

/**
 *
 * @author Chris O
 */
public class SCTDisjointPAreaTaxonomyConfiguration extends SCTPAreaTaxonomyConfiguration {
    
    private final DisjointPAreaTaxonomy disjointTaxonomy;
    
    private final SCTDisjointPAreaTaxonomyGEPConfiguration gepConfiguration;
    
    public SCTDisjointPAreaTaxonomyConfiguration(DisjointPAreaTaxonomy disjointTaxonomy, SCTDisplayFrameListener displayListener, 
            
            SCTDisjointPAreaTaxonomyGEPConfiguration gepConfiguration) {
        
        super(disjointTaxonomy.getParentAbstractionNetwork(), displayListener, null);
        
        this.disjointTaxonomy = disjointTaxonomy;
        
        this.gepConfiguration = gepConfiguration;
    }
    
    public SCTDisjointPAreaTaxonomyGEPConfiguration getDisjointGEPConfiguration() {
        return gepConfiguration;
    }
    
    public DisjointPAreaTaxonomy getDisjointPAreaTaxonomy() {
        return disjointTaxonomy;
    }
}
