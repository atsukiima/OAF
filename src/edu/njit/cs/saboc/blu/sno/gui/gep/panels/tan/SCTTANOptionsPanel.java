package edu.njit.cs.saboc.blu.sno.gui.gep.panels.tan;

import edu.njit.cs.saboc.blu.core.abn.tan.ClusterTribalAbstractionNetwork;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.AbNOptionsPanel;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.optionbuttons.abn.AbNHelpButton;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.optionbuttons.abn.ExportAbNButton;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.optionbuttons.abn.PopoutAbNDetailsButton;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.optionbuttons.abn.SavePNGButton;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.tan.configuration.SCTTANConfiguration;

/**
 *
 * @author Chris O
 */
public class SCTTANOptionsPanel extends AbNOptionsPanel<ClusterTribalAbstractionNetwork> {
    
    public SCTTANOptionsPanel(SCTTANConfiguration config) {
        
        AbNHelpButton helpBtn = new AbNHelpButton(config);
        ExportAbNButton exportBtn = new ExportAbNButton("Export Tribal Abstraction Network", config);
        SavePNGButton pngBtn = new SavePNGButton(config);
        PopoutAbNDetailsButton popoutBtn = new PopoutAbNDetailsButton(config);
        
        helpBtn.setCurrentAbN(config.getAbstractionNetwork());
        exportBtn.setCurrentAbN(config.getAbstractionNetwork());
        pngBtn.setCurrentAbN(config.getAbstractionNetwork());
        popoutBtn.setCurrentEntity(config.getAbstractionNetwork());
        
        super.addOptionButton(helpBtn);
        super.addOptionButton(exportBtn);
        super.addOptionButton(pngBtn);
        super.addOptionButton(popoutBtn);
    }
}
