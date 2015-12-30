package edu.njit.cs.saboc.blu.sno.gui.dialogs.panels.concepthierarchy;

import edu.njit.cs.saboc.blu.core.abn.tan.TribalAbstractionNetwork;
import edu.njit.cs.saboc.blu.sno.abn.tan.local.SCTCluster;
import edu.njit.cs.saboc.blu.sno.abn.tan.local.SCTTribalAbstractionNetwork;
import edu.njit.cs.saboc.blu.sno.datastructure.hierarchy.SCTConceptHierarchy;

/**
 *
 * @author Cgris
 */
public class SCTTANHierarchyLoader extends SCTConceptGroupHierarchyLoader<SCTCluster> {
    
    private TribalAbstractionNetwork tan;
    
    public SCTTANHierarchyLoader(SCTTribalAbstractionNetwork tan, SCTCluster cluster, SCTConceptHierarchyViewPanel panel) {
        super(cluster, panel);
        
        this.tan = tan;
    }
    
    public SCTConceptHierarchy getGroupHierarchy(SCTCluster cluster) {
        return cluster.getHierarchy();
    }
}
