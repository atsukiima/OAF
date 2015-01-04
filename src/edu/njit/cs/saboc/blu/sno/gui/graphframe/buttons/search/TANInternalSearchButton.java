package edu.njit.cs.saboc.blu.sno.gui.graphframe.buttons.search;

import SnomedShared.SearchResult;
import SnomedShared.generic.GenericConceptGroup;
import SnomedShared.overlapping.ClusterSummary;
import SnomedShared.overlapping.CommonOverlapSet;
import edu.njit.cs.saboc.blu.core.gui.graphframe.buttons.search.BluGraphSearchAction;
import edu.njit.cs.saboc.blu.core.gui.graphframe.buttons.search.GenericInternalSearchButton;
import edu.njit.cs.saboc.blu.core.gui.graphframe.buttons.search.SearchButtonResult;
import edu.njit.cs.saboc.blu.sno.abn.tan.TribalAbstractionNetwork;
import edu.njit.cs.saboc.blu.sno.abn.tan.local.ConceptClusterInfo;
import edu.njit.cs.saboc.blu.sno.gui.graphframe.ClusterInternalGraphFrame;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import javax.swing.JFrame;

/**
 *
 * @author Chris
 */
public class TANInternalSearchButton extends GenericInternalSearchButton {


    public TANInternalSearchButton(JFrame parent, final ClusterInternalGraphFrame igf) {
        super(parent);
        
        this.addSearchAction(new BluGraphSearchAction("Concepts", igf) {
            public ArrayList<SearchButtonResult> doSearch(String query) {
                
                ArrayList<SearchButtonResult> results = new ArrayList<SearchButtonResult>();
                
                if (query.length() >= 3) {
                    ArrayList<ClusterSummary> pareas = new ArrayList<ClusterSummary>();
                    
                    TribalAbstractionNetwork tan = (TribalAbstractionNetwork) graphFrame.getGraph().getAbstractionNetwork();

                    for (CommonOverlapSet band : tan.getBands()) {
                        pareas.addAll(band.getAllClusters());
                    }
                    
                    ArrayList<SearchResult> conceptResults = tan.getDataSource().searchForConceptsWithinTAN(tan, pareas, query.toLowerCase());
                    
                    for(SearchResult sr : conceptResults) {
                        results.add(new SearchButtonResult(sr.toString(), sr));
                    }
                }

                return results;
            }
            
            public void resultSelected(SearchButtonResult o) {
                SearchResult result = (SearchResult) o.getResult();

                TribalAbstractionNetwork tan = (TribalAbstractionNetwork) graphFrame.getGraph().getAbstractionNetwork();

                ArrayList<ConceptClusterInfo> clusterInfo = tan.getDataSource().getConceptClusterInfo(tan,
                        tan.getDataSource().getConceptFromId(result.getConceptId()));

                ArrayList<GenericConceptGroup> conceptGroups = new ArrayList<GenericConceptGroup>();
                
                for(ConceptClusterInfo cluster : clusterInfo) {
                    conceptGroups.add(graphFrame.getGraph().getAbstractionNetwork().getGroupFromRootConceptId(cluster.getClusterRootId()));
                }
                
                graphFrame.getEnhancedGraphExplorationPanel().highlightEntriesForSearch(conceptGroups);
                
                graphFrame.focusOnComponent(graphFrame.getGraph().getGroupEntries().get(conceptGroups.get(0).getId()));
            }
        });
        
        this.addSearchAction(new BluGraphSearchAction("Clusters", igf) {
            public ArrayList<SearchButtonResult> doSearch(String query) {
                
                ArrayList<SearchButtonResult> results = new ArrayList<SearchButtonResult>();
                
                List<GenericConceptGroup> clusters = graphFrame.getGraph().getAbstractionNetwork().searchAnywhereInGroupRoots(query.toLowerCase());
                
                for(GenericConceptGroup cluster : clusters) {
                    results.add(new SearchButtonResult(String.format("%s (%d concepts)", cluster.getRoot().getName(), cluster.getConceptCount()), cluster));
                }
               
                return results;
            }
            
            public void resultSelected(SearchButtonResult o) {
                ClusterSummary result = ((ClusterSummary)o.getResult());

                graphFrame.focusOnComponent(graphFrame.getGraph().getGroupEntries().get(result.getId()));
                
                graphFrame.getEnhancedGraphExplorationPanel().highlightEntriesForSearch(new ArrayList<GenericConceptGroup>(Arrays.asList(result)));
            }
        });
        
        this.addSearchAction(new BluGraphSearchAction("Bands", igf) {
            public ArrayList<SearchButtonResult> doSearch(String query) {
                
                ArrayList<SearchButtonResult> results = new ArrayList<SearchButtonResult>();
                
                TribalAbstractionNetwork tan = (TribalAbstractionNetwork) graphFrame.getGraph().getAbstractionNetwork();
                
                ArrayList<CommonOverlapSet> bands = tan.searchBands(query.toLowerCase());

                HashMap<Long, String> patriarchs = tan.getPatriarchNames();

                for (CommonOverlapSet band : bands) {
                    boolean first = true;

                    String name = "";

                    for (long patriarchId : band.getSetEntryPoints()) {   // Otherwise derive the title from its relationships.
                        if (!first) {
                            name += ", ";
                        } else {
                            first = false;
                        }

                        name += patriarchs.get(patriarchId);
                    }
                    
                    results.add(new SearchButtonResult(name, band));
                }

                return results;
            }
            
            public void resultSelected(SearchButtonResult o) {
                CommonOverlapSet band = ((CommonOverlapSet)o.getResult());

                graphFrame.focusOnComponent(graph.getContainerEntries().get(band.getId()));
                
                // TODO: Highlight area for search
            }
        });
    }
}
