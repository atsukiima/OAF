package edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy.parea.aggregate;

import SnomedShared.Concept;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.pareataxonomy.GenericAggregatePAreaPanel;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTAggregatePArea;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTPArea;
import edu.njit.cs.saboc.blu.sno.datastructure.hierarchy.SCTConceptHierarchy;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy.configuration.SCTPAreaTaxonomyConfiguration;

/**
 *
 * @author Chris O
 */
public class SCTAggregatePAreaPanel extends GenericAggregatePAreaPanel<Concept, SCTAggregatePArea, SCTPArea, SCTConceptHierarchy, SCTPAreaTaxonomyConfiguration> {
    public SCTAggregatePAreaPanel(SCTPAreaTaxonomyConfiguration config) {
        super(
                new SCTAggregatePAreaDetailsPanel(config),
                new SCTAggregatePAreaHierarchyPanel(config), 
                new SCTAggregatedPAreaConceptHierarchyPanel(config),  
                new SCTAggregatedPAreasPanel(config), 
                config);
    }
}
