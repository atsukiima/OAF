package edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy.area;

import SnomedShared.pareataxonomy.InheritedRelationship;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.models.ConceptTableModel;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy.configuration.SCTPAreaTaxonomyConfiguration;

/**
 *
 * @author Chris O
 */
public class SCTAreaRelationshipTableModel extends ConceptTableModel<InheritedRelationship> {

    private final SCTPAreaTaxonomyConfiguration config;
    
    public SCTAreaRelationshipTableModel(SCTPAreaTaxonomyConfiguration config) {
        super(new String[]{"Relationship Name", "Relationship ID"});
        
        this.config = config;
    }
    
    @Override
    protected Object[] createRow(InheritedRelationship rel) {

        return new Object[] {
            config.getDataConfiguration().getPAreaTaxonomy().getLateralRelsInHierarchy().get(rel.getRelationshipTypeId()),
            rel.getRelationshipTypeId()
        };
    }
}
