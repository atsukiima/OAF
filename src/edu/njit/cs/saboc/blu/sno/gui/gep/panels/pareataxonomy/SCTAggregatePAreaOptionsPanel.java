package edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy;

import edu.njit.cs.saboc.blu.core.abn.node.Node;
import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.AggregatePArea;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.AbstractNodeOptionsPanel;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.NodeDashboardPanel;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.optionbuttons.PopoutNodeDetailsButton;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.optionbuttons.pareataxonomy.SCTCreateExpandedSubtaxonomyButton;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.optionbuttons.pareataxonomy.SCTCreateTANFromAggregatePAreaButton;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.optionbuttons.pareataxonomy.SCTExportAggregatePAreaButton;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.optionbuttons.pareataxonomy.SCTCreateAncestorSubtaxonomyButton;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.optionbuttons.pareataxonomy.SCTCreateRootSubtaxonomyButton;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy.configuration.SCTPAreaTaxonomyConfiguration;
import java.util.Optional;

/**
 *
 * @author Chris O
 */
public class SCTAggregatePAreaOptionsPanel extends AbstractNodeOptionsPanel {
    
    private Optional<AggregatePArea> selectedPArea = Optional.empty();
    
    private final SCTPAreaTaxonomyConfiguration config;
    
//    private final SCTOpenBrowserButton btnNAT;
    
    private final SCTCreateExpandedSubtaxonomyButton expandedSubtaxonomyBtn;
    
    private final SCTCreateTANFromAggregatePAreaButton tanBtn;
    
    private final SCTCreateRootSubtaxonomyButton rootSubtaxonomyBtn;
    
    private final SCTCreateAncestorSubtaxonomyButton ancestorSubtaxonomyBtn;
    
    private final PopoutNodeDetailsButton popoutBtn;
    
    private final SCTExportAggregatePAreaButton exportBtn;
    
    public SCTAggregatePAreaOptionsPanel(SCTPAreaTaxonomyConfiguration config) {
        this.config = config;

//        btnNAT = new SCTOpenBrowserButton(config.getDataConfiguration().getPAreaTaxonomy().getDataSource(),
//                "aggregate partial-area", config.getUIConfiguration().getDisplayFrameListener());
//
//        super.addOptionButton(btnNAT);
        
        expandedSubtaxonomyBtn = new SCTCreateExpandedSubtaxonomyButton(config);
        
        super.addOptionButton(expandedSubtaxonomyBtn);
        
        ancestorSubtaxonomyBtn = new SCTCreateAncestorSubtaxonomyButton(config);
        
        super.addOptionButton(ancestorSubtaxonomyBtn);
        
        tanBtn = new SCTCreateTANFromAggregatePAreaButton(config);
        
        super.addOptionButton(tanBtn);
        
        rootSubtaxonomyBtn = new SCTCreateRootSubtaxonomyButton(config);
        
        super.addOptionButton(rootSubtaxonomyBtn);
        
        popoutBtn = new PopoutNodeDetailsButton("aggregate partial-area", () -> {
            NodeDashboardPanel anp = config.getUIConfiguration().createGroupDetailsPanel();
            anp.setContents(selectedPArea.get());

            return anp;
        });
        
        super.addOptionButton(popoutBtn);
        
        exportBtn = new SCTExportAggregatePAreaButton(config);
        
        super.addOptionButton(exportBtn);
    }
    
    @Override
    public void enableOptionsForNode(Node node) {
        AggregatePArea parea = (AggregatePArea)node;
        
        if(parea.getAggregatedNodes().isEmpty()) {
            expandedSubtaxonomyBtn.setEnabled(false);
        } else {
            expandedSubtaxonomyBtn.setEnabled(true);
        }
        
        if(config.getPAreaTaxonomy().getPAreaHierarchy().getChildren(parea).isEmpty()) {
            rootSubtaxonomyBtn.setEnabled(false);
        } else { 
            rootSubtaxonomyBtn.setEnabled(true);
        }
        
        if(parea.getConceptCount() > 1) {
            if(parea.getAggregatedHierarchy().getChildren(parea.getAggregatedHierarchy().getRoot()).size() > 1) {
                tanBtn.setEnabled(true);
            } else {
                tanBtn.setEnabled(false);
            }
        } else {
            tanBtn.setEnabled(false);
        }
        
        if(config.getPAreaTaxonomy().getPAreaHierarchy().getParents(parea).isEmpty()) {
            ancestorSubtaxonomyBtn.setEnabled(false);
        } else {
            ancestorSubtaxonomyBtn.setEnabled(true);
        }
    }

    @Override
    public void setContents(Node node) {
        AggregatePArea parea = (AggregatePArea)node;
        
        selectedPArea = Optional.of(parea);
        
//        btnNAT.setCurrentRootConcept(parea.getRoot());
        
        exportBtn.setCurrentPArea(parea);
        
        expandedSubtaxonomyBtn.setCurrentPArea(parea);
        
        tanBtn.setCurrentPArea(parea);
        
        rootSubtaxonomyBtn.setCurrentPArea(parea);
        
        ancestorSubtaxonomyBtn.setCurrentPArea(parea);
        
        this.enableOptionsForNode(parea);
    }
    
    public void clearContents() {
        selectedPArea = Optional.empty();
        
//        btnNAT.setCurrentRootConcept(null);
        
        exportBtn.setCurrentPArea(null);
        
        expandedSubtaxonomyBtn.setCurrentPArea(null);
        
        tanBtn.setCurrentPArea(null);
        
        rootSubtaxonomyBtn.setCurrentPArea(null);
        
        ancestorSubtaxonomyBtn.setCurrentPArea(null);
    }
}