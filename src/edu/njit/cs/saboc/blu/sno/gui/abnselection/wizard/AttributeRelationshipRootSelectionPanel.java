package edu.njit.cs.saboc.blu.sno.gui.abnselection.wizard;

import edu.njit.cs.saboc.blu.core.abn.AbstractionNetwork;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.configuration.AbNConfiguration;
import edu.njit.cs.saboc.blu.core.gui.panels.abnderivationwizard.OntologySearcher;
import edu.njit.cs.saboc.blu.core.gui.panels.abnderivationwizard.rootselection.BaseRootSelectionOptionsPanel;
import edu.njit.cs.saboc.blu.core.gui.panels.abnderivationwizard.rootselection.SearchForRootPanel;
import edu.njit.cs.saboc.blu.core.gui.panels.abnderivationwizard.rootselection.SelectOntologyRootPanel;
import edu.njit.cs.saboc.blu.core.ontology.Ontology;
import edu.njit.cs.saboc.blu.core.utils.comparators.ConceptNameComparator;
import edu.njit.cs.saboc.blu.sno.localdatasource.concept.SCTConcept;
import edu.njit.cs.saboc.blu.sno.sctdatasource.SCTRelease;
import java.util.ArrayList;

/**
 * A panel for selecting SNOMED CT hierarchy root concepts where 
 * there are concepts in the hierarchy that are modeled with 
 * at least one attribute relationship
 * 
 * @author Chris O
 * 
 * @param <T>
 */
public class AttributeRelationshipRootSelectionPanel<T extends AbstractionNetwork> extends BaseRootSelectionOptionsPanel<T> {
    
    private final SelectOntologyRootPanel selectRoot;
    private final SearchForRootPanel searchForRoot;
    
    public AttributeRelationshipRootSelectionPanel(AbNConfiguration config) {
        this.selectRoot = new SelectOntologyRootPanel("Select SNOMED CT Subhierarchy", config) {

            @Override
            public void initialize(Ontology ontology, OntologySearcher searcher) {
                super.initialize(ontology, searcher);
                
                SCTRelease release = (SCTRelease)ontology;
                
                ArrayList<SCTConcept> validRoots = new ArrayList<>(release.getHierarchiesWithAttributeRelationships());
                validRoots.sort(new ConceptNameComparator());
                
                super.setRootList(validRoots);
            }
        };
        
        this.searchForRoot = new SearchForRootPanel(config);
        
        super.addRootSelectionOption(selectRoot);
        super.addRootSelectionOption(searchForRoot);
    }
    
    
}
