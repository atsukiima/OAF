package edu.njit.cs.saboc.blu.sno.abn.tan.provenance;

import edu.njit.cs.saboc.blu.core.abn.tan.provenance.SimpleClusterTANDerivation;

/**
 * Stores the arguments needed to create a TAN from the stated
 * hierarchical relationships of a hierarchy of concepts
 * 
 * @author Chris O
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
