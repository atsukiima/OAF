
package edu.njit.cs.saboc.blu.sno.gui.gep.panels.tan.configuration;

import edu.njit.cs.saboc.blu.core.abn.tan.ClusterTribalAbstractionNetwork;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.tan.configuration.TANConfiguration;
import edu.njit.cs.saboc.blu.sno.sctdatasource.SCTRelease;

/**
 *
 * @author Chris O
 */
public class SCTTANConfiguration extends TANConfiguration {
    
    private final SCTRelease release;
    
    public SCTTANConfiguration(SCTRelease release, ClusterTribalAbstractionNetwork tan) {
        super(tan);
        
        this.release = release;
    }
    
    public SCTRelease getRelease() {
        return release;
    }

    public void setTextConfiguration(SCTTANTextConfiguration textConfiguration) {
        super.setTextConfiguration(textConfiguration);
    }

    public void setUIConfiguration(SCTTANUIConfiguration uiConfiguration) {
        super.setUIConfiguration(uiConfiguration); 
    }
    
    public SCTTANTextConfiguration getTextConfiguration() {
        return (SCTTANTextConfiguration)super.getTextConfiguration();
    }
    
    public SCTTANUIConfiguration getUIConfiguration() {
        return (SCTTANUIConfiguration)super.getUIConfiguration();
    }
}
