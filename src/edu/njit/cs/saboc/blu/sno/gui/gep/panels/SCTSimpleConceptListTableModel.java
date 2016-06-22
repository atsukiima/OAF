package edu.njit.cs.saboc.blu.sno.gui.gep.panels;

import SnomedShared.Concept;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.models.ConceptTableModel;

/**
 *
 * @author Chris O
 */
public class SCTSimpleConceptListTableModel extends ConceptTableModel<Concept> {

    public SCTSimpleConceptListTableModel() {
        super(new String[] {"Concept Name", "Concept ID"});
    }
   
    @Override
    protected Object[] createRow(Concept concept) {
        return new Object[] {concept.getName(), concept.getId()};
    }
}
