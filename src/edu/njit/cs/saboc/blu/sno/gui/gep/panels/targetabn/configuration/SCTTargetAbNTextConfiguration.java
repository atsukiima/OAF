package edu.njit.cs.saboc.blu.sno.gui.gep.panels.targetabn.configuration;

import edu.njit.cs.saboc.blu.core.abn.targetbased.TargetAbstractionNetwork;
import edu.njit.cs.saboc.blu.core.abn.targetbased.TargetGroup;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.targetbased.configuration.TargetAbNTextConfiguration;
import edu.njit.cs.saboc.blu.sno.localdatasource.concept.SCTEntityNameUtils;

/**
 *
 * @author Chris O
 */
public class SCTTargetAbNTextConfiguration extends TargetAbNTextConfiguration {

    public SCTTargetAbNTextConfiguration(TargetAbstractionNetwork targetAbN) {
        super(targetAbN);
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
    
    @Override
    public String getNodeHelpDescription(TargetGroup group) {
        return "*** SCT TARGET ABN TARGET GROUP DESCRIPTION ***";
    }
}
