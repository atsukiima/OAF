
package edu.njit.cs.saboc.blu.sno.gui.gep.panels.tan.band;

import SnomedShared.Concept;
import SnomedShared.overlapping.CommonOverlapSet;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.AbstractContainerPanel;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.entry.ContainerConceptEntry;
import edu.njit.cs.saboc.blu.sno.abn.tan.local.SCTCluster;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.SCTTANConfiguration;

/**
 *
 * @author Chris O
 */
public class SCTBandPanel extends AbstractContainerPanel<CommonOverlapSet, SCTCluster, Concept, ContainerConceptEntry<Concept, SCTCluster>> {
    
    private final SCTTANConfiguration config;

    public SCTBandPanel(SCTTANConfiguration config) {
        super(new SCTBandDetailsPanel(config), 
                new SCTBandClusterListPanel(config), 
                config);

        this.config = config;
    }

    @Override
    protected String getNodeTitle(CommonOverlapSet area) {
        return config.getContainerName(area);
    }
}
