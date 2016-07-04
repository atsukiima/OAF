package edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy;

import edu.njit.cs.saboc.blu.core.abn.node.Node;
import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.PArea;
import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.PAreaTaxonomy;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.AbstractNodeOptionsPanel;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.NodeDashboardPanel;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.optionbuttons.PopoutNodeDetailsButton;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.optionbuttons.pareataxonomy.SCTCreateRootSubtaxonomyButton;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.optionbuttons.pareataxonomy.SCTCreateTANFromPAreaButton;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.optionbuttons.pareataxonomy.SCTExportPAreaButton;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.optionbuttons.pareataxonomy.SCTCreateAncestorSubtaxonomyButton;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy.configuration.SCTPAreaTaxonomyConfiguration;
import java.util.Optional;

/**
 *
 * @author Chris O
 */
public class SCTPAreaOptionsPanel extends AbstractNodeOptionsPanel {
    
    private Optional<PArea> selectedPArea = Optional.empty();
    
    private final SCTPAreaTaxonomyConfiguration config;

//    private final SCTOpenBrowserButton btnNAT;
    
    private final PopoutNodeDetailsButton popoutBtn;
    
    private final SCTExportPAreaButton exportBtn;
    
    private final SCTCreateRootSubtaxonomyButton rootSubtaxonomyBtn;
    
    private final SCTCreateAncestorSubtaxonomyButton ancestorSubtaxonomyBtn;
    
    private final SCTCreateTANFromPAreaButton tanBtn;
        
    public SCTPAreaOptionsPanel(SCTPAreaTaxonomyConfiguration config) {
        this.config = config;
       
//        btnNAT = new SCTOpenBrowserButton(config.getDataConfiguration().getPAreaTaxonomy().getDataSource(),
//            "partial-area", config.getUIConfiguration().getDisplayFrameListener());
//        
//        super.addOptionButton(btnNAT);
        
        rootSubtaxonomyBtn = new SCTCreateRootSubtaxonomyButton(config);
        
        super.addOptionButton(rootSubtaxonomyBtn);
        
        ancestorSubtaxonomyBtn = new SCTCreateAncestorSubtaxonomyButton(config);
        
        super.addOptionButton(ancestorSubtaxonomyBtn);
        
        tanBtn = new SCTCreateTANFromPAreaButton(config);
        
        super.addOptionButton(tanBtn);
        
        popoutBtn = new PopoutNodeDetailsButton("partial-area", () -> {
            NodeDashboardPanel anp = config.getUIConfiguration().createGroupDetailsPanel();
            anp.setContents(selectedPArea.get());

            return anp;
        });

        super.addOptionButton(popoutBtn);
        
        exportBtn = new SCTExportPAreaButton();
        
        super.addOptionButton(exportBtn);
    }
    
    @Override
    public void enableOptionsForNode(Node node) {
        
        PArea parea = (PArea)node;
        
        PAreaTaxonomy taxonomy = config.getPAreaTaxonomy();
        
        if(taxonomy.getPAreaHierarchy().getDescendants(parea).isEmpty()) {
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
        
        if(taxonomy.getPAreaHierarchy().getParents(parea).isEmpty()) {
            ancestorSubtaxonomyBtn.setEnabled(false);
        } else {
            ancestorSubtaxonomyBtn.setEnabled(true);
        }
    }

    @Override
    public void setContents(Node node) {
        PArea parea = (PArea)node;
        
        selectedPArea = Optional.of(parea);
        
//        btnNAT.setCurrentRootConcept((SCTConcept)parea.getRoot());
        
        exportBtn.setCurrentPArea(parea);
        rootSubtaxonomyBtn.setCurrentPArea(parea);
        tanBtn.setCurrentPArea(parea);
        
        ancestorSubtaxonomyBtn.setCurrentPArea(parea);
        
        this.enableOptionsForNode(parea);
    }
    
    public void clearContents() {
        selectedPArea = Optional.empty();

//        btnNAT.setCurrentRootConcept(null);
        exportBtn.setCurrentPArea(null);
        rootSubtaxonomyBtn.setCurrentPArea(null);
        tanBtn.setCurrentPArea(null);
        ancestorSubtaxonomyBtn.setCurrentPArea(null);
    }
}
