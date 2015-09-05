package edu.njit.cs.saboc.blu.sno.gui.gep.panels.tan.band;

import SnomedShared.Concept;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.models.BLUAbstractConceptTableModel;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.entry.ContainerConceptEntry;
import edu.njit.cs.saboc.blu.sno.abn.tan.local.SCTCluster;

/**
 *
 * @author Chris O
 */
public class SCTBandConceptTableModel extends BLUAbstractConceptTableModel<ContainerConceptEntry<Concept, SCTCluster>> {

    public SCTBandConceptTableModel() {
        super(new String[]{"Concept Name", "Concept ID", "Overlapping Concept"});
    }

    @Override
    protected Object[] createRow(ContainerConceptEntry<Concept, SCTCluster> item) {
        String overlappingStr;
        
        if(item.getGroups().size() == 1) {
            overlappingStr = "No";
        } else {
            overlappingStr = String.format("Yes (%d)", item.getGroups().size());
        }
        
        return new Object[] {
            item.getConcept().getName(),
            item.getConcept().getId(),
            overlappingStr
        };
    }    
}
