package edu.njit.cs.saboc.blu.sno.nat.panels;

import edu.njit.cs.saboc.blu.core.utils.filterable.list.Filterable;
import edu.njit.cs.saboc.blu.sno.localdatasource.concept.AttributeRelationship;
import edu.njit.cs.saboc.blu.sno.localdatasource.concept.SCTConcept;
import edu.njit.cs.saboc.blu.sno.nat.SCTConceptBrowserDataSource;
import edu.njit.cs.saboc.blu.sno.nat.SCTNATDataRetrievers;
import edu.njit.cs.saboc.blu.sno.nat.panels.attributerels.FilterableAttributeRelationshipEntry;
import edu.njit.cs.saboc.blu.sno.nat.panels.attributerels.FilterableRelationshipGroupEntry;
import edu.njit.cs.saboc.blu.sno.nat.panels.attributerels.RelationshipGroup;
import edu.njit.cs.saboc.blu.sno.nat.panels.attributerels.RelationshipGroupPanel;
import edu.njit.cs.saboc.nat.generic.NATBrowserPanel;
import edu.njit.cs.saboc.nat.generic.gui.filterable.nestedlist.FilterableNestedEntry;
import edu.njit.cs.saboc.nat.generic.gui.filterable.nestedlist.FilterableNestedEntryPanel;
import edu.njit.cs.saboc.nat.generic.gui.filterable.nestedlist.NestedFilterableList;
import edu.njit.cs.saboc.nat.generic.gui.filterable.nestedlist.NestedFilterableList.EntrySelectionListener;
import edu.njit.cs.saboc.nat.generic.gui.panels.ResultPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import static javafx.scene.input.KeyCode.T;
import javax.swing.BorderFactory;

/**
 *
 * @author Chris O
 */
public class AttributeRelationshipPanel extends ResultPanel<SCTConcept, ArrayList<AttributeRelationship>> {
    
    private final NestedFilterableList<RelationshipGroup, AttributeRelationship> attributeRelaitonshipList;
    
    public AttributeRelationshipPanel(
            NATBrowserPanel<SCTConcept> mainPanel,
            SCTConceptBrowserDataSource dataSource) {

        super(mainPanel, 
                dataSource, 
                SCTNATDataRetrievers.getAttributeRelationshipRetriever(dataSource));
        
        this.setBorder(BorderFactory.createTitledBorder(
                    BorderFactory.createLineBorder(Color.BLACK), 
                    "Attribute Relationships")
            );
        
        this.attributeRelaitonshipList = new NestedFilterableList<RelationshipGroup, AttributeRelationship>() {

            @Override
            public FilterableNestedEntryPanel<FilterableNestedEntry<RelationshipGroup, AttributeRelationship>> getEntry(
                    FilterableNestedEntry<RelationshipGroup, AttributeRelationship> entry, Optional<String> filter) {
                
                if(filter.isPresent()) {
                    entry.setCurrentFilter(filter.get());
                }
                
                FilterableRelationshipGroupEntry groupEntry = (FilterableRelationshipGroupEntry)entry;
                
                RelationshipGroupPanel relGroupPanel = new RelationshipGroupPanel(groupEntry);
                
                return  (FilterableNestedEntryPanel<FilterableNestedEntry<RelationshipGroup, AttributeRelationship>>)
                        (FilterableNestedEntryPanel<?>)relGroupPanel;
            }
        };
        
        this.attributeRelaitonshipList.addEntrySelectionListener(new EntrySelectionListener<AttributeRelationship>() {

            @Override
            public void entryClicked(AttributeRelationship entry) {
                
            }

            @Override
            public void entryDoubleClicked(AttributeRelationship entry) {
                mainPanel.getFocusConceptManager().navigateTo(entry.getTarget());
            }

            @Override
            public void noEntrySelected() {
                
            }
        });
        
        this.setLayout(new BorderLayout());
        
        this.add(attributeRelaitonshipList, BorderLayout.CENTER);
    }

    @Override
    public void dataPending() {
        attributeRelaitonshipList.clearContents();
    }

    @Override
    public void displayResults(ArrayList<AttributeRelationship> data) {
        
        ArrayList<RelationshipGroup> relGroups = new ArrayList<>();
        
        Map<Integer, ArrayList<AttributeRelationship>> relGroupMap = new HashMap<>();
        
        data.forEach( (rel) -> {
            if(!relGroupMap.containsKey(rel.getGroup())) {
                relGroupMap.put(rel.getGroup(), new ArrayList<>());
            }
            
            relGroupMap.get(rel.getGroup()).add(rel);
        });
        
        relGroupMap.forEach( (id, attributeRels) -> {
            relGroups.add(new RelationshipGroup(id, attributeRels));
        });
        
        ArrayList<FilterableNestedEntry<RelationshipGroup, AttributeRelationship>> relGroupEntries = new ArrayList<>();
        
        relGroups.forEach( (relGroup) -> {
            ArrayList<Filterable<AttributeRelationship>> relEntries = new ArrayList<>();
            
            relGroup.getAttributeRelationships().forEach( (rel) -> {
                relEntries.add(new FilterableAttributeRelationshipEntry(rel));
            });
            
            relGroupEntries.add(new FilterableRelationshipGroupEntry(relGroup, relEntries));
        });
        
        attributeRelaitonshipList.displayContents(relGroupEntries);
        
        this.setBorder(BorderFactory.createTitledBorder(
                    BorderFactory.createLineBorder(Color.BLACK), 
                    String.format("Attribute Relationships (%d)", data.size()))
            );
    }

    @Override
    protected void setFontSize(int fontSize) {
        
    }

}
