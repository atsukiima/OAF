package edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy;

import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.PAreaTaxonomy;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.AbNOptionsPanel;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.optionbuttons.abn.ExportAbNButton;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.optionbuttons.abn.PopoutAbNDetailsButton;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.optionbuttons.abn.SavePNGButton;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.pareataxonomy.buttons.PAreaTaxonomyHelpButton;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy.configuration.SCTPAreaTaxonomyConfiguration;

/**
 *
 * @author Chris O
 */
public class SCTPAreaTaxonomyOptionsPanel extends AbNOptionsPanel<PAreaTaxonomy> {
    
    public SCTPAreaTaxonomyOptionsPanel(SCTPAreaTaxonomyConfiguration config) {
        PAreaTaxonomyHelpButton pareaHelpBtn = new PAreaTaxonomyHelpButton(config);
        ExportAbNButton exportBtn = new ExportAbNButton("Export Partial-area Taxonomy", config);
        SavePNGButton pngBtn = new SavePNGButton(config);
        PopoutAbNDetailsButton popoutBtn = new PopoutAbNDetailsButton(config);
        
        pareaHelpBtn.setCurrentAbN(config.getPAreaTaxonomy());
        exportBtn.setCurrentAbN(config.getPAreaTaxonomy());
        pngBtn.setCurrentAbN(config.getPAreaTaxonomy());
        popoutBtn.setCurrentEntity(config.getPAreaTaxonomy());
        
        super.addOptionButton(pareaHelpBtn);
        super.addOptionButton(exportBtn);
        super.addOptionButton(pngBtn);
        super.addOptionButton(popoutBtn);
    }
}
