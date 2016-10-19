package edu.njit.cs.saboc.blu.sno.gui.abnselection;

import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.pareataxonomy.configuration.PAreaTaxonomyConfiguration;
import edu.njit.cs.saboc.blu.core.gui.panels.ConceptSearchConfiguration;
import edu.njit.cs.saboc.blu.core.gui.panels.ConceptSearchPanel;
import edu.njit.cs.saboc.blu.core.gui.panels.ConceptSearchPanel.SearchType;
import edu.njit.cs.saboc.blu.core.ontology.Concept;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy.configuration.SCTPAreaTaxonomyConfigurationFactory;
import edu.njit.cs.saboc.blu.sno.sctdatasource.SCTRelease;
import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * A user interface for creating subject-specific abstraction networks
 * 
 * @author Chris O
 */
public class SubjectAbstractionNetworkPanel extends JPanel {
    
    private final ConceptSearchPanel conceptSearchPanel;
    
    private Optional<SCTRelease> currentRelease = Optional.empty();
    
    private final HierarchySelectionAction selectionAction;
    
    private final JButton deriveButton;
    
    private Optional<Concept> selectedConcept = Optional.empty();
    
    public SubjectAbstractionNetworkPanel(
            String abstractionNetworkName, 
            HierarchySelectionAction selectionAction) {
        
        this.selectionAction = selectionAction;
        
        this.setLayout(new BorderLayout());
        
        SCTPAreaTaxonomyConfigurationFactory dummyFactory = new SCTPAreaTaxonomyConfigurationFactory();
        PAreaTaxonomyConfiguration config = dummyFactory.createConfiguration(null, null);
        
        conceptSearchPanel = new ConceptSearchPanel(config, new ConceptSearchConfiguration() {

            @Override
            public ArrayList<Concept> doSearch(ConceptSearchPanel.SearchType type, String query) {
                
                if(currentRelease.isPresent()) {
                    return searchOntology(type, query);
                } else {
                    return new ArrayList<>();
                }
            }

            @Override
            public void searchResultSelected(Concept c) {
                deriveButton.setEnabled(true);
                
                selectedConcept = Optional.of(c);
            }

            @Override
            public void noSearchResultSelected() {
                deriveButton.setEnabled(false);
                
                selectedConcept = Optional.empty();
            }
        });
        
        this.conceptSearchPanel.setEnabled(false);
        
        this.add(conceptSearchPanel, BorderLayout.CENTER);
        
        this.deriveButton = new JButton(String.format("Create %s", abstractionNetworkName));
        this.deriveButton.addActionListener( (ae) -> {
            
            if(selectedConcept.isPresent()) {
                Concept c = selectedConcept.get();
                
                DummyConcept dummyConcept = new DummyConcept((Long)c.getID(), c.getName());
                
                selectionAction.performHierarchySelectionAction(dummyConcept, false);
            }
        });
        
        JPanel lowerPanel = new JPanel();
        lowerPanel.add(deriveButton);
        
        this.add(lowerPanel, BorderLayout.SOUTH);
    }
   
    public void setCurrentRelease(SCTRelease release) {
        this.currentRelease = Optional.of(release);
        
        this.conceptSearchPanel.setEnabled(true);
    }
    
    public void clearCurrentRelease() {
        this.currentRelease = Optional.empty();
        
        this.conceptSearchPanel.setEnabled(false);
    }
    
    public ArrayList<Concept> searchOntology(ConceptSearchPanel.SearchType type, String query) {
        SCTRelease theRelease = currentRelease.get();
        
        ArrayList<Concept> results;
        
        if(type.equals(SearchType.Starting)) {
            results = new ArrayList<>(theRelease.searchStarting(query));
        } else if(type.equals(SearchType.Anywhere)) {
            results = new ArrayList<>(theRelease.searchAnywhere(query));
        } else if(type.equals(SearchType.Exact)) {
            results = new ArrayList<>(theRelease.searchExact(query));
        } else {
            if(isValidConceptId(query)) {
                results = new ArrayList<>(Collections.singletonList(theRelease.getConceptFromId(Long.parseLong(query))));
            } else {
                results = new ArrayList<>();
            }
        }
        
        results.sort( (a, b) -> {
            return a.getName().compareToIgnoreCase(b.getName());
        });
        
        return results;
    }
    
    private boolean isValidConceptId(String str) {
        try {
            
            long l = Long.parseLong(str);
            
        } catch (NumberFormatException nfe) {
            return false;
        }
        
        return true;
    }
    
    public void setEnabled(boolean value) {
        super.setEnabled(value);
        
        this.deriveButton.setEnabled(value);
        this.conceptSearchPanel.setEnabled(value);
    }
}
