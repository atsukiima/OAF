package edu.njit.cs.saboc.blu.sno.gui.gep.panels.tan;

import edu.njit.cs.saboc.blu.core.abn.tan.Cluster;
import edu.njit.cs.saboc.blu.core.abn.tan.aggregate.AggregateCluster;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.NodeOptionsPanel;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.NodeDashboardPanel;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.optionbuttons.*;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.tan.buttons.CreateAncestorTANButton;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.tan.buttons.CreateRootTANButton;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.tan.buttons.ExpandAggregateClusterButton;
import edu.njit.cs.saboc.blu.sno.gui.gep.configuration.listener.DisplayTANAction;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.tan.configuration.SCTTANConfiguration;

/**
 *
 * @author Chris O
 */
public class SCTClusterOptionsPanel extends NodeOptionsPanel {

    public SCTClusterOptionsPanel(SCTTANConfiguration config, boolean forAggregate) {
        
        if(forAggregate) {
            
        } else {
            CreateDisjointAbNFromSinglyRootedNodeButton<Cluster> createDisjointBtn = new CreateDisjointAbNFromSinglyRootedNodeButton<>(
                    config,
                    (disjointTAN) -> {
                        config.getUIConfiguration().getAbNDisplayManager().displayDisjointTribalAbstractionNetwork(disjointTAN);
                    });

            super.addOptionButton(createDisjointBtn);
        }

        CreateRootTANButton rootTANBtn = new CreateRootTANButton(config,
                new DisplayTANAction(config.getUIConfiguration().getAbNDisplayManager()));

        super.addOptionButton(rootTANBtn);

        CreateAncestorTANButton ancestorTANBtn = new CreateAncestorTANButton(config,
                new DisplayTANAction(config.getUIConfiguration().getAbNDisplayManager()));

        super.addOptionButton(ancestorTANBtn);

        if (forAggregate) {
            ExpandAggregateClusterButton<AggregateCluster> expandAggregateBtn = new ExpandAggregateClusterButton<>(config,
                    (tan) -> {
                        config.getUIConfiguration().getAbNDisplayManager().displayTribalAbstractionNetwork(tan);
                    });

            super.addOptionButton(expandAggregateBtn);
        }

        CreateTANFromSinglyRootedNodeButton tanBtn = new CreateTANFromSinglyRootedNodeButton(
                config.getTribalAbstractionNetwork().getSourceFactory(),
                config,
                new DisplayTANAction(config.getUIConfiguration().getAbNDisplayManager()));

        super.addOptionButton(tanBtn);

        ExportSinglyRootedNodeButton exportBtn = new ExportSinglyRootedNodeButton(config);

        super.addOptionButton(exportBtn);

        PopoutNodeDetailsButton popoutBtn = new PopoutNodeDetailsButton("cluster", () -> {
            Cluster cluster = (Cluster) super.getCurrentNode().get();

            NodeDashboardPanel anp = config.getUIConfiguration().createGroupDetailsPanel();
            anp.setContents(cluster);

            return anp;
        });

        super.addOptionButton(popoutBtn);

        HelpButton helpBtn = new HelpButton(config);

        super.addOptionButton(helpBtn);
    }
}
