package edu.njit.cs.saboc.blu.sno.gui.gep.panels;

import SnomedShared.Concept;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.models.BLUAbstractAbNNodeTableModel;

/**
 *
 * @author Chris O
 */
public class SCTSimpleAbNNodeConceptListTableModel<NODE_T> extends BLUAbstractAbNNodeTableModel<NODE_T, Concept> {

    public SCTSimpleAbNNodeConceptListTableModel() {
        super(new String[]{"Concept Name", "Concept ID"});
    }

    @Override
    protected Object[] createRow(Concept concept) {
        return new Object[]{concept.getName(), concept.getId()};
    }
}
