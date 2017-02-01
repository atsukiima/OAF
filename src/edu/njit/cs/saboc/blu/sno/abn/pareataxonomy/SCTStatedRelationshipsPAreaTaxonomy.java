package edu.njit.cs.saboc.blu.sno.abn.pareataxonomy;

import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.PAreaTaxonomy;
import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.provenance.SimplePAreaTaxonomyDerivation;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.provenance.SCTBasicStatedPAreaTaxonomyDerivation;

/**
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
