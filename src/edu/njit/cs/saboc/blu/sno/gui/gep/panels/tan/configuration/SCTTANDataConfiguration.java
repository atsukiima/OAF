package edu.njit.cs.saboc.blu.sno.gui.gep.panels.tan.configuration;

import SnomedShared.Concept;
import SnomedShared.overlapping.ClusterSummary;
import SnomedShared.overlapping.CommonOverlapSet;
import edu.njit.cs.saboc.blu.core.abn.OverlappingConceptResult;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.configuration.data.BLUPartitionedAbNDataConfiguration;
import edu.njit.cs.saboc.blu.sno.abn.tan.local.SCTCluster;
import edu.njit.cs.saboc.blu.sno.abn.tan.local.SCTTribalAbstractionNetwork;
import edu.njit.cs.saboc.blu.sno.utils.comparators.ConceptNameComparator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

/**
 *
 * @author Chris O
 */
public class SCTTANDataConfiguration implements BLUPartitionedAbNDataConfiguration<SCTTribalAbstractionNetwork, CommonOverlapSet, SCTCluster, Concept> {

    private final SCTTribalAbstractionNetwork tan;

    public SCTTANDataConfiguration(SCTTribalAbstractionNetwork tan) {
        this.tan = tan;
    }

    public SCTTribalAbstractionNetwork getTribalAbstractionNetwork() {
        return tan;
    }

    public ArrayList<SCTCluster> convertClusterSummaryList(ArrayList<ClusterSummary> clusters) {
        ArrayList<SCTCluster> correctClusters = new ArrayList<>();

        clusters.forEach((ClusterSummary cs) -> {
            correctClusters.add((SCTCluster) cs);
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

        overlappingResults.forEach((OverlappingConceptResult<Concept, SCTCluster> result) -> {
            concepts.add(result.getConcept());
        });

        return concepts;
    }

    @Override
    public HashSet<OverlappingConceptResult<Concept, SCTCluster>> getContainerOverlappingResults(CommonOverlapSet band) {
        HashMap<Concept, HashSet<SCTCluster>> conceptClusters = new HashMap<>();

        ArrayList<ClusterSummary> clusters = band.getAllClusters();

        clusters.forEach((ClusterSummary clusterSummary) -> {
            SCTCluster cluster = (SCTCluster) clusterSummary;

            HashSet<Concept> concepts = cluster.getConcepts();

            concepts.forEach((Concept c) -> {
                if (!conceptClusters.containsKey(c)) {
                    conceptClusters.put(c, new HashSet<>());
                }

                conceptClusters.get(c).add(cluster);
            });
        });

        HashSet<OverlappingConceptResult<Concept, SCTCluster>> results = new HashSet<>();

        conceptClusters.forEach((Concept c, HashSet<SCTCluster> overlaps) -> {
            if (overlaps.size() > 1) {
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
    public ArrayList<Concept> getSortedConceptList(SCTCluster cluster) {
        ArrayList<Concept> clusterConcepts = new ArrayList<>(cluster.getConcepts());

        Collections.sort(clusterConcepts, new ConceptNameComparator());

        return clusterConcepts;
    }
}
