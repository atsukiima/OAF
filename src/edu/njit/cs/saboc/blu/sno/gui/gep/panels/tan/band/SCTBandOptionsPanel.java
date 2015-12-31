package edu.njit.cs.saboc.blu.sno.gui.gep.panels.tan.band;

import SnomedShared.overlapping.CommonOverlapSet;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.AbstractNodeOptionsPanel;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.AbstractNodePanel;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.optionbuttons.PopoutNodeDetailsButton;
import edu.njit.cs.saboc.blu.sno.abn.tan.local.SCTBand;
import edu.njit.cs.saboc.blu.sno.abn.tan.local.SCTCluster;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.optionbuttons.tan.SCTCreateTANFromBandButton;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.optionbuttons.tan.SCTExportBandButton;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.tan.configuration.SCTTANConfiguration;
import java.util.ArrayList;
import java.util.Optional;

/**
 *
 * @author Chris O
 */
public class SCTBandOptionsPanel extends AbstractNodeOptionsPanel<SCTBand> {
    
    private Optional<SCTBand> selectedArea = Optional.empty();
    
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
            AbstractNodePanel anp = config.getUIConfiguration().createContainerDetailsPanel();
            anp.setContents(selectedArea.get());

            return anp;
        });

        super.addOptionButton(popoutBtn);
    }
    
    @Override
    public void enableOptionsForGroup(SCTBand band) {
        if(band.getAllClusters().size() > 1) {
            boolean tanPossible = false;
            
            ArrayList<SCTCluster> clusters = band.getAllClusters();
            
            for(SCTCluster cluster : clusters) {
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
    public void setContents(SCTBand band) {
        selectedArea = Optional.of(band);
        
        exportBtn.setCurrentBand(band);
        
        tanBtn.setCurrentBand(band);
        
        this.enableOptionsForGroup(band);
    }
    
    public void clearContents() {
        selectedArea = Optional.empty();
        
        tanBtn.setCurrentBand(null);
        
        exportBtn.setCurrentBand(null);
    }    
}
