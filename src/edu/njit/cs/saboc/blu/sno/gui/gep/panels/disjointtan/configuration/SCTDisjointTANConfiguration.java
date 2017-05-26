package edu.njit.cs.saboc.blu.sno.gui.gep.panels.disjointtan.configuration;

import edu.njit.cs.saboc.blu.core.abn.disjoint.DisjointAbstractionNetwork;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.disjointabn.tan.DisjointTANConfiguration;
import edu.njit.cs.saboc.blu.sno.sctdatasource.SCTRelease;

/**
 *
 * @author Chris O
 */
public class SCTDisjointTANConfiguration extends DisjointTANConfiguration {
    
    private final SCTRelease release;
    
    public SCTDisjointTANConfiguration(SCTRelease release, DisjointAbstractionNetwork disjointAbN) {
        super(disjointAbN);
        
        this.release = release;
    }
    
    public SCTRelease getRelease() {
        return release;
    }
    
    public void setUIConfiguration(SCTDisjointTANUIConfiguration uiConfiguation) {
        super.setUIConfiguration(uiConfiguation);
    }
    
    public void setTextConfiguration(SCTDisjointTANTextConfiguration uiConfiguation) {
        super.setTextConfiguration(uiConfiguation);
    }
    
    public SCTDisjointTANUIConfiguration getUIConfiguration() {
        return (SCTDisjointTANUIConfiguration)super.getUIConfiguration();
    }
    
    public SCTDisjointTANTextConfiguration getTextConfiguration() {
        return (SCTDisjointTANTextConfiguration)super.getTextConfiguration();
    }
}
