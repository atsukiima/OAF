package edu.njit.cs.saboc.blu.sno.gui.dialogs.panels.concepthierarchy;

import SnomedShared.Concept;
import SnomedShared.overlapping.ClusterSummary;
import edu.njit.cs.saboc.blu.core.gui.dialogs.concepthierarchy.ConceptGroupHierarchicalViewPanel;
import edu.njit.cs.saboc.blu.core.gui.dialogs.concepthierarchy.HierarchyPanelClickListener;
import edu.njit.cs.saboc.blu.sno.abn.SCTAbstractionNetwork;
import edu.njit.cs.saboc.blu.sno.abn.disjointpareataxonomy.DisjointPAreaTaxonomy;
import edu.njit.cs.saboc.blu.sno.abn.disjointpareataxonomy.DisjointPartialArea;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTAggregatePArea;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTPArea;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTPAreaTaxonomy;
import edu.njit.cs.saboc.blu.sno.abn.tan.TribalAbstractionNetwork;
import edu.njit.cs.saboc.blu.sno.datastructure.hierarchy.SCTConceptHierarchy;

/**
 *
 * @author Chris
 */
public class SCTConceptHierarchyViewPanel extends ConceptGroupHierarchicalViewPanel<Concept, SCTConceptHierarchy> {
        
    public SCTConceptHierarchyViewPanel(
            final SCTAbstractionNetwork abn, 
            final String groupType, 
            final HierarchyPanelClickListener<Concept> conceptSelectListener) {
        
        super(abn.getAbstractionNetwork(), groupType, "Concept", new SCTConceptPainter(), conceptSelectListener);
    }
    
    public SCTConceptGroupHierarchyLoader getHierarchyLoader() {
        if(abstractionNetwork instanceof SCTPAreaTaxonomy) {
            SCTPAreaTaxonomy taxonomy = (SCTPAreaTaxonomy)abstractionNetwork;
            
            if(taxonomy.isReduced()) {
                return new SCTAggregatePAreaHierarchyLoader(taxonomy, (SCTAggregatePArea)group, this);
            } else {
                return new SCTPAreaHierarchyLoader(taxonomy, (SCTPArea)group, this);
            }
 
        } else if(abstractionNetwork instanceof TribalAbstractionNetwork) {
            return new SCTTANHierarchyLoader((TribalAbstractionNetwork)abstractionNetwork, (ClusterSummary)group, this);
        } else if(abstractionNetwork instanceof DisjointPAreaTaxonomy) {
            return new SCTDisjointPAreaHierarchyLoader((DisjointPAreaTaxonomy)abstractionNetwork, (DisjointPartialArea)group, this);
        } else {
            return null;
        }
    }
}
