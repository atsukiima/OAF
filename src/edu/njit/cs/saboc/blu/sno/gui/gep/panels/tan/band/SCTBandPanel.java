
package edu.njit.cs.saboc.blu.sno.gui.gep.panels.tan.band;

import SnomedShared.Concept;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.PartitionedNodePanel;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.entry.PartitionedNodeConceptEntry;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.tan.band.BandClusterListPanel;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.tan.band.BandDetailsPanel;
import edu.njit.cs.saboc.blu.sno.abn.tan.local.SCTBand;
import edu.njit.cs.saboc.blu.sno.abn.tan.local.SCTCluster;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.SCTClusterList;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.SCTConceptList;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.tan.configuration.SCTTANConfiguration;

/**
 *
 * @author Chris O
 */
public class SCTBandPanel extends PartitionedNodePanel<SCTBand, SCTCluster, Concept, PartitionedNodeConceptEntry<Concept, SCTCluster>, SCTTANConfiguration> {
    
    private final SCTTANConfiguration config;

    public SCTBandPanel(SCTTANConfiguration config) {
        super(new BandDetailsPanel<SCTBand, SCTCluster, Concept>(new SCTBandOptionsPanel(config), config), 
                new BandClusterListPanel<SCTBand, SCTCluster, Concept>(new SCTClusterList(config), new SCTConceptList(), config), 
                config);

        this.config = config;
    }

    @Override
    protected String getNodeTitle(SCTBand band) {
        return config.getTextConfiguration().getContainerName(band);
    }
}
