package edu.njit.cs.saboc.blu.sno.gui.gep.panels.disjointpareataxonomy;

import edu.njit.cs.saboc.blu.core.abn.disjoint.DisjointAbstractionNetwork;
import edu.njit.cs.saboc.blu.core.abn.disjoint.DisjointNode;
import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.DisjointPArea;
import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.PArea;
import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.PAreaTaxonomy;
import edu.njit.cs.saboc.blu.core.abn.tan.TANFactory;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.NodeDashboardPanel;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.NodeOptionsPanel;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.disjointabn.buttons.CreateAncestorDisjointAbNButton;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.disjointabn.buttons.ExpandAggregateDisjointNodeButton;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.optionbuttons.CreateTANFromSinglyRootedNodeButton;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.optionbuttons.ExportSinglyRootedNodeButton;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.optionbuttons.PopoutNodeDetailsButton;
import edu.njit.cs.saboc.blu.sno.gui.gep.configuration.listener.DisplayTANAction;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.disjointpareataxonomy.configuration.SCTDisjointPAreaTaxonomyConfiguration;

/**
 *
 * @author Chris O
 */
public class SCTDisjointPAreaOptionsPanel extends NodeOptionsPanel {

    public SCTDisjointPAreaOptionsPanel(SCTDisjointPAreaTaxonomyConfiguration config, boolean forAggregate) {
        
        if(forAggregate) {
            ExpandAggregateDisjointNodeButton expandAggregateButton = new ExpandAggregateDisjointNodeButton(config, (disjointAbN) -> {
                DisjointAbstractionNetwork<DisjointNode<PArea>, PAreaTaxonomy<PArea>, PArea> disjointTaxonomy
                        = (DisjointAbstractionNetwork<DisjointNode<PArea>, PAreaTaxonomy<PArea>, PArea>) disjointAbN;

                config.getUIConfiguration().getDisplayFrameListener().addNewDisjointPAreaTaxonomyGraphFrame(disjointTaxonomy);
            });
            
            super.addOptionButton(expandAggregateButton);
        }

        CreateAncestorDisjointAbNButton ancestorBtn = new CreateAncestorDisjointAbNButton(config, (disjointAbN) -> {

            DisjointAbstractionNetwork<DisjointNode<PArea>, PAreaTaxonomy<PArea>, PArea> disjointTaxonomy
                    = (DisjointAbstractionNetwork<DisjointNode<PArea>, PAreaTaxonomy<PArea>, PArea>) disjointAbN;

            config.getUIConfiguration().getDisplayFrameListener().addNewDisjointPAreaTaxonomyGraphFrame(disjointTaxonomy);
        });
        
        super.addOptionButton(ancestorBtn);
                
        
        CreateTANFromSinglyRootedNodeButton tanBtn = new CreateTANFromSinglyRootedNodeButton(
                new TANFactory(),
                config, 
                new DisplayTANAction(config.getUIConfiguration().getDisplayFrameListener()));
        
        super.addOptionButton(tanBtn);
        

        ExportSinglyRootedNodeButton exportBtn = new ExportSinglyRootedNodeButton(config);

        super.addOptionButton(exportBtn);
        

        PopoutNodeDetailsButton popoutBtn = new PopoutNodeDetailsButton("disjoint partial-area", () -> {
            DisjointPArea parea = (DisjointPArea) super.getCurrentNode().get();

            NodeDashboardPanel anp = config.getUIConfiguration().createGroupDetailsPanel();
            anp.setContents(parea);

            return anp;
        });

        super.addOptionButton(popoutBtn);
    }
}
