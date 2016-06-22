package edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy.parea;

import SnomedShared.Concept;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.NodeDetailsPanel;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTPArea;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTPAreaTaxonomy;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.SCTAbNNodeConceptList;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy.configuration.SCTPAreaTaxonomyConfiguration;
import edu.njit.cs.saboc.blu.sno.utils.comparators.ConceptNameComparator;
import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author Chris O
 */
public class SCTPAreaDetailsPanel extends NodeDetailsPanel<SCTPArea, Concept> {
    
    protected final SCTPAreaTaxonomy taxonomy;
    
    public SCTPAreaDetailsPanel(SCTPAreaTaxonomyConfiguration config) {
        
        super(new SCTPAreaSummaryPanel(config), 
               new SCTPAreaOptionsPanel(config),
              new SCTAbNNodeConceptList<>());
        
        this.taxonomy = config.getDataConfiguration().getPAreaTaxonomy();
        
        getConceptList().addEntitySelectionListener(config.getUIConfiguration().getListenerConfiguration().getGroupConceptListListener());
    }
    
    @Override
    protected ArrayList<Concept> getSortedConceptList(SCTPArea conceptGroup) {
        ArrayList<Concept> clses = conceptGroup.getConceptsInPArea();
        Collections.sort(clses, new ConceptNameComparator());
        
        return clses;
    }
}
