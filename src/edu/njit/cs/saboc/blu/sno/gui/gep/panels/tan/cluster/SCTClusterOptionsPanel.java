package edu.njit.cs.saboc.blu.sno.gui.gep.panels.tan.cluster;

import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.AbstractNodeOptionsPanel;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.AbstractNodePanel;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.optionbuttons.PopoutNodeDetailsButton;
import edu.njit.cs.saboc.blu.sno.abn.tan.local.SCTCluster;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.optionbuttons.tan.SCTCreateTANFromClusterButton;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.optionbuttons.tan.SCTExportClusterButton;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.optionbuttons.SCTOpenBrowserButton;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.tan.configuration.SCTTANConfiguration;
import java.util.Optional;

/**
 *
 * @author Chris O
 */
public class SCTClusterOptionsPanel extends AbstractNodeOptionsPanel<SCTCluster> {
    
    private Optional<SCTCluster> selectedCluster = Optional.empty();
    
    private final SCTTANConfiguration config;

    private final SCTOpenBrowserButton btnNAT;
    
    private final SCTCreateTANFromClusterButton tanBtn;
    
    private final SCTExportClusterButton exportBtn;
    
    private final PopoutNodeDetailsButton popoutBtn;
        
    public SCTClusterOptionsPanel(SCTTANConfiguration config) {
        this.config = config;
       
        btnNAT = new SCTOpenBrowserButton(config.getDataConfiguration().getTribalAbstractionNetwork().getDataSource(),
            "cluster", config.getUIConfiguration().getDisplayFrameListener());
        
        super.addOptionButton(btnNAT);
        
        tanBtn = new SCTCreateTANFromClusterButton(config);
        
        super.addOptionButton(tanBtn);
        
        exportBtn = new SCTExportClusterButton();
        
        super.addOptionButton(exportBtn);
        
        popoutBtn = new PopoutNodeDetailsButton("cluster") {

            @Override
            public AbstractNodePanel getCurrentDetailsPanel() {
                AbstractNodePanel anp = config.getUIConfiguration().createGroupDetailsPanel();
                anp.setContents(selectedCluster.get());
                
                return anp;
            }
        };
        
        super.addOptionButton(popoutBtn);
    }
    
    @Override
    public void enableOptionsForGroup(SCTCluster cluster) {
        if (cluster.getConceptCount() > 1) {
            if (cluster.getConceptHierarchy().getChildren(cluster.getRoot()).size() > 1) {
                tanBtn.setEnabled(true);
            } else {
                tanBtn.setEnabled(false);
            }
        } else {
            tanBtn.setEnabled(false);
        }
    }

    @Override
    public void setContents(SCTCluster cluster) {
        selectedCluster = Optional.of(cluster);
        
        btnNAT.setCurrentRootConcept(cluster.getRoot());
        
        tanBtn.setCurrentCluster(cluster);
        
        exportBtn.setCurrentCluster(cluster);

        this.enableOptionsForGroup(cluster);
    }
    
    public void clearContents() {
        selectedCluster = Optional.empty();

        btnNAT.setCurrentRootConcept(null);
        tanBtn.setCurrentCluster(null);
        exportBtn.setCurrentCluster(null);
    }
}
