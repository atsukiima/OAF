package edu.njit.cs.saboc.blu.sno.abn.tan.local;

/**
 *
 * @author Chris
 */
public class ConceptClusterInfo {
    private long hierarchyId;
    
    private long clusterRootId;
    
    public ConceptClusterInfo(long hierarchyId, long clusterRootId) {
        this.hierarchyId = hierarchyId;
        this.clusterRootId = clusterRootId;
    }
    
    public long getHierarchyId() {
        return hierarchyId;
    }
    
    public long getClusterRootId() {
        return clusterRootId;
    }
}
