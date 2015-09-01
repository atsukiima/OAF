package edu.njit.cs.saboc.blu.sno.sctdatasource;

import SnomedShared.Concept;
import SnomedShared.OutgoingLateralRelationship;
import SnomedShared.PAreaDetailsForConcept;
import SnomedShared.SearchResult;
import SnomedShared.overlapping.ClusterSummary;
import SnomedShared.pareataxonomy.ConceptPAreaInfo;
import SnomedShared.pareataxonomy.GroupParentInfo;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTPArea;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTPAreaTaxonomy;
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

    public ArrayList<Concept> getConceptsInPArea(SCTPAreaTaxonomy taxonomy, SCTPArea parea) {
        return proxy.getConceptsInPArea(sctVersion, taxonomy.getSCTRootConcept(), parea.getRoot());
    }

    public HashMap<Long, ArrayList<Concept>> getConceptsInPAreaSet(SCTPAreaTaxonomy taxonomy, ArrayList<SCTPArea> pareas) {
        ArrayList<Long> rootIds = new ArrayList<Long>();
        
        for(SCTPArea parea : pareas) {
            rootIds.add(parea.getRoot().getId());
        }
        
        return proxy.getConceptsInPAreaSet(sctVersion, taxonomy.getSCTRootConcept(), rootIds);
    }

    public ArrayList<Concept> getConceptsInCluster(TribalAbstractionNetwork tan, ClusterSummary cluster) {
        throw new RuntimeException("Method not yet support...");
    }

    public HashMap<Long, ArrayList<Concept>> getConceptsInClusterSet(TribalAbstractionNetwork tan, ArrayList<ClusterSummary> clusters) {
        throw new RuntimeException("Method not yet support...");
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

    public HashMap<Long, String> getUniqueLateralRelationshipsInHierarchy(SCTPAreaTaxonomy taxonomy, Concept hierarchyRoot) {
        return proxy.getUniqueLateralRelationshipsInHierarchy(sctVersion, hierarchyRoot);
    }

    public int getConceptCountInPAreaHierarchy(SCTPAreaTaxonomy taxonomy, ArrayList<SCTPArea> pareas) {
        ArrayList<Integer> pareaIds = new ArrayList<Integer>();
        
        for(SCTPArea parea : pareas) {
            pareaIds.add(parea.getId());
        }
        
        return proxy.getConceptCountInPAreaHierarchy(sctVersion, taxonomy.getSCTRootConcept(), pareaIds);
    }

    public int getConceptCountInClusterHierarchy(TribalAbstractionNetwork tan, ArrayList<ClusterSummary> clusters) {
        throw new RuntimeException("Method not yet support...");
    }

    public ArrayList<ConceptPAreaInfo> getConceptPAreaInfo(SCTPAreaTaxonomy taxonomy, Concept c) {
        return proxy.getConceptPAreaInfo(sctVersion, c.getId());
    }
    
    public ArrayList<ConceptClusterInfo> getConceptClusterInfo(TribalAbstractionNetwork tan, Concept c) {
        throw new RuntimeException("Method not yet support...");
    }

    public ArrayList<GroupParentInfo> getPAreaParentInfo(SCTPAreaTaxonomy taxonomy, SCTPArea parea) {
        return proxy.getPAreaParentInfo(sctVersion, taxonomy.getSCTRootConcept(), parea);
    }
    
    public ArrayList<GroupParentInfo> getClusterParentInfo(TribalAbstractionNetwork tan, ClusterSummary cluster) {
         throw new RuntimeException("Method not yet support...");
    }
    
    public SCTConceptHierarchy getPAreaConceptHierarchy(SCTPAreaTaxonomy taxonomy, SCTPArea parea) {
        HashMap<Concept, ArrayList<Concept>> conceptHierarchy = 
                proxy.getPAreaConceptHierarchy(sctVersion, taxonomy.getSCTRootConcept().getId(), parea.getRoot().getId());
        
        return new SCTConceptHierarchy(parea.getRoot(), UtilityMethods.convertALMapToHSMap(conceptHierarchy));
    }

    public SCTConceptHierarchy getClusterConceptHierarchy(TribalAbstractionNetwork tan, ClusterSummary cluster) {
        throw new RuntimeException("Method not yet support...");
    }

    public SCTMultiRootedConceptHierarchy getRegionConceptHierarchy(SCTPAreaTaxonomy taxonomy, 
            ArrayList<SCTPArea> pareas) {
        
        HashSet<Concept> roots = new HashSet<Concept>();
        ArrayList<Long> pareaRootIds = new ArrayList<Long>();
        
        for(SCTPArea parea : pareas) {
            pareaRootIds.add(parea.getRoot().getId());
            roots.add(parea.getRoot());
        }
        
        HashMap<Concept, ArrayList<Concept>> conceptHierarchy = 
                proxy.getRegionConceptHierarchy(sctVersion, taxonomy.getSCTRootConcept().getId(), pareaRootIds);
    
        return new SCTMultiRootedConceptHierarchy(roots, UtilityMethods.convertALMapToHSMap(conceptHierarchy));
    }
    
    public ArrayList<SearchResult> searchForConceptsWithinTaxonomy(SCTPAreaTaxonomy taxonomy,
            ArrayList<SCTPArea> pareas, String term) {
        
        ArrayList<Integer> pareaIds = new ArrayList<Integer>();
        
        for(SCTPArea parea : pareas) {
            pareaIds.add(parea.getId());
        }
        
        return proxy.searchAnywhereWithinHierarchy(sctVersion, taxonomy.getSCTRootConcept().getId(), pareaIds, term);
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
    
    public boolean isLocalDataSource() {
        return false;
    }
}
