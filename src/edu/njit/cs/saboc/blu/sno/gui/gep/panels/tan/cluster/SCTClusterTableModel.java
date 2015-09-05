package edu.njit.cs.saboc.blu.sno.gui.gep.panels.tan.cluster;

import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.models.BLUAbstractGroupTableModel;
import edu.njit.cs.saboc.blu.sno.abn.tan.local.SCTCluster;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.SCTTANConfiguration;

/**
 *
 * @author Chris O
 */
public class SCTClusterTableModel extends BLUAbstractGroupTableModel<SCTCluster> {
    public SCTClusterTableModel(SCTTANConfiguration config) {
        super(config);
    }
}
