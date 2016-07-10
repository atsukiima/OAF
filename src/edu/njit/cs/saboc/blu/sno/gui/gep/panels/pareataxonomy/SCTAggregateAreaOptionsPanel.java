package edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy;

import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.Area;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.NodeDashboardPanel;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.NodeOptionsPanel;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.optionbuttons.CreateTANFromPartitionedNodeButton;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.optionbuttons.PopoutNodeDetailsButton;
import edu.njit.cs.saboc.blu.sno.gui.gep.configuration.listener.DisplayTANListener;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.optionbuttons.pareataxonomy.SCTCreateDisjointTaxonomyButton;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy.configuration.SCTPAreaTaxonomyConfiguration;

/**
 *
 * @author Chris O
 */
public class SCTAggregateAreaOptionsPanel extends NodeOptionsPanel {
    
    public SCTAggregateAreaOptionsPanel(SCTPAreaTaxonomyConfiguration config) {
              
        super.addOptionButton(new SCTCreateDisjointTaxonomyButton(config));

         CreateTANFromPartitionedNodeButton tanBtn = new CreateTANFromPartitionedNodeButton(config, new DisplayTANListener(
            config.getUIConfiguration().getDisplayFrameListener()));
        
        super.addOptionButton(tanBtn);
        
        PopoutNodeDetailsButton popoutBtn = new PopoutNodeDetailsButton("aggregate area", () -> {
            Area area = (Area)getCurrentNode().get();
            
            NodeDashboardPanel anp = config.getUIConfiguration().createContainerDetailsPanel();
            anp.setContents(area);

            return anp;
        });
        
        super.addOptionButton(popoutBtn);
    }
}
