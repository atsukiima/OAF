package edu.njit.cs.saboc.blu.sno.gui.graphframe.buttons.search;

import SnomedShared.SearchResult;
import SnomedShared.generic.GenericConceptGroup;
import SnomedShared.pareataxonomy.Area;
import SnomedShared.pareataxonomy.ConceptPAreaInfo;
import SnomedShared.pareataxonomy.InheritedRelationship;
import SnomedShared.pareataxonomy.PAreaSummary;
import edu.njit.cs.saboc.blu.core.gui.graphframe.buttons.search.BluGraphSearchAction;
import edu.njit.cs.saboc.blu.core.gui.graphframe.buttons.search.GenericInternalSearchButton;
import edu.njit.cs.saboc.blu.core.gui.graphframe.buttons.search.SearchButtonResult;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTArea;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTPArea;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTPAreaTaxonomy;
import edu.njit.cs.saboc.blu.sno.gui.graphframe.PAreaInternalGraphFrame;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import javax.swing.JFrame;

/**
 *
 * @author Chris
 */
public class PAreaInternalSearchButton extends GenericInternalSearchButton {

    public PAreaInternalSearchButton(JFrame parent, final PAreaInternalGraphFrame igf) {
        super(parent);
        
        this.addSearchAction(new BluGraphSearchAction("Concepts", igf) {
            public ArrayList<SearchButtonResult> doSearch(String query) {
                
                ArrayList<SearchButtonResult> results = new ArrayList<SearchButtonResult>();
                
                if (query.length() >= 3) {
                    
                    SCTPAreaTaxonomy taxonomy = (SCTPAreaTaxonomy)graphFrame.getGraph().getAbstractionNetwork();

                    ArrayList<SCTPArea> pareas = new ArrayList<SCTPArea>();

                    for (SCTArea a : taxonomy.getHierarchyAreas()) {
                        pareas.addAll(a.getAllPAreas());
                    }
                    
                    ArrayList<SearchResult> searchResults = taxonomy.getDataSource().searchForConceptsWithinTaxonomy(taxonomy, pareas, query.toLowerCase());

                    for(SearchResult sr : searchResults) {
                        results.add(new SearchButtonResult(sr.toString(), sr));
                    }
                }
                
                return results;
            }
            
            public void resultSelected(SearchButtonResult o) {
                SearchResult result = (SearchResult)o.getResult();
                
                SCTPAreaTaxonomy taxonomy = (SCTPAreaTaxonomy)graphFrame.getGraph().getAbstractionNetwork();

                ArrayList<ConceptPAreaInfo> pareaInfo = taxonomy.getDataSource().getConceptPAreaInfo(taxonomy,
                        taxonomy.getDataSource().getConceptFromId(result.getConceptId()));
                
                ArrayList<GenericConceptGroup> conceptGroups = new ArrayList<GenericConceptGroup>();
                
                for(ConceptPAreaInfo parea : pareaInfo) {
                    conceptGroups.add(graphFrame.getGraph().getAbstractionNetwork().getGroupFromRootConceptId(parea.getPAreaRootId()));
                }
                
                graphFrame.focusOnComponent(graphFrame.getGraph().getNodeEntries().get(conceptGroups.get(0).getId()));
                
                graphFrame.getEnhancedGraphExplorationPanel().highlightEntriesForSearch(conceptGroups);
            }
        });
        
        this.addSearchAction(new BluGraphSearchAction("Partial-areas", igf) {
            public ArrayList<SearchButtonResult> doSearch(String query) {
                
                ArrayList<SearchButtonResult> results = new ArrayList<SearchButtonResult>();

                List<GenericConceptGroup> pareas = graphFrame.getGraph().getAbstractionNetwork().searchAnywhereInGroupRoots(query.toLowerCase());
                
                for(GenericConceptGroup parea : pareas) {
                    results.add(new SearchButtonResult(String.format("%s (%d concepts)", parea.getRoot().getName(), parea.getConceptCount()), parea));
                }

                return results;
            }
            
            public void resultSelected(SearchButtonResult o) {
                SCTPArea result = (SCTPArea)o.getResult();

                graphFrame.focusOnComponent(graphFrame.getGraph().getNodeEntries().get(result.getId()));
                
                graphFrame.getEnhancedGraphExplorationPanel().highlightEntriesForSearch(new ArrayList<GenericConceptGroup>(Arrays.asList(result)));
            }
        });
        
        this.addSearchAction(new BluGraphSearchAction("Areas", igf) {
            public ArrayList<SearchButtonResult> doSearch(String query) {
                
                SCTPAreaTaxonomy taxonomy = (SCTPAreaTaxonomy)graphFrame.getGraph().getAbstractionNetwork();
                
                ArrayList<SCTArea> areas = taxonomy.searchAreas(query.toLowerCase());
                
                ArrayList<SearchButtonResult> results = new ArrayList<SearchButtonResult>();

                HashMap<Long, String> lateralRels = taxonomy.getLateralRelsInHierarchy();

                for (SCTArea a : areas) {
                    boolean first = true;

                    String name = "";

                    for (InheritedRelationship rel : a.getRelationships()) {   // Otherwise derive the title from its relationships.
                        if (!first) {
                            name += ", ";
                        } else {
                            first = false;
                        }

                        name += lateralRels.get(rel.getRelationshipTypeId());
                    }

                    results.add(new SearchButtonResult(name, a));
                }

                return results;
            }
            
            public void resultSelected(SearchButtonResult o) {
                SCTArea a = ((SCTArea) o.getResult());

                graphFrame.focusOnComponent(graph.getContainerEntries().get(a.getId()));
                
                // TODO: Highlight area for search
            }
        });
        
    }
}
