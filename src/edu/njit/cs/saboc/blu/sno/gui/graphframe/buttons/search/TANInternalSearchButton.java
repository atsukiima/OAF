package edu.njit.cs.saboc.blu.sno.gui.graphframe.buttons.search;

import edu.njit.cs.saboc.blu.core.abn.ConceptNodeDetails;
import edu.njit.cs.saboc.blu.core.abn.node.Node;
import edu.njit.cs.saboc.blu.core.abn.tan.Band;
import edu.njit.cs.saboc.blu.core.abn.tan.Cluster;
import edu.njit.cs.saboc.blu.core.abn.tan.TribalAbstractionNetwork;
import edu.njit.cs.saboc.blu.core.gui.graphframe.buttons.search.BluGraphSearchAction;
import edu.njit.cs.saboc.blu.core.gui.graphframe.buttons.search.GenericInternalSearchButton;
import edu.njit.cs.saboc.blu.core.gui.graphframe.buttons.search.SearchButtonResult;
import edu.njit.cs.saboc.blu.sno.gui.graphframe.ClusterInternalGraphFrame;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;
import javax.swing.JFrame;

/**
 *
 * @author Chris
 */
public class TANInternalSearchButton extends GenericInternalSearchButton {

    public TANInternalSearchButton(JFrame parent, final ClusterInternalGraphFrame igf) {
        super(parent);
        
        this.addSearchAction(new BluGraphSearchAction<ConceptNodeDetails<Cluster>>("Concepts", igf) {
            public ArrayList<SearchButtonResult<ConceptNodeDetails<Cluster>>> doSearch(String query) {
                
                ArrayList<SearchButtonResult<ConceptNodeDetails<Cluster>>> results = new ArrayList<>();
                
                if (query.length() >= 3) {
                    TribalAbstractionNetwork tan = (TribalAbstractionNetwork) getGraphFrame().getGraph().getAbstractionNetwork();
                    
                    Set<ConceptNodeDetails<Cluster>> queryResult = tan.searchConcepts(query);
                    
                    queryResult.forEach( (result) -> {
                        results.add(new SearchButtonResult<>(result.getConcept().getName(), result));
                    });
                    
                    results.sort((a,b) -> {
                        return a.getResult().getConcept().getName().compareTo(b.getResult().getConcept().getName());
                    });
                }

                return results;
            }
            
            public void resultSelected(SearchButtonResult<ConceptNodeDetails<Cluster>> o) {
                ConceptNodeDetails<Cluster> result = o.getResult();

                getGraphFrame().getEnhancedGraphExplorationPanel().highlightEntriesForSearch(new ArrayList<>(result.getNodes()));
                getGraphFrame().focusOnComponent(getGraphFrame().getGraph().getNodeEntries().get(result.getNodes().iterator().next()));
            }
        });
        
        this.addSearchAction(new BluGraphSearchAction<Cluster>("Clusters", igf) {
            public ArrayList<SearchButtonResult<Cluster>> doSearch(String query) {
                
                ArrayList<SearchButtonResult<Cluster>> results = new ArrayList<>();
                
                TribalAbstractionNetwork tan = (TribalAbstractionNetwork) getGraphFrame().getGraph().getAbstractionNetwork();
                
                Set<Node> clusters = tan.searchNodes(query);
                
                clusters.forEach((node) -> {
                    Cluster cluster = (Cluster)node;
                    
                    results.add(new SearchButtonResult<>(String.format("%s (%d concepts)", cluster.getName(), cluster.getConceptCount()), cluster));
                });
                
                results.sort( (a, b) -> {
                    return a.getResult().getName().compareToIgnoreCase(b.getResult().getName());
                });
               
                return results;
            }
            
            public void resultSelected(SearchButtonResult<Cluster> o) {
                Cluster result = o.getResult();

                getGraphFrame().focusOnComponent(getGraphFrame().getGraph().getNodeEntries().get(result));
                
                getGraphFrame().getEnhancedGraphExplorationPanel().highlightEntriesForSearch(new ArrayList<>(Arrays.asList(result)));
            }
        });
        
        this.addSearchAction(new BluGraphSearchAction<Band>("Bands", igf) {
            public ArrayList<SearchButtonResult<Band>> doSearch(String query) {
                
                ArrayList<SearchButtonResult<Band>> results = new ArrayList<>();
                
                TribalAbstractionNetwork tan = (TribalAbstractionNetwork) getGraphFrame().getGraph().getAbstractionNetwork();
                
                Set<Band> queryResult = tan.getBandTAN().searchBands(query);

                queryResult.stream().forEach((band) -> {
                    results.add(new SearchButtonResult<>(band.getName(), band));
                });
                
                results.sort((a,b) -> {
                    return a.getResult().getName().compareToIgnoreCase(b.getResult().getName());
                });

                return results;
            }
            
            public void resultSelected(SearchButtonResult<Band> o) {
                Band band = o.getResult();

                getGraphFrame().focusOnComponent(graph.getContainerEntries().get(band));
                
                // TODO: Highlight area for search
            }
        });
    }
}
