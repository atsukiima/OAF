package edu.njit.cs.saboc.blu.sno.gui.gep.panels.tan;

import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.optionbuttons.node.NodeHelpButton;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.optionbuttons.node.CreateDisjointAbNFromPartitionNodeButton;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.optionbuttons.node.ExportPartitionedNodeButton;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.optionbuttons.node.CreateTANFromPartitionedNodeButton;
import edu.njit.cs.saboc.blu.core.abn.tan.Band;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.NodeOptionsPanel;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.NodeDashboardPanel;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.optionbuttons.*;
import edu.njit.cs.saboc.blu.sno.gui.gep.configuration.listener.DisplayDisjointTANAction;
import edu.njit.cs.saboc.blu.sno.gui.gep.configuration.listener.DisplayTANAction;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.tan.configuration.SCTTANConfiguration;

/**
 *
 * @author Chris O
 */
public class SCTBandOptionsPanel extends NodeOptionsPanel {

    public SCTBandOptionsPanel(SCTTANConfiguration config, boolean forAggregate) {
        
        super.addOptionButton(new CreateDisjointAbNFromPartitionNodeButton(
                config,
                new DisplayDisjointTANAction(config.getUIConfiguration().getAbNDisplayManager())));
        

        CreateTANFromPartitionedNodeButton tanBtn = new CreateTANFromPartitionedNodeButton(
                config.getTribalAbstractionNetwork().getSourceFactory(),
                config,
            new DisplayTANAction(config.getUIConfiguration().getAbNDisplayManager()));
        
        super.addOptionButton(tanBtn);
        
        
        ExportPartitionedNodeButton exportBtn = new ExportPartitionedNodeButton(config);
        
        super.addOptionButton(exportBtn);
        
        PopoutDetailsButton popoutBtn = new PopoutDetailsButton("band", () -> {
            Band band = (Band)super.getCurrentNode().get();
            
            NodeDashboardPanel anp = config.getUIConfiguration().createPartitionedNodeDetailsPanel();
            anp.setContents(band);

            return anp;
        });

        super.addOptionButton(popoutBtn);

        NodeHelpButton helpBtn = new NodeHelpButton(config);

        super.addOptionButton(helpBtn);
    }
}
