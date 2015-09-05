package edu.njit.cs.saboc.blu.sno.gui.gep.panels.tan.cluster;

import SnomedShared.Concept;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.AbstractGroupPanel;
import edu.njit.cs.saboc.blu.sno.abn.tan.local.SCTCluster;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.SCTTANConfiguration;

/**
 *
 * @author Chris O
 */
public class SCTClusterPanel extends AbstractGroupPanel<SCTCluster, Concept> {

    private final SCTClusterConceptHierarchyPanel conceptHierarchyPanel;
    
    public SCTClusterPanel(SCTTANConfiguration config) {
        super(new SCTClusterDetailsPanel(config), 
                new SCTClusterHierarchyPanel(config), 
                config);
        
         conceptHierarchyPanel = new SCTClusterConceptHierarchyPanel(config);
        
        super.addGroupDetailsTab(conceptHierarchyPanel, "Cluster Concept Hierarchy");
    }
}
