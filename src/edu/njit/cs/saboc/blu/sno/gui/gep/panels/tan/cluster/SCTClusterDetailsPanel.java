package edu.njit.cs.saboc.blu.sno.gui.gep.panels.tan.cluster;

import SnomedShared.Concept;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.tan.cluster.ClusterDetailsPanel;
import edu.njit.cs.saboc.blu.sno.abn.tan.local.SCTCluster;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.SCTConceptList;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.tan.configuration.SCTTANConfiguration;

/**
 *
 * @author Chris O
 */
public class SCTClusterDetailsPanel extends ClusterDetailsPanel<Concept, SCTCluster> {
    
    public SCTClusterDetailsPanel(SCTTANConfiguration config) {
        
        super(
              new SCTClusterOptionsPanel(config),
              new SCTConceptList(),
              config);
    }
}
