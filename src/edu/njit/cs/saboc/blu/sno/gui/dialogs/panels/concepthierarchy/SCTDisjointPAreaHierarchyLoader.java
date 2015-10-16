package edu.njit.cs.saboc.blu.sno.gui.dialogs.panels.concepthierarchy;

import edu.njit.cs.saboc.blu.sno.abn.disjointpareataxonomy.DisjointPAreaTaxonomy;
import edu.njit.cs.saboc.blu.sno.abn.disjointpareataxonomy.DisjointPartialArea;
import edu.njit.cs.saboc.blu.sno.datastructure.hierarchy.SCTConceptHierarchy;

/**
 *
 * @author Chris O
 */
public class SCTDisjointPAreaHierarchyLoader extends SCTConceptGroupHierarchyLoader<DisjointPartialArea> {
    
    private final DisjointPAreaTaxonomy taxonomy;
    
    public SCTDisjointPAreaHierarchyLoader(DisjointPAreaTaxonomy taxonomy, DisjointPartialArea parea, SCTConceptHierarchyViewPanel panel) {
        super(parea, panel);
        
        this.taxonomy = taxonomy;
    }
    
    public SCTConceptHierarchy getGroupHierarchy(DisjointPartialArea parea) {
        return parea.getConceptHierarchy();
    }
}