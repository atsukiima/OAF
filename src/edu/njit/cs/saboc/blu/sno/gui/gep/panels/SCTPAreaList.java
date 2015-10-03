package edu.njit.cs.saboc.blu.sno.gui.gep.panels;

import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.AbstractGroupList;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.models.BLUAbstractTableModel;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTPArea;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy.configuration.SCTPAreaTaxonomyConfiguration;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy.parea.SCTPAreaTableModel;
import java.util.ArrayList;
import java.util.Optional;

/**
 *
 * @author Chris O
 */
public class SCTPAreaList extends AbstractGroupList<SCTPArea> {

    public SCTPAreaList(SCTPAreaTaxonomyConfiguration config) {
        super(new SCTPAreaTableModel(config));
    }
    
    public SCTPAreaList(BLUAbstractTableModel<SCTPArea> model) {
        super(model);
    }

    @Override
    protected String getBorderText(Optional<ArrayList<SCTPArea>> pareas) {
        if(pareas.isPresent()) {
            return String.format("Partial-areas (%d)", pareas.get().size());
        } else {
            return "Partial-areas";
        }
    }
}
