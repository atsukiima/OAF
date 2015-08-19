package edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy.parea;

import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.models.BLUAbstractChildGroupTableModel;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTPArea;

/**
 *
 * @author Chris O
 */
public class SCTChildPAreaTableModel extends BLUAbstractChildGroupTableModel<SCTPArea> {

    public SCTChildPAreaTableModel() {
        super(new String[] {
            "Child Partial-area", 
            "# Concepts", 
            "Area"
        });
    }
    

    @Override
    protected Object[] createRow(SCTPArea item) {       
        return new Object[] {
            item.getRoot().getName(),
            item.getConceptCount(),
            "---ATTRIBUTE RELS---"
        };
    }
}
