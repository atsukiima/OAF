package edu.njit.cs.saboc.blu.sno.gui.gep.panels.tan.cluster;

import SnomedShared.Concept;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.AbstractGroupHierarchyPanel;
import edu.njit.cs.saboc.blu.sno.abn.tan.local.SCTCluster;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.tan.configuration.SCTTANConfiguration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;

/**
 *
 * @author Chris O
 */
public class SCTClusterHierarchyPanel extends AbstractGroupHierarchyPanel<Concept, SCTCluster, SCTTANConfiguration> {
    
    private final SCTTANConfiguration config;

    public SCTClusterHierarchyPanel(SCTTANConfiguration config) {
        super(config,
                new SCTParentClusterTableModel(config),
                new SCTChildClusterTableModel(config));
        
        this.config = config;
    }
    
    @Override
    protected void loadParentGroupInfo(SCTCluster group) {
        parentModel.setContents(group.getParentGroupInformation());
    }

    @Override
    protected void loadChildGroupInfo(SCTCluster group) {
       HashSet<SCTCluster> childClusters = config.getDataConfiguration().getTribalAbstractionNetwork().getChildGroups(group);
       
       ArrayList<SCTCluster> sortedChildClusters = new ArrayList<>(childClusters);
       
       Collections.sort(sortedChildClusters, new Comparator<SCTCluster>() {
           public int compare(SCTCluster a, SCTCluster b) {
               int aLevel = a.getEntryPointSet().size();
               int bLevel = b.getEntryPointSet().size();
               
               if(aLevel == bLevel) {
                   return a.getRoot().getName().compareTo(b.getRoot().getName());
               } else {
                   return aLevel - bLevel;
               }
           }
       });
       
       childModel.setContents(sortedChildClusters);
    }
}
