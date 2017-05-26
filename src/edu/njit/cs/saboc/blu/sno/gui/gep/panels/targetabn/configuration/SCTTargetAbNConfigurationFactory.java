package edu.njit.cs.saboc.blu.sno.gui.gep.panels.targetabn.configuration;

import edu.njit.cs.saboc.blu.core.abn.targetbased.TargetAbstractionNetwork;
import edu.njit.cs.saboc.blu.core.gui.graphframe.AbNDisplayManager;
import edu.njit.cs.saboc.blu.sno.gui.abnselection.SCTAbNFrameManager;
import edu.njit.cs.saboc.blu.sno.sctdatasource.SCTRelease;

/**
 *
 * @author Chris O
 */
public class SCTTargetAbNConfigurationFactory {
    
    public SCTTargetAbNConfiguration createConfiguration(
            SCTRelease release,
            TargetAbstractionNetwork targetAbN, 
            AbNDisplayManager displayListener,
            SCTAbNFrameManager frameManager) {
        
        SCTTargetAbNConfiguration targetAbNConfiguration = new SCTTargetAbNConfiguration(release, targetAbN);
        targetAbNConfiguration.setUIConfiguration(new SCTTargetAbNUIConfiguration(targetAbNConfiguration, displayListener, frameManager));
        targetAbNConfiguration.setTextConfiguration(new SCTTargetAbNTextConfiguration(targetAbN));
        
        return targetAbNConfiguration;
    }
}
