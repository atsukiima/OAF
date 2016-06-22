package edu.njit.cs.saboc.blu.sno.gui.gep.panels.tan.cluster;

import SnomedShared.Concept;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.SinglyRootedNodePanel;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.tan.cluster.ClusterHierarchyPanel;
import edu.njit.cs.saboc.blu.sno.abn.tan.local.SCTCluster;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.tan.configuration.SCTTANConfiguration;

/**
 *
 * @author Chris O
 */
public class SCTClusterPanel extends SinglyRootedNodePanel<SCTCluster, Concept, SCTTANConfiguration> {

    private final SCTClusterConceptHierarchyPanel conceptHierarchyPanel;
    
    public SCTClusterPanel(SCTTANConfiguration config) {
        super(new SCTClusterDetailsPanel(config), 
                new ClusterHierarchyPanel<Concept, SCTCluster, SCTTANConfiguration>(config), 
                config);
        
         conceptHierarchyPanel = new SCTClusterConceptHierarchyPanel(config);
        
        super.addGroupDetailsTab(conceptHierarchyPanel, "Cluster Concept Hierarchy");
    }
}
