package edu.njit.cs.saboc.blu.sno.gui.gep.panels;

import SnomedShared.Concept;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.models.BLUAbstractConceptTableModel;

/**
 *
 * @author Chris O
 */
public class SCTSimpleConceptListTableModel extends BLUAbstractConceptTableModel<Concept> {

    public SCTSimpleConceptListTableModel() {
        super(new String[] {"Concept FSN", "ConceptID"});
    }
   
    @Override
    protected Object[] createRow(Concept concept) {
        return new Object[] {concept.getName(), concept.getId()};
    }
}
