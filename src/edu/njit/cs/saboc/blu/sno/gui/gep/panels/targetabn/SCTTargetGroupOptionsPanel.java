package edu.njit.cs.saboc.blu.sno.gui.gep.panels.targetabn;

import edu.njit.cs.saboc.blu.core.abn.targetbased.TargetAbstractionNetwork;
import edu.njit.cs.saboc.blu.core.abn.targetbased.TargetGroup;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.NodeDashboardPanel;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.NodeOptionsPanel;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.optionbuttons.node.NodeHelpButton;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.optionbuttons.PopoutDetailsButton;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.targetabn.buttons.ExpandAggregateTargetGroupButton;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.optionbuttons.SCTOpenBrowserButton;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.targetabn.configuration.SCTTargetAbNConfiguration;

/**
 *
 * @author Chris O
 */
public class SCTTargetGroupOptionsPanel extends NodeOptionsPanel<TargetGroup> {

    public SCTTargetGroupOptionsPanel(SCTTargetAbNConfiguration config) {
        
        SCTOpenBrowserButton openBrowserButton = new SCTOpenBrowserButton(
                config, 
                config.getUIConfiguration().getFrameManager());
        
        super.addOptionButton(openBrowserButton);
        
        if(config.getTargetAbstractionNetwork().isAggregated()) {
            ExpandAggregateTargetGroupButton expandAggregateButton = new ExpandAggregateTargetGroupButton(config, (targetAbN) -> {
                TargetAbstractionNetwork abn = (TargetAbstractionNetwork)targetAbN;
                
                config.getUIConfiguration().getAbNDisplayManager().displayTargetAbstractionNetwork(abn);
            });
            
            super.addOptionButton(expandAggregateButton);
        }
        
        PopoutDetailsButton popoutBtn = new PopoutDetailsButton("target group", () -> {
            TargetGroup group = super.getCurrentNode().get();

            NodeDashboardPanel anp = config.getUIConfiguration().createNodeDetailsPanel();
            anp.setContents(group);

            return anp;
        });

        super.addOptionButton(popoutBtn);

        NodeHelpButton helpBtn = new NodeHelpButton(config);

        super.addOptionButton(helpBtn);
    }
}
