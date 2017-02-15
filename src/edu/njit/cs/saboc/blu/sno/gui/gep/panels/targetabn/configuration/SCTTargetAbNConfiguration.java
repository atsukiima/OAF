package edu.njit.cs.saboc.blu.sno.gui.gep.panels.targetabn.configuration;

import edu.njit.cs.saboc.blu.core.abn.targetbased.TargetAbstractionNetwork;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.targetbased.configuration.TargetAbNConfiguration;

/**
 *
 * @author Chris O
 */
public class SCTTargetAbNConfiguration extends TargetAbNConfiguration {

    public SCTTargetAbNConfiguration(TargetAbstractionNetwork targetAbN) {
        super(targetAbN);
    }
  
    public void setUIConfiguration(SCTTargetAbNUIConfiguration config) {
        super.setUIConfiguration(config);
    }
    
    public void setTextConfiguration(SCTTargetAbNTextConfiguration config) {
        super.setTextConfiguration(config);
    }
    
    public SCTTargetAbNUIConfiguration getUIConfiguration() {
        return (SCTTargetAbNUIConfiguration)super.getUIConfiguration();
    }
    
    public SCTTargetAbNTextConfiguration getTextConfiguration() {
        return (SCTTargetAbNTextConfiguration)super.getTextConfiguration();
    }
}
