package edu.njit.cs.saboc.blu.sno.gui.gep.panels.tan.configuration;

import edu.njit.cs.saboc.blu.sno.abn.tan.local.SCTTribalAbstractionNetwork;
import edu.njit.cs.saboc.blu.sno.gui.abnselection.SCTDisplayFrameListener;

/**
 *
 * @author Chris O
 */
public class SCTTANConfigurationFactory {
    public SCTTANConfiguration createConfiguration(SCTTribalAbstractionNetwork tan, SCTDisplayFrameListener displayListener) {

        SCTTANConfiguration pareaTaxonomyConfiguration = new SCTTANConfiguration();
        pareaTaxonomyConfiguration.setDataConfiguration(new SCTTANDataConfiguration(tan));
        pareaTaxonomyConfiguration.setUIConfiguration(new SCTTANUIConfiguration(pareaTaxonomyConfiguration, displayListener));
        pareaTaxonomyConfiguration.setTextConfiguration(new SCTTANTextConfiguration(tan));

        return pareaTaxonomyConfiguration;
    }
}
