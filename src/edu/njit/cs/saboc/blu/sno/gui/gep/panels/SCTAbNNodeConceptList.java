package edu.njit.cs.saboc.blu.sno.gui.gep.panels;

import SnomedShared.Concept;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.NodeEntityList;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.models.AbstractNodeEntityTableModel;
import java.util.ArrayList;
import java.util.Optional;

/**
 *
 * @author Chris O
 */
public class SCTAbNNodeConceptList<NODE_T> extends NodeEntityList<NODE_T, Concept> {

    public SCTAbNNodeConceptList( AbstractNodeEntityTableModel<NODE_T, Concept> model) {
        super(model);
    }
    
    public SCTAbNNodeConceptList() {
        this(new SCTSimpleAbNNodeConceptListTableModel<>());
    }
  
    @Override
    protected String getBorderText(Optional<ArrayList<Concept>> concepts) {
        if (concepts.isPresent()) {
            return String.format("Concepts (%d)", concepts.get().size());
        } else {
            return "Concepts";
        }
    }
}
