package edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy.area.aggregate;

import SnomedShared.Concept;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.AbstractNodeDetailsPanel;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTAggregatePArea;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTArea;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTPArea;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.SCTConceptList;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy.configuration.SCTPAreaTaxonomyConfiguration;
import edu.njit.cs.saboc.blu.sno.utils.comparators.ConceptNameComparator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

/**
 *
 * @author Chris O
 */
public class SCTAggregateAreaDetailsPanel extends AbstractNodeDetailsPanel<SCTArea, Concept> {
    
    private final SCTPAreaTaxonomyConfiguration configuration;

    public SCTAggregateAreaDetailsPanel(SCTPAreaTaxonomyConfiguration configuration) {

        super(new SCTAggregateAreaSummaryPanel(configuration), 
                new SCTAggregateAreaOptionsPanel(configuration), 
                new SCTConceptList());
        
        this.configuration = configuration;
    }

    @Override
    protected ArrayList<Concept> getSortedConceptList(SCTArea area) {
        ArrayList<SCTPArea> pareas = area.getAllPAreas();
        
        HashSet<Concept> clsSet = new HashSet<>();
        
        pareas.forEach((SCTPArea parea) -> {
            SCTAggregatePArea aggregatePArea = (SCTAggregatePArea)parea;
            
            clsSet.addAll(aggregatePArea.getAllGroupsConcepts());
        });
        
        ArrayList<Concept> clsList = new ArrayList<>(clsSet);
        
        Collections.sort(clsList, new ConceptNameComparator());
       
        return clsList;
    }
}
