package edu.njit.cs.saboc.blu.sno.gui.gep.panels;

import SnomedShared.Concept;
import SnomedShared.overlapping.ClusterSummary;
import SnomedShared.overlapping.CommonOverlapSet;
import SnomedShared.overlapping.EntryPoint;
import SnomedShared.overlapping.EntryPointSet;
import edu.njit.cs.saboc.blu.core.abn.OverlappingConceptResult;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.configuration.BLUPartitionedAbNConfiguration;
import edu.njit.cs.saboc.blu.sno.abn.tan.local.SCTCluster;
import edu.njit.cs.saboc.blu.sno.abn.tan.local.SCTTribalAbstractionNetwork;
import edu.njit.cs.saboc.blu.sno.gui.abnselection.SCTDisplayFrameListener;
import edu.njit.cs.saboc.blu.sno.gui.gep.listeners.SCTTANGEPConfiguration;
import edu.njit.cs.saboc.blu.sno.utils.comparators.ConceptNameComparator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

/**
 *
 * @author Chris O
 */
public class SCTTANConfiguration implements BLUPartitionedAbNConfiguration<
        Concept, 
        SCTCluster, 
        CommonOverlapSet> {

    private final SCTTribalAbstractionNetwork tan;
    
    private final SCTDisplayFrameListener displayListener;
    
    private final SCTTANGEPConfiguration gepConfig;
    
    public SCTTANConfiguration(SCTTribalAbstractionNetwork tan, 
            SCTDisplayFrameListener displayListener, 
            SCTTANGEPConfiguration gepConfig) {
        
        
        this.tan = tan;
        this.displayListener = displayListener;
        
        this.gepConfig = gepConfig;
    }
    
    public SCTTribalAbstractionNetwork getTribalAbstractionNetwork() {
        return tan;
    }
    
    public SCTDisplayFrameListener getDisplayListener() {
        return displayListener;
    }
    
    public SCTTANGEPConfiguration getGEPConfiguration() {
        return gepConfig;
    }

    @Override
    public String getConceptTypeName(boolean plural) {
        if(plural) {
            return "Concepts";
        } else {
            return "Concept";
        }
    }

    @Override
    public String getConceptName(Concept concept) {
        return concept.getName();
    }
    
    @Override
    public String getContainerTypeName(boolean plural) {
        if(plural) {
            return "Bands";
        } else {
            return "Band";
        }
    }
    
    private String getCommaSeparatedBandName(HashSet<Long> tribeIds) {
        ArrayList<String> tribeNames = new ArrayList<>();
        
        tribeIds.forEach( (Long tribeId) -> {
            tribeNames.add(tan.getConcepts().get(tribeId).getName());
        });
        
        Collections.sort(tribeNames);
        
        String bandName = tribeNames.get(0);
        
        for(int c = 1; c < tribeNames.size(); c++) {
            bandName += ", " + tribeNames.get(c);
        }
        
        return bandName;
    }

    @Override
    public String getContainerName(CommonOverlapSet band) {
        HashSet<Long> tribes = band.getSetEntryPoints();
        
        if(tribes.isEmpty()) {
            return "";
        } else {
            return getCommaSeparatedBandName(band.getSetEntryPoints());
        }
    }

    @Override
    public String getGroupsContainerName(SCTCluster group) {
        EntryPointSet entryPoints = group.getEntryPointSet();
        
        HashSet<Long> entryPointIds = new HashSet<>();
        
        entryPoints.forEach( (EntryPoint ep) -> {
            entryPointIds.add(ep.getEntryPointConceptId());
        });
        
        if(entryPoints.isEmpty()) {
            return "";
        } else {
            return getCommaSeparatedBandName(entryPointIds);
        }
    }
    
    public ArrayList<SCTCluster> convertClusterSummaryList(ArrayList<ClusterSummary> clusters) {
        ArrayList<SCTCluster> correctClusters = new ArrayList<>();
        
        clusters.forEach((ClusterSummary cs) -> {
            correctClusters.add((SCTCluster)cs);
        });
          
        return correctClusters;
    }

    @Override
    public ArrayList<SCTCluster> getSortedGroupList(CommonOverlapSet band) {
        ArrayList<ClusterSummary> simpleClusters = band.getAllClusters();

        return convertClusterSummaryList(simpleClusters);
    }

    @Override
    public HashSet<SCTCluster> getContainerGroupSet(CommonOverlapSet band) {
        return new HashSet<>(convertClusterSummaryList(band.getAllClusters()));
    }

    @Override
    public HashSet<Concept> getGroupConceptSet(SCTCluster cluster) {
        return cluster.getConcepts();
    }

    @Override
    public HashSet<Concept> getContainerOverlappingConcepts(CommonOverlapSet band) {
        HashSet<Concept> concepts = new HashSet<>();
        
        HashSet<OverlappingConceptResult<Concept, SCTCluster>> overlappingResults = this.getContainerOverlappingResults(band);
        
        overlappingResults.forEach( (OverlappingConceptResult<Concept, SCTCluster> result) -> {
            concepts.add(result.getConcept());
        });
        
        return concepts;
    }

    @Override
    public HashSet<OverlappingConceptResult<Concept, SCTCluster>> getContainerOverlappingResults(CommonOverlapSet band) {
        HashMap<Concept, HashSet<SCTCluster>> conceptClusters = new HashMap<>();
        
        ArrayList<ClusterSummary> clusters = band.getAllClusters();
        
        clusters.forEach((ClusterSummary clusterSummary) -> {
            SCTCluster cluster = (SCTCluster)clusterSummary;
            
            HashSet<Concept> concepts = cluster.getConcepts();
            
            concepts.forEach((Concept c) -> {
                if(!conceptClusters.containsKey(c)) {
                    conceptClusters.put(c, new HashSet<>());
                }
                
                conceptClusters.get(c).add(cluster);
            });
        });
        
        HashSet<OverlappingConceptResult<Concept, SCTCluster>> results = new HashSet<>();
        
        conceptClusters.forEach((Concept c, HashSet<SCTCluster> overlaps) -> {
            if(overlaps.size() > 1) {
                results.add(new OverlappingConceptResult<>(c, overlaps));
            }
        });
        
        return results;
    }

    @Override
    public int getContainerLevel(CommonOverlapSet band) {
        return band.getSetEntryPoints().size();
    }

    @Override
    public String getGroupTypeName(boolean plural) {
        if(plural) {
            return "Clusters";
        } else {
            return "Cluster";
        }
    }

    @Override
    public String getGroupName(SCTCluster cluster) {
        return cluster.getRoot().getName();
    }

    @Override
    public ArrayList<Concept> getSortedConceptList(SCTCluster cluster) {
        ArrayList<Concept> clusterConcepts = new ArrayList<>(cluster.getConcepts());
        
        Collections.sort(clusterConcepts, new ConceptNameComparator());
        
        return clusterConcepts;
    }

}
