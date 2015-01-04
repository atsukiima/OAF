package edu.njit.cs.saboc.blu.sno.abn.disjointpareataxonomy;

import SnomedShared.Concept;
import edu.njit.cs.saboc.blu.core.abn.disjoint.DisjointAbstractionNetwork;
import edu.njit.cs.saboc.blu.core.datastructure.hierarchy.MultiRootedHierarchy;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTPArea;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTPAreaTaxonomy;
import edu.njit.cs.saboc.blu.sno.datastructure.hierarchy.SCTMultiRootedConceptHierarchy;
import java.util.HashSet;

/**
 *
 * @author Chris
 */
public class DisjointPAreaTaxonomy extends DisjointAbstractionNetwork<SCTPAreaTaxonomy, SCTPArea, Concept, DisjointPartialArea> {
    
    public DisjointPAreaTaxonomy(HashSet<SCTPArea> pareas, SCTMultiRootedConceptHierarchy areaConceptHierarchy, 
               SCTPAreaTaxonomy taxonomy) {

        super(taxonomy, pareas, areaConceptHierarchy);
    }
    
    protected MultiRootedHierarchy<DisjointPartialArea> createDisjointGroupHierarchy(HashSet<DisjointPartialArea> roots) {
        return new DisjointPAreaHierarchy(roots);
    }
    
    protected DisjointPartialArea createDisjointGroup(Concept root, HashSet<SCTPArea> overlapsIn) {
        return new DisjointPartialArea(root, overlapsIn);
    }
    
    protected Concept getGroupRoot(SCTPArea parea) {
        return parea.getRoot();
    }
    
    public DisjointPAreaHierarchy getDisjointPAreaHierarchy() {
        return (DisjointPAreaHierarchy)super.getHierarchy();
    }
    
    public HashSet<DisjointPartialArea> getDisjointPAreas() {
        return getDisjointPAreaHierarchy().getNodesInHierarchy();
    }
    
    public HashSet<SCTPArea> getPAreasWithNoOverlap() {
        HashSet<SCTPArea> allPAreas = new HashSet<SCTPArea>(super.allGroups);
        
        allPAreas.removeAll(super.overlappingGroups);
        
        return allPAreas;
    }
    
    public SCTPAreaTaxonomy getSourcePAreaTaxonomy() {
        return abstractionNetwork;
    }
}

