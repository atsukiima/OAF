package edu.njit.cs.saboc.blu.sno.gui.gep.panels.tan.band;

import SnomedShared.overlapping.CommonOverlapSet;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.AbstractNodeOptionsPanel;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.AbstractNodePanel;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.optionbuttons.PopoutNodeDetailsButton;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.SCTTANConfiguration;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.optionbuttons.tan.SCTCreateTANFromBandButton;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.optionbuttons.tan.SCTExportBandButton;
import java.util.Optional;

/**
 *
 * @author Chris O
 */
public class SCTBandOptionsPanel extends AbstractNodeOptionsPanel<CommonOverlapSet> {
    
    private Optional<CommonOverlapSet> selectedArea = Optional.empty();
    
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
        
        popoutBtn = new PopoutNodeDetailsButton("band") {

            @Override
            public AbstractNodePanel getCurrentDetailsPanel() {
                AbstractNodePanel anp = config.getGEPConfiguration().createContainerDetailsPanel();
                anp.setContents(selectedArea.get());

                return anp;
            }
        };
        
        super.addOptionButton(popoutBtn);
    }
    
    @Override
    public void enableOptionsForGroup(CommonOverlapSet band) {
        if(config.getContainerOverlappingConcepts(band).isEmpty()) {
            tanBtn.setEnabled(false);
        } else {
            tanBtn.setEnabled(true);
        }
    }

    @Override
    public void setContents(CommonOverlapSet band) {
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
