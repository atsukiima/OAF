package edu.njit.cs.saboc.blu.sno.gui.gep.panels.targetabn.configuration;

import edu.njit.cs.saboc.blu.core.abn.targetbased.TargetAbstractionNetwork;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.targetbased.configuration.TargetAbNTextConfiguration;
import edu.njit.cs.saboc.blu.sno.localdatasource.concept.SCTEntityNameConfiguration;

/**
 *
 * @author Chris O
 */
public class SCTTargetAbNTextConfiguration extends TargetAbNTextConfiguration {

    public SCTTargetAbNTextConfiguration(TargetAbstractionNetwork targetAbN) {
        super(new SCTEntityNameConfiguration(), targetAbN);
    }
}
