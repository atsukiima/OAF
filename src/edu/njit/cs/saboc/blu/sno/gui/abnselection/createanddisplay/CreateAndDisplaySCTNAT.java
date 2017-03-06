package edu.njit.cs.saboc.blu.sno.gui.abnselection.createanddisplay;

import edu.njit.cs.saboc.blu.core.gui.dialogs.CreateAndDisplayDialog;
import edu.njit.cs.saboc.blu.sno.gui.abnselection.SCTAbNFrameManager;
import edu.njit.cs.saboc.blu.sno.nat.SCTConceptBrowserDataSource;
import edu.njit.cs.saboc.blu.sno.sctdatasource.SCTRelease;

/**
 *
 * @author Chris O
 */
public class CreateAndDisplaySCTNAT extends CreateAndDisplayDialog<SCTConceptBrowserDataSource> {
    
    private final SCTRelease release;
    
    public CreateAndDisplaySCTNAT(SCTAbNFrameManager displayManager, SCTRelease release) {
        super("Preparing Neighborhood Auditing Tool (NAT) for SNOMED CT", displayManager);
        
        this.release = release;
    }

    @Override
    public SCTAbNFrameManager getDisplayFrameListener() {
        return (SCTAbNFrameManager)super.getDisplayFrameListener();
    }

    @Override
    protected void display(SCTConceptBrowserDataSource dataSource) {
        getDisplayFrameListener().displayConceptBrowserFrame(dataSource);
    }

    @Override
    protected SCTConceptBrowserDataSource create() {
        return new SCTConceptBrowserDataSource(release);
    }
}
