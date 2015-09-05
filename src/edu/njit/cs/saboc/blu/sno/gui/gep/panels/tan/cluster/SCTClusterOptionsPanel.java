package edu.njit.cs.saboc.blu.sno.gui.gep.panels.tan.cluster;

import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.AbstractNodeOptionsPanel;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.AbstractNodePanel;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.optionbuttons.PopoutNodeDetailsButton;
import edu.njit.cs.saboc.blu.sno.abn.tan.local.SCTCluster;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.SCTTANConfiguration;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.optionbuttons.SCTCreateTANFromClusterButton;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.optionbuttons.SCTOpenBrowserButton;
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
    
    private final PopoutNodeDetailsButton popoutBtn;
        
    public SCTClusterOptionsPanel(SCTTANConfiguration config) {
        this.config = config;
       
        btnNAT = new SCTOpenBrowserButton(config.getTribalAbstractionNetwork().getDataSource(),
            "cluster", config.getDisplayListener());
        
        super.addOptionButton(btnNAT);
        
        tanBtn = new SCTCreateTANFromClusterButton(config);
        
        super.addOptionButton(tanBtn);
        
        popoutBtn = new PopoutNodeDetailsButton("cluster") {

            @Override
            public AbstractNodePanel getCurrentDetailsPanel() {
                AbstractNodePanel anp = config.getGEPConfiguration().createGroupDetailsPanel();
                anp.setContents(selectedCluster.get());
                
                return anp;
            }
        };
        
        super.addOptionButton(popoutBtn);
    }
    
    @Override
    public void enableOptionsForGroup(SCTCluster parea) {

    }

    @Override
    public void setContents(SCTCluster cluster) {
        selectedCluster = Optional.of(cluster);
        
        btnNAT.setCurrentRootConcept(cluster.getRoot());
        
        tanBtn.setCurrentCluster(cluster);

        
        this.enableOptionsForGroup(cluster);
    }
    
    public void clearContents() {
        selectedCluster = Optional.empty();

        btnNAT.setCurrentRootConcept(null);
        tanBtn.setCurrentCluster(null);
    }
}
