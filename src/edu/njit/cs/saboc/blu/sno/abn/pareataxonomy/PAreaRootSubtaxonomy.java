
package edu.njit.cs.saboc.blu.sno.abn.pareataxonomy;

import SnomedShared.Concept;
import SnomedShared.pareataxonomy.Area;
import SnomedShared.pareataxonomy.PAreaSummary;
import edu.njit.cs.saboc.blu.sno.sctdatasource.SCTDataSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 *
 * @author Den
 */
public class PAreaRootSubtaxonomy extends PAreaTaxonomy implements RootSubtaxonomy {
    
    private PAreaTaxonomy topLevelTaxonomy;
    
    public PAreaRootSubtaxonomy(
            Concept SNOMEDHierarchyRoot,
            PAreaSummary rootPArea,
            ArrayList<Area> areas,
            HashMap<Integer, PAreaSummary> pareas,
            HashMap<Integer, HashSet<Integer>> pareaHierarchy,
            HashMap<Long, String> lateralRels,
            String version,
            SCTDataSource dataSource,
            PAreaTaxonomy topLevelTaxonomy) {
        
        super(SNOMEDHierarchyRoot, rootPArea, areas, pareas, pareaHierarchy, lateralRels, version, dataSource);
        
        this.topLevelTaxonomy = topLevelTaxonomy;
    }
    
    public PAreaTaxonomy getTopLevelTaxonomy() {
        return topLevelTaxonomy;
    }
    
}
