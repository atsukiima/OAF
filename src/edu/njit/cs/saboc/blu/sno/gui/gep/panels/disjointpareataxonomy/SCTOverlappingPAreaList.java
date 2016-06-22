
package edu.njit.cs.saboc.blu.sno.gui.gep.panels.disjointpareataxonomy;

import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.disjointabn.OverlappingNodeList;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.disjointabn.OverlappingNodeTableModel;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTPArea;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.disjointpareataxonomy.configuration.SCTDisjointPAreaTaxonomyConfiguration;

/**
 *
 * @author Chris O
 */
public class SCTOverlappingPAreaList extends OverlappingNodeList<SCTPArea> {
    public SCTOverlappingPAreaList(SCTDisjointPAreaTaxonomyConfiguration config) {
        super(new GenericOverlappingGroupTableModel<SCTPArea>(config), 
                config);
    }
}
