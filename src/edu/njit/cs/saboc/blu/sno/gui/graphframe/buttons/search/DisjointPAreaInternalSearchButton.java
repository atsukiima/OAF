package edu.njit.cs.saboc.blu.sno.gui.graphframe.buttons.search;

import edu.njit.cs.saboc.blu.core.abn.ConceptNodeDetails;
import edu.njit.cs.saboc.blu.core.abn.disjoint.DisjointAbstractionNetwork;
import edu.njit.cs.saboc.blu.core.abn.disjoint.DisjointNode;
import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.DisjointPArea;
import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.PArea;
import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.PAreaTaxonomy;
import edu.njit.cs.saboc.blu.core.graph.disjointabn.DisjointBluGraph;
import edu.njit.cs.saboc.blu.core.gui.graphframe.buttons.search.BluGraphSearchAction;
import edu.njit.cs.saboc.blu.core.gui.graphframe.buttons.search.GenericInternalSearchButton;
import edu.njit.cs.saboc.blu.core.gui.graphframe.buttons.search.SearchButtonResult;
import edu.njit.cs.saboc.blu.sno.gui.graphframe.DisjointPAreaInternalGraphFrame;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;
import javax.swing.JFrame;

/**
 *
 * @author Chris O
 */
public class DisjointPAreaInternalSearchButton extends GenericInternalSearchButton {


    public DisjointPAreaInternalSearchButton(JFrame parent, final DisjointPAreaInternalGraphFrame igf) {
        super(parent);
        
        
        this.addSearchAction(new BluGraphSearchAction<ConceptNodeDetails<DisjointPArea>>("Concepts", igf) {
            public ArrayList<SearchButtonResult<ConceptNodeDetails<DisjointPArea>>> doSearch(String query) {
                
                query = query.toLowerCase();
                
                ArrayList<SearchButtonResult<ConceptNodeDetails<DisjointPArea>>> results = new ArrayList<>();
                
                if (query.length() >= 3) {
                    DisjointAbstractionNetwork<PAreaTaxonomy, PArea> disjointTaxonomy = (DisjointAbstractionNetwork<PAreaTaxonomy, PArea>)((DisjointBluGraph) graph).getAbstractionNetwork();

                    Set<ConceptNodeDetails<DisjointNode<PArea>>> queryResult = disjointTaxonomy.searchConcepts(query);
                    
                    queryResult.forEach((result) -> {
                        ConceptNodeDetails<DisjointPArea> converted = (ConceptNodeDetails<DisjointPArea>)(ConceptNodeDetails<?>)result;

                        results.add(new SearchButtonResult<>(converted.getConcept().getName(), converted));
                    });
      
                    results.sort( (a,b) -> {
                        return a.getResult().getConcept().getName().compareToIgnoreCase(b.getResult().getConcept().getName());
                    });
                    
                }
                
                return results;
            }
            
            public void resultSelected(SearchButtonResult<ConceptNodeDetails<DisjointPArea>> o) {
                ConceptNodeDetails<DisjointPArea> result = o.getResult();

                getGraphFrame().focusOnComponent(getGraphFrame().getGraph().getNodeEntries().get(result.getNodes().iterator().next()));
                
                getGraphFrame().getEnhancedGraphExplorationPanel().highlightEntriesForSearch(new ArrayList<>(result.getNodes()));
            }
        });
        
        this.addSearchAction(new BluGraphSearchAction<DisjointPArea>("Disjoint Partial-areas", igf) {
            public ArrayList<SearchButtonResult<DisjointPArea>> doSearch(String query) {
                
                ArrayList<SearchButtonResult<DisjointPArea>> results = new ArrayList<>();
                
                DisjointAbstractionNetwork<PAreaTaxonomy, PArea> disjointTaxonomy = (DisjointAbstractionNetwork<PAreaTaxonomy, PArea>)((DisjointBluGraph) graph).getAbstractionNetwork();
                
                Set<DisjointPArea> queryResult = (Set<DisjointPArea>)(Set<?>)disjointTaxonomy.searchNodes(query);

                for(DisjointPArea parea : queryResult) {
                    results.add(new SearchButtonResult(String.format("%s (%d concepts)", parea.getRoot().getName(), parea.getConceptCount()), parea));
                }
                
                results.sort( (a,b) -> {
                    return a.getResult().getName().compareToIgnoreCase(b.getResult().getName());
                });
                
                return results;
            }
            
            public void resultSelected(SearchButtonResult<DisjointPArea> o) {
                DisjointPArea parea = o.getResult();
                
                getGraphFrame().getEnhancedGraphExplorationPanel().highlightEntriesForSearch(new ArrayList<>(Arrays.asList(parea)));
                getGraphFrame().focusOnComponent(getGraphFrame().getGraph().getNodeEntries().get(parea));
            }
        });
    }
   
}