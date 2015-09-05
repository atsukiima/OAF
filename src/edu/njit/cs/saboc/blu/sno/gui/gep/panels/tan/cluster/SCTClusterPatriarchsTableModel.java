package edu.njit.cs.saboc.blu.sno.gui.gep.panels.tan.cluster;

import SnomedShared.overlapping.EntryPoint;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.models.BLUAbstractTableModel;
import edu.njit.cs.saboc.blu.sno.abn.tan.local.SCTTribalAbstractionNetwork;

/**
 *
 * @author Chris O
 */
public class SCTClusterPatriarchsTableModel extends BLUAbstractTableModel<EntryPoint> {

    private final SCTTribalAbstractionNetwork tan;
    
    public SCTClusterPatriarchsTableModel(SCTTribalAbstractionNetwork tan) {
        super(new String[]{"Tribe Patriarch Name", "Tribal Inheritance"});
        
        this.tan = tan;
    }
    
    @Override
    protected Object[] createRow(EntryPoint patriarch) {
        return new Object[] {
            tan.getConcepts().get(patriarch.getEntryPointConceptId()).getName(),
            patriarch.getInheritanceType().toString()
        };
    }
}
