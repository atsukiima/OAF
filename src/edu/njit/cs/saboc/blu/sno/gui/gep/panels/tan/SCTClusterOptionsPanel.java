package edu.njit.cs.saboc.blu.sno.gui.gep.panels.tan;

import edu.njit.cs.saboc.blu.core.abn.node.Node;
import edu.njit.cs.saboc.blu.core.abn.tan.Cluster;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.AbstractNodeOptionsPanel;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.NodeDashboardPanel;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.optionbuttons.PopoutNodeDetailsButton;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.optionbuttons.tan.SCTCreateTANFromClusterButton;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.optionbuttons.tan.SCTExportClusterButton;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.optionbuttons.SCTOpenBrowserButton;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.optionbuttons.tan.SCTCreateAncestorTANButton;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.optionbuttons.tan.SCTCreateRootTANButton;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.tan.configuration.SCTTANConfiguration;
import java.util.Optional;

/**
 *
 * @author Chris O
 */
public class SCTClusterOptionsPanel extends AbstractNodeOptionsPanel {
    
    private Optional<Cluster> selectedCluster = Optional.empty();
    
    private final SCTTANConfiguration config;

    //private final SCTOpenBrowserButton btnNAT;
    
    private final SCTCreateRootTANButton rootTANBtn;
    
    private final SCTCreateAncestorTANButton ancestorTANBtn;
    
    private final SCTCreateTANFromClusterButton tanBtn;
    
    private final SCTExportClusterButton exportBtn;
    
    private final PopoutNodeDetailsButton popoutBtn;
        
    public SCTClusterOptionsPanel(SCTTANConfiguration config) {
        this.config = config;
       
//        btnNAT = new SCTOpenBrowserButton(config.getDataConfiguration().getTribalAbstractionNetwork().getDataSource(),
//            "cluster", config.getUIConfiguration().getDisplayFrameListener());
//        
//        super.addOptionButton(btnNAT);
        
        rootTANBtn = new SCTCreateRootTANButton(config);
        
        super.addOptionButton(rootTANBtn);
        
        ancestorTANBtn = new SCTCreateAncestorTANButton(config);
        
        super.addOptionButton(ancestorTANBtn);
        
        tanBtn = new SCTCreateTANFromClusterButton(config);
        
        super.addOptionButton(tanBtn);
        
        exportBtn = new SCTExportClusterButton();
        
        super.addOptionButton(exportBtn);
        
        popoutBtn = new PopoutNodeDetailsButton("cluster", () -> {
            NodeDashboardPanel anp = config.getUIConfiguration().createGroupDetailsPanel();
            anp.setContents(selectedCluster.get());

            return anp;
        });

        super.addOptionButton(popoutBtn);
    }
    
    @Override
    public void enableOptionsForNode(Node node) {
        Cluster cluster = (Cluster)node;
        
        if (cluster.getConceptCount() > 1) {
            if (cluster.getHierarchy().getChildren(cluster.getRoot()).size() > 1) {
                tanBtn.setEnabled(true);
            } else {
                tanBtn.setEnabled(false);
            }
        } else {
            tanBtn.setEnabled(false);
        }
        
        if(config.getTribalAbstractionNetwork().getClusterHierarchy().getChildren(cluster).isEmpty()) {
            rootTANBtn.setEnabled(false);
        } else {
            rootTANBtn.setEnabled(true);
        }
        
        if(config.getTribalAbstractionNetwork().getClusterHierarchy().getParents(cluster).isEmpty()) {
            ancestorTANBtn.setEnabled(false);
        } else {
            ancestorTANBtn.setEnabled(true);
        }
    }

    @Override
    public void setContents(Node node) {
        Cluster cluster = (Cluster)node;
        
        selectedCluster = Optional.of(cluster);
        
        //btnNAT.setCurrentRootConcept(cluster.getRoot());
        
        rootTANBtn.setCurrentCluster(cluster);
        
        ancestorTANBtn.setCurrentCluster(cluster);
        
        tanBtn.setCurrentCluster(cluster);
        
        exportBtn.setCurrentCluster(cluster);

        this.enableOptionsForNode(cluster);
    }
    
    public void clearContents() {
        selectedCluster = Optional.empty();

        //btnNAT.setCurrentRootConcept(null);
        rootTANBtn.setCurrentCluster(null);
        ancestorTANBtn.setCurrentCluster(null);
        tanBtn.setCurrentCluster(null);
        exportBtn.setCurrentCluster(null);
    }
}
