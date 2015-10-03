package edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy.parea;

import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.AbstractNodeOptionsPanel;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.AbstractNodePanel;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.optionbuttons.PopoutNodeDetailsButton;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTPArea;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTPAreaTaxonomy;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.optionbuttons.pareataxonomy.SCTCreateRootSubtaxonomyButton;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.optionbuttons.pareataxonomy.SCTCreateTANFromPAreaButton;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.optionbuttons.pareataxonomy.SCTExportPAreaButton;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.optionbuttons.SCTOpenBrowserButton;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy.configuration.SCTPAreaTaxonomyConfiguration;
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
    
    private final SCTCreateTANFromPAreaButton tanBtn;
        
    public SCTPAreaOptionsPanel(SCTPAreaTaxonomyConfiguration config) {
        this.config = config;
       
        btnNAT = new SCTOpenBrowserButton(config.getDataConfiguration().getPAreaTaxonomy().getDataSource(),
            "partial-area", config.getUIConfiguration().getDisplayFrameListener());
        
        super.addOptionButton(btnNAT);
        
        rootSubtaxonomyBtn = new SCTCreateRootSubtaxonomyButton(config);
        
        super.addOptionButton(rootSubtaxonomyBtn);
        
        tanBtn = new SCTCreateTANFromPAreaButton(config);
        
        super.addOptionButton(tanBtn);
        
        popoutBtn = new PopoutNodeDetailsButton("partial-area") {

            @Override
            public AbstractNodePanel getCurrentDetailsPanel() {
                AbstractNodePanel anp = config.getUIConfiguration().createGroupDetailsPanel();
                anp.setContents(selectedPArea.get());
                
                return anp;
            }
        };
        
        super.addOptionButton(popoutBtn);
        
        exportBtn = new SCTExportPAreaButton();
        
        super.addOptionButton(exportBtn);
    }
    
    @Override
    public void enableOptionsForGroup(SCTPArea parea) {
        SCTPAreaTaxonomy taxonomy = config.getDataConfiguration().getPAreaTaxonomy();
        
        if(taxonomy.getDescendantGroups(parea).isEmpty()) {
            rootSubtaxonomyBtn.setEnabled(false);
        } else {
            rootSubtaxonomyBtn.setEnabled(true);
        }
        
        if(parea.getConceptCount() > 1) {
            if(parea.getHierarchy().getChildren(parea.getRoot()).size() > 1) {
                tanBtn.setEnabled(true);
            } else {
                tanBtn.setEnabled(false);
            }
        } else {
            tanBtn.setEnabled(false);
        }
    }

    @Override
    public void setContents(SCTPArea parea) {
        selectedPArea = Optional.of(parea);
        
        btnNAT.setCurrentRootConcept(parea.getRoot());
        exportBtn.setCurrentPArea(parea);
        rootSubtaxonomyBtn.setCurrentPArea(parea);
        tanBtn.setCurrentPArea(parea);
        
        this.enableOptionsForGroup(parea);
    }
    
    public void clearContents() {
        selectedPArea = Optional.empty();

        btnNAT.setCurrentRootConcept(null);
        exportBtn.setCurrentPArea(null);
        rootSubtaxonomyBtn.setCurrentPArea(null);
        tanBtn.setCurrentPArea(null);
    }
}
