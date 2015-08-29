package edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy.parea;

import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.AbstractNodeOptionsPanel;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.AbstractNodePanel;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.optionbuttons.PopoutNodeDetailsButton;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTPArea;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTPAreaTaxonomy;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.SCTPAreaTaxonomyConfiguration;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.optionbuttons.SCTCreateRootSubtaxonomyButton;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.optionbuttons.SCTExportPAreaButton;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.optionbuttons.SCTOpenBrowserButton;
import java.util.Optional;

/**
 *
 * @author Chris O
 */
public class SCTPAreaOptionsPanel extends AbstractNodeOptionsPanel<SCTPArea> {
    
    private Optional<SCTPArea> selectedPArea = Optional.empty();
    
    private final SCTPAreaTaxonomyConfiguration config;

    private final SCTOpenBrowserButton btnNAT;
    
    private final PopoutNodeDetailsButton popoutBtn;
    
    private final SCTExportPAreaButton exportBtn;
    
    private final SCTCreateRootSubtaxonomyButton rootSubtaxonomyBtn;
        
    public SCTPAreaOptionsPanel(SCTPAreaTaxonomyConfiguration config) {
        this.config = config;
       
        btnNAT = new SCTOpenBrowserButton(config.getPAreaTaxonomy().getDataSource(),
            "partial-area", config.getDisplayListener());
        
        super.addOptionButton(btnNAT);
        
        popoutBtn = new PopoutNodeDetailsButton("partial-area") {

            @Override
            public AbstractNodePanel getCurrentDetailsPanel() {
                AbstractNodePanel anp = config.getGEPConfiguration().createGroupDetailsPanel();
                anp.setContents(selectedPArea.get());
                
                return anp;
            }
        };
        
        super.addOptionButton(popoutBtn);
        
        exportBtn = new SCTExportPAreaButton();
        
        super.addOptionButton(exportBtn);
        
        rootSubtaxonomyBtn = new SCTCreateRootSubtaxonomyButton(config);
        
        super.addOptionButton(rootSubtaxonomyBtn);
    }
    
    @Override
    public void enableOptionsForGroup(SCTPArea parea) {
        SCTPAreaTaxonomy taxonomy = config.getPAreaTaxonomy();
        
        if(taxonomy.getDescendantGroups(parea).isEmpty()) {
            rootSubtaxonomyBtn.setEnabled(false);
        } else {
            rootSubtaxonomyBtn.setEnabled(true);
        }
    }

    @Override
    public void setContents(SCTPArea parea) {
        selectedPArea = Optional.of(parea);
        
        btnNAT.setCurrentRootConcept(parea.getRoot());
        exportBtn.setCurrentPArea(parea);
        rootSubtaxonomyBtn.setCurrentPArea(parea);
        
        this.enableOptionsForGroup(parea);
    }
    
    public void clearContents() {
        selectedPArea = Optional.empty();

        btnNAT.setCurrentRootConcept(null);
        exportBtn.setCurrentPArea(null);
        rootSubtaxonomyBtn.setCurrentPArea(null);
    }
}
