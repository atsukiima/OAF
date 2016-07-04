
package edu.njit.cs.saboc.blu.sno.gui.gep.panels.tan.configuration;

import edu.njit.cs.saboc.blu.core.abn.tan.ClusterTribalAbstractionNetwork;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.tan.TANConfiguration;

/**
 *
 * @author Chris O
 */
public class SCTTANConfiguration extends TANConfiguration {
    
    public SCTTANConfiguration(ClusterTribalAbstractionNetwork tan) {
        super(tan);
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
