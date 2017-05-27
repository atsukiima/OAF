package edu.njit.cs.saboc.blu.sno.gui.gep.panels.targetabn;

import edu.njit.cs.saboc.blu.core.abn.targetbased.TargetAbstractionNetwork;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.AbNOptionsPanel;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.optionbuttons.abn.ExportAbNButton;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.optionbuttons.abn.PopoutAbNDetailsButton;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.optionbuttons.abn.SavePNGButton;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.targetabn.buttons.TargetAbNHelpButton;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.targetabn.configuration.SCTTargetAbNConfiguration;

/**
 *
 * @author Chris O
 */
public class SCTTargetAbNOptionsPanel extends AbNOptionsPanel<TargetAbstractionNetwork> {
    
    public SCTTargetAbNOptionsPanel(SCTTargetAbNConfiguration config) {
        
        TargetAbNHelpButton helpBtn = new TargetAbNHelpButton(config);
        ExportAbNButton exportBtn = new ExportAbNButton("Export Target Abstraction Network", config);
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