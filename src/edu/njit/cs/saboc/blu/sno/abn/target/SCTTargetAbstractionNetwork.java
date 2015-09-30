package edu.njit.cs.saboc.blu.sno.abn.target;

import edu.njit.cs.saboc.blu.core.abn.targetbased.TargetAbstractionNetwork;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 *
 * @author Chris O
 */
public class SCTTargetAbstractionNetwork extends TargetAbstractionNetwork<SCTTargetGroup, SCTTargetAbstractionNetwork> {
   
    public SCTTargetAbstractionNetwork(
            SCTTargetGroup rootGroup,
            ArrayList<SCTTargetContainer> containers,
            HashMap<Integer, SCTTargetGroup> groups,
            HashMap<Integer, HashSet<Integer>> groupHierarchy) {
        
        super(rootGroup, containers, groups, groupHierarchy);
    }
    
    public SCTTargetAbstractionNetwork getReduced(int smallest, int largest) {
        return super.createReducedTargetAbN(new SCTTargetAbstractionNetworkGenerator(null), new SCTAggregateTargetAbNGenerator(), smallest, largest);
    }

}
