package edu.njit.cs.saboc.blu.sno.gui.gep.panels.disjointtan;

import edu.njit.cs.saboc.blu.core.abn.disjoint.DisjointAbstractionNetwork;
import edu.njit.cs.saboc.blu.core.abn.disjoint.DisjointNode;
import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.DisjointPArea;
import edu.njit.cs.saboc.blu.core.abn.tan.Cluster;
import edu.njit.cs.saboc.blu.core.abn.tan.ClusterTribalAbstractionNetwork;
import edu.njit.cs.saboc.blu.core.abn.tan.TANFactory;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.NodeDashboardPanel;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.NodeOptionsPanel;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.disjointabn.buttons.CreateAncestorDisjointAbNButton;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.disjointabn.buttons.ExpandAggregateDisjointNodeButton;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.optionbuttons.node.CreateTANFromSinglyRootedNodeButton;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.optionbuttons.node.ExportSinglyRootedNodeButton;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.optionbuttons.node.NodeHelpButton;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.optionbuttons.PopoutDetailsButton;
import edu.njit.cs.saboc.blu.sno.gui.gep.configuration.listener.DisplayTANAction;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.disjointtan.configuration.SCTDisjointTANConfiguration;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.optionbuttons.SCTOpenBrowserButton;

/**
 *
 * @author Chris O
 */
public class SCTDisjointClusterOptionsPanel extends NodeOptionsPanel {

    public SCTDisjointClusterOptionsPanel(SCTDisjointTANConfiguration config, boolean forAggregate) {
        
        SCTOpenBrowserButton openBrowserButton = new SCTOpenBrowserButton(
                config, 
                config.getRelease(),
                config.getUIConfiguration().getFrameManager());

        super.addOptionButton(openBrowserButton);
        
        if(forAggregate) {
            ExpandAggregateDisjointNodeButton expandAggregateButton = new ExpandAggregateDisjointNodeButton(config, (disjointAbN) -> {
                DisjointAbstractionNetwork<DisjointNode<Cluster>, ClusterTribalAbstractionNetwork<Cluster>, Cluster> disjointTAN
                        = (DisjointAbstractionNetwork<DisjointNode<Cluster>, ClusterTribalAbstractionNetwork<Cluster>, Cluster>) disjointAbN;

                config.getUIConfiguration().getAbNDisplayManager().displayDisjointTribalAbstractionNetwork(disjointTAN);
            });
            
            super.addOptionButton(expandAggregateButton);
        }

        CreateAncestorDisjointAbNButton ancestorBtn = new CreateAncestorDisjointAbNButton(config, (disjointAbN) -> {

            DisjointAbstractionNetwork<DisjointNode<Cluster>, ClusterTribalAbstractionNetwork<Cluster>, Cluster> disjointTAN
                    = (DisjointAbstractionNetwork<DisjointNode<Cluster>, ClusterTribalAbstractionNetwork<Cluster>, Cluster>) disjointAbN;

            config.getUIConfiguration().getAbNDisplayManager().displayDisjointTribalAbstractionNetwork(disjointTAN);
        });
        
        super.addOptionButton(ancestorBtn);
                
        
        CreateTANFromSinglyRootedNodeButton tanBtn = new CreateTANFromSinglyRootedNodeButton(
                new TANFactory(config.getRelease()),
                config, 
                new DisplayTANAction(config.getUIConfiguration().getAbNDisplayManager()));
        
        super.addOptionButton(tanBtn);
        

        ExportSinglyRootedNodeButton exportBtn = new ExportSinglyRootedNodeButton(config);

        super.addOptionButton(exportBtn);
        

        PopoutDetailsButton popoutBtn = new PopoutDetailsButton("disjoint cluster", () -> {
            DisjointPArea parea = (DisjointPArea) super.getCurrentNode().get();

            NodeDashboardPanel anp = config.getUIConfiguration().createNodeDetailsPanel();
            anp.setContents(parea);

            return anp;
        });

        super.addOptionButton(popoutBtn);

        NodeHelpButton helpBtn = new NodeHelpButton(config);

        super.addOptionButton(helpBtn);
    }
}
