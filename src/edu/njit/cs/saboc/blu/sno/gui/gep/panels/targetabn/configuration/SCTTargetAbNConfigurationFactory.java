package edu.njit.cs.saboc.blu.sno.gui.gep.panels.targetabn.configuration;

import edu.njit.cs.saboc.blu.core.abn.targetbased.TargetAbstractionNetwork;
import edu.njit.cs.saboc.blu.core.gui.graphframe.AbNDisplayManager;
import edu.njit.cs.saboc.blu.sno.gui.abnselection.SCTAbNFrameManager;

/**
 *
 * @author Chris O
 */
public class SCTTargetAbNConfigurationFactory {
    
    public SCTTargetAbNConfiguration createConfiguration(
            TargetAbstractionNetwork targetAbN, 
            AbNDisplayManager displayListener,
            SCTAbNFrameManager frameManager) {
        
        SCTTargetAbNConfiguration targetAbNConfiguration = new SCTTargetAbNConfiguration(targetAbN);
        targetAbNConfiguration.setUIConfiguration(new SCTTargetAbNUIConfiguration(targetAbNConfiguration, displayListener, frameManager));
        targetAbNConfiguration.setTextConfiguration(new SCTTargetAbNTextConfiguration(targetAbN));
        
        return targetAbNConfiguration;
    }
}
