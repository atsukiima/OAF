package edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.provenance;

import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.provenance.SimplePAreaTaxonomyDerivation;

/**
 * Identifies the arguments needed to create a stated partial-area taxonomy
 * from a hierarchy of concepts
 * 
 * @author Chris O
 */
public class SCTBasicStatedPAreaTaxonomyDerivation extends SimplePAreaTaxonomyDerivation {
    
    public SCTBasicStatedPAreaTaxonomyDerivation(
            SimplePAreaTaxonomyDerivation baseDerivation) {
        
        super(baseDerivation);
    }

    @Override
    public String getDescription() {
        return "Derived SCT Stated PArea Taxonomy";
    }
}
