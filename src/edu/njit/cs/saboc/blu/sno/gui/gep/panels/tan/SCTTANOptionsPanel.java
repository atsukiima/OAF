package edu.njit.cs.saboc.blu.sno.gui.gep.panels.tan;

import edu.njit.cs.saboc.blu.core.abn.tan.ClusterTribalAbstractionNetwork;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.AbNOptionsPanel;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.optionbuttons.abn.AbNReportsBtn;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.optionbuttons.abn.ExportAbNButton;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.optionbuttons.abn.HighlightOverlappingButton;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.optionbuttons.abn.PopoutAbNDetailsButton;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.optionbuttons.abn.SavePNGButton;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.optionbuttons.abn.ShowSourceHierarchyButton;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.tan.buttons.TANHelpButton;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.tan.configuration.SCTTANConfiguration;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.tan.reports.SCTTANReportDialog;

/**
 *
 * @author Chris O
 */
public class SCTTANOptionsPanel extends AbNOptionsPanel<ClusterTribalAbstractionNetwork> {
    
    public SCTTANOptionsPanel(SCTTANConfiguration config) {
        
        TANHelpButton helpBtn = new TANHelpButton(config);
        ExportAbNButton exportBtn = new ExportAbNButton("Export Tribal Abstraction Network", config);
        SavePNGButton pngBtn = new SavePNGButton(config);
        PopoutAbNDetailsButton popoutBtn = new PopoutAbNDetailsButton(config);
        
         AbNReportsBtn<ClusterTribalAbstractionNetwork> reportsBtn = new AbNReportsBtn<ClusterTribalAbstractionNetwork>() {

             @Override
             public void displayReportsAndMetrics() {

                 SCTTANReportDialog reportDialog = new SCTTANReportDialog(config);
                 reportDialog.showReports(config.getAbstractionNetwork());
                 reportDialog.setModal(true);

                 reportDialog.setVisible(true);
             }

            @Override
            public void setEnabledFor(ClusterTribalAbstractionNetwork entity) {
                this.setEnabled(true);
            }
        };
                 
        ShowSourceHierarchyButton showHierarchyBtn = new ShowSourceHierarchyButton();
        HighlightOverlappingButton showOverlappingBtn = new HighlightOverlappingButton(config);

        showHierarchyBtn.setCurrentAbN(config.getAbstractionNetwork());
        showOverlappingBtn.setCurrentAbN(config.getAbstractionNetwork());
        reportsBtn.setCurrentAbN(config.getAbstractionNetwork());
        helpBtn.setCurrentAbN(config.getAbstractionNetwork());
        exportBtn.setCurrentAbN(config.getAbstractionNetwork());
        pngBtn.setCurrentAbN(config.getAbstractionNetwork());
        popoutBtn.setCurrentEntity(config.getAbstractionNetwork());
        
        
        super.addOptionButton(showHierarchyBtn);
        super.addOptionButton(showOverlappingBtn);
        super.addOptionButton(reportsBtn);
        super.addOptionButton(helpBtn);
        super.addOptionButton(exportBtn);
        super.addOptionButton(pngBtn);
        super.addOptionButton(popoutBtn);
    }
}
