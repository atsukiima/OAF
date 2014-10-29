/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local;

import SnomedShared.pareataxonomy.Area;
import SnomedShared.pareataxonomy.PAreaSummary;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.PAreaTaxonomy;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.RootSubtaxonomy;
import edu.njit.cs.saboc.blu.sno.datastructure.hierarchy.SCTConceptHierarchy;
import edu.njit.cs.saboc.blu.sno.sctdatasource.SCTLocalDataSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 *
 * @author Den
 */
public class LocalPAreaRootSubtaxonomy extends LocalPAreaTaxonomy implements RootSubtaxonomy {
    
    private LocalPAreaTaxonomy topLevelTaxonomy;
    
    public LocalPAreaRootSubtaxonomy(SCTConceptHierarchy conceptHierarchy,
            PAreaSummary rootPArea,
            ArrayList<Area> areas,
            HashMap<Integer, PAreaSummary> pareas,
            HashMap<Integer, HashSet<Integer>> pareaHierarchy,
            HashMap<Long, String> lateralRels,
            String version,
            SCTLocalDataSource dataSource,
            LocalPAreaTaxonomy topLevelTaxonomy) {
        
        super(conceptHierarchy, rootPArea, areas, pareas, pareaHierarchy, lateralRels, version, dataSource);
        
        this.topLevelTaxonomy = topLevelTaxonomy;
    }
    
    public PAreaTaxonomy getTopLevelTaxonomy() {
        return topLevelTaxonomy;
    }

}
