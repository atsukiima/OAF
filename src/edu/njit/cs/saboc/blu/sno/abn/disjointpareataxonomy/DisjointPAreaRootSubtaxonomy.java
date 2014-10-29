package edu.njit.cs.saboc.blu.sno.abn.disjointpareataxonomy;

import SnomedShared.Concept;
import SnomedShared.pareataxonomy.PAreaSummary;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.PAreaTaxonomy;
import edu.njit.cs.saboc.blu.sno.datastructure.hierarchy.SCTMultiRootedConceptHierarchy;
import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Queue;

/**
 *
 * @author Den
 */
public class DisjointPAreaRootSubtaxonomy extends DisjointPAreaTaxonomy {
    
    private HashSet<PAreaSummary> subtaxonomyPAreas;
    
    private HashSet<DisjointPartialArea> implicitDisjointPAreas;
    
    public DisjointPAreaRootSubtaxonomy(
            HashSet<PAreaSummary> topLevelTaxonomyPAreas, 
            HashSet<PAreaSummary> subtaxonomyPAreas, 
            SCTMultiRootedConceptHierarchy topLevelConceptHierarchy,
            PAreaTaxonomy subtaxonomy) {
        
        super(topLevelTaxonomyPAreas, topLevelConceptHierarchy, subtaxonomy);
        
        this.subtaxonomyPAreas = subtaxonomyPAreas;

        HashSet<DisjointPartialArea> pareas = disjointGroupHierarchy.getNodesInHierarchy();
        HashSet<DisjointPartialArea> djpaRoots = disjointGroupHierarchy.getRoots();
        
        HashSet<Concept> subtaxonomyPAreaRoots = new HashSet<Concept>();
        
        for(PAreaSummary parea : subtaxonomyPAreas) {
            subtaxonomyPAreaRoots.add(parea.getRoot());
        }
        
        HashSet<DisjointPartialArea> subtaxonomyDisjointPAreas = new HashSet<DisjointPartialArea>();
        
        HashSet<DisjointPartialArea> roots = new HashSet<DisjointPartialArea>();
        
        // Identify the disjoint pareas that are direct descdents of the area roots in the root subtaxonomy
        for(DisjointPartialArea parea : pareas) {
            HashSet<PAreaSummary> overlaps = parea.getOverlaps();
            
            for(PAreaSummary overlap : overlaps) {

                if(subtaxonomyPAreaRoots.contains(overlap.getRoot())){
                    
                    if(disjointGroupHierarchy.getRoots().contains(parea)) {
                        roots.add(parea);
                    }
                    
                    subtaxonomyDisjointPAreas.add(parea);
                    break;
                }
            }
        }
        
        HashSet<DisjointPartialArea> subtaxonomyLeaves = new HashSet<DisjointPartialArea>();
        
        // Identify the leaf concepts that are direct descendents of the area roots in the root subtaxonomy
        for(DisjointPartialArea parea : subtaxonomyDisjointPAreas) {
            if(disjointGroupHierarchy.getChildren(parea).isEmpty()) {
                subtaxonomyLeaves.add(parea);
            }
        }
        
        HashSet<DisjointPartialArea> implicitDisjointPAreas = new HashSet<DisjointPartialArea>();

        DisjointPAreaHierarchy subtaxonomyHierarchy = new DisjointPAreaHierarchy(roots);
        
        Queue<DisjointPartialArea> queue = new ArrayDeque<DisjointPartialArea>();
        
        HashSet<DisjointPartialArea> processed = new HashSet<DisjointPartialArea>();
        processed.addAll(subtaxonomyLeaves);
        
        queue.addAll(subtaxonomyLeaves);
        
        // Add all of the ancestors of the leaves (including those which are not descendents of the subtaxonomy area roots) 
        // to the new hierarchy
        while(!queue.isEmpty()) {
            DisjointPartialArea parea = queue.remove();
            
            HashSet<DisjointPartialArea> parents = disjointGroupHierarchy.getParents(parea);
            
            for(DisjointPartialArea parent : parents) {
                if(!processed.contains(parent)) {
                    processed.add(parent);
                    
                    if(!djpaRoots.contains(parent)) {
                        queue.add(parent);
                    }
                    
                    if(!subtaxonomyDisjointPAreas.contains(parent)) {
                        implicitDisjointPAreas.add(parent);
                    }
                }
                
                subtaxonomyHierarchy.addIsA(parea, parent);
            }
        }
        
        this.disjointGroupHierarchy = subtaxonomyHierarchy;
        this.implicitDisjointPAreas = implicitDisjointPAreas;
    }
    
    public HashSet<PAreaSummary> getSubtaxonomyPAreas() {
        return subtaxonomyPAreas;
    }
    
    public HashSet<DisjointPartialArea> getImplicitDisjointPAreas() {
        return implicitDisjointPAreas;
    }
}
