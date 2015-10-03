package edu.njit.cs.saboc.blu.sno.gui.gep.panels.tan.band;

import SnomedShared.Concept;
import SnomedShared.overlapping.CommonOverlapSet;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.AbstractContainerGroupListPanel;
import edu.njit.cs.saboc.blu.sno.abn.tan.local.SCTCluster;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.SCTClusterList;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.SCTConceptList;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.tan.configuration.SCTTANConfiguration;
import java.util.ArrayList;

/**
 *
 * @author Chris O
 */
public class SCTBandClusterListPanel extends AbstractContainerGroupListPanel<CommonOverlapSet, SCTCluster, Concept> {

    public SCTBandClusterListPanel(SCTTANConfiguration config) {
        super(new SCTClusterList(config), 
                new SCTConceptList(), 
                config);
    }
    
    @Override
    public ArrayList<Concept> getSortedConceptList(SCTCluster parea) {
        return configuration.getDataConfiguration().getSortedConceptList(parea);
    }
}
