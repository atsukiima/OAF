package edu.njit.cs.saboc.blu.sno.gui.gep.panels.tan.configuration;

import edu.njit.cs.saboc.blu.core.abn.tan.ClusterTribalAbstractionNetwork;
import edu.njit.cs.saboc.blu.core.gui.graphframe.AbNDisplayManager;
import edu.njit.cs.saboc.blu.sno.gui.abnselection.SCTAbNFrameManager;
import edu.njit.cs.saboc.blu.sno.sctdatasource.SCTRelease;

/**
 *
 * @author Chris O
 */
public class SCTTANConfigurationFactory {
    
    public SCTTANConfiguration createConfiguration(
            SCTRelease release,
            ClusterTribalAbstractionNetwork tan, 
            AbNDisplayManager displayListener, 
            SCTAbNFrameManager frameManager,
            boolean showingBandTAN) {

        SCTTANConfiguration pareaTaxonomyConfiguration = new SCTTANConfiguration(release, tan);
        
        pareaTaxonomyConfiguration.setUIConfiguration(
                new SCTTANUIConfiguration(
                        pareaTaxonomyConfiguration, 
                        displayListener, 
                        frameManager,
                        showingBandTAN));
        
        pareaTaxonomyConfiguration.setTextConfiguration(new SCTTANTextConfiguration(tan));

        return pareaTaxonomyConfiguration;
    }
}
