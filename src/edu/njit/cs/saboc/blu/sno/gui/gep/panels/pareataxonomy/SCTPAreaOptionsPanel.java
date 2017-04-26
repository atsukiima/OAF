package edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy;

import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.optionbuttons.node.NodeHelpButton;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.optionbuttons.node.CreateTANFromSinglyRootedNodeButton;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.optionbuttons.node.CreateDisjointAbNFromSinglyRootedNodeButton;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.optionbuttons.node.ExportSinglyRootedNodeButton;
import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.PArea;
import edu.njit.cs.saboc.blu.core.abn.tan.TANFactory;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.NodeOptionsPanel;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.NodeDashboardPanel;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.optionbuttons.*;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.pareataxonomy.buttons.CreateAncestorSubtaxonomyButton;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.pareataxonomy.buttons.CreateExpandedSubtaxonomyButton;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.pareataxonomy.buttons.CreateRootSubtaxonomyButton;
import edu.njit.cs.saboc.blu.sno.gui.gep.configuration.listener.DisplayPAreaTaxonomyAction;
import edu.njit.cs.saboc.blu.sno.gui.gep.configuration.listener.DisplayTANAction;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.optionbuttons.SCTOpenBrowserButton;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy.configuration.SCTPAreaTaxonomyConfiguration;

/**
 *
 * @author Chris O
 */
public class SCTPAreaOptionsPanel extends NodeOptionsPanel {

    public SCTPAreaOptionsPanel(SCTPAreaTaxonomyConfiguration config, boolean aggregated) {
        
        SCTOpenBrowserButton openBrowserButton = new SCTOpenBrowserButton(config, config.getUIConfiguration().getFrameManager());
        
        super.addOptionButton(openBrowserButton);

        if (aggregated) {
            CreateExpandedSubtaxonomyButton expandedSubtaxonomyBtn = new CreateExpandedSubtaxonomyButton(
                    config, new DisplayPAreaTaxonomyAction(config.getUIConfiguration().getAbNDisplayManager()));

            super.addOptionButton(expandedSubtaxonomyBtn);
        } else {
            CreateDisjointAbNFromSinglyRootedNodeButton<PArea> createDisjointBtn = new CreateDisjointAbNFromSinglyRootedNodeButton<>(
                    config,
                    (disjointTaxonomy) -> {
                        config.getUIConfiguration().getAbNDisplayManager().displayDisjointPAreaTaxonomy(disjointTaxonomy);
                    });

            super.addOptionButton(createDisjointBtn);
        }
        
        
        CreateRootSubtaxonomyButton rootSubtaxonomyBtn = new CreateRootSubtaxonomyButton(config,
            new DisplayPAreaTaxonomyAction(config.getUIConfiguration().getAbNDisplayManager()));
        
        super.addOptionButton(rootSubtaxonomyBtn);
        
        
        CreateAncestorSubtaxonomyButton ancestorSubtaxonomyBtn = new CreateAncestorSubtaxonomyButton(config, 
            new DisplayPAreaTaxonomyAction(config.getUIConfiguration().getAbNDisplayManager()));
        
        super.addOptionButton(ancestorSubtaxonomyBtn);
        
        
        CreateTANFromSinglyRootedNodeButton tanBtn = new CreateTANFromSinglyRootedNodeButton(
                new TANFactory(config.getPAreaTaxonomy().getDerivation().getSourceOntology()),
                config, 
                new DisplayTANAction(config.getUIConfiguration().getAbNDisplayManager()));
        
        super.addOptionButton(tanBtn);
        
        
        PopoutDetailsButton popoutBtn = new PopoutDetailsButton("partial-area", () -> {
            PArea parea = (PArea)super.getCurrentNode().get();
            
            NodeDashboardPanel anp = config.getUIConfiguration().createNodeDetailsPanel();
            anp.setContents(parea);

            return anp;
        });

        super.addOptionButton(popoutBtn);
        
        
        ExportSinglyRootedNodeButton exportBtn = new ExportSinglyRootedNodeButton(config);
        
        super.addOptionButton(exportBtn);
        

        NodeHelpButton helpBtn = new NodeHelpButton(config);

        super.addOptionButton(helpBtn);
    }
}
