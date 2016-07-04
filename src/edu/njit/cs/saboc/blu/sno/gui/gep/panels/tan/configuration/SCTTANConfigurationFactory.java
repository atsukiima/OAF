package edu.njit.cs.saboc.blu.sno.gui.gep.panels.tan.configuration;

import edu.njit.cs.saboc.blu.core.abn.tan.TribalAbstractionNetwork;
import edu.njit.cs.saboc.blu.sno.gui.abnselection.SCTDisplayFrameListener;

/**
 *
 * @author Chris O
 */
public class SCTTANConfigurationFactory {
    public SCTTANConfiguration createConfiguration(TribalAbstractionNetwork tan, SCTDisplayFrameListener displayListener) {

        SCTTANConfiguration pareaTaxonomyConfiguration = new SCTTANConfiguration(tan);
        pareaTaxonomyConfiguration.setUIConfiguration(new SCTTANUIConfiguration(pareaTaxonomyConfiguration, displayListener));
        pareaTaxonomyConfiguration.setTextConfiguration(new SCTTANTextConfiguration(tan));

        return pareaTaxonomyConfiguration;
    }
}
