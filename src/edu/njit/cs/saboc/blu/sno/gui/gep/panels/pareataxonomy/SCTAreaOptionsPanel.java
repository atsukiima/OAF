package edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy;

import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.Area;
import edu.njit.cs.saboc.blu.core.abn.tan.TANFactory;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.NodeOptionsPanel;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.NodeDashboardPanel;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.optionbuttons.node.CreateDisjointAbNFromPartitionNodeButton;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.optionbuttons.node.CreateTANFromPartitionedNodeButton;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.optionbuttons.node.ExportPartitionedNodeButton;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.optionbuttons.node.NodeHelpButton;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.optionbuttons.PopoutDetailsButton;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.pareataxonomy.buttons.CreateDisjointSubjectSubtaxonomyButton;
import edu.njit.cs.saboc.blu.sno.gui.gep.configuration.listener.DisplayDisjointTaxonomyAction;
import edu.njit.cs.saboc.blu.sno.gui.gep.configuration.listener.DisplayTANAction;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy.configuration.SCTPAreaTaxonomyConfiguration;

/**
 *
 * @author Chris O
 */
public class SCTAreaOptionsPanel extends NodeOptionsPanel {
    
    public SCTAreaOptionsPanel(SCTPAreaTaxonomyConfiguration config, boolean aggregated) {
        
        CreateDisjointAbNFromPartitionNodeButton createdDisjointTaxonomyBtn = new CreateDisjointAbNFromPartitionNodeButton(
                config,
                new DisplayDisjointTaxonomyAction(config.getUIConfiguration().getAbNDisplayManager()));
        
        super.addOptionButton(createdDisjointTaxonomyBtn);
        
        CreateDisjointSubjectSubtaxonomyButton createDisjointSubjectSubtaxonomyBtn = 
                new CreateDisjointSubjectSubtaxonomyButton(config, 
                new DisplayDisjointTaxonomyAction(config.getUIConfiguration().getAbNDisplayManager()));
        
        super.addOptionButton(createDisjointSubjectSubtaxonomyBtn);

        CreateTANFromPartitionedNodeButton tanBtn = new CreateTANFromPartitionedNodeButton(
                new TANFactory(config.getRelease()),
                config,
                new DisplayTANAction(config.getUIConfiguration().getAbNDisplayManager()));
        
        super.addOptionButton(tanBtn);
                
        PopoutDetailsButton popoutBtn = new PopoutDetailsButton("area", () -> {
            Area area = (Area)super.getCurrentNode().get();
            
            NodeDashboardPanel anp = config.getUIConfiguration().createPartitionedNodeDetailsPanel();
            anp.setContents(area);

            return anp;
        });

        super.addOptionButton(popoutBtn);
        
        ExportPartitionedNodeButton exportBtn = new ExportPartitionedNodeButton(config);
        
        super.addOptionButton(exportBtn);

        NodeHelpButton helpBtn = new NodeHelpButton(config);

        super.addOptionButton(helpBtn);
    }
}
