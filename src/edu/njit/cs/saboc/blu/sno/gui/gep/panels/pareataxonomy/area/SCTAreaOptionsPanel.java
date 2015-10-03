package edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy.area;

import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.AbstractNodeOptionsPanel;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.AbstractNodePanel;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.optionbuttons.PopoutNodeDetailsButton;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTArea;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTPArea;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.optionbuttons.pareataxonomy.SCTCreateDisjointTaxonomyButton;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.optionbuttons.pareataxonomy.SCTCreateTANFromAreaButton;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.optionbuttons.pareataxonomy.SCTExportAreaButton;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy.configuration.SCTPAreaTaxonomyConfiguration;
import java.util.ArrayList;
import java.util.Optional;

/**
 *
 * @author Chris O
 */
public class SCTAreaOptionsPanel extends AbstractNodeOptionsPanel<SCTArea> {
    
    private Optional<SCTArea> selectedArea = Optional.empty();
    
    private final SCTPAreaTaxonomyConfiguration config;
    
    private final SCTCreateDisjointTaxonomyButton disjointTaxonomyBtn;
    
    private final SCTCreateTANFromAreaButton tanBtn;
    
    private final PopoutNodeDetailsButton popoutBtn;
    
    private final SCTExportAreaButton exportBtn;
    

    public SCTAreaOptionsPanel(SCTPAreaTaxonomyConfiguration config) {
        this.config = config;
        
        this.disjointTaxonomyBtn = new SCTCreateDisjointTaxonomyButton(config);
        
        super.addOptionButton(disjointTaxonomyBtn);

        this.tanBtn = new SCTCreateTANFromAreaButton(config);
        
        super.addOptionButton(tanBtn);
                
        popoutBtn = new PopoutNodeDetailsButton("area") {

            @Override
            public AbstractNodePanel getCurrentDetailsPanel() {
                AbstractNodePanel anp = config.getUIConfiguration().createContainerDetailsPanel();
                anp.setContents(selectedArea.get());

                return anp;
            }
        };
        
        super.addOptionButton(popoutBtn);
        
        this.exportBtn = new SCTExportAreaButton(config);
        
        super.addOptionButton(exportBtn);
    }
    
    @Override
    public void enableOptionsForGroup(SCTArea area) {
        if(config.getDataConfiguration().getContainerOverlappingConcepts(area).isEmpty()) {
            disjointTaxonomyBtn.setEnabled(false);
        } else {
            disjointTaxonomyBtn.setEnabled(true);
        }
        
        if(area.getAllPAreas().size() > 2) {
            
            ArrayList<SCTPArea> pareas = area.getAllPAreas();
            
            boolean tanPossible = false;
            
            for(SCTPArea parea : pareas) {
                if(parea.getConceptCount() > 1) {
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
        
        exportBtn.setCurrentArea(area);
        
        disjointTaxonomyBtn.setCurrentArea(area);
        tanBtn.setCurrentArea(area);
        
        this.enableOptionsForGroup(area);
    }
    
    public void clearContents() {
        selectedArea = Optional.empty();
        
        exportBtn.setCurrentArea(null);
        disjointTaxonomyBtn.setCurrentArea(null);
        tanBtn.setCurrentArea(null);
    }    
}
