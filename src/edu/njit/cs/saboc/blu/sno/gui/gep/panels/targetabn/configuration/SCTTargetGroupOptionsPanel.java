package edu.njit.cs.saboc.blu.sno.gui.gep.panels.targetabn.configuration;

import edu.njit.cs.saboc.blu.core.abn.targetbased.TargetGroup;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.NodeDashboardPanel;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.NodeOptionsPanel;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.optionbuttons.PopoutNodeDetailsButton;

/**
 *
 * @author Chris O
 */
public class SCTTargetGroupOptionsPanel extends NodeOptionsPanel<TargetGroup> {

    public SCTTargetGroupOptionsPanel(SCTTargetAbNConfiguration config) {
        
        PopoutNodeDetailsButton popoutBtn = new PopoutNodeDetailsButton("partial-area", () -> {
            TargetGroup group = super.getCurrentNode().get();

            NodeDashboardPanel anp = config.getUIConfiguration().createGroupDetailsPanel();
            anp.setContents(group);

            return anp;
        });

        super.addOptionButton(popoutBtn);
    }
}
