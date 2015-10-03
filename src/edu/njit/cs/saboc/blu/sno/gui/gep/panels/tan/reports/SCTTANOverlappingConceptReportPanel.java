package edu.njit.cs.saboc.blu.sno.gui.gep.panels.tan.reports;

import SnomedShared.Concept;
import SnomedShared.overlapping.CommonOverlapSet;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.reports.GenericOverlappingConceptReportPanel;
import edu.njit.cs.saboc.blu.sno.abn.tan.local.SCTCluster;
import edu.njit.cs.saboc.blu.sno.abn.tan.local.SCTTribalAbstractionNetwork;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.tan.configuration.SCTTANConfiguration;

/**
 *
 * @author Chris O
 */
public class SCTTANOverlappingConceptReportPanel extends GenericOverlappingConceptReportPanel<Concept, SCTTribalAbstractionNetwork, CommonOverlapSet, SCTCluster>{
    public SCTTANOverlappingConceptReportPanel(SCTTANConfiguration config) {
        super(config);
    }
}
