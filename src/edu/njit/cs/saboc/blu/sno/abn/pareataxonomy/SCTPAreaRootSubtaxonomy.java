
package edu.njit.cs.saboc.blu.sno.abn.pareataxonomy;

import SnomedShared.Concept;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTArea;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTPArea;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTPAreaTaxonomy;
import edu.njit.cs.saboc.blu.sno.datastructure.hierarchy.SCTConceptHierarchy;
import edu.njit.cs.saboc.blu.sno.sctdatasource.SCTDataSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 *
 * @author Den
 */
public class SCTPAreaRootSubtaxonomy extends SCTPAreaTaxonomy implements RootSubtaxonomy {
    
    private SCTPAreaTaxonomy topLevelTaxonomy;
    
    public SCTPAreaRootSubtaxonomy(
            Concept sctRootConcept,
            String version,
            SCTDataSource dataSource,
            SCTConceptHierarchy conceptHierarchy,
            SCTPArea rootPArea,
            ArrayList<SCTArea> areas,
            HashMap<Integer, SCTPArea> pareas,
            HashMap<Integer, HashSet<Integer>> pareaHierarchy, 
            HashMap<Long, String> relNames,
            SCTPAreaTaxonomy topLevelTaxonomy) {
        
        super(sctRootConcept, version, dataSource, conceptHierarchy, rootPArea, areas, pareas, pareaHierarchy, relNames);
        
        this.topLevelTaxonomy = topLevelTaxonomy;
    }
    
    public SCTPAreaTaxonomy getTopLevelTaxonomy() {
        return topLevelTaxonomy;
    }
    
}
