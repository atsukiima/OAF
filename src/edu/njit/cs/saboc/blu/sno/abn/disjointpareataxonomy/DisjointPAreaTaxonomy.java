package edu.njit.cs.saboc.blu.sno.abn.disjointpareataxonomy;

import SnomedShared.Concept;
import edu.njit.cs.saboc.blu.core.abn.disjoint.DisjointAbstractionNetwork;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTPArea;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTPAreaTaxonomy;
import edu.njit.cs.saboc.blu.sno.datastructure.hierarchy.SCTConceptHierarchy;
import java.util.HashMap;
import java.util.HashSet;

/**
 *
 * @author Chris
 */
public class DisjointPAreaTaxonomy extends DisjointAbstractionNetwork<SCTPAreaTaxonomy, SCTPArea, Concept, SCTConceptHierarchy, DisjointPartialArea> {
    
    public DisjointPAreaTaxonomy(SCTPAreaTaxonomy parentTaxonomy, 
            HashMap<Integer, DisjointPartialArea> disjointPAreas, 
            HashMap<Integer, HashSet<Integer>> disjointGroupHierarchy,
            int levels,
            HashSet<SCTPArea> allAreaPAreas,
            HashSet<SCTPArea> overlappingPAreas) {

        super(parentTaxonomy, disjointPAreas, disjointGroupHierarchy, levels, allAreaPAreas, overlappingPAreas);
    }
            
    protected Concept getGroupRoot(SCTPArea parea) {
        return parea.getRoot();
    }
    
    public HashSet<SCTPArea> getPAreasWithNoOverlap() {
        HashSet<SCTPArea> allPAreas = new HashSet<SCTPArea>(super.allGroups);
        
        allPAreas.removeAll(super.overlappingGroups);
        
        return allPAreas;
    }
    
    public SCTPAreaTaxonomy getSourcePAreaTaxonomy() {
        return parentAbN;
    }
}

