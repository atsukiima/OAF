package edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.diffpareataxonomy;

import edu.njit.cs.saboc.blu.core.abn.diff.OntologyDifferences;
import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.AreaTaxonomy;
import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.PAreaTaxonomy;
import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.diff.DiffArea;
import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.diff.DiffAreaTaxonomy;
import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.diff.DiffPArea;
import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.diff.DiffPAreaTaxonomy;
import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.diff.DiffPAreaTaxonomyFactory;
import edu.njit.cs.saboc.blu.core.datastructure.hierarchy.Hierarchy;
import edu.njit.cs.saboc.blu.sno.descriptivedelta.DescriptiveDelta;

/**
 *
 * @author Chris O
 */
public class SCTDescriptiveDiffPAreaTaxonomyFactory extends DiffPAreaTaxonomyFactory {
    
    private final DescriptiveDelta descriptiveDelta;
    
    public SCTDescriptiveDiffPAreaTaxonomyFactory(DescriptiveDelta descriptiveDelta) {
        this.descriptiveDelta = descriptiveDelta;
    }

    @Override
    public DiffPAreaTaxonomy createDiffPAreaTaxonomy(
            DiffAreaTaxonomy areaTaxonomy, 
            PAreaTaxonomy toSourceTaxonomy, 
            PAreaTaxonomy fromSourceTaxonomy, 
            Hierarchy<DiffPArea> pareaHierarchy) {
        
        return new SCTDescriptiveDiffPAreaTaxonomy(
                (SCTDescriptiveDiffAreaTaxonomy)areaTaxonomy, 
                fromSourceTaxonomy, 
                toSourceTaxonomy, 
                pareaHierarchy);
    }

    @Override
    public DiffAreaTaxonomy createDiffAreaTaxonomy(
            OntologyDifferences ontDifferences, 
            AreaTaxonomy fromSourceTaxonomy, 
            AreaTaxonomy toSourceTaxonomy, 
            Hierarchy<DiffArea> diffAreas) {
        
        return new SCTDescriptiveDiffAreaTaxonomy(
                this, 
                ontDifferences,  
                descriptiveDelta, 
                fromSourceTaxonomy, 
                toSourceTaxonomy, 
                diffAreas);
    }
}
