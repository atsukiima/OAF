package edu.njit.cs.saboc.blu.sno.abn.target;

import edu.njit.cs.saboc.blu.core.abn.GroupHierarchy;
import edu.njit.cs.saboc.blu.core.abn.targetbased.TargetAbstractionNetwork;
import java.util.HashMap;

/**
 *
 * @author Chris O
 */
public class SCTTargetAbstractionNetwork extends TargetAbstractionNetwork<SCTTargetGroup, SCTTargetAbstractionNetwork> {

    public SCTTargetAbstractionNetwork(
            SCTTargetGroup rootGroup,
            HashMap<Integer, SCTTargetGroup> groups,
            GroupHierarchy<SCTTargetGroup> groupHierarchy) {
        
        super(rootGroup, groups, groupHierarchy);
    }
    

    @Override
    public SCTTargetAbstractionNetwork getReduced(int smallest) {
        return null;
    }

}
