
package edu.njit.cs.saboc.blu.sno.gui.gep.panels.disjointtan.configuration;

import edu.njit.cs.saboc.blu.core.abn.disjoint.DisjointAbstractionNetwork;
import edu.njit.cs.saboc.blu.core.abn.disjoint.DisjointNode;
import edu.njit.cs.saboc.blu.core.abn.tan.Cluster;
import edu.njit.cs.saboc.blu.core.abn.tan.ClusterTribalAbstractionNetwork;
import edu.njit.cs.saboc.blu.core.gui.graphframe.AbNDisplayManager;
import edu.njit.cs.saboc.blu.sno.gui.abnselection.SCTAbNFrameManager;
import edu.njit.cs.saboc.blu.sno.sctdatasource.SCTRelease;

/**
 *
 * @author Chris O
 */
public class SCTDisjointTANConfigurationFactory {
    
    public SCTDisjointTANConfiguration createConfiguration(
            SCTRelease release,
            DisjointAbstractionNetwork<DisjointNode<Cluster>, ClusterTribalAbstractionNetwork<Cluster>, Cluster> disjointTAN, 
            AbNDisplayManager displayListener,
            SCTAbNFrameManager frameManager) {
        
        SCTDisjointTANConfiguration disjointConfiguration = new SCTDisjointTANConfiguration(release, disjointTAN);
        disjointConfiguration.setUIConfiguration(new SCTDisjointTANUIConfiguration(disjointConfiguration, displayListener, frameManager));
        disjointConfiguration.setTextConfiguration(new SCTDisjointTANTextConfiguration(disjointTAN));
        
        return disjointConfiguration;
    }
}
