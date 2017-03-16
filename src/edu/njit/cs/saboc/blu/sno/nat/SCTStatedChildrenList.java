package edu.njit.cs.saboc.blu.sno.nat;

import edu.njit.cs.saboc.blu.sno.localdatasource.concept.SCTConcept;
import edu.njit.cs.saboc.nat.generic.NATBrowserPanel;
import edu.njit.cs.saboc.nat.generic.gui.filterable.list.renderer.SimpleConceptRenderer;
import edu.njit.cs.saboc.nat.generic.gui.panels.ConceptListPanel;

/**
 *
 * @author Chris O
 */
public class SCTStatedChildrenList extends ConceptListPanel<SCTConcept> {
    
    public SCTStatedChildrenList(
            NATBrowserPanel<SCTConcept> mainPanel, 
            SCTConceptBrowserDataSource dataSource) {
        
        super(mainPanel, 
                dataSource,
                SCTNATDataRetrievers.getStatedChildrenRetriever(dataSource),
                new SimpleConceptRenderer<>(dataSource, SimpleConceptRenderer.HierarchyDisplayInfo.None), 
                true,
                true,
                true);
    }
}
