package edu.njit.cs.saboc.blu.sno.gui.gep.panels.tan.reports;

import SnomedShared.Concept;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.reports.AbstractAbNContainerReportPanel;
import edu.njit.cs.saboc.blu.sno.abn.tan.local.SCTBand;
import edu.njit.cs.saboc.blu.sno.abn.tan.local.SCTCluster;
import edu.njit.cs.saboc.blu.sno.abn.tan.local.SCTTribalAbstractionNetwork;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.tan.configuration.SCTTANConfiguration;

/**
 *
 * @author Chris O
 */
public class SCTTANBandReportPanel extends AbstractAbNContainerReportPanel<SCTTribalAbstractionNetwork, SCTBand, SCTCluster, Concept> {
    public SCTTANBandReportPanel(SCTTANConfiguration config) {
        super(config);
    }
}
