package edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy.parea.aggregate;

import SnomedShared.Concept;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.AbstractNodeDetailsPanel;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTAggregatePArea;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTPAreaTaxonomy;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.SCTConceptList;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.SCTPAreaTaxonomyConfiguration;
import edu.njit.cs.saboc.blu.sno.utils.comparators.ConceptNameComparator;
import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author Chris O
 */
public class SCTAggregatePAreaDetailsPanel extends AbstractNodeDetailsPanel<SCTAggregatePArea, Concept> {
    
    protected final SCTPAreaTaxonomy taxonomy;
    
    public SCTAggregatePAreaDetailsPanel(SCTPAreaTaxonomyConfiguration config) {
        
        super(new SCTAggregatePAreaSummaryPanel(config), 
              new SCTAggregatePAreaOptionsPanel(config),
              new SCTConceptList());
        
        this.taxonomy = config.getPAreaTaxonomy();
        
        getConceptList().addEntitySelectionListener(config.getGroupConceptListListener());
    }
    
    @Override
    protected ArrayList<Concept> getSortedConceptList(SCTAggregatePArea conceptGroup) {
        ArrayList<Concept> concepts = new ArrayList<>(conceptGroup.getAllGroupsConcepts());
        
        Collections.sort(concepts, new ConceptNameComparator());
        
        return concepts;
    }
    
}
