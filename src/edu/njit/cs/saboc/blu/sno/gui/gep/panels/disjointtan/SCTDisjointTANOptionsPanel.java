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
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.optionbuttons.CreateTANFromSinglyRootedNodeButton;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.optionbuttons.ExportSinglyRootedNodeButton;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.optionbuttons.HelpButton;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.optionbuttons.PopoutNodeDetailsButton;
import edu.njit.cs.saboc.blu.sno.gui.gep.configuration.listener.DisplayTANAction;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.disjointtan.configuration.SCTDisjointTANConfiguration;

/**
 *
 * @author Chris O
 */
public class SCTDisjointTANOptionsPanel extends NodeOptionsPanel {

    public SCTDisjointTANOptionsPanel(SCTDisjointTANConfiguration config, boolean forAggregate) {
        
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
                new TANFactory(config.getAbstractionNetwork().getDerivation().getSourceOntology()),
                config, 
                new DisplayTANAction(config.getUIConfiguration().getAbNDisplayManager()));
        
        super.addOptionButton(tanBtn);
        

        ExportSinglyRootedNodeButton exportBtn = new ExportSinglyRootedNodeButton(config);

        super.addOptionButton(exportBtn);
        

        PopoutNodeDetailsButton popoutBtn = new PopoutNodeDetailsButton("disjoint cluster", () -> {
            DisjointPArea parea = (DisjointPArea) super.getCurrentNode().get();

            NodeDashboardPanel anp = config.getUIConfiguration().createNodeDetailsPanel();
            anp.setContents(parea);

            return anp;
        });

        super.addOptionButton(popoutBtn);

        HelpButton helpBtn = new HelpButton(config);

        super.addOptionButton(helpBtn);
    }
}
