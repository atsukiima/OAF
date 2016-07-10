package edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy;

import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.Area;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.NodeOptionsPanel;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.NodeDashboardPanel;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.optionbuttons.CreateTANFromPartitionedNodeButton;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.optionbuttons.ExportPartitionedNodeButton;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.optionbuttons.PopoutNodeDetailsButton;
import edu.njit.cs.saboc.blu.sno.gui.gep.configuration.listener.DisplayTANListener;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.optionbuttons.pareataxonomy.SCTCreateDisjointTaxonomyButton;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy.configuration.SCTPAreaTaxonomyConfiguration;

/**
 *
 * @author Chris O
 */
public class SCTAreaOptionsPanel extends NodeOptionsPanel {
    
    public SCTAreaOptionsPanel(SCTPAreaTaxonomyConfiguration config) {
        
        SCTCreateDisjointTaxonomyButton disjointTaxonomyBtn = new SCTCreateDisjointTaxonomyButton(config);
        
        super.addOptionButton(disjointTaxonomyBtn);
        

        CreateTANFromPartitionedNodeButton tanBtn = new CreateTANFromPartitionedNodeButton(config,
                new DisplayTANListener(config.getUIConfiguration().getDisplayFrameListener()));
        
        super.addOptionButton(tanBtn);
                
        PopoutNodeDetailsButton popoutBtn = new PopoutNodeDetailsButton("area", () -> {
            Area area = (Area)super.getCurrentNode().get();
            
            NodeDashboardPanel anp = config.getUIConfiguration().createContainerDetailsPanel();
            anp.setContents(area);

            return anp;
        });

        super.addOptionButton(popoutBtn);
        
        
        ExportPartitionedNodeButton exportBtn = new ExportPartitionedNodeButton(config);
        
        super.addOptionButton(exportBtn);
    }
}
