package edu.njit.cs.saboc.blu.sno.gui.dialogs.panels.concepthierarchy;

import SnomedShared.overlapping.ClusterSummary;
import edu.njit.cs.saboc.blu.sno.abn.tan.TribalAbstractionNetwork;
import edu.njit.cs.saboc.blu.sno.datastructure.hierarchy.SCTConceptHierarchy;

/**
 *
 * @author Cgris
 */
public class SCTTANHierarchyLoader extends SCTConceptGroupHierarchyLoader<ClusterSummary> {
    
    private TribalAbstractionNetwork tan;
    
    public SCTTANHierarchyLoader(TribalAbstractionNetwork tan, ClusterSummary cluster, SCTConceptHierarchyViewPanel panel) {
        super(cluster, panel);
        
        this.tan = tan;
    }
    
    public SCTConceptHierarchy getGroupHierarchy(ClusterSummary cluster) {
        return tan.getDataSource().getClusterConceptHierarchy(tan, cluster);
    }
}
