package edu.njit.cs.saboc.blu.sno.gui.gep.panels.disjointpareataxonomy;

import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.AbstractNodeOptionsPanel;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.AbstractNodePanel;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.optionbuttons.PopoutNodeDetailsButton;
import edu.njit.cs.saboc.blu.sno.abn.disjointpareataxonomy.DisjointPartialArea;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.optionbuttons.SCTOpenBrowserButton;
import java.util.Optional;

/**
 *
 * @author Chris O
 */
public class SCTDisjointPAreaOptionsPanel extends AbstractNodeOptionsPanel<DisjointPartialArea> {
    
    private Optional<DisjointPartialArea> selectedPArea = Optional.empty();
    
    private final SCTDisjointPAreaTaxonomyConfiguration configuration;
    
    private final SCTOpenBrowserButton btnNAT;
    
    private final PopoutNodeDetailsButton popoutBtn;

    public SCTDisjointPAreaOptionsPanel(SCTDisjointPAreaTaxonomyConfiguration config) {

        this.configuration = config;
        
        btnNAT = new SCTOpenBrowserButton(config.getDisjointPAreaTaxonomy().getSourcePAreaTaxonomy().getDataSource(),
                "aggregate partial-area", config.getDisplayListener());

        super.addOptionButton(btnNAT);
        
        popoutBtn = new PopoutNodeDetailsButton("disjoint partial-area") {

            @Override
            public AbstractNodePanel getCurrentDetailsPanel() {
                AbstractNodePanel anp = config.getGEPConfiguration().createGroupDetailsPanel();
                anp.setContents(selectedPArea.get());
                
                return anp;
            }
        };
        
        super.addOptionButton(popoutBtn);
    }
    
    @Override
    public void enableOptionsForGroup(DisjointPartialArea group) {
        
    }

    @Override
    public void setContents(DisjointPartialArea parea) {
        selectedPArea = Optional.of(parea);
        
        btnNAT.setCurrentRootConcept(parea.getRoot());
        
        this.enableOptionsForGroup(parea);
    }
    
    public void clearContents() {
        selectedPArea = Optional.empty();
        
        btnNAT.setCurrentRootConcept(null);
    }
}
