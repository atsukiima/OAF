
package edu.njit.cs.saboc.blu.sno.gui.gep.panels;

import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.abn.AbstractAbNDetailsPanel;
import edu.njit.cs.saboc.blu.sno.abn.tan.local.SCTTribalAbstractionNetwork;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.tan.configuration.SCTTANConfiguration;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.tan.reports.SCTTANBandReportPanel;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.tan.reports.SCTTANLevelReportPanel;

/**
 *
 * @author Chris O
 */
public class SCTTANDetailsPanel extends AbstractAbNDetailsPanel<SCTTribalAbstractionNetwork> {

    public SCTTANDetailsPanel(SCTTANConfiguration config) {
        super(config);

        SCTTANLevelReportPanel levelReportPanel = new SCTTANLevelReportPanel(config);
        levelReportPanel.displayAbNReport(config.getDataConfiguration().getTribalAbstractionNetwork());

        SCTTANBandReportPanel bandReportPanel = new SCTTANBandReportPanel(config);
        bandReportPanel.displayAbNReport(config.getDataConfiguration().getTribalAbstractionNetwork());

        super.addDetailsTab("Tribal Abstraction Network Levels", levelReportPanel);
        super.addDetailsTab("Bands in Tribal Abstraction Network", bandReportPanel);

    }
}
