package edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy.area;

import SnomedShared.pareataxonomy.InheritedRelationship;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.pareataxonomy.GenericRelationshipPanel;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy.configuration.SCTPAreaTaxonomyConfiguration;
import java.util.ArrayList;
import java.util.Optional;

/**
 *
 * @author Chris O
 */
public class SCTAreaRelationshipsDetailsPanel extends GenericRelationshipPanel<InheritedRelationship> {

    public SCTAreaRelationshipsDetailsPanel(SCTPAreaTaxonomyConfiguration config) {
        super(new SCTAreaRelationshipTableModel(config));
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
