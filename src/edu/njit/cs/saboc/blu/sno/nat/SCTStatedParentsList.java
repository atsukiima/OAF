package edu.njit.cs.saboc.blu.sno.nat;

import edu.njit.cs.saboc.blu.sno.localdatasource.concept.SCTConcept;
import edu.njit.cs.saboc.nat.generic.NATBrowserPanel;
import edu.njit.cs.saboc.nat.generic.gui.filterable.list.renderer.SimpleConceptRenderer;
import edu.njit.cs.saboc.nat.generic.gui.panels.ConceptListPanel;

/**
 * Concept list for displaying the stated parents of the focus concept
 * 
 * @author Chris O
 */
public class SCTStatedParentsList extends ConceptListPanel<SCTConcept> {
    
    public SCTStatedParentsList(NATBrowserPanel<SCTConcept> mainPanel) {
        
        super(mainPanel, 
                SCTNATDataRetrievers.getStatedParentRetriever(mainPanel),
                new SimpleConceptRenderer<>(mainPanel), 
                true,
                true,
                true);
    }
}
