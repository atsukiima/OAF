package edu.njit.cs.saboc.blu.sno.gui.graphframe.buttons.search;

import SnomedShared.Concept;
import SnomedShared.generic.GenericConceptGroup;
import edu.njit.cs.saboc.blu.core.gui.graphframe.buttons.search.BluGraphSearchAction;
import edu.njit.cs.saboc.blu.core.gui.graphframe.buttons.search.GenericInternalSearchButton;
import edu.njit.cs.saboc.blu.core.gui.graphframe.buttons.search.SearchButtonResult;
import edu.njit.cs.saboc.blu.sno.abn.disjointpareataxonomy.DisjointPAreaTaxonomy;
import edu.njit.cs.saboc.blu.sno.abn.disjointpareataxonomy.DisjointPartialArea;
import edu.njit.cs.saboc.blu.sno.graph.DisjointPAreaBluGraph;
import edu.njit.cs.saboc.blu.sno.gui.graphframe.DisjointPAreaInternalGraphFrame;
import edu.njit.cs.saboc.blu.sno.utils.comparators.ConceptNameComparator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.JFrame;

/**
 *
 * @author Chris O
 */
public class DisjointPAreaInternalSearchButton extends GenericInternalSearchButton {


    public DisjointPAreaInternalSearchButton(JFrame parent, final DisjointPAreaInternalGraphFrame igf) {
        super(parent);
        
        
        this.addSearchAction(new BluGraphSearchAction("Concepts", igf) {
            public ArrayList<SearchButtonResult> doSearch(String query) {
                
                query = query.toLowerCase();
                
                ArrayList<SearchButtonResult> results = new ArrayList<>();
                
                if (!query.isEmpty()) {
                    DisjointPAreaTaxonomy disjointTaxonomy = ((DisjointPAreaBluGraph) graph).getDisjointPAreaTaxonomy();

                    HashSet<DisjointPartialArea> disjointPAreas = disjointTaxonomy.getDisjointGroups();

                    ArrayList<Concept> resultConcepts = new ArrayList<>();

                    for (DisjointPartialArea disjointPArea : disjointPAreas) {
                        Set<Concept> pareaConcepts = disjointPArea.getConceptHierarchy().getConceptsInHierarchy();

                        for (Concept c : pareaConcepts) {
                            String name = c.getName();
                            String lowerCaseName = name.toLowerCase();

                            if (lowerCaseName.contains(query)
                                    || Long.toString(c.getId()).contains(query)) {
                                
                                resultConcepts.add(c);
                            }
                        }
                    }

                    Collections.sort(resultConcepts, new ConceptNameComparator());
                    
                    for(Concept resultConcept : resultConcepts) {
                        results.add(new SearchButtonResult(String.format("%s (%d)", 
                                    resultConcept.getName(), 
                                    resultConcept.getId()), 
                                resultConcept));
                    }
                }
                
                return results;
            }
            
            public void resultSelected(SearchButtonResult o) {
                Concept cls = ((Concept) o.getResult());

                DisjointPAreaTaxonomy disjointTaxonomy = ((DisjointPAreaBluGraph) graph).getDisjointPAreaTaxonomy();

                HashSet<DisjointPartialArea> disjointPAreas = disjointTaxonomy.getDisjointGroups();
                
                
                HashSet<DisjointPartialArea> result = new HashSet<>();
                
                for(DisjointPartialArea disjointPArea : disjointPAreas) {
                    if(disjointPArea.getConceptHierarchy().getConceptsInHierarchy().contains(cls)) {
                        result.add(disjointPArea);
                        break;
                    }
                }
                
                graphFrame.focusOnComponent(graphFrame.getGraph().getNodeEntries().get(result.iterator().next().getId()));
                
                graphFrame.getEnhancedGraphExplorationPanel().highlightEntriesForSearch(new ArrayList<>(result));
            }
        });
        
        this.addSearchAction(new BluGraphSearchAction("Disjoint Partial-areas", igf) {
            public ArrayList<SearchButtonResult> doSearch(String query) {
                
                ArrayList<SearchButtonResult> results = new ArrayList<>();
                
                List<GenericConceptGroup> groups = graphFrame.getGraph().getAbstractionNetwork().searchAnywhereInGroupRoots(query);
                
                for(GenericConceptGroup parea : groups) {
                    results.add(new SearchButtonResult(String.format("%s (%d concepts)", parea.getRoot().getName(), parea.getConceptCount()), parea));
                }
                
                return results;
            }
            
            public void resultSelected(SearchButtonResult o) {
                DisjointPartialArea parea = (DisjointPartialArea)o.getResult();
                
                graphFrame.getEnhancedGraphExplorationPanel().highlightEntriesForSearch(new ArrayList<>(Arrays.asList(parea)));
                graphFrame.focusOnComponent(graphFrame.getGraph().getNodeEntries().get(parea.getId()));
            }
        });
    }
   
}