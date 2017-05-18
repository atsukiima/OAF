package edu.njit.cs.saboc.blu.sno.gui.abnselection.createanddisplay;

import edu.njit.cs.saboc.blu.core.gui.dialogs.AbNCreateAndDisplayDialog;
import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.PAreaTaxonomy;
import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.PAreaTaxonomyFactory;
import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.PAreaTaxonomyGenerator;
import edu.njit.cs.saboc.blu.core.datastructure.hierarchy.Hierarchy;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.SCTInferredPAreaTaxonomyFactory;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.SCTInheritableProperty;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.SCTStatedRelationshipsPAreaTaxonomyFactory;
import edu.njit.cs.saboc.blu.sno.gui.abnselection.SCTAbNFrameManager;
import edu.njit.cs.saboc.blu.sno.localdatasource.concept.SCTConcept;
import edu.njit.cs.saboc.blu.sno.sctdatasource.SCTRelease;
import edu.njit.cs.saboc.blu.sno.sctdatasource.SCTReleaseWithStated;
import java.util.Set;

/**
 * Creates and displays a partial-area taxonomy for a given SNOMED CT release
 * 
 * @author Kevyn
 */
public class CreateAndDisplaySCTPAreaTaxonomy extends AbNCreateAndDisplayDialog<PAreaTaxonomy> {

    private final SCTConcept selectedRoot;
    
    private final SCTRelease release;
    
    private final Set<SCTInheritableProperty> availableProperties;
    private final Set<SCTInheritableProperty> selectedProperties;
    
    private final boolean useStatedRelationships;

    public CreateAndDisplaySCTPAreaTaxonomy(
            String text, 
            SCTConcept selectedRoot, 
            Set<SCTInheritableProperty> availableProperties,
            Set<SCTInheritableProperty> selectedProperties,
            SCTAbNFrameManager displayFrameListener, 
            SCTRelease release,
            boolean useStatedRelationships) {
        
        super(text, displayFrameListener);
        
        this.selectedRoot = selectedRoot;
        
        this.release = release;
        this.availableProperties = availableProperties;
        this.selectedProperties = selectedProperties;
        
        this.useStatedRelationships = useStatedRelationships;
    }

    @Override
    protected void display(PAreaTaxonomy taxonomy) {
        super.getDisplayFrameListener().displayPAreaTaxonomy(taxonomy);
    }

    @Override
    protected PAreaTaxonomy create() {
        
        Hierarchy<SCTConcept> conceptHierarchy;
        PAreaTaxonomyFactory factory;
        
        if(useStatedRelationships) {
            SCTReleaseWithStated statedRelease = (SCTReleaseWithStated)release;
            
            conceptHierarchy = statedRelease.getStatedHierarchy().getSubhierarchyRootedAt(selectedRoot);
            factory = new SCTStatedRelationshipsPAreaTaxonomyFactory(statedRelease, selectedRoot); 
        } else {
            conceptHierarchy = release.getConceptHierarchy().getSubhierarchyRootedAt(selectedRoot);
            factory = new SCTInferredPAreaTaxonomyFactory(release, release.getConceptHierarchy());
        }
        
        PAreaTaxonomyGenerator taxonomyGenerator = new PAreaTaxonomyGenerator();
        PAreaTaxonomy taxonomy = taxonomyGenerator.derivePAreaTaxonomy(factory, conceptHierarchy);
        
        if(availableProperties.equals(selectedProperties)) {
            return taxonomy;
        } else {
            return taxonomy.getRelationshipSubtaxonomy(selectedProperties);
        }
    }
}
