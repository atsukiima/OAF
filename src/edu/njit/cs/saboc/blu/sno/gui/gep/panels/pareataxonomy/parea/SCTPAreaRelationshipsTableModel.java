package edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy.parea;

import SnomedShared.pareataxonomy.InheritedRelationship;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.models.BLUAbstractTableModel;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTPAreaTaxonomy;

/**
 *
 * @author Chris O
 */
public class SCTPAreaRelationshipsTableModel extends BLUAbstractTableModel<InheritedRelationship> {

    private final SCTPAreaTaxonomy taxonomy;
    
    public SCTPAreaRelationshipsTableModel(SCTPAreaTaxonomy taxonomy) {
        super(new String[]{"Relationship Name", "Relationship Inheritance"});
        
        this.taxonomy = taxonomy;
    }
    
    @Override
    protected Object[] createRow(InheritedRelationship rel) {
        return new Object[] {
            taxonomy.getLateralRelsInHierarchy().get(rel.getRelationshipTypeId()),
            rel.getInheritanceType().name()
        };
    }
}
