package edu.njit.cs.saboc.blu.sno.gui.abnselection.createanddisplay;

import edu.njit.cs.saboc.blu.core.gui.dialogs.CreateAndDisplayDialog;
import edu.njit.cs.saboc.blu.sno.gui.abnselection.SCTAbNFrameManager;
import edu.njit.cs.saboc.blu.sno.localdatasource.concept.SCTConcept;
import edu.njit.cs.saboc.blu.sno.nat.SCTConceptBrowserDataSource;
import edu.njit.cs.saboc.blu.sno.sctdatasource.SCTRelease;
import java.util.Optional;

/**
 * Creates and displays a NAT frame for SNOMED CT
 * 
 * @author Chris O
 */
public class CreateAndDisplaySCTNAT extends CreateAndDisplayDialog<SCTConceptBrowserDataSource> {
    
    private final SCTRelease release;
    
    private Optional<SCTConcept> focusConcept = Optional.empty();
    
    public CreateAndDisplaySCTNAT(SCTAbNFrameManager displayManager, SCTRelease release) {
        super("Preparing Neighborhood Auditing Tool (NAT) for SNOMED CT", displayManager);
        
        this.release = release;
    }
    
    public CreateAndDisplaySCTNAT(SCTAbNFrameManager displayManager, SCTRelease release, SCTConcept focusConcept) {
        this(displayManager, release);
        
        this.focusConcept = Optional.of(focusConcept);
    }

    @Override
    public SCTAbNFrameManager getDisplayFrameListener() {
        return (SCTAbNFrameManager)super.getDisplayFrameListener();
    }

    @Override
    protected void display(SCTConceptBrowserDataSource dataSource) {
        getDisplayFrameListener().displayConceptBrowserFrame(dataSource, focusConcept);
    }

    @Override
    protected SCTConceptBrowserDataSource create() {
        return release.getConceptBrowserDataSource();
    }
}
