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
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Represents a source of SNOMED CT data. E.g., a database or local release files.
 * @author Chris
 */
public interface SCTDataSource {
    
    /**
     * Returns the concept with the given CID
     * @param id The concept's unique identifier
     * @return The concept, if it exists
     */
    public Concept getConceptFromId(long id);
    
    /**
     * Returns the set of parents for a given concept
     * @param c 
     * @return 
     */
    public ArrayList<Concept> getConceptParents(Concept c);

    /**
     * Returns the set of children for a given concept
     * @param c
     * @return 
     */
    public ArrayList<Concept> getConceptChildren(Concept c);

    /**
     * Returns all of the descriptions for a given concept
     * @param c
     * @return 
     */
    public ArrayList<String> getConceptSynoynms(Concept c);

    /**
     * Returns all of the siblings (other children of concept's parent(s)) for a given concept
     * @param c
     * @return 
     */
    public ArrayList<Concept> getConceptSiblings(Concept c);

    /**
     * Returns the set of lateral attribute relationships for a given concept
     * @param c
     * @return 
     */
    public ArrayList<OutgoingLateralRelationship> getOutgoingLateralRelationships(Concept c);

    /**
     * Returns the 19 top level root concepts (the children of SNOMED CT Concept)
     * @return 
     */
    public ArrayList<Concept> getHierarchyRootConcepts();

    /**
     * Returns information about which partial-area(s) a given concept belongs to
     * @param c
     * @return 
     */
    public ArrayList<PAreaDetailsForConcept> getSummaryOfPAreasContainingConcept(Concept c);
    
    /**
     * Returns the list of concepts summarized by the given partial-area. Should be sorted alphabetically according to FSN
     * @param taxonomy
     * @param parea
     * @return 
     */
    public ArrayList<Concept> getConceptsInPArea(PAreaTaxonomy taxonomy, PAreaSummary parea);

    /**
     * For a set of partial-areas, returns the list of concepts within each partial-area. For each partial-area
     * the list should be sorted alphabetically.
     * @param taxonomy
     * @param pareas
     * @return Map from PArea root Concept Id to the list of concepts in the partial-area
     */
    public HashMap<Long, ArrayList<Concept>> getConceptsInPAreaSet(PAreaTaxonomy taxonomy, ArrayList<PAreaSummary> pareas);

    /**
     * Returns the list of concepts summarized by the given cluster. Should be sorted alphabetically.
     * @param tan
     * @param cluster
     * @return 
     */
    public ArrayList<Concept> getConceptsInCluster(TribalAbstractionNetwork tan, ClusterSummary cluster);

    /**
     * For a set of clusters, returns the list of concepts summarized by each cluster. For each cluster
     * the list should be sorted alphabetically.
     * 
     * @param tan
     * @param clusters
     * @return 
     */
    public HashMap<Long, ArrayList<Concept>> getConceptsInClusterSet(TribalAbstractionNetwork tan, ArrayList<ClusterSummary> clusters);

    /**
     * Search for a concept with a description that is equal to (without regards to case) the given search term
     * @param term
     * @return 
     */
    public ArrayList<SearchResult> searchExact(String term);

    /**
     * Search for concepts with a description that start with the given search term (ignoring case)
     * @param term
     * @return 
     */
    public ArrayList<SearchResult> searchStarting(String term);

    /**
     * Search for concepts with a description that contains the given search term (ignoring case)
     * @param term
     * @return 
     */
    public ArrayList<SearchResult> searchAnywhere(String term);

    /**
     * Returns the set of attribute relationships that are defined for a given top-level hierarchy (e.g., Procedure)
     * @param taxonomy
     * @param hierarchyRoot
     * @return 
     */
    public HashMap<Long, String> getUniqueLateralRelationshipsInHierarchy(PAreaTaxonomy taxonomy, Concept hierarchyRoot);

