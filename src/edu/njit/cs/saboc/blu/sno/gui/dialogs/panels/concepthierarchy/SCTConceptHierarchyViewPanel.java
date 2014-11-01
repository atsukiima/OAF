package edu.njit.cs.saboc.blu.sno.gui.dialogs.panels.concepthierarchy;

import SnomedShared.Concept;
import SnomedShared.generic.GenericConceptGroup;
import SnomedShared.overlapping.ClusterSummary;
import SnomedShared.pareataxonomy.PAreaSummary;
import edu.njit.cs.saboc.blu.core.gui.dialogs.concepthierarchy.ConceptGroupHierarchicalViewPanel;
import edu.njit.cs.saboc.blu.core.gui.dialogs.concepthierarchy.HierarchyPanelClickListener;
import edu.njit.cs.saboc.blu.sno.abn.SCTAbstractionNetwork;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.PAreaTaxonomy;
import edu.njit.cs.saboc.blu.sno.abn.tan.TribalAbstractionNetwork;

/**
 *
 * @author Chris
 */
public class SCTConceptHierarchyViewPanel extends ConceptGroupHierarchicalViewPanel<Concept> {
        
    public SCTConceptHierarchyViewPanel(GenericConceptGroup group, 
            final SCTAbstractionNetwork abn, 
            final String groupType, 
            final HierarchyPanelClickListener<Concept> conceptSelectListener) {
        
        super(group, abn, groupType, "Concept", new SCTConceptPainter(), conceptSelectListener);
    }
    
    public SCTConceptGroupHierarchyLoader getHierarchyLoader() {
        if(abstractionNetwork instanceof PAreaTaxonomy) {
            return new SCTPAreaHierarchyLoader((PAreaTaxonomy)abstractionNetwork, (PAreaSummary)group, this);
        } else if(abstractionNetwork instanceof TribalAbstractionNetwork) {
            return new SCTTANHierarchyLoader((TribalAbstractionNetwork)abstractionNetwork, (ClusterSummary)group, this);
        } else {
            return null;
        }
    }
}
