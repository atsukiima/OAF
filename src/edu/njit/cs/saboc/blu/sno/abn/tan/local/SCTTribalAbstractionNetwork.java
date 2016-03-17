package edu.njit.cs.saboc.blu.sno.abn.tan.local;

import SnomedShared.Concept;
import edu.njit.cs.saboc.blu.core.abn.GroupHierarchy;
import edu.njit.cs.saboc.blu.core.abn.tan.TribalAbstractionNetwork;
import edu.njit.cs.saboc.blu.core.datastructure.hierarchy.MultiRootedHierarchy;
import edu.njit.cs.saboc.blu.sno.abn.SCTAbstractionNetwork;
import edu.njit.cs.saboc.blu.sno.abn.tan.SCTTribalAbstractionNetworkGenerator;
import edu.njit.cs.saboc.blu.sno.sctdatasource.SCTLocalDataSource;
import edu.njit.cs.saboc.blu.sno.utils.comparators.ConceptNameComparator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

/**
 *
 * @author Chris
 */
public class SCTTribalAbstractionNetwork extends TribalAbstractionNetwork<Concept, SCTTribalAbstractionNetwork, SCTBand, SCTCluster> 
        implements SCTAbstractionNetwork<SCTTribalAbstractionNetwork> {
    
    private final SCTLocalDataSource dataSource;
  
    public SCTTribalAbstractionNetwork(
            String tanName,
            ArrayList<SCTBand> bands,
            HashMap<Integer, SCTCluster> clusters,
            GroupHierarchy<SCTCluster> clusterHierarchy,
            ArrayList<SCTCluster> patriarchClusters,
            MultiRootedHierarchy<Concept> sourceHierarchy,
            SCTLocalDataSource dataSource) {
        
        super(tanName, bands, clusters, clusterHierarchy, patriarchClusters, sourceHierarchy);
        
        this.dataSource = dataSource;
    }
    
    public SCTLocalDataSource getDataSource() {
        return dataSource;
    }

    public ArrayList<Concept> searchConcepts(String query) {
        ArrayList<Concept> results = new ArrayList<>();
        
        HashSet<Concept> concepts = super.getSourceConceptHierarchy().getNodesInHierarchy();
        
        for(Concept c : concepts) {
            if(c.getName().contains(query)) {
                results.add(c);
            }
        }
        
        Collections.sort(results, new ConceptNameComparator());
        
        return results;
    }
    
    @Override
    public String getSCTVersion() {
        return dataSource.getSelectedVersion();
    }

    @Override
    public SCTTribalAbstractionNetwork getAbstractionNetwork() {
        return this;
    }
    
    public ArrayList<SCTBand> searchBands(String term) {
        ArrayList<SCTBand> searchResults = new ArrayList<>();

        ArrayList<SCTBand> bands = this.getBands();

        String [] searchPatriarchs = term.split(", ");

        if(searchPatriarchs == null) {
            return new ArrayList<SCTBand>();
        }

        for(SCTBand band : bands) {
            ArrayList<String> bandPatriarchNames = new ArrayList<String>();
            
            HashSet<Concept> patriarchs = band.getPatriarchs();

            for(Concept patriarch : patriarchs) {
                bandPatriarchNames.add(patriarch.getName());
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
        
    public SCTTribalAbstractionNetwork createRootSubTAN(SCTCluster root) {
        SCTTribalAbstractionNetworkGenerator generator = new SCTTribalAbstractionNetworkGenerator(root.getRoot().getName(), dataSource);
        return super.createRootSubTAN(root, generator);
    }
    
    public SCTTribalAbstractionNetwork createAncestorTAN(SCTCluster root) {
        SCTTribalAbstractionNetworkGenerator generator = new SCTTribalAbstractionNetworkGenerator(root.getRoot().getName(), dataSource);
        
        return super.createAncestorTAN(root, generator);
    }
}
