package edu.njit.cs.saboc.blu.sno.gui.gep.panels.listeners;

import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.listeners.EntitySelectionAdapter;
import edu.njit.cs.saboc.blu.core.ontology.Concept;
import edu.njit.cs.saboc.blu.sno.gui.abnselection.SCTAbNFrameManager;
import edu.njit.cs.saboc.blu.sno.gui.abnselection.createanddisplay.CreateAndDisplaySCTNAT;
import edu.njit.cs.saboc.blu.sno.localdatasource.concept.SCTConcept;
import edu.njit.cs.saboc.blu.sno.sctdatasource.SCTRelease;

/**
 *
 * @author Chris O
 */
public class SCTDisplayNATListener extends EntitySelectionAdapter<Concept> {
    
    private final SCTAbNFrameManager displayManager;
    private final SCTRelease release;

    public SCTDisplayNATListener(SCTAbNFrameManager displayManager, SCTRelease release) {
        this.displayManager = displayManager;
        this.release = release;
    }
    
    @Override
    public void entityDoubleClicked(Concept entity) {
        CreateAndDisplaySCTNAT showNAT = new CreateAndDisplaySCTNAT(displayManager, release, (SCTConcept)entity);
        
        showNAT.run();
    }
}
