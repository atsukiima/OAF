package edu.njit.cs.saboc.blu.sno.gui.gep.panels.tan.band;

import SnomedShared.Concept;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.models.BLUAbstractConceptTableModel;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.tan.configuration.SCTTANConfiguration;

/**
 *
 * @author Chris O
 */
public class SCTBandPatriarchTableModel extends BLUAbstractConceptTableModel<Concept> {

    public SCTBandPatriarchTableModel(SCTTANConfiguration config) {
        super(new String[]{"Tribal Patriarch"});
    }
    
    @Override
    protected Object[] createRow(Concept patriarch) {

        return new Object[] {
            patriarch.getName()
        };
    }
}
