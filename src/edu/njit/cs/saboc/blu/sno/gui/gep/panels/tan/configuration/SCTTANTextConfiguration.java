package edu.njit.cs.saboc.blu.sno.gui.gep.panels.tan.configuration;

import edu.njit.cs.saboc.blu.core.abn.tan.ClusterTribalAbstractionNetwork;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.tan.TANTextConfiguration;
import edu.njit.cs.saboc.blu.sno.localdatasource.concept.SCTEntityNameUtils;

/**
 *
 * @author Chris O
 */
public class SCTTANTextConfiguration extends TANTextConfiguration {

    public SCTTANTextConfiguration(ClusterTribalAbstractionNetwork tan) {
        super(tan);
    }

    @Override
    public String getAbNName() {
        return "[TAN_NAME]";
    }

    @Override
    public String getConceptTypeName(boolean plural) {
        return SCTEntityNameUtils.getConceptTypeName(plural);
    }

    @Override
    public String getPropertyTypeName(boolean plural) {
        return SCTEntityNameUtils.getPropertyTypeName(plural);
    }

    @Override
    public String getParentConceptTypeName(boolean plural) {
        return SCTEntityNameUtils.getParentConceptTypeName(plural);
    }

    @Override
    public String getChildConceptTypeName(boolean plural) {
        return SCTEntityNameUtils.getChildConceptTypeName(plural);
    }
}
