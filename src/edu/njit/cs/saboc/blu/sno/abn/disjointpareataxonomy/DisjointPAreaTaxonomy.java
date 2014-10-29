package edu.njit.cs.saboc.blu.sno.abn.disjointpareataxonomy;

import SnomedShared.Concept;
import SnomedShared.pareataxonomy.PAreaSummary;
import edu.njit.cs.saboc.blu.core.abn.disjoint.DisjointAbstractionNetwork;
import edu.njit.cs.saboc.blu.core.datastructure.hierarchy.MultiRootedHierarchy;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.PAreaTaxonomy;
import edu.njit.cs.saboc.blu.sno.datastructure.hierarchy.SCTMultiRootedConceptHierarchy;
import java.util.HashSet;

/**
 *
 * @author Chris
 */
public class DisjointPAreaTaxonomy extends DisjointAbstractionNetwork<PAreaTaxonomy, PAreaSummary, Concept, DisjointPartialArea> {
    
    public DisjointPAreaTaxonomy(HashSet<PAreaSummary> pareas, SCTMultiRootedConceptHierarchy areaConceptHierarchy, 
               PAreaTaxonomy taxonomy) {

        super(taxonomy, pareas, areaConceptHierarchy);
    }
    
    protected MultiRootedHierarchy<DisjointPartialArea> createDisjointGroupHierarchy(HashSet<DisjointPartialArea> roots) {
        return new DisjointPAreaHierarchy(roots);
    }
    
    protected DisjointPartialArea createDisjointGroup(Concept root, HashSet<PAreaSummary> overlapsIn) {
        return new DisjointPartialArea(root, overlapsIn);
    }
    
    protected Concept getGroupRoot(PAreaSummary parea) {
        return parea.getRoot();
    }
    
    public DisjointPAreaHierarchy getDisjointPAreaHierarchy() {
        return (DisjointPAreaHierarchy)super.getHierarchy();
    }
    
    public HashSet<DisjointPartialArea> getDisjointPAreas() {
        return getDisjointPAreaHierarchy().getNodesInHierarchy();
    }
    
    public HashSet<PAreaSummary> getPAreasWithNoOverlap() {
        HashSet<PAreaSummary> allPAreas = new HashSet<PAreaSummary>(super.allGroups);
        
        allPAreas.removeAll(super.overlappingGroups);
        
        return allPAreas;
    }
    
    public PAreaTaxonomy getSourcePAreaTaxonomy() {
        return abstractionNetwork;
    }
}

