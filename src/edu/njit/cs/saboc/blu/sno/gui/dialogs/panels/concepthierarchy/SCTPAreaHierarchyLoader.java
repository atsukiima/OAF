package edu.njit.cs.saboc.blu.sno.gui.dialogs.panels.concepthierarchy;

import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTPArea;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTPAreaTaxonomy;
import edu.njit.cs.saboc.blu.sno.datastructure.hierarchy.SCTConceptHierarchy;

/**
 *
 * @author Chris
 */
public class SCTPAreaHierarchyLoader extends SCTConceptGroupHierarchyLoader<SCTPArea> {
    
    private SCTPAreaTaxonomy taxonomy;
    
    public SCTPAreaHierarchyLoader(SCTPAreaTaxonomy taxonomy, SCTPArea parea, SCTConceptHierarchyViewPanel panel) {
        super(parea, panel);
        
        this.taxonomy = taxonomy;
    }
    
    public SCTConceptHierarchy getGroupHierarchy(SCTPArea parea) {
        return taxonomy.getDataSource().getPAreaConceptHierarchy(taxonomy, parea);
    }
}
