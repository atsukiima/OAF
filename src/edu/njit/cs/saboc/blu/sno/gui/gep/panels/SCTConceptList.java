
package edu.njit.cs.saboc.blu.sno.gui.gep.panels;

import SnomedShared.Concept;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.AbstractEntityList;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.SCTSimpleConceptListTableModel;
import java.util.ArrayList;
import java.util.Optional;

/**
 *
 * @author Chris
 */
public class SCTConceptList extends AbstractEntityList<Concept> {

    public SCTConceptList() {
        super(new SCTSimpleConceptListTableModel());
    }
    
    @Override
    protected String getBorderText(Optional<ArrayList<Concept>> concepts) {
        if(concepts.isPresent()) {
            return String.format("Concepts (%d)", concepts.get().size());
        } else {
            return "Concepts";
        }
    }
}
