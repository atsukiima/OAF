package edu.njit.cs.saboc.blu.sno.gui.gep.panels;

import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.AbstractGroupList;
import edu.njit.cs.saboc.blu.sno.abn.tan.local.SCTCluster;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.tan.cluster.SCTClusterTableModel;
import java.util.ArrayList;
import java.util.Optional;

/**
 *
 * @author Chris O
 */
public class SCTClusterList extends AbstractGroupList<SCTCluster> {

    public SCTClusterList(SCTTANConfiguration config) {
        super(new SCTClusterTableModel(config));
    }

    @Override
    protected String getBorderText(Optional<ArrayList<SCTCluster>> pareas) {
        if(pareas.isPresent()) {
            return String.format("Clusters (%d)", pareas.get().size());
        } else {
            return "Clusters";
        }
    }
}