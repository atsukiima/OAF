package edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy.area;

import SnomedShared.Concept;
import edu.njit.cs.saboc.blu.core.abn.OverlappingConceptResult;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.AbstractNodeDetailsPanel;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.entry.ContainerConceptEntry;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTArea;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTPArea;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy.configuration.SCTPAreaTaxonomyConfiguration;
import edu.njit.cs.saboc.blu.sno.utils.comparators.ConceptNameComparator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;

/**
 *
 * @author Chris O
 */
public class SCTAreaDetailsPanel extends AbstractNodeDetailsPanel<SCTArea, ContainerConceptEntry<Concept, SCTPArea>> {
    
    private final SCTPAreaTaxonomyConfiguration configuration;

    public SCTAreaDetailsPanel(SCTPAreaTaxonomyConfiguration configuration) {

        super(new SCTAreaSummaryPanel(configuration), 
                new SCTAreaOptionsPanel(configuration), 
                new SCTAreaConceptList());
        
        this.configuration = configuration;
    }

    @Override
    protected ArrayList<ContainerConceptEntry<Concept, SCTPArea>> getSortedConceptList(SCTArea area) {
        
        HashSet<OverlappingConceptResult<Concept,SCTPArea>> overlappingConcepts = configuration.getDataConfiguration().getContainerOverlappingResults(area);
        
        HashMap<Concept, HashSet<SCTPArea>> conceptPAreas = new HashMap<>();
        
        overlappingConcepts.forEach( (OverlappingConceptResult<Concept,SCTPArea> overlappingResult) -> {
            conceptPAreas.put(overlappingResult.getConcept(), overlappingResult.getOverlappingGroups());
        });
        
        ArrayList<SCTPArea> pareas = area.getAllPAreas();
        
        pareas.forEach((SCTPArea parea) -> {
            ArrayList<Concept> pareaClses = parea.getConceptsInPArea();
            
            pareaClses.forEach((Concept c) -> {
                if(!conceptPAreas.containsKey(c)) {
                    conceptPAreas.put(c, new HashSet<>(Arrays.asList(parea)));
                }
            });
        });
        
        ArrayList<ContainerConceptEntry<Concept, SCTPArea>> areaEntries = new ArrayList<>();
        
        conceptPAreas.forEach((Concept c, HashSet<SCTPArea> conceptsPAreas) -> {
            areaEntries.add(new ContainerConceptEntry<>(c, conceptsPAreas));
        });
        
        final ConceptNameComparator comparator = new ConceptNameComparator();
        
        Collections.sort(areaEntries, new Comparator<ContainerConceptEntry<Concept, SCTPArea>>() {
            public int compare(ContainerConceptEntry<Concept, SCTPArea> a, ContainerConceptEntry<Concept, SCTPArea> b) {
                return comparator.compare(a.getConcept(), b.getConcept());
            }
        });
        
        return areaEntries;
    }
}
