package edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.diffpareataxonomy;

import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.AreaTaxonomy;
import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.PAreaTaxonomy;
import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.diff.DiffArea;
import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.diff.DiffAreaTaxonomy;
import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.diff.DiffPArea;
import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.diff.DiffPAreaTaxonomy;
import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.diff.DiffPAreaTaxonomyConceptChanges;
import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.diff.DiffPAreaTaxonomyFactory;
import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.diff.explain.PropertyChangeDetailsFactory;
import edu.njit.cs.saboc.blu.core.datastructure.hierarchy.Hierarchy;
import edu.njit.cs.saboc.blu.sno.descriptivedelta.DescriptiveDelta;
import edu.njit.cs.saboc.blu.sno.sctdatasource.SCTRelease;

/**
 * Factory for creating partial-area taxonomies with descriptive delta
 * change information. 
 * 
 * @author Chris O
 */
public class SCTDescriptiveDiffPAreaTaxonomyFactory extends DiffPAreaTaxonomyFactory {
    
    private final DescriptiveDelta descriptiveDelta;

    private final SCTRelease fromRelease;
    private final SCTRelease toRelease;
    
    private final PAreaTaxonomy fromTaxonomy;
    private final PAreaTaxonomy toTaxonomy;
    
    public SCTDescriptiveDiffPAreaTaxonomyFactory(
            SCTRelease fromRelease,
            SCTRelease toRelease,
            PAreaTaxonomy fromTaxonomy,
            PAreaTaxonomy toTaxonomy,
            DescriptiveDelta descriptiveDelta) {
        
        this.descriptiveDelta = descriptiveDelta;
        
        this.fromRelease = fromRelease;
        this.toRelease = toRelease;
        
        this.fromTaxonomy = fromTaxonomy;
        this.toTaxonomy = toTaxonomy;
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
            DiffPAreaTaxonomyConceptChanges ontDifferences,
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

    @Override
    public PropertyChangeDetailsFactory getPropertyChangeDetailsFactory() {
        return new SCTPropertyChangeDetailsFactory(fromRelease, toRelease, fromTaxonomy, toTaxonomy);
    }
}
