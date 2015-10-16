
package edu.njit.cs.saboc.blu.sno.gui.gep.panels.tan.cluster;

import SnomedShared.Concept;
import edu.njit.cs.saboc.blu.core.abn.GenericParentGroupInfo;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.models.BLUAbstractParentGroupTableModel;
import edu.njit.cs.saboc.blu.sno.abn.tan.local.SCTCluster;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.tan.configuration.SCTTANConfiguration;

/**
 *
 * @author Chris O
 */
public class SCTParentClusterTableModel extends BLUAbstractParentGroupTableModel<Concept, SCTCluster, GenericParentGroupInfo<Concept, SCTCluster>> {

    private final SCTTANConfiguration config;
    
    public SCTParentClusterTableModel(SCTTANConfiguration config) {
        super(new String[] {
            "Parent Concept", 
            "Parent Concept ID", 
            "Parent Cluster", 
            "# Concepts in Parent Cluster", 
            "Band"
        });
        
        this.config = config;
    }

    @Override
    protected Object[] createRow(GenericParentGroupInfo<Concept, SCTCluster> item) {
       
        return new Object[] {
            item.getParentConcept().getName(),
            item.getParentConcept().getId(),
            item.getParentGroup().getRoot().getName(),
            item.getParentGroup().getConceptCount(),
            config.getTextConfiguration().getGroupsContainerName(item.getParentGroup())
        };
    }
}
