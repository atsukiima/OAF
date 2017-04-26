package edu.njit.cs.saboc.blu.sno.gui.gep.panels.tan.configuration;

import edu.njit.cs.saboc.blu.core.abn.tan.ClusterTribalAbstractionNetwork;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.tan.configuration.TANTextConfiguration;
import edu.njit.cs.saboc.blu.sno.localdatasource.concept.SCTEntityNameConfiguration;

/**
 *
 * @author Chris O
 */
public class SCTTANTextConfiguration extends TANTextConfiguration {

    public SCTTANTextConfiguration(ClusterTribalAbstractionNetwork tan) {
        super(new SCTEntityNameConfiguration(), tan);
    }
}
