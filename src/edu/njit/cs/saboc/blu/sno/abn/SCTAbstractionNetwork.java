package edu.njit.cs.saboc.blu.sno.abn;

import SnomedShared.Concept;
import edu.njit.cs.saboc.blu.core.abn.AbstractionNetwork;
import edu.njit.cs.saboc.blu.sno.sctdatasource.SCTDataSource;

/**
 * Represents some type of SNOMED CT abstraction network.
 * @author Chris
 */
public interface SCTAbstractionNetwork<T extends AbstractionNetwork> {
    
    public String getSCTVersion();
    
    public SCTDataSource getDataSource();
    
    public T getAbstractionNetwork();
}
