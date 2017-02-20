package edu.njit.cs.saboc.blu.sno.abn.tan.provenance;

import edu.njit.cs.saboc.blu.core.abn.tan.provenance.SimpleClusterTANDerivation;

/**
 *
 * @author cro3
 */
public class SCTStatedTANDerivation extends SimpleClusterTANDerivation {
    public SCTStatedTANDerivation(SimpleClusterTANDerivation base) {
        super(base);
    }

    @Override
    public String getDescription() {
        return "Derived Stated Cluster TAN";
    }
}
