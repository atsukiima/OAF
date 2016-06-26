package edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy.parea;

import SnomedShared.pareataxonomy.InheritedRelationship;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.pareataxonomy.RelationshipPanel;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTPAreaTaxonomy;
import java.util.ArrayList;
import java.util.Optional;

/**
 *
 * @author Chris O
 */
public class SCTPAreaRelationshipsDetailsPanel extends RelationshipPanel<InheritedRelationship> {
    
    public SCTPAreaRelationshipsDetailsPanel(SCTPAreaTaxonomy taxonomy) {
        super(new SCTPAreaRelationshipsTableModel(taxonomy));
    }

    @Override
    protected String getBorderText(Optional<ArrayList<InheritedRelationship>> entities) {
        if(entities.isPresent()) {
            return String.format("Relationships (%d)", entities.get().size());
        } else {
            return "Relationships";
        }
    }
}
