package edu.njit.cs.saboc.blu.sno.sctdatasource;

import SnomedShared.Concept;
import SnomedShared.OutgoingLateralRelationship;
import SnomedShared.PAreaDetailsForConcept;
import SnomedShared.SearchResult;
import SnomedShared.overlapping.ClusterSummary;
import SnomedShared.pareataxonomy.ConceptPAreaInfo;
import SnomedShared.pareataxonomy.GroupParentInfo;
import SnomedShared.pareataxonomy.PAreaSummary;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.PAreaTaxonomy;
import edu.njit.cs.saboc.blu.sno.abn.tan.TribalAbstractionNetwork;
import edu.njit.cs.saboc.blu.sno.abn.tan.local.ConceptClusterInfo;
import edu.njit.cs.saboc.blu.sno.datastructure.hierarchy.SCTConceptHierarchy;
import edu.njit.cs.saboc.blu.sno.datastructure.hierarchy.SCTMultiRootedConceptHierarchy;
import edu.njit.cs.saboc.blu.sno.sctdatasource.middlewareproxy.MiddlewareAccessorProxy;
import edu.njit.cs.saboc.blu.sno.utils.UtilityMethods;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 *
 * @author Chris
 */
public class SCTRemoteDataSource implements SCTDataSource {
    
    private String sctVersion;
    
    private MiddlewareAccessorProxy proxy = MiddlewareAccessorProxy.getProxy();
    
    public SCTRemoteDataSource() {
        sctVersion = getSupportedVersions().get(0);
    }
    
    public SCTRemoteDataSource(String sctVersion) {
        this.sctVersion = sctVersion;
    }
    
    public Concept getConceptFromId(long id) {
        return proxy.getConceptFromId(sctVersion, id);
    }
    
    public ArrayList<Concept> getConceptParents(Concept c) {
        return proxy.getConceptParents(sctVersion, c);
    }

    public ArrayList<Concept> getConceptChildren(Concept c) {
        return proxy.getConceptChildren(sctVersion, c);
    }

    public ArrayList<String> getConceptSynoynms(Concept c) {
        return proxy.getConceptSynoynms(sctVersion, c);
    }

    public ArrayList<Concept> getConceptSiblings(Concept c) {
        return proxy.getConceptSiblings(sctVersion, c);
    }

    public ArrayList<OutgoingLateralRelationship> getOutgoingLateralRelationships(Concept c) {
        return proxy.getOutgoingLateralRelationships(sctVersion, c);
    }

    public ArrayList<Concept> getHierarchyRootConcepts() {
        return proxy.getHierarchyRootConcepts(sctVersion);
    }

    public ArrayList<PAreaDetailsForConcept> getSummaryOfPAreasContainingConcept(Concept c) {
        return proxy.getSummaryOfPAreasContainingConcept(sctVersion, c.getId());
    }

    public ArrayList<Concept> getConceptsInPArea(PAreaTaxonomy taxonomy, PAreaSummary parea) {
        return proxy.getConceptsInPArea(sctVersion, taxonomy.getSNOMEDHierarchyRoot(), parea.getRoot());
    }

    public HashMap<Long, ArrayList<Concept>> getConceptsInPAreaSet(PAreaTaxonomy taxonomy, ArrayList<PAreaSummary> pareas) {
        ArrayList<Long> rootIds = new ArrayList<Long>();
        
        for(PAreaSummary summary : pareas) {
            rootIds.add(summary.getRoot().getId());
        }
        
        return proxy.getConceptsInPAreaSet(sctVersion, taxonomy.getSNOMEDHierarchyRoot(), rootIds);
    }

    public ArrayList<Concept> getConceptsInCluster(TribalAbstractionNetwork tan, ClusterSummary cluster) {
        return proxy.getConceptsInCluster(sctVersion, tan.getSNOMEDHierarchyRoot(), cluster.getRoot());
    }

    public HashMap<Long, ArrayList<Concept>> getConceptsInClusterSet(TribalAbstractionNetwork tan, ArrayList<ClusterSummary> clusters) {
        ArrayList<Long> clusterRootIds = new ArrayList<Long>();
        
        for(ClusterSummary cluster : clusters) {
            clusterRootIds.add(cluster.getRoot().getId());
        }
        
        return proxy.getConceptsInClusterSet(sctVersion, tan.getSNOMEDHierarchyRoot(), clusterRootIds);
    }

    public ArrayList<SearchResult> searchExact(String term) {
        return proxy.searchExact(sctVersion, term);
    }

    public ArrayList<SearchResult> searchStarting(String term) {
        return proxy.searchStarting(sctVersion, term);
    }

    public ArrayList<SearchResult> searchAnywhere(String term) {
        return proxy.searchAnywhere(sctVersion, term);
    }

    public HashMap<Long, String> getUniqueLateralRelationshipsInHierarchy(PAreaTaxonomy taxonomy, Concept hierarchyRoot) {
        return proxy.getUniqueLateralRelationshipsInHierarchy(sctVersion, hierarchyRoot);
    }

