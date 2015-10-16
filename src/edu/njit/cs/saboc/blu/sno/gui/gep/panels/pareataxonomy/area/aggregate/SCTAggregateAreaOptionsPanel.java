package edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy.area.aggregate;

import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.AbstractNodeOptionsPanel;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.AbstractNodePanel;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.optionbuttons.PopoutNodeDetailsButton;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTArea;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTPArea;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.optionbuttons.pareataxonomy.SCTCreateDisjointTaxonomyButton;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.optionbuttons.pareataxonomy.SCTCreateTANFromAreaButton;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy.configuration.SCTPAreaTaxonomyConfiguration;
import java.util.ArrayList;
import java.util.Optional;

/**
 *
 * @author Chris O
 */
public class SCTAggregateAreaOptionsPanel extends AbstractNodeOptionsPanel<SCTArea> {
    
    private Optional<SCTArea> selectedArea = Optional.empty();
    
    private final SCTPAreaTaxonomyConfiguration config;
    
    private final SCTCreateDisjointTaxonomyButton disjointTaxonomyBtn;
    
    private final SCTCreateTANFromAreaButton tanBtn;
    
    private final PopoutNodeDetailsButton popoutBtn;

    public SCTAggregateAreaOptionsPanel(SCTPAreaTaxonomyConfiguration config) {
        this.config = config;
        
        this.disjointTaxonomyBtn = new SCTCreateDisjointTaxonomyButton(config);
        
        super.addOptionButton(disjointTaxonomyBtn);

        this.tanBtn = new SCTCreateTANFromAreaButton(config);
        
        super.addOptionButton(tanBtn);
        
        popoutBtn = new PopoutNodeDetailsButton("aggregate area") {

            @Override
            public AbstractNodePanel getCurrentDetailsPanel() {
                AbstractNodePanel anp = config.getUIConfiguration().createContainerDetailsPanel();
                anp.setContents(selectedArea.get());
                
                return anp;
            }
        };
        
        super.addOptionButton(popoutBtn);
    }

    @Override
    public void enableOptionsForGroup(SCTArea area) {
        if (area.getAllPAreas().size() > 2) {
            ArrayList<SCTPArea> pareas = area.getAllPAreas();

            boolean tanPossible = false;

            for (SCTPArea parea : pareas) {
                if (parea.getConceptCount() > 1) {
                    tanPossible = true;
                    break;
                }
            }

            tanBtn.setEnabled(tanPossible);
        } else {
            tanBtn.setEnabled(false);
        }
    }

    @Override
    public void setContents(SCTArea area) {
        selectedArea = Optional.of(area);
        
        disjointTaxonomyBtn.setCurrentArea(area);
        tanBtn.setCurrentArea(area);
        
        this.enableOptionsForGroup(area);
    }
    
    public void clearContents() {
        selectedArea = Optional.empty();
        
        disjointTaxonomyBtn.setCurrentArea(null);
        tanBtn.setCurrentArea(null);
    }
}
