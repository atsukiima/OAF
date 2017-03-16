package edu.njit.cs.saboc.blu.sno.nat;

import edu.njit.cs.saboc.blu.sno.localdatasource.concept.SCTConcept;
import edu.njit.cs.saboc.nat.generic.NATBrowserPanel;
import edu.njit.cs.saboc.nat.generic.gui.filterable.list.renderer.SimpleConceptRenderer;
import edu.njit.cs.saboc.nat.generic.gui.filterable.list.renderer.SimpleConceptRenderer.HierarchyDisplayInfo;
import edu.njit.cs.saboc.nat.generic.gui.panels.ConceptListPanel;

/**
 *
 * @author Chris O
 */
public class SCTStatedParentsList extends ConceptListPanel<SCTConcept> {
    
    public SCTStatedParentsList(
            NATBrowserPanel<SCTConcept> mainPanel, 
            SCTConceptBrowserDataSource dataSource) {
        
        super(
                mainPanel, 
                dataSource,
                SCTNATDataRetrievers.getStatedParentRetriever(dataSource),
                new SimpleConceptRenderer<>(dataSource, HierarchyDisplayInfo.None), 
                true,
                true,
                true);
    }
}
