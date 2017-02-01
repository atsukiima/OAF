package edu.njit.cs.saboc.blu.sno.abn.tan;

import edu.njit.cs.saboc.blu.core.abn.tan.ClusterTribalAbstractionNetwork;
import edu.njit.cs.saboc.blu.core.abn.tan.provenance.SimpleClusterTANDerivation;
import edu.njit.cs.saboc.blu.sno.abn.tan.provenance.SCTStatedTANDerivation;

/**
 *
 * @author Chris O
 */
public class SCTStatedTAN extends ClusterTribalAbstractionNetwork {
    public SCTStatedTAN(ClusterTribalAbstractionNetwork base) {
        
        super(base, new SCTStatedTANDerivation(
                (SimpleClusterTANDerivation)base.getDerivation()));
    }
}
