package edu.njit.cs.saboc.blu.sno.gui.graphframe.buttons.search;

import edu.njit.cs.saboc.blu.core.abn.ConceptNodeDetails;
import edu.njit.cs.saboc.blu.core.abn.node.Node;
import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.Area;
import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.PArea;
import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.PAreaTaxonomy;
import edu.njit.cs.saboc.blu.core.gui.graphframe.buttons.search.BluGraphSearchAction;
import edu.njit.cs.saboc.blu.core.gui.graphframe.buttons.search.GenericInternalSearchButton;
import edu.njit.cs.saboc.blu.core.gui.graphframe.buttons.search.SearchButtonResult;
import edu.njit.cs.saboc.blu.sno.gui.graphframe.PAreaInternalGraphFrame;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;
import javax.swing.JFrame;

/**
 *
 * @author Chris
 */
public class PAreaInternalSearchButton extends GenericInternalSearchButton {

    public PAreaInternalSearchButton(JFrame parent, final PAreaInternalGraphFrame igf) {
        super(parent);
        
        this.addSearchAction(new BluGraphSearchAction<ConceptNodeDetails<PArea>>("Concepts", igf) {
            public ArrayList<SearchButtonResult<ConceptNodeDetails<PArea>>> doSearch(String query) {
                
                ArrayList<SearchButtonResult<ConceptNodeDetails<PArea>>> results = new ArrayList<>();
                
                if (query.length() >= 3) {
                    PAreaTaxonomy taxonomy = (PAreaTaxonomy)getGraphFrame().getGraph().getAbstractionNetwork();

                    Set<ConceptNodeDetails<PArea>> searchResults = taxonomy.searchConcepts(query);

                    searchResults.stream().forEach((sr) -> {
                        results.add(new SearchButtonResult(sr.getConcept().getName(), sr));
                    });
                    
                    results.sort( (a,b) -> {
                        return a.getResult().getConcept().getName().compareToIgnoreCase(b.getResult().getConcept().getName());
                    });
                }
                
                return results;
            }
            
            public void resultSelected(SearchButtonResult<ConceptNodeDetails<PArea>> o) {
                ConceptNodeDetails<PArea> result = o.getResult();

                getGraphFrame().focusOnComponent(getGraphFrame().getGraph().getNodeEntries().get(result.getNodes().iterator().next()));
                
                getGraphFrame().getEnhancedGraphExplorationPanel().highlightEntriesForSearch(new ArrayList<>(result.getNodes()));
            }
        });
        
        this.addSearchAction(new BluGraphSearchAction<PArea>("Partial-areas", igf) {
            public ArrayList<SearchButtonResult<PArea>> doSearch(String query) {
                
                PAreaTaxonomy taxonomy = (PAreaTaxonomy)getGraphFrame().getGraph().getAbstractionNetwork();
                
                ArrayList<SearchButtonResult<PArea>> results = new ArrayList<>();

                Set<Node> pareas = taxonomy.searchNodes(query);
                
                pareas.forEach((node) -> {
                    PArea parea = (PArea)node;
                    
                    results.add(new SearchButtonResult<>(String.format("%s (%d concepts)", parea.getName(), parea.getConceptCount()), parea));
                });
                
                results.sort( (a, b) -> {
                    return a.getResult().getName().compareToIgnoreCase(b.getResult().getName());
                });

                return results;
            }
            
            public void resultSelected(SearchButtonResult<PArea> o) {
                PArea result = o.getResult();

                getGraphFrame().focusOnComponent(getGraphFrame().getGraph().getNodeEntries().get(result));
                
                getGraphFrame().getEnhancedGraphExplorationPanel().highlightEntriesForSearch(new ArrayList<>(Arrays.asList(result)));
            }
        });
        
        this.addSearchAction(new BluGraphSearchAction<Area>("Areas", igf) {
            public ArrayList<SearchButtonResult<Area>> doSearch(String query) {
                
                PAreaTaxonomy taxonomy = (PAreaTaxonomy)getGraphFrame().getGraph().getAbstractionNetwork();
                
                Set<Area> areas = taxonomy.getAreaTaxonomy().findAreas(query);
                
                ArrayList<SearchButtonResult<Area>> results = new ArrayList<>();

                for (Area area : areas) {
                    results.add(new SearchButtonResult<>(area.getName(), area));
                }
                
                results.sort( (a, b) -> {
                    return a.getResult().getName().compareTo(b.getResult().getName());
                });

                return results;
            }
            
            public void resultSelected(SearchButtonResult<Area> o) {
                Area area = o.getResult();

                getGraphFrame().focusOnComponent(graph.getContainerEntries().get(area));
            }
        });
        
    }
}
