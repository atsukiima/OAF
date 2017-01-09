package edu.njit.cs.saboc.blu.sno.gui.gep.panels.disjointtan.configuration;

import edu.njit.cs.saboc.blu.core.abn.disjoint.DisjointAbstractionNetwork;
import edu.njit.cs.saboc.blu.core.abn.disjoint.DisjointNode;
import edu.njit.cs.saboc.blu.core.abn.tan.Cluster;
import edu.njit.cs.saboc.blu.core.abn.tan.ClusterTribalAbstractionNetwork;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.disjointabn.tan.DisjointTANTextConfiguration;
import edu.njit.cs.saboc.blu.sno.localdatasource.concept.SCTEntityNameUtils;

/**
 *
 * @author Chris O
 */
public class SCTDisjointTANTextConfiguration extends DisjointTANTextConfiguration {

    public SCTDisjointTANTextConfiguration(
            DisjointAbstractionNetwork<DisjointNode<Cluster>, ClusterTribalAbstractionNetwork<Cluster>, Cluster> disjointTAN) {
        
        super(disjointTAN);
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
        return SCTEntityNameUtils.getConceptTypeName(plural);
    }
}
