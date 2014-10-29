package edu.njit.cs.saboc.blu.sno.abn;

import SnomedShared.Concept;
import SnomedShared.generic.GenericConceptGroup;
import SnomedShared.generic.GenericGroupContainer;
import edu.njit.cs.saboc.blu.core.abn.AbstractionNetwork;
import edu.njit.cs.saboc.blu.sno.sctdatasource.SCTDataSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Represents some type of SNOMED CT abstraction network.
 * @author Chris
 */
public abstract class SCTAbstractionNetwork extends AbstractionNetwork {

    protected String SNOMEDVersion;
    protected SCTDataSource dataSource;
    protected Concept SNOMEDHierarchyRoot;
    
    public SCTAbstractionNetwork(Concept SNOMEDHierarchyRoot,
            ArrayList<? extends GenericGroupContainer> containers,
            HashMap<Integer, ? extends GenericConceptGroup> groups,
            HashMap<Integer, HashSet<Integer>> groupHierarchy,
            String SNOMEDVersion,
            SCTDataSource dataSource) {
        
        super(containers, groups, groupHierarchy);
        
        this.SNOMEDVersion = SNOMEDVersion;
        this.dataSource = dataSource;
        this.SNOMEDHierarchyRoot = SNOMEDHierarchyRoot;
    }
    
    public Concept getSNOMEDHierarchyRoot() {
        return SNOMEDHierarchyRoot;
    }

    public String getVersion() {
        return SNOMEDVersion;
    }
    
    public SCTDataSource getSCTDataSource() {
        return dataSource;
    }
}
