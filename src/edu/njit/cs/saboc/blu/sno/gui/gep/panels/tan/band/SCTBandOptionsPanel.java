package edu.njit.cs.saboc.blu.sno.gui.gep.panels.tan.band;

import SnomedShared.overlapping.CommonOverlapSet;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.AbstractNodeOptionsPanel;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.AbstractNodePanel;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.optionbuttons.PopoutNodeDetailsButton;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.SCTTANConfiguration;
import java.util.Optional;

/**
 *
 * @author Chris O
 */
public class SCTBandOptionsPanel extends AbstractNodeOptionsPanel<CommonOverlapSet> {
    
    private Optional<CommonOverlapSet> selectedArea = Optional.empty();
    
    private final SCTTANConfiguration config;
    
    private final PopoutNodeDetailsButton popoutBtn;

    public SCTBandOptionsPanel(SCTTANConfiguration config) {
        this.config = config;
        
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
        
    }

    @Override
    public void setContents(CommonOverlapSet band) {
        selectedArea = Optional.of(band);
        
        this.enableOptionsForGroup(band);
    }
    
    public void clearContents() {
        selectedArea = Optional.empty();
        
    }    
}
