package edu.njit.cs.saboc.blu.sno.gui.gep.panels.tan.cluster;

import SnomedShared.Concept;
import edu.njit.cs.saboc.blu.core.gui.dialogs.concepthierarchy.HierarchyPanelClickListener;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.AbNNodeInformationPanel;
import edu.njit.cs.saboc.blu.sno.abn.tan.local.SCTCluster;
import edu.njit.cs.saboc.blu.sno.gui.dialogs.panels.concepthierarchy.SCTConceptHierarchyViewPanel;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.SCTTANConfiguration;
import java.awt.BorderLayout;
import javax.swing.JScrollPane;

/**
 *
 * @author Chris O
 */
public class SCTClusterConceptHierarchyPanel extends AbNNodeInformationPanel<SCTCluster> {

    private final SCTConceptHierarchyViewPanel hierarchyPanel;
    
    public SCTClusterConceptHierarchyPanel(SCTTANConfiguration config) {
        
        this.hierarchyPanel = new SCTConceptHierarchyViewPanel(
                config.getTribalAbstractionNetwork(),
                "Cluster",
                new HierarchyPanelClickListener<Concept>() {
                    public void conceptDoubleClicked(Concept c) {
                       
                    }
                });
        
        this.setLayout(new BorderLayout());
        
        this.add(new JScrollPane(hierarchyPanel), BorderLayout.CENTER);
    }
    
    @Override
    public void setContents(SCTCluster group) {
        hierarchyPanel.setGroup(group);
    }

    @Override
    public void clearContents() {
        hierarchyPanel.setGroup(null);
    }
}
