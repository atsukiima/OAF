package edu.njit.cs.saboc.blu.sno.gui.gep.panels.disjointtan.configuration;

import edu.njit.cs.saboc.blu.core.abn.disjoint.DisjointAbstractionNetwork;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.disjointabn.tan.DisjointTANConfiguration;

/**
 *
 * @author Chris O
 */
public class SCTDisjointTANConfiguration extends DisjointTANConfiguration {
    
    public SCTDisjointTANConfiguration(DisjointAbstractionNetwork disjointAbN) {
        super(disjointAbN);
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
