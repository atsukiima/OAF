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
import edu.njit.cs.saboc.nat.generic.DataSourceChangeListener;
import edu.njit.cs.saboc.nat.generic.NATBrowserPanel;
import edu.njit.cs.saboc.nat.generic.data.ConceptBrowserDataSource;
import edu.njit.cs.saboc.nat.generic.gui.filterable.nestedlist.FilterableNestedEntry;
import edu.njit.cs.saboc.nat.generic.gui.filterable.nestedlist.FilterableNestedEntryPanel;
import edu.njit.cs.saboc.nat.generic.gui.filterable.nestedlist.NestedFilterableList;
import edu.njit.cs.saboc.nat.generic.gui.filterable.nestedlist.NestedFilterableList.EntrySelectionListener;
import edu.njit.cs.saboc.nat.generic.gui.panels.ResultPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JPanel;

/**
 * A panel for displaying a concept's inferred (or stated) attribute relationships,
 * with the option of showing the unique set of attribute relationships
 * 
 * @author Chris O
 */
public class AttributeRelationshipPanel extends ResultPanel<SCTConcept, ArrayList<AttributeRelationship>> {
    
    private final NestedFilterableList<RelationshipGroup, AttributeRelationship> attributeRelationshipList;
    
    private final JCheckBox chkHideRelationshipGroups;
    
    private final JCheckBox chkDisplayOnlyStatedRels;
    
    public AttributeRelationshipPanel(NATBrowserPanel<SCTConcept> mainPanel) {

        super(mainPanel, 
                SCTNATDataRetrievers.getAttributeRelationshipRetriever(mainPanel));
        
        this.setBorder(BorderFactory.createTitledBorder(
                    BorderFactory.createLineBorder(Color.BLACK), 
                    "Attribute Relationships")
            );
        
        this.attributeRelationshipList = new NestedFilterableList<RelationshipGroup, AttributeRelationship>() {

            @Override
            public FilterableNestedEntryPanel<FilterableNestedEntry<RelationshipGroup, AttributeRelationship>> getEntry(
                    FilterableNestedEntry<RelationshipGroup, AttributeRelationship> entry, Optional<String> filter) {
                
                if(filter.isPresent()) {
                    entry.setCurrentFilter(filter.get());
                }
                
                FilterableRelationshipGroupEntry groupEntry = (FilterableRelationshipGroupEntry)entry;
                
                RelationshipGroupPanel relGroupPanel = new RelationshipGroupPanel(mainPanel, groupEntry);
                
                return  (FilterableNestedEntryPanel<FilterableNestedEntry<RelationshipGroup, AttributeRelationship>>)
                        (FilterableNestedEntryPanel<?>)relGroupPanel;
            }
        };
        
        this.attributeRelationshipList.addEntrySelectionListener(new EntrySelectionListener<AttributeRelationship>() {

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
        
        this.attributeRelationshipList.setRightClickMenuGenerator(new AttributeRelationshipRightClickMenu(mainPanel));
        
        this.chkHideRelationshipGroups = new JCheckBox("Hide Relationship Groups");
        this.chkHideRelationshipGroups.addActionListener( (ae) -> {
            super.forceReload();
        });
        
        this.chkDisplayOnlyStatedRels = new JCheckBox("Show Only Stated Attribute Relationships");
        this.chkDisplayOnlyStatedRels.addActionListener( (ae) -> {
            
            if(chkDisplayOnlyStatedRels.isSelected()) {
                super.setDataRetriever(SCTNATDataRetrievers.getStatedAttributRelationshipsRetriever(mainPanel));
            } else {
                super.setDataRetriever(SCTNATDataRetrievers.getAttributeRelationshipRetriever(mainPanel));
            }
            
        });
        
        JPanel optionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        
        optionPanel.add(chkHideRelationshipGroups);
        optionPanel.add(chkDisplayOnlyStatedRels);
        
        this.chkDisplayOnlyStatedRels.setEnabled(false);
        
        mainPanel.addDataSourceChangeListener(new DataSourceChangeListener<SCTConcept>() {
            
            @Override
            public void dataSourceLoaded(ConceptBrowserDataSource<SCTConcept> dataSource) {
                SCTConceptBrowserDataSource sctDataSource = (SCTConceptBrowserDataSource)dataSource;
                chkDisplayOnlyStatedRels.setEnabled(sctDataSource.getRelease().supportsStatedRelationships());
            }

            @Override
            public void dataSourceRemoved() {
                
            }
        });
        
        this.attributeRelationshipList.addOptionPanelComponent(optionPanel);
        
        this.setLayout(new BorderLayout());
        
        this.add(attributeRelationshipList, BorderLayout.CENTER);
    }

    @Override
    public void dataPending() {
        attributeRelationshipList.clearContents();
    }

    @Override
    public void displayResults(ArrayList<AttributeRelationship> data) {
        
        if(!getMainPanel().getDataSource().isPresent()) {
            return;
        }
        
        ArrayList<RelationshipGroup> relGroups = new ArrayList<>();
        
        Map<Integer, ArrayList<AttributeRelationship>> relGroupMap = new HashMap<>();
        
        if(this.chkHideRelationshipGroups.isSelected()) {
            
            // If relationship groups are hidden then attribute relationships are
            // sorted by their type and then subsorted based on the longest path 
            // depth of their target concepts, with the most specific concepts
            // displayed last (i.e., deepest in the hierarchy displayed last).
            
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
                
                Hierarchy<SCTConcept> ancestorHierarchy = 
                        getMainPanel().getDataSource().get().getOntology().
                                getConceptHierarchy().getAncestorHierarchy(uniqueTargets);
                
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
                relEntries.add(new FilterableAttributeRelationshipEntry(
                        getMainPanel(), 
                        rel));
            });
            
            relGroupEntries.add(new FilterableRelationshipGroupEntry(relGroup, relEntries));
        });
        
        attributeRelationshipList.displayContents(relGroupEntries);
        
        this.setBorder(BorderFactory.createTitledBorder(
                    BorderFactory.createLineBorder(Color.BLACK), 
                    String.format("Attribute Relationships (%d)", data.size()))
            );
    }

    @Override
    public void setEnabled(boolean value) {
        super.setEnabled(value);
        
        this.attributeRelationshipList.setEnabled(value);
        
        this.chkHideRelationshipGroups.setEnabled(value);
        
        boolean enableStatedRels = false;
        
        if(value) {
            if(getMainPanel().getDataSource().isPresent()) {
                SCTConceptBrowserDataSource sctDataSource = (SCTConceptBrowserDataSource)getMainPanel().getDataSource().get();
                enableStatedRels = sctDataSource.getRelease().supportsStatedRelationships();
            }
        }
        
        this.chkDisplayOnlyStatedRels.setEnabled(enableStatedRels);
    }

    @Override
    public void reset() {
        this.attributeRelationshipList.clearContents();
    }
}
