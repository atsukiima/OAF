
package edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy.parea;

import SnomedShared.Concept;
import edu.njit.cs.saboc.blu.core.abn.GenericParentGroupInfo;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.models.BLUAbstractParentGroupTableModel;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTPArea;

/**
 *
 * @author Chris O
 */
public class SCTParentPAreaTableModel extends BLUAbstractParentGroupTableModel<Concept, SCTPArea, GenericParentGroupInfo<Concept, SCTPArea>> {

    public SCTParentPAreaTableModel () {
        super(new String[] {
            "Parent Concept", 
            "Parent ConceptID", 
            "Parent Partial-area", 
            "# Concepts in Parent Partial-area", 
            "Area"
        });
    }

    @Override
    protected Object[] createRow(GenericParentGroupInfo<Concept, SCTPArea> item) {
       
        return new Object[] {
            item.getParentConcept().getName(),
            item.getParentConcept().getId(),
            item.getParentGroup().getRoot().getName(),
            item.getParentGroup().getConceptCount(),
            "---- RELATIONSHIPS ----"
        };
    }
}
