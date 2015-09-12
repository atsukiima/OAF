
package edu.njit.cs.saboc.blu.sno.gui.gep.panels.disjointpareataxonomy;

import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.disjointabn.GenericOverlappingGroupList;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.disjointabn.GenericOverlappingGroupTableModel;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTPArea;

/**
 *
 * @author Chris O
 */
public class SCTOverlappingPAreaList extends GenericOverlappingGroupList<SCTPArea> {
    public SCTOverlappingPAreaList(SCTDisjointPAreaTaxonomyConfiguration config) {
        super(new GenericOverlappingGroupTableModel<SCTPArea>(config), 
                config);
    }
}
