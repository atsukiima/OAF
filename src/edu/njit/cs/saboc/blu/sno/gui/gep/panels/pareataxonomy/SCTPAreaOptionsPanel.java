package edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy;

import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.PArea;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.NodeOptionsPanel;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.NodeDashboardPanel;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.optionbuttons.CreateTANFromSinglyRootedNodeButton;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.optionbuttons.ExportSinglyRootedNodeButton;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.optionbuttons.PopoutNodeDetailsButton;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.pareataxonomy.buttons.CreateAncestorSubtaxonomyButton;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.pareataxonomy.buttons.CreateRootSubtaxonomyButton;
import edu.njit.cs.saboc.blu.sno.gui.gep.configuration.listener.DisplayPAreaTaxonomyListener;
import edu.njit.cs.saboc.blu.sno.gui.gep.configuration.listener.DisplayTANListener;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy.configuration.SCTPAreaTaxonomyConfiguration;

/**
 *
 * @author Chris O
 */
public class SCTPAreaOptionsPanel extends NodeOptionsPanel {

    public SCTPAreaOptionsPanel(SCTPAreaTaxonomyConfiguration config) {

        CreateRootSubtaxonomyButton rootSubtaxonomyBtn = new CreateRootSubtaxonomyButton(config,
            new DisplayPAreaTaxonomyListener(config.getUIConfiguration().getDisplayFrameListener()));
        
        super.addOptionButton(rootSubtaxonomyBtn);
        
        
        CreateAncestorSubtaxonomyButton ancestorSubtaxonomyBtn = new CreateAncestorSubtaxonomyButton(config, 
            new DisplayPAreaTaxonomyListener(config.getUIConfiguration().getDisplayFrameListener()));
        
        super.addOptionButton(ancestorSubtaxonomyBtn);
        
        
        CreateTANFromSinglyRootedNodeButton tanBtn = new CreateTANFromSinglyRootedNodeButton(config, 
            new DisplayTANListener(config.getUIConfiguration().getDisplayFrameListener()));
        
        super.addOptionButton(tanBtn);
        
        
        PopoutNodeDetailsButton popoutBtn = new PopoutNodeDetailsButton("partial-area", () -> {
            PArea parea = (PArea)super.getCurrentNode().get();
            
            NodeDashboardPanel anp = config.getUIConfiguration().createGroupDetailsPanel();
            anp.setContents(parea);

            return anp;
        });

        super.addOptionButton(popoutBtn);
        
        
        ExportSinglyRootedNodeButton exportBtn = new ExportSinglyRootedNodeButton(config);
        
        super.addOptionButton(exportBtn);
    }
}
