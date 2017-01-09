
package edu.njit.cs.saboc.blu.sno.gui.gep.panels.disjointtan.configuration;

import edu.njit.cs.saboc.blu.core.abn.disjoint.DisjointAbstractionNetwork;
import edu.njit.cs.saboc.blu.core.abn.disjoint.DisjointNode;
import edu.njit.cs.saboc.blu.core.abn.tan.Cluster;
import edu.njit.cs.saboc.blu.core.abn.tan.ClusterTribalAbstractionNetwork;
import edu.njit.cs.saboc.blu.core.gui.graphframe.AbNDisplayManager;

/**
 *
 * @author Chris O
 */
public class SCTDisjointTANConfigurationFactory {
    
    public SCTDisjointTANConfiguration createConfiguration(
            DisjointAbstractionNetwork<DisjointNode<Cluster>, ClusterTribalAbstractionNetwork<Cluster>, Cluster> disjointTAN, 
            AbNDisplayManager displayListener) {
        
        SCTDisjointTANConfiguration disjointConfiguration = new SCTDisjointTANConfiguration(disjointTAN);
        disjointConfiguration.setUIConfiguration(new SCTDisjointTANUIConfiguration(disjointConfiguration, displayListener));
        disjointConfiguration.setTextConfiguration(new SCTDisjointTANTextConfiguration(disjointTAN));
        
        return disjointConfiguration;
    }
}
