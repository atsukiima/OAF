package edu.njit.cs.saboc.blu.sno.gui.abnselection.createanddisplay;

import edu.njit.cs.saboc.blu.core.gui.dialogs.AbNCreateAndDisplayDialog;
import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.PAreaTaxonomy;
import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.PAreaTaxonomyFactory;
import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.PAreaTaxonomyGenerator;
import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.diff.DiffPAreaTaxonomy;
import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.diff.DiffPAreaTaxonomyGenerator;
import edu.njit.cs.saboc.blu.core.datastructure.hierarchy.Hierarchy;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.SCTInferredPAreaTaxonomyFactory;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.SCTInheritableProperty;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.SCTStatedRelationshipsPAreaTaxonomyFactory;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.diffpareataxonomy.SCTDescriptiveDiffPAreaTaxonomyFactory;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.diffpareataxonomy.SCTDiffPAreaTaxonomyFactory;
import edu.njit.cs.saboc.blu.sno.descriptivedelta.DescriptiveDelta;
import edu.njit.cs.saboc.blu.sno.descriptivedelta.derivation.DeltaRelationshipLoader;
import edu.njit.cs.saboc.blu.sno.descriptivedelta.derivation.DeltaRelationships;
import edu.njit.cs.saboc.blu.sno.descriptivedelta.derivation.DescriptiveDeltaGenerator;
import edu.njit.cs.saboc.blu.sno.gui.abnselection.SCTAbNFrameManager;
import edu.njit.cs.saboc.blu.sno.localdatasource.concept.SCTConcept;
import edu.njit.cs.saboc.blu.sno.sctdatasource.SCTRelease;
import edu.njit.cs.saboc.blu.sno.sctdatasource.SCTReleaseWithStated;
import java.util.Set;

/**
 * Derives and displays a diff partial-area taxonomy derived using the given
 * arguments
 * 
 * @author Chris O
 */
public class CreateAndDisplaySCTDiffPAreaTaxonomy extends AbNCreateAndDisplayDialog<DiffPAreaTaxonomy> {

    private final SCTConcept selectedRoot;
    
    private final SCTRelease fromRelease;
    private final SCTRelease toRelease;
    
    private final Set<SCTInheritableProperty> availableProperties;
    private final Set<SCTInheritableProperty> selectedProperties;
    
    private final boolean useStatedRelationships;
    private final boolean deriveDescriptiveDelta;

    public CreateAndDisplaySCTDiffPAreaTaxonomy(String text, 
            SCTConcept selectedRoot, 
            Set<SCTInheritableProperty> availableProperties,
            Set<SCTInheritableProperty> selectedProperties,
            SCTAbNFrameManager displayFrameListener, 
            SCTRelease fromRelease, 
            SCTRelease toRelease,
            boolean useStatedRelationships,
            boolean deriveDescriptiveDelta) {
        
        super(text, displayFrameListener);
        
        this.selectedRoot = selectedRoot;
        
        this.fromRelease = fromRelease;
        
        this.toRelease = toRelease;
        
        this.availableProperties = availableProperties;
        this.selectedProperties = selectedProperties;
        
        this.useStatedRelationships = useStatedRelationships;
        this.deriveDescriptiveDelta = deriveDescriptiveDelta;
    }

    @Override
    protected void display(DiffPAreaTaxonomy taxonomy) {
        super.getDisplayFrameListener().displayDiffPAreaTaxonomy(taxonomy);
    }

    @Override
    protected DiffPAreaTaxonomy create() {
        
        PAreaTaxonomyGenerator taxonomyGenerator = new PAreaTaxonomyGenerator();
        
        Hierarchy<SCTConcept> fromHierarchy;
        Hierarchy<SCTConcept> toHierarchy;
        
        PAreaTaxonomyFactory fromFactory;
        PAreaTaxonomyFactory toFactory;
        
        if(useStatedRelationships) {
            SCTReleaseWithStated fromStatedRelease = (SCTReleaseWithStated)fromRelease;
            SCTReleaseWithStated toStatedRelease = (SCTReleaseWithStated)toRelease;
            
            fromHierarchy = fromStatedRelease.getStatedHierarchy().getSubhierarchyRootedAt(selectedRoot);
            toHierarchy = toStatedRelease.getStatedHierarchy().getSubhierarchyRootedAt(selectedRoot);
            
            fromFactory = new SCTStatedRelationshipsPAreaTaxonomyFactory(fromStatedRelease, selectedRoot);
            toFactory = new SCTStatedRelationshipsPAreaTaxonomyFactory(toStatedRelease, selectedRoot);
        } else {
            fromHierarchy = fromRelease.getConceptHierarchy().getSubhierarchyRootedAt(selectedRoot);
            toHierarchy = toRelease.getConceptHierarchy().getSubhierarchyRootedAt(selectedRoot);
            
            fromFactory = new SCTInferredPAreaTaxonomyFactory(fromRelease, fromHierarchy);
            toFactory = new SCTInferredPAreaTaxonomyFactory(toRelease, toHierarchy);
        }
      
        PAreaTaxonomy fromTaxonomy = taxonomyGenerator.derivePAreaTaxonomy(fromFactory, fromHierarchy);
        PAreaTaxonomy toTaxonomy = taxonomyGenerator.derivePAreaTaxonomy(toFactory, toHierarchy);
        
        if(!availableProperties.equals(selectedProperties)) {
            fromTaxonomy = fromTaxonomy.getRelationshipSubtaxonomy(selectedProperties);
            toTaxonomy = toTaxonomy.getRelationshipSubtaxonomy(selectedProperties);
        }
        
        DiffPAreaTaxonomyGenerator diffTaxonomyGenerator = new DiffPAreaTaxonomyGenerator();

        if(deriveDescriptiveDelta) {
            DeltaRelationshipLoader deltaRelLoader = new DeltaRelationshipLoader();

            DeltaRelationships deltaRelationships = deltaRelLoader.loadDeltaRelationships(toRelease);

            DescriptiveDeltaGenerator descriptiveDeltaGenerator = new DescriptiveDeltaGenerator();

            DescriptiveDelta descriptiveDelta = descriptiveDeltaGenerator.createDescriptiveDelta(
                    fromRelease,
                    toRelease,
                    selectedRoot,
                    deltaRelationships);
                                                    
           return diffTaxonomyGenerator.createDiffPAreaTaxonomy(
                    new SCTDescriptiveDiffPAreaTaxonomyFactory(
                            fromRelease,
                            toRelease,
                            fromTaxonomy,
                            toTaxonomy,
                            descriptiveDelta),

                    fromRelease,
                    fromTaxonomy,
                    toRelease,
                    toTaxonomy);
        } else {           
            return diffTaxonomyGenerator.createDiffPAreaTaxonomy(
                    new SCTDiffPAreaTaxonomyFactory(fromRelease, toRelease, fromTaxonomy, toTaxonomy),
                    fromRelease,
                    fromTaxonomy,
                    toRelease,
                    toTaxonomy);
        }
    }
}