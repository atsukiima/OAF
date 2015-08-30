package edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy.area.aggregate;

import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.AbstractNodeOptionsPanel;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.AbstractNodePanel;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.optionbuttons.PopoutNodeDetailsButton;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTArea;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.SCTPAreaTaxonomyConfiguration;
import java.util.Optional;

/**
 *
 * @author Chris O
 */
public class SCTAggregateAreaOptionsPanel extends AbstractNodeOptionsPanel<SCTArea> {
    
    private Optional<SCTArea> selectedArea = Optional.empty();
    
    private final SCTPAreaTaxonomyConfiguration config;
    
    private final PopoutNodeDetailsButton popoutBtn;

    public SCTAggregateAreaOptionsPanel(SCTPAreaTaxonomyConfiguration config) {
        this.config = config;

        popoutBtn = new PopoutNodeDetailsButton("aggregate area") {

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
    public void enableOptionsForGroup(SCTArea area) {
        
    }

    @Override
    public void setContents(SCTArea area) {
        selectedArea = Optional.of(area);
        
        this.enableOptionsForGroup(area);
    }
    
    public void clearContents() {
        selectedArea = Optional.empty();
    }
}
