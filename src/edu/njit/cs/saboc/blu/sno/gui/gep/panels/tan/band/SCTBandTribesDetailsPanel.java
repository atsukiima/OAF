package edu.njit.cs.saboc.blu.sno.gui.gep.panels.tan.band;

import SnomedShared.Concept;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.AbstractEntityList;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.SCTTANConfiguration;
import java.util.ArrayList;
import java.util.Optional;

/**
 *
 * @author Chris O
 */
public class SCTBandTribesDetailsPanel extends AbstractEntityList<Concept> {

    public SCTBandTribesDetailsPanel(SCTTANConfiguration config) {
        super(new SCTBandPatriarchTableModel(config));
    }

    @Override
    protected String getBorderText(Optional<ArrayList<Concept>> entities) {
        if(entities.isPresent()) {
            return String.format("Tribes (%d)", entities.get().size());
        } else {
            return "Tribes";
        }
    }
}
