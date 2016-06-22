package edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy.area.aggregate;

import SnomedShared.Concept;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.PartitionedNodeSubNodeList;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTAggregatePArea;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTArea;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.SCTAggregatePAreaList;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.SCTConceptList;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy.configuration.SCTPAreaTaxonomyConfiguration;
import edu.njit.cs.saboc.blu.sno.utils.comparators.ConceptNameComparator;
import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author Chris O
 */
public class SCTAreaAggregatedPAreaPanel extends PartitionedNodeSubNodeList<SCTArea, SCTAggregatePArea, Concept> {

    public SCTAreaAggregatedPAreaPanel(SCTPAreaTaxonomyConfiguration config) {
                
        super(new SCTAggregatePAreaList(config),
                new SCTConceptList(), 
                config);
    }
    
    @Override
    public ArrayList<Concept> getSortedConceptList(SCTAggregatePArea parea) {

        ArrayList<Concept> concepts = new ArrayList<>(parea.getAllGroupsConcepts());

        Collections.sort(concepts, new ConceptNameComparator());
        
        return concepts;
    }
}