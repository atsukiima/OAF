package edu.njit.cs.saboc.blu.sno.gui.dialogs.panels.concepthierarchy;

import SnomedShared.Concept;
import SnomedShared.generic.GenericConceptGroup;
import SnomedShared.overlapping.ClusterSummary;
import edu.njit.cs.saboc.blu.core.gui.dialogs.concepthierarchy.ConceptGroupHierarchicalViewPanel;
import edu.njit.cs.saboc.blu.core.gui.dialogs.concepthierarchy.HierarchyPanelClickListener;
import edu.njit.cs.saboc.blu.sno.abn.SCTAbstractionNetwork;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTPArea;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTPAreaTaxonomy;
import edu.njit.cs.saboc.blu.sno.abn.tan.TribalAbstractionNetwork;
import edu.njit.cs.saboc.blu.sno.datastructure.hierarchy.SCTConceptHierarchy;

/**
 *
 * @author Chris
 */
public class SCTConceptHierarchyViewPanel extends ConceptGroupHierarchicalViewPanel<Concept, SCTConceptHierarchy> {
        
    public SCTConceptHierarchyViewPanel(GenericConceptGroup group, 
            final SCTAbstractionNetwork abn, 
            final String groupType, 
            final HierarchyPanelClickListener<Concept> conceptSelectListener) {
        
        super(group, abn.getAbstractionNetwork(), groupType, "Concept", new SCTConceptPainter(), conceptSelectListener);
    }
    
    public SCTConceptGroupHierarchyLoader getHierarchyLoader() {
        if(abstractionNetwork instanceof SCTPAreaTaxonomy) {
            return new SCTPAreaHierarchyLoader((SCTPAreaTaxonomy)abstractionNetwork, (SCTPArea)group, this);
        } else if(abstractionNetwork instanceof TribalAbstractionNetwork) {
            return new SCTTANHierarchyLoader((TribalAbstractionNetwork)abstractionNetwork, (ClusterSummary)group, this);
        } else {
            return null;
        }
    }
}
