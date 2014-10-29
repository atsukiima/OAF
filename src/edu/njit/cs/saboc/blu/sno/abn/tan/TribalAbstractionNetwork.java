package edu.njit.cs.saboc.blu.sno.abn.tan;

import SnomedShared.Concept;
import SnomedShared.generic.GenericConceptGroup;
import SnomedShared.overlapping.ClusterSummary;
import SnomedShared.overlapping.CommonOverlapSet;
import edu.njit.cs.saboc.blu.sno.abn.SCTAbstractionNetwork;
import edu.njit.cs.saboc.blu.sno.sctdatasource.SCTDataSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 *
 * @author Chris
 */
public class TribalAbstractionNetwork extends SCTAbstractionNetwork {

    private ArrayList<ClusterSummary> disjointClusters;

    private ArrayList<ClusterSummary> nonOverlappingDisjointClusters;

    private HashMap<Long, String> patriarchNames = new HashMap<Long, String>();

    public TribalAbstractionNetwork(Concept SNOMEDHierarchyRoot,
            ArrayList<CommonOverlapSet> overlapSets,
            HashMap<Integer, ? extends ClusterSummary> clusters,
            HashMap<Integer, HashSet<Integer>> clusterHierarchy,
            String SNOMEDVersion,
            ArrayList<ClusterSummary> entryPoints,
            ArrayList<ClusterSummary> nonContributingEntryPoints,
            SCTDataSource dataSource) {

        super(SNOMEDHierarchyRoot, overlapSets, clusters, clusterHierarchy, SNOMEDVersion, dataSource);

        this.disjointClusters = entryPoints;
        this.nonOverlappingDisjointClusters = nonContributingEntryPoints;

        for(ClusterSummary entryPoint : entryPoints) {
            String entryPointName = entryPoint.getHeaderConcept().getName();
            entryPointName = entryPointName.substring(0, entryPointName.lastIndexOf("(")).trim();

            patriarchNames.put(entryPoint.getHeaderConcept().getId(),
                    entryPointName);
        }
    }

    public ArrayList<ClusterSummary> getHierarchyEntryPoints() {
        return disjointClusters;
    }

    public ArrayList<ClusterSummary> getNonOverlappingEntryPoints() {
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

    public HashMap<Integer, ClusterSummary> getClusters() {
        return (HashMap<Integer, ClusterSummary>)groups;
    }

    public HashSet<Integer> getClusterChildren(int clusterId) {
        return groupHierarchy.get(clusterId);
    }
    
    public ClusterSummary getClusterFromRootConceptId(long rootConceptId) {
        return (ClusterSummary)getGroupFromRootConceptId(rootConceptId);
    }

    public HashMap<Long, String> getPatriarchNames() {
        return patriarchNames;
    }
    
    public ClusterSummary getRootCluster() {
        return disjointClusters.get(0);
    }
    
    public GenericConceptGroup getRootGroup() {
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
