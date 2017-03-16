package edu.njit.cs.saboc.blu.sno.abn.pareataxonomy;

import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.PAreaTaxonomy;
import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.provenance.SimplePAreaTaxonomyDerivation;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.provenance.SCTBasicStatedPAreaTaxonomyDerivation;

/**
 * A partial-area taxonomy created from concepts' stated relationships 
 * (both hierarchical and attribute relationships)
 * 
 * @author Chris O
 */
public class SCTStatedRelationshipsPAreaTaxonomy extends PAreaTaxonomy  {
    
    public SCTStatedRelationshipsPAreaTaxonomy(PAreaTaxonomy derivedTaxonomy){
        super(derivedTaxonomy, 
                new SCTBasicStatedPAreaTaxonomyDerivation(
                        (SimplePAreaTaxonomyDerivation)derivedTaxonomy.getDerivation()));
    }
    
}
