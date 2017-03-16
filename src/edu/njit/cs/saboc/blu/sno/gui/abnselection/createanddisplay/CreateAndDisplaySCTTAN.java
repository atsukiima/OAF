package edu.njit.cs.saboc.blu.sno.gui.abnselection.createanddisplay;

import edu.njit.cs.saboc.blu.core.abn.tan.ClusterTribalAbstractionNetwork;
import edu.njit.cs.saboc.blu.core.abn.tan.TANFactory;
import edu.njit.cs.saboc.blu.core.abn.tan.TribalAbstractionNetworkGenerator;
import edu.njit.cs.saboc.blu.core.datastructure.hierarchy.Hierarchy;
import edu.njit.cs.saboc.blu.core.gui.dialogs.AbNCreateAndDisplayDialog;
import edu.njit.cs.saboc.blu.sno.abn.tan.SCTStatedTANFactory;
import edu.njit.cs.saboc.blu.sno.gui.abnselection.SCTAbNFrameManager;
import edu.njit.cs.saboc.blu.sno.localdatasource.concept.SCTConcept;
import edu.njit.cs.saboc.blu.sno.sctdatasource.SCTRelease;
import edu.njit.cs.saboc.blu.sno.sctdatasource.SCTReleaseWithStated;
import java.util.Set;

/**
 * Creates and displays a TAN with the given arguments for the given SNOMED CT
 * release.
 * 
 * @author Kevyn
 */
public class CreateAndDisplaySCTTAN extends AbNCreateAndDisplayDialog<ClusterTribalAbstractionNetwork> {

    private final SCTRelease release;
    private final Set<SCTConcept> roots;
    
    private final boolean useStatedRelationships;

    public CreateAndDisplaySCTTAN(
            String text, 
            Set<SCTConcept> roots,
            boolean useStatedRelationships,
            SCTAbNFrameManager frameManager, 
            SCTRelease release) {
        
        super(text, frameManager);

        this.release = release;
        this.roots = roots;
        this.useStatedRelationships = useStatedRelationships;
    }

    @Override
    protected void display(ClusterTribalAbstractionNetwork abn) {
        super.getDisplayFrameListener().displayTribalAbstractionNetwork(abn);
    }

    @Override
    protected ClusterTribalAbstractionNetwork create() {
        Hierarchy<SCTConcept> hierarchy;

        TANFactory factory;

        if (useStatedRelationships) {
            SCTReleaseWithStated statedRelease = (SCTReleaseWithStated) release;
            
            hierarchy = statedRelease.getStatedHierarchy().getSubhierarchyRootedAt(roots);
            factory = new SCTStatedTANFactory(statedRelease);
        } else {
            hierarchy = release.getConceptHierarchy().getSubhierarchyRootedAt(roots);
            factory = new TANFactory(release);
        }

        return new TribalAbstractionNetworkGenerator().deriveTANFromMultiRootedHierarchy(
                hierarchy,
                factory);
    }
}