    /**
     * Returns the number of unique concepts in a given list of partial-areas
     * @param taxonomy
     * @param pareas
     * @return 
     */
    public int getConceptCountInPAreaHierarchy(PAreaTaxonomy taxonomy, ArrayList<PAreaSummary> pareas);

    /**
     * Returns the number of unique concepts in a given list of clusters
     * @param tan
     * @param clusters
     * @return 
     */
    public int getConceptCountInClusterHierarchy(TribalAbstractionNetwork tan, ArrayList<ClusterSummary> clusters);

    /**
     * Returns the list of partial-areas this concept belongs to
     * @param taxonomy
     * @param c
     * @return 
     */
    public ArrayList<ConceptPAreaInfo> getConceptPAreaInfo(PAreaTaxonomy taxonomy, Concept c);
    
    
    /**
     * Returns the list of clusters this concept belongs to
     * @param tan
     * @param c
     * @return 
     */
    public ArrayList<ConceptClusterInfo> getConceptClusterInfo(TribalAbstractionNetwork tan, Concept c);

    /**
     * Returns information about the parent concepts of the given partial-area's root, e.g., what partial-areas they belong to
     * @param taxonomy
     * @param parea
     * @return 
     */
    public ArrayList<GroupParentInfo> getPAreaParentInfo(PAreaTaxonomy taxonomy, PAreaSummary parea);
    
    /**
     * Returns information about the parent concepts of the given cluster's root, e.g., what clusters they belong to
     * @param taxonomy
     * @param parea
     * @return 
     */
    public ArrayList<GroupParentInfo> getClusterParentInfo(TribalAbstractionNetwork tan, ClusterSummary cluster);
    
    /**
     * Returns the hierarchy of concepts summarized bv the given partial-area in the given taxonomy
     * @param taxonomy
     * @param parea
     * @return 
     */
    public SCTConceptHierarchy getPAreaConceptHierarchy(PAreaTaxonomy taxonomy, PAreaSummary parea);

    /**
     * Returns the hierarchy of concepts summarized by the given cluster in the given TAN
     * @param tan
     * @param cluster
     * @return 
     */
    public SCTConceptHierarchy getClusterConceptHierarchy(TribalAbstractionNetwork tan, ClusterSummary cluster);

    
    /**
     * Returns the multi-rooted hierarchy of concepts within an area/region
     * @param taxonomy
     * @param pareas
     * @return 
     */
    public SCTMultiRootedConceptHierarchy getRegionConceptHierarchy(PAreaTaxonomy taxonomy, 
            ArrayList<PAreaSummary> pareas);
    
    /**
     * Searches for a concept (term anywhere in the description) in a given partial-area taxonomy
     * @param taxonomy
     * @param pareas
     * @param term
     * @return 
     */
    public ArrayList<SearchResult> searchForConceptsWithinTaxonomy(PAreaTaxonomy taxonomy,
            ArrayList<PAreaSummary> pareas, String term);
    
    /**
     * Searches for a concept (term anywhere in the) in a given tribal abstraction network
     * @param tan
     * @param clusters
     * @param term
     * @return 
     */
    public ArrayList<SearchResult> searchForConceptsWithinTAN(TribalAbstractionNetwork tan, 
            ArrayList<ClusterSummary> clusters, String term);
    
    /**
     * Returns if the given SCT Data Source supports accessing multiple versions of SCT
     * @return 
     */
    public boolean supportsMultipleVersions();
    
    /**
     * Returns the version which is currently selected for this data source. If a data source 
     * does not support multiple versions then the one version is returned.
     * @return 
     */
    public String getSelectedVersion();
    
    /**
     * Returns the list of SCT versions supported by the data source
     * @return 
     */
    public ArrayList<String> getSupportedVersions();
    
    /**
     * Sets the selected version of this data source. If a data source does not support multiple versions
     * then this method should do nothing.
     * @param version 
     */
    public void setVersion(String version);
}
