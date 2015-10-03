package edu.njit.cs.saboc.blu.sno.gui.gep.panels.tan.cluster;

import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.models.BLUAbstractChildGroupTableModel;
import edu.njit.cs.saboc.blu.sno.abn.tan.local.SCTCluster;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.tan.configuration.SCTTANConfiguration;

/**
 *
 * @author Chris O
 */
public class SCTChildClusterTableModel extends BLUAbstractChildGroupTableModel<SCTCluster> {

    private final SCTTANConfiguration config;
    
    public SCTChildClusterTableModel(SCTTANConfiguration config) {
        
        super(new String[] {
            "Child Cluster", 
            "# Concepts", 
            "Tribal Band"
        });
        
        this.config = config;
    }
    

    @Override
    protected Object[] createRow(SCTCluster cluster) {       

        return new Object[] {
            cluster.getRoot().getName(),
            cluster.getConceptCount(),
            config.getTextConfiguration().getGroupsContainerName(cluster).replaceAll(", ", "\n")
        };
    }
}
