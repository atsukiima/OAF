package edu.njit.cs.saboc.blu.sno.abn.tan.local;

import SnomedShared.Concept;
import SnomedShared.overlapping.ClusterSummary;
import SnomedShared.overlapping.CommonOverlapSet;
import edu.njit.cs.saboc.blu.core.abn.GroupHierarchy;
import edu.njit.cs.saboc.blu.sno.abn.local.LocalAbstractionNetwork;
import edu.njit.cs.saboc.blu.sno.abn.tan.TribalAbstractionNetwork;
import edu.njit.cs.saboc.blu.sno.datastructure.hierarchy.SCTMultiRootedConceptHierarchy;
import edu.njit.cs.saboc.blu.sno.sctdatasource.SCTLocalDataSource;
import edu.njit.cs.saboc.blu.sno.utils.comparators.ConceptNameComparator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 *
 * @author Chris
 */
public class SCTTribalAbstractionNetwork extends TribalAbstractionNetwork<SCTCluster> implements LocalAbstractionNetwork {
    
    private SCTMultiRootedConceptHierarchy conceptHierarchy;
    
    private HashMap<Long, Concept> concepts;
    
    public SCTTribalAbstractionNetwork(
            String tanName,
            ArrayList<CommonOverlapSet> overlapSets,
            HashMap<Integer, SCTCluster> clusters,
            GroupHierarchy<SCTCluster> clusterHierarchy,
            String SNOMEDVersion,
            ArrayList<SCTCluster> entryPoints,
            ArrayList<SCTCluster> nonContributingEntryPoints,
            HashMap<Long, Concept> concepts,
            SCTMultiRootedConceptHierarchy conceptHierarchy,
            SCTLocalDataSource dataSource) {
        
        super(tanName, overlapSets, clusters, clusterHierarchy, SNOMEDVersion, entryPoints, nonContributingEntryPoints, dataSource);
        
        this.concepts = concepts;
        this.conceptHierarchy = conceptHierarchy;
    }
    
    public HashMap<Long, Concept> getConcepts() {
        return concepts;
    }
    
    public ArrayList<SCTCluster> convertClusters(ArrayList<ClusterSummary> summaries) {
        ArrayList<SCTCluster> clusters = new ArrayList<>();
        
        summaries.forEach( (ClusterSummary summary) -> {
            clusters.add( (SCTCluster) summary);
        });
        
        return clusters;
    }
    
    public SCTMultiRootedConceptHierarchy getConceptHierarchy() {
        return conceptHierarchy;
    }
    
    public int getHierarchyConceptCount() {
        return conceptHierarchy.getConceptsInHierarchy().size();
    }
    
    public ArrayList<Concept> searchConcepts(String query) {
        ArrayList<Concept> results = new ArrayList<Concept>();
        
        for(Concept c : concepts.values()) {
            if(c.getName().contains(query)) {
                results.add(c);
            }
        }
        
        Collections.sort(results, new ConceptNameComparator());
        
        return results;
    }
}
