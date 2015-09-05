package edu.njit.cs.saboc.blu.sno.abn.tan;

import SnomedShared.Concept;
import SnomedShared.overlapping.ClusterSummary;
import SnomedShared.overlapping.CommonOverlapSet;
import edu.njit.cs.saboc.blu.core.abn.GroupHierarchy;
import edu.njit.cs.saboc.blu.core.abn.PartitionedAbstractionNetwork;
import edu.njit.cs.saboc.blu.sno.abn.SCTAbstractionNetwork;
import edu.njit.cs.saboc.blu.sno.sctdatasource.SCTDataSource;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Chris
 */
public class TribalAbstractionNetwork<CLUSTER_T extends ClusterSummary> extends PartitionedAbstractionNetwork<CommonOverlapSet, CLUSTER_T> 
        implements SCTAbstractionNetwork<TribalAbstractionNetwork> {
    
    protected String sctVersion;
    
    protected SCTDataSource dataSource;

    private ArrayList<CLUSTER_T> disjointClusters;

    private ArrayList<CLUSTER_T> nonOverlappingDisjointClusters;

    private HashMap<Long, String> patriarchNames = new HashMap<Long, String>();

    public TribalAbstractionNetwork(
            ArrayList<CommonOverlapSet> overlapSets,
            HashMap<Integer, CLUSTER_T> clusters,
            GroupHierarchy<CLUSTER_T> clusterHierarchy,
            String SNOMEDVersion,
            ArrayList<CLUSTER_T> entryPoints,
            ArrayList<CLUSTER_T> nonContributingEntryPoints,
            SCTDataSource dataSource) {

        super(overlapSets, clusters, clusterHierarchy);
        
        this.dataSource = dataSource;
        
        this.sctVersion = SNOMEDVersion;

        this.disjointClusters = entryPoints;
        this.nonOverlappingDisjointClusters = nonContributingEntryPoints;

        for(CLUSTER_T entryPoint : entryPoints) {
            String entryPointName = entryPoint.getHeaderConcept().getName();
            entryPointName = entryPointName.substring(0, entryPointName.lastIndexOf("(")).trim();

            patriarchNames.put(entryPoint.getHeaderConcept().getId(),
                    entryPointName);
        }
    }
    
    public TribalAbstractionNetwork getAbstractionNetwork() {
        return this;
    }
    
    public String getSCTVersion() {
        return sctVersion;
    }
    
    public SCTDataSource getDataSource() {
        return dataSource;
    }

    public ArrayList<CLUSTER_T> getHierarchyEntryPoints() {
        return disjointClusters;
    }

    public ArrayList<CLUSTER_T> getNonOverlappingEntryPoints() {
        return nonOverlappingDisjointClusters;
    }

    public ArrayList<CommonOverlapSet> getBands() {
        return (ArrayList<CommonOverlapSet>)containers;
    }

    public int getClusterCount() {
        return getGroupCount();
    }

    public int getCommonOverlapSetCount() {
        return getContainerCount();
    }

    public HashMap<Integer, CLUSTER_T> getClusters() {
        return (HashMap<Integer, CLUSTER_T>)groups;
    }

    public CLUSTER_T getClusterFromRootConceptId(long rootConceptId) {
        return (CLUSTER_T)getGroupFromRootConceptId(rootConceptId);
    }

    public HashMap<Long, String> getPatriarchNames() {
        return patriarchNames;
    }
    
    public CLUSTER_T getRootCluster() {
        return disjointClusters.get(0);
    }
    
    public CLUSTER_T getRootGroup() {
        return getRootCluster();
    }
    
    public ArrayList<CommonOverlapSet> searchBands(String term) {
        ArrayList<CommonOverlapSet> searchResults = new ArrayList<CommonOverlapSet>();

        ArrayList<CommonOverlapSet> bands = this.getBands();

        String [] searchPatriarchs = term.split(", ");

        if(searchPatriarchs == null) {
            return new ArrayList<CommonOverlapSet>();
        }

        for(CommonOverlapSet band : bands) {
            ArrayList<String> bandPatriarchNames = new ArrayList<String>();

            for(long patriarchId : band.getSetEntryPoints()) {
                bandPatriarchNames.add(patriarchNames.get(patriarchId));
            }

            boolean allPatriarchsFound = true;

            for(String patriarch : searchPatriarchs) {

                boolean patriarchFound = false;

                for(String bandPatriarch : bandPatriarchNames) {
                    if(bandPatriarch.toLowerCase().contains(patriarch)) {
                        patriarchFound = true;
                        break;
                    }
                }

                if(!patriarchFound) {
                    allPatriarchsFound = false;
                    break;
                }
            }

            if(allPatriarchsFound) {
                searchResults.add(band);
            }
        }

        return searchResults;
    }
}
