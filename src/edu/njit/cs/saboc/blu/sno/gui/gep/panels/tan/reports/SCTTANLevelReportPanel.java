package edu.njit.cs.saboc.blu.sno.gui.gep.panels.tan.reports;

import SnomedShared.Concept;
import SnomedShared.overlapping.CommonOverlapSet;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.reports.GenericAbNLevelReportPanel;
import edu.njit.cs.saboc.blu.sno.abn.tan.local.SCTCluster;
import edu.njit.cs.saboc.blu.sno.abn.tan.local.SCTTribalAbstractionNetwork;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.SCTTANConfiguration;

/**
 *
 * @author Chris O
 */
public class SCTTANLevelReportPanel extends GenericAbNLevelReportPanel<Concept, SCTTribalAbstractionNetwork, CommonOverlapSet, SCTCluster> {
    public SCTTANLevelReportPanel(SCTTANConfiguration config) {
        super(config);
    }
}
