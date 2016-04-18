package edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy.area;

import SnomedShared.Concept;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.entry.ContainerConceptEntry;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.models.BLUAbstractAbNNodeTableModel;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTArea;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTPArea;

/**
 *
 * @author Chris O
 */
public class SCTAreaConceptTableModel extends BLUAbstractAbNNodeTableModel<SCTArea, ContainerConceptEntry<Concept, SCTPArea>> {

    public SCTAreaConceptTableModel() {
        super(new String[]{"Concept Name", "Concept ID", "Overlapping Concept"});
    }

    @Override
    protected Object[] createRow(ContainerConceptEntry<Concept, SCTPArea> item) {
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
