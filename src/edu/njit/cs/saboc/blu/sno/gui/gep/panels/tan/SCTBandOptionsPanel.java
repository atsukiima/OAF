package edu.njit.cs.saboc.blu.sno.gui.gep.panels.tan;

import edu.njit.cs.saboc.blu.core.abn.node.Node;
import edu.njit.cs.saboc.blu.core.abn.tan.Band;
import edu.njit.cs.saboc.blu.core.abn.tan.Cluster;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.AbstractNodeOptionsPanel;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.NodeDashboardPanel;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.optionbuttons.PopoutNodeDetailsButton;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.optionbuttons.tan.SCTCreateTANFromBandButton;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.optionbuttons.tan.SCTExportBandButton;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.tan.configuration.SCTTANConfiguration;
import java.util.Optional;
import java.util.Set;

/**
 *
 * @author Chris O
 */
public class SCTBandOptionsPanel extends AbstractNodeOptionsPanel {
    
    private Optional<Band> selectedArea = Optional.empty();
    
    private final SCTTANConfiguration config;
    
    private final SCTCreateTANFromBandButton tanBtn;
    
    private final SCTExportBandButton exportBtn;
    
    private final PopoutNodeDetailsButton popoutBtn;

    public SCTBandOptionsPanel(SCTTANConfiguration config) {
        this.config = config;
        
        tanBtn = new SCTCreateTANFromBandButton(config);
        
        super.addOptionButton(tanBtn);
        
        exportBtn = new SCTExportBandButton(config);
        
        super.addOptionButton(exportBtn);
        
        popoutBtn = new PopoutNodeDetailsButton("band", () -> {
            NodeDashboardPanel anp = config.getUIConfiguration().createContainerDetailsPanel();
            anp.setContents(selectedArea.get());

            return anp;
        });

        super.addOptionButton(popoutBtn);
    }
    
    @Override
    public void enableOptionsForNode(Node node) {
        Band band = (Band)node;
        
        if(band.getClusters().size() > 1) {
            boolean tanPossible = false;
            
            Set<Cluster> clusters = band.getClusters();
            
            for(Cluster cluster : clusters) {
                if(cluster.getConceptCount() > 1) {
                    tanPossible = true;
                    break;
                }
            }
            
            tanBtn.setEnabled(tanPossible);
        } else {
            tanBtn.setEnabled(true);
        }
    }

    @Override
    public void setContents(Node node) {
        Band band = (Band)node;
        
        selectedArea = Optional.of(band);
        
        exportBtn.setCurrentBand(band);
        
        tanBtn.setCurrentBand(band);
        
        this.enableOptionsForNode(node);
    }
    
    public void clearContents() {
        selectedArea = Optional.empty();
        
        tanBtn.setCurrentBand(null);
        
        exportBtn.setCurrentBand(null);
    }    
}
