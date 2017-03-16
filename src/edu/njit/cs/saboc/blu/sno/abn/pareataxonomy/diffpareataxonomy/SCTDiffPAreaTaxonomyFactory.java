package edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.diffpareataxonomy;

import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.PAreaTaxonomy;
import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.diff.DiffPAreaTaxonomyFactory;
import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.diff.explain.PropertyChangeDetailsFactory;
import edu.njit.cs.saboc.blu.sno.sctdatasource.SCTRelease;

/**
 * A factory for creating SNOMED CT diff partial-area taxonomies
 * 
 * @author Chris O
 */
public class SCTDiffPAreaTaxonomyFactory extends DiffPAreaTaxonomyFactory {
    private final SCTRelease fromRelease;
    private final SCTRelease toRelease;
    
    private final PAreaTaxonomy fromTaxonomy;
    private final PAreaTaxonomy toTaxonomy;
    
    public SCTDiffPAreaTaxonomyFactory(SCTRelease fromRelease, SCTRelease toRelease, PAreaTaxonomy fromTaxonomy, PAreaTaxonomy toTaxonomy) {
        this.fromRelease = fromRelease;
        this.toRelease = toRelease;
        
        this.fromTaxonomy = fromTaxonomy;
        this.toTaxonomy = toTaxonomy;
    }

    @Override
    public PropertyChangeDetailsFactory getPropertyChangeDetailsFactory() {
        return new SCTPropertyChangeDetailsFactory(fromRelease, toRelease, fromTaxonomy, toTaxonomy);
    }
}
