package edu.njit.cs.saboc.blu.sno.gui.gep.panels.disjointpareataxonomy;

import SnomedShared.Concept;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.AbstractNodeOptionsPanel;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.AbstractNodePanel;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.optionbuttons.PopoutNodeDetailsButton;
import edu.njit.cs.saboc.blu.sno.abn.disjointpareataxonomy.DisjointPAreaTaxonomy;
import edu.njit.cs.saboc.blu.sno.abn.disjointpareataxonomy.DisjointPartialArea;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.disjointpareataxonomy.configuration.SCTDisjointPAreaTaxonomyConfiguration;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.optionbuttons.SCTOpenBrowserButton;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.optionbuttons.pareataxonomy.disjoint.SCTCreateTANFromDisjointPAreaButton;
import java.util.Optional;

/**
 *
 * @author Chris O
 */
public class SCTDisjointPAreaOptionsPanel extends AbstractNodeOptionsPanel<DisjointPartialArea> {
    
    private Optional<DisjointPartialArea> selectedPArea = Optional.empty();
    
    private final SCTDisjointPAreaTaxonomyConfiguration configuration;
    
    private final SCTOpenBrowserButton btnNAT;
    
    private final SCTCreateTANFromDisjointPAreaButton tanBtn;
    
    private final PopoutNodeDetailsButton popoutBtn;

    public SCTDisjointPAreaOptionsPanel(SCTDisjointPAreaTaxonomyConfiguration config) {

        this.configuration = config;
        
        btnNAT = new SCTOpenBrowserButton(config.getDataConfiguration().getDisjointPAreaTaxonomy().getSourcePAreaTaxonomy().getDataSource(),
                "aggregate partial-area", config.getUIConfiguration().getDisplayListener());

        super.addOptionButton(btnNAT);
        
        tanBtn = new SCTCreateTANFromDisjointPAreaButton(config);
        
        super.addOptionButton(tanBtn);
        
        popoutBtn = new PopoutNodeDetailsButton("disjoint partial-area") {

            @Override
            public AbstractNodePanel getCurrentDetailsPanel() {
                AbstractNodePanel anp = config.getUIConfiguration().createGroupDetailsPanel();
                anp.setContents(selectedPArea.get());
                
                return anp;
            }
        };
        
        super.addOptionButton(popoutBtn);
    }
    
    @Override
    public void enableOptionsForGroup(DisjointPartialArea parea) {
        DisjointPAreaTaxonomy disjointTaxonomy = configuration.getDataConfiguration().getDisjointPAreaTaxonomy();

        if (parea.getConceptCount() > 2) {
            Concept root = parea.getRoot();
            
            if(disjointTaxonomy.getDataSource().getConceptChildren(root).size() > 1) {
                tanBtn.setEnabled(true);
            } else {
                tanBtn.setEnabled(false);
            }
        } else {
            tanBtn.setEnabled(false);
        }
    }

    @Override
    public void setContents(DisjointPartialArea parea) {
        selectedPArea = Optional.of(parea);
        
        btnNAT.setCurrentRootConcept(parea.getRoot());
        tanBtn.setCurrentPArea(parea);
        
        this.enableOptionsForGroup(parea);
    }
    
    public void clearContents() {
        selectedPArea = Optional.empty();
        
        btnNAT.setCurrentRootConcept(null);
    }
}
