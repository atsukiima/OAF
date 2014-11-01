package edu.njit.cs.saboc.blu.sno.gui.dialogs.panels.concepthierarchy;

import SnomedShared.Concept;
import SnomedShared.pareataxonomy.PAreaSummary;
import edu.njit.cs.saboc.blu.core.datastructure.hierarchy.SingleRootedHierarchy;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.PAreaTaxonomy;

/**
 *
 * @author Chris
 */
public class SCTPAreaHierarchyLoader extends SCTConceptGroupHierarchyLoader<PAreaSummary> {
    
    private PAreaTaxonomy taxonomy;
    
    public SCTPAreaHierarchyLoader(PAreaTaxonomy taxonomy, PAreaSummary parea, SCTConceptHierarchyViewPanel panel) {
        super(parea, panel);
        
        this.taxonomy = taxonomy;
    }
    
    public SingleRootedHierarchy<Concept> getGroupHierarchy(PAreaSummary parea) {
        return taxonomy.getSCTDataSource().getPAreaConceptHierarchy(taxonomy, parea);
    }
}
