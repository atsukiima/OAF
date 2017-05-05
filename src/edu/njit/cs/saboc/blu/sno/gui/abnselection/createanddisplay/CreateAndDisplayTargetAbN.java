package edu.njit.cs.saboc.blu.sno.gui.abnselection.createanddisplay;

import edu.njit.cs.saboc.blu.core.gui.dialogs.AbNCreateAndDisplayDialog;
import edu.njit.cs.saboc.blu.core.abn.targetbased.TargetAbstractionNetwork;
import edu.njit.cs.saboc.blu.core.abn.targetbased.TargetAbstractionNetworkGenerator;
import edu.njit.cs.saboc.blu.core.abn.targetbased.TargetGroup;
import edu.njit.cs.saboc.blu.core.datastructure.hierarchy.Hierarchy;
import edu.njit.cs.saboc.blu.core.ontology.Concept;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.SCTInheritableProperty;
import edu.njit.cs.saboc.blu.sno.abn.target.SCTTargetAbstractionNetworkFactory;
import edu.njit.cs.saboc.blu.sno.gui.abnselection.SCTAbNFrameManager;
import edu.njit.cs.saboc.blu.sno.localdatasource.concept.SCTConcept;
import edu.njit.cs.saboc.blu.sno.sctdatasource.SCTReleaseWithStated;

/**
 * Creates and displays a target AbN (i.e., ingredient abn) with the given arguments
 * for the given SNOMED CT release.
 * 
 * @author Chris O
 */
public class CreateAndDisplayTargetAbN extends AbNCreateAndDisplayDialog<TargetAbstractionNetwork> {
    
    private final Hierarchy<SCTConcept> sourceHierarchy;
    private final Hierarchy<SCTConcept> targetHierarchy;
    
    private final SCTInheritableProperty propertyType;
    
    private final SCTReleaseWithStated release;

    public CreateAndDisplayTargetAbN(
            String displayText, 
            SCTAbNFrameManager frameManager,
            Hierarchy<SCTConcept> sourceHierarchy, 
            SCTInheritableProperty propertyType, 
            Hierarchy<SCTConcept> targetHierarchy,
            SCTReleaseWithStated release) {
        
        super(displayText, frameManager);
        
        this.sourceHierarchy = sourceHierarchy;
        this.targetHierarchy = targetHierarchy;
        this.propertyType = propertyType;
        this.release = release;
    }

    @Override
    protected void display(TargetAbstractionNetwork abn) {
        super.getDisplayFrameListener().displayTargetAbstractionNetwork(abn);
    }

    @Override
    protected TargetAbstractionNetwork create() {
        SCTTargetAbstractionNetworkFactory factory = new SCTTargetAbstractionNetworkFactory(
                release, 
                sourceHierarchy, 
                propertyType, 
                targetHierarchy);
        
        TargetAbstractionNetworkGenerator generator = new TargetAbstractionNetworkGenerator();
        
        TargetAbstractionNetwork<TargetGroup> targetAbN = generator.deriveTargetAbstractionNetwork(factory, 
                (Hierarchy<Concept>)(Hierarchy<?>)sourceHierarchy, 
                propertyType, 
                (Hierarchy<Concept>)(Hierarchy<?>)targetHierarchy);

        return targetAbN;
    }
}
