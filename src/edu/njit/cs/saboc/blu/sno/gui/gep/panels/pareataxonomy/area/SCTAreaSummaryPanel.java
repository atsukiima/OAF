package edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy.area;

import SnomedShared.Concept;
import SnomedShared.pareataxonomy.InheritedRelationship;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.pareataxonomy.AreaSummaryPanel;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTArea;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTPAreaTaxonomy;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy.configuration.SCTPAreaTaxonomyConfiguration;

/**
 *
 * @author Chris O
 */
public class SCTAreaSummaryPanel extends AreaSummaryPanel<Concept, InheritedRelationship, SCTPAreaTaxonomy, SCTArea> {
    
    public SCTAreaSummaryPanel(SCTPAreaTaxonomyConfiguration config) {
        super(new SCTAreaRelationshipsDetailsPanel(config), 
                config);
    }
}
