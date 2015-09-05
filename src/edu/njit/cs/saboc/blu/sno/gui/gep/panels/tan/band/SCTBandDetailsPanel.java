package edu.njit.cs.saboc.blu.sno.gui.gep.panels.tan.band;

import SnomedShared.Concept;
import SnomedShared.overlapping.CommonOverlapSet;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.AbstractNodeDetailsPanel;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.entry.ContainerConceptEntry;
import edu.njit.cs.saboc.blu.sno.abn.tan.local.SCTCluster;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.SCTTANConfiguration;
import edu.njit.cs.saboc.blu.sno.utils.comparators.ConceptNameComparator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;

/**
 *
 * @author Chris O
 */
public class SCTBandDetailsPanel extends AbstractNodeDetailsPanel<CommonOverlapSet, ContainerConceptEntry<Concept, SCTCluster>> {
    
    private final SCTTANConfiguration configuration;

    public SCTBandDetailsPanel(SCTTANConfiguration configuration) {

        super(new SCTBandSummaryPanel(configuration), 
                new SCTBandOptionsPanel(configuration), 
                new SCTBandConceptList());
        
        this.configuration = configuration;
    }

    @Override
    protected ArrayList<ContainerConceptEntry<Concept, SCTCluster>> getSortedConceptList(CommonOverlapSet band) {
        
        HashMap<Concept, HashSet<SCTCluster>> conceptClusters = new HashMap<>();
        
        ArrayList<SCTCluster> clusters = configuration.convertClusterSummaryList(band.getAllClusters());
        
        clusters.forEach((SCTCluster cluster) -> {
            HashSet<Concept> pareaClses = cluster.getConcepts();
            
            pareaClses.forEach((Concept c) -> {
                if(!conceptClusters.containsKey(c)) {
                    conceptClusters.put(c, new HashSet<>());
                }
                
                conceptClusters.get(c).add(cluster);
            });
        });
        
        ArrayList<ContainerConceptEntry<Concept, SCTCluster>> areaEntries = new ArrayList<>();
        
        conceptClusters.forEach((Concept c, HashSet<SCTCluster> conceptsClusters) -> {
            areaEntries.add(new ContainerConceptEntry<>(c, conceptsClusters));
        });
        
        final ConceptNameComparator comparator = new ConceptNameComparator();
        
        Collections.sort(areaEntries, new Comparator<ContainerConceptEntry<Concept, SCTCluster>>() {
            public int compare(ContainerConceptEntry<Concept, SCTCluster> a, ContainerConceptEntry<Concept, SCTCluster> b) {
                return comparator.compare(a.getConcept(), b.getConcept());
            }
        });
        
        return areaEntries;
    }
}
