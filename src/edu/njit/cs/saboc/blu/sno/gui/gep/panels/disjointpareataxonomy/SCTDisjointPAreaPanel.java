
package edu.njit.cs.saboc.blu.sno.gui.gep.panels.disjointpareataxonomy;

import SnomedShared.Concept;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.disjointabn.GenericDisjointGroupPanel;
import edu.njit.cs.saboc.blu.sno.abn.disjointpareataxonomy.DisjointPartialArea;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTPArea;
import edu.njit.cs.saboc.blu.sno.datastructure.hierarchy.SCTConceptHierarchy;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.disjointpareataxonomy.configuration.SCTDisjointPAreaTaxonomyConfiguration;

/**
 *
 * @author Chris O
 */
public class SCTDisjointPAreaPanel extends GenericDisjointGroupPanel<Concept, DisjointPartialArea, SCTPArea, SCTConceptHierarchy, SCTDisjointPAreaTaxonomyConfiguration> {
    
    public SCTDisjointPAreaPanel(SCTDisjointPAreaTaxonomyConfiguration config) {
        super(
            new SCTDisjointPAreaDetailsPanel(config),
            new SCTDisjointPAreaHierarchyPanel(config), 
            new SCTDisjointPAreaConceptHierarchyPanel(config),
            config);
    }
}
