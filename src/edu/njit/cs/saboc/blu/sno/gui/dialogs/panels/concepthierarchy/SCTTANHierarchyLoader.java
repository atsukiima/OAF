package edu.njit.cs.saboc.blu.sno.gui.dialogs.panels.concepthierarchy;

import SnomedShared.Concept;
import SnomedShared.overlapping.ClusterSummary;
import edu.njit.cs.saboc.blu.core.datastructure.hierarchy.SingleRootedHierarchy;
import edu.njit.cs.saboc.blu.sno.abn.tan.TribalAbstractionNetwork;

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
    
    public SingleRootedHierarchy<Concept> getGroupHierarchy(ClusterSummary cluster) {
        return tan.getDataSource().getClusterConceptHierarchy(tan, cluster);
    }
}
