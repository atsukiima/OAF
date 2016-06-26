package edu.njit.cs.saboc.blu.sno.gui.gep.panels.tan.configuration;

import SnomedShared.Concept;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.tan.TANTextConfiguration;
import edu.njit.cs.saboc.blu.sno.abn.tan.local.SCTBand;
import edu.njit.cs.saboc.blu.sno.abn.tan.local.SCTCluster;
import edu.njit.cs.saboc.blu.sno.abn.tan.local.SCTTribalAbstractionNetwork;

/**
 *
 * @author Chris O
 */
public class SCTTANTextConfiguration extends TANTextConfiguration<SCTTribalAbstractionNetwork, SCTBand, SCTCluster, Concept> {


    public SCTTANTextConfiguration(SCTTribalAbstractionNetwork tan) {
        super(tan);
    }
    
    @Override
    public String getConceptTypeName(boolean plural) {
        if (plural) {
            return "Concepts";
        } else {
            return "Concept";
        }
    }

    @Override
    public String getConceptName(Concept concept) {
        return concept.getName();
    }
    
    @Override
    public String getConceptUniqueIdentifier(Concept concept) {
        return Long.toString(concept.getId());
    }
    
    @Override
    public String getGroupRootUniqueIdentifier(SCTCluster group) {
        return getConceptUniqueIdentifier(group.getRoot());
    }
}
