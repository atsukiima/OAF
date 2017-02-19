package edu.njit.cs.saboc.blu.sno.nat.panels;

import edu.njit.cs.saboc.blu.core.datastructure.hierarchy.Hierarchy;
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
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import javax.swing.BorderFactory;
import javax.swing.JCheckBox;

/**
 *
 * @author Chris O
 */
public class AttributeRelationshipPanel extends ResultPanel<SCTConcept, ArrayList<AttributeRelationship>> {
    
    private final NestedFilterableList<RelationshipGroup, AttributeRelationship> attributeRelaitonshipList;
    
    private final JCheckBox chkHideRelationshipGroups;
    
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
        
        
        this.chkHideRelationshipGroups = new JCheckBox("Hide Relationship Groups");
        this.chkHideRelationshipGroups.addActionListener( (ae) -> {
            super.reload();
        });
        
        this.attributeRelaitonshipList.addOptionPanelComponent(chkHideRelationshipGroups);
        
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
        
        if(this.chkHideRelationshipGroups.isSelected()) {
            
            Map<SCTConcept, ArrayList<AttributeRelationship>> uniqueRelsOfType = new HashMap<>();
            
            data.forEach((rel) -> {
               if(!uniqueRelsOfType.containsKey(rel.getType())) {
                   uniqueRelsOfType.put(rel.getType(), new ArrayList<>());
               }
               
               if(uniqueRelsOfType.get(rel.getType()).stream().noneMatch( (uniqueRel) -> {
                   return uniqueRel.equalsIgnoreGroup(rel);
               })) {
                   uniqueRelsOfType.get(rel.getType()).add(rel);
               }
            });
            
            
            Map<SCTConcept, Integer> targetDepth = new HashMap<>();
            
            ArrayList<AttributeRelationship> sortedUniqueRels = new ArrayList<>();
          
            uniqueRelsOfType.forEach( (type, rels) -> {
                Set<SCTConcept> uniqueTargets = new HashSet<>();
                
                rels.forEach( (rel) -> {
                    uniqueTargets.add(rel.getTarget());
                });
                
                Hierarchy<SCTConcept> ancestorHierarchy = getDataSource().getOntology().getConceptHierarchy().getAncestorHierarchy(uniqueTargets);
                
                Map<SCTConcept, Integer> longestPathDepths = ancestorHierarchy.getAllLongestPathDepths();
                
                uniqueTargets.forEach( (target) -> {
                    targetDepth.put(target, longestPathDepths.get(target));
                });
                
                sortedUniqueRels.addAll(rels);
            });
            
            
            sortedUniqueRels.sort( (a, b) -> {
                
                if(a.getType().equals(b.getType())) {
                    int aDepth = targetDepth.get(a.getTarget());
                    int bDepth = targetDepth.get(b.getTarget());
                    
                    if(aDepth == bDepth) {
                        return a.getTarget().getName().compareToIgnoreCase(b.getTarget().getName());
                    } else {
                        return aDepth - bDepth;
                    }
                    
                } else {
                    return a.getType().getName().compareToIgnoreCase(b.getType().getName());
                }
            });

            relGroupMap.put(0, sortedUniqueRels);
            
        } else {
            data.forEach((rel) -> {
                if (!relGroupMap.containsKey(rel.getGroup())) {
                    relGroupMap.put(rel.getGroup(), new ArrayList<>());
                }

                relGroupMap.get(rel.getGroup()).add(rel);
            });
        }

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