    public int getConceptCountInPAreaHierarchy(PAreaTaxonomy taxonomy, ArrayList<PAreaSummary> pareas) {
        ArrayList<Integer> pareaIds = new ArrayList<Integer>();
        
        for(PAreaSummary summary : pareas) {
            pareaIds.add(summary.getId());
        }
        
        return proxy.getConceptCountInPAreaHierarchy(sctVersion, taxonomy.getSNOMEDHierarchyRoot(), pareaIds);
    }

    public int getConceptCountInClusterHierarchy(TribalAbstractionNetwork tan, ArrayList<ClusterSummary> clusters) {
        ArrayList<Integer> clusterIds = new ArrayList<Integer>();
        
        for(ClusterSummary cluster : clusters) {
            clusterIds.add(cluster.getId());
        }
        
        return proxy.getConceptCountInClusterHierarchy(sctVersion, tan.getSNOMEDHierarchyRoot(), clusterIds);
    }

    public ArrayList<ConceptPAreaInfo> getConceptPAreaInfo(PAreaTaxonomy taxonomy, Concept c) {
        return proxy.getConceptPAreaInfo(sctVersion, c.getId());
    }
    
    public ArrayList<ConceptClusterInfo> getConceptClusterInfo(TribalAbstractionNetwork tan, Concept c) {
        throw new RuntimeException("Method not yet support...");
    }

    public ArrayList<GroupParentInfo> getPAreaParentInfo(PAreaTaxonomy taxonomy, PAreaSummary parea) {
        return proxy.getPAreaParentInfo(sctVersion, taxonomy.getSNOMEDHierarchyRoot(), parea);
    }
    
    public ArrayList<GroupParentInfo> getClusterParentInfo(TribalAbstractionNetwork tan, ClusterSummary cluster) {
        return proxy.getClusterParentInfo(sctVersion, tan.getSNOMEDHierarchyRoot(), cluster);
    }
    
    public SCTConceptHierarchy getPAreaConceptHierarchy(PAreaTaxonomy taxonomy, PAreaSummary parea) {
        HashMap<Concept, ArrayList<Concept>> conceptHierarchy = 
                proxy.getPAreaConceptHierarchy(sctVersion, taxonomy.getSNOMEDHierarchyRoot().getId(), parea.getRoot().getId());
        
        return new SCTConceptHierarchy(parea.getRoot(), UtilityMethods.convertALMapToHSMap(conceptHierarchy));
    }

    public SCTConceptHierarchy getClusterConceptHierarchy(TribalAbstractionNetwork tan, ClusterSummary cluster) {
        HashMap<Concept, ArrayList<Concept>> conceptHierarchy =
                proxy.getClusterConceptHierarchy(sctVersion, tan.getSNOMEDHierarchyRoot().getId(), cluster.getRoot().getId());
        
        return new SCTConceptHierarchy(cluster.getRoot(), UtilityMethods.convertALMapToHSMap(conceptHierarchy));
    }

    public SCTMultiRootedConceptHierarchy getRegionConceptHierarchy(PAreaTaxonomy taxonomy, 
            ArrayList<PAreaSummary> pareas) {
        
        HashSet<Concept> roots = new HashSet<Concept>();
        ArrayList<Long> pareaRootIds = new ArrayList<Long>();
        
        for(PAreaSummary parea : pareas) {
            pareaRootIds.add(parea.getRoot().getId());
            roots.add(parea.getRoot());
        }
        
        HashMap<Concept, ArrayList<Concept>> conceptHierarchy = 
                proxy.getRegionConceptHierarchy(sctVersion, taxonomy.getSNOMEDHierarchyRoot().getId(), pareaRootIds);
    
        return new SCTMultiRootedConceptHierarchy(roots, UtilityMethods.convertALMapToHSMap(conceptHierarchy));
    }
    
    public ArrayList<SearchResult> searchForConceptsWithinTaxonomy(PAreaTaxonomy taxonomy,
            ArrayList<PAreaSummary> pareas, String term) {
        
        ArrayList<Integer> pareaIds = new ArrayList<Integer>();
        
        for(PAreaSummary parea : pareas) {
            pareaIds.add(parea.getId());
        }
        
        return proxy.searchAnywhereWithinHierarchy(sctVersion, taxonomy.getSNOMEDHierarchyRoot().getId(), pareaIds, term);
    }
    
    public ArrayList<SearchResult> searchForConceptsWithinTAN(TribalAbstractionNetwork tan, 
            ArrayList<ClusterSummary> clusters, String term) {
        
        throw new RuntimeException("Method not yet support...");
    }
    
    public boolean supportsMultipleVersions() {
        return true;
    }
    
    public String getSelectedVersion() {
        return sctVersion;
    }
    
    public ArrayList<String> getSupportedVersions() {
        return proxy.getSupportedSnomedVersions();
    }
    
    public void setVersion(String version) {
        this.sctVersion = version;
    }
    
    public boolean supportsStatedRelationships() {
        return false;
    }
}
