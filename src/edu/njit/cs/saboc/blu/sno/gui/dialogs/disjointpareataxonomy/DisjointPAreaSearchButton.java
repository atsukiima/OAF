package edu.njit.cs.saboc.blu.sno.gui.dialogs.disjointpareataxonomy;

import edu.njit.cs.saboc.blu.core.gui.graphframe.buttons.search.GenericInternalSearchButton;
import javax.swing.JFrame;

/**
 *
 * @author Chris
 */
public class DisjointPAreaSearchButton extends GenericInternalSearchButton {
    
    public DisjointPAreaSearchButton(JFrame parent) {
        super(parent);
        
    }
    /*
        
        this.dap = dap;
        
        this.addSearchAction(new SearchAction("Concepts") {
            public ArrayList<SearchButtonResult> doSearch(String query) {
                
                ArrayList<SearchButtonResult> results = new ArrayList<SearchButtonResult>();
                
                if (query.length() >= 3) {
                    HashSet<DisjointPartialArea> pareas = dap.getDisjointPAreaTaxonomy().getDisjointPAreas();

                    ArrayList<SearchResult> resultConcepts = new ArrayList<SearchResult>();

                    for (DisjointPartialArea parea : pareas) {
                        ArrayList<Concept> concepts = parea.getConceptsAsList();

                        for (Concept c : concepts) {

                            if (c.getName().toLowerCase().contains(query.toLowerCase())) {
                                resultConcepts.add(new SearchResult(c.getName(), c.getName(), c.getId()));
                            }
                        }
                    }

                    Collections.sort(resultConcepts, new SearchResultComparator());
                    
                    for(SearchResult sr : resultConcepts) {
                        results.add(new SearchButtonResult(sr.toString(), sr));
                    }
                }

                return results;
            }
            
            public void resultSelected(SearchButtonResult o) {
                SearchResult result = ((SearchResult)o.getResult());
                
                // TODO: Focus on Disjoint partial-area
            }
        });
        
        this.addSearchAction(new SearchAction("Disjoint Partial-areas") {
            public ArrayList<SearchButtonResult> doSearch(String query) {
                
                HashSet<DisjointPartialArea> pareas = dap.getDisjointPAreaTaxonomy().getDisjointPAreas();

                ArrayList<DisjointPartialArea> resultDisjointPAreas = new ArrayList<DisjointPartialArea>();

                for (DisjointPartialArea parea : pareas) {

                    if (parea.getRoot().getName().toLowerCase().contains(query.toLowerCase())) {
                        resultDisjointPAreas.add(parea);
                    }
                }

                Collections.sort(resultDisjointPAreas, new Comparator<DisjointPartialArea>() {
                    public int compare(DisjointPartialArea a, DisjointPartialArea b) {
                        return a.getRoot().getName().compareToIgnoreCase(b.getRoot().getName());
                    }
                });
                
                ArrayList<SearchButtonResult> results = new ArrayList<SearchButtonResult>();
                
                for(DisjointPartialArea djpa : resultDisjointPAreas) {
                    results.add(new SearchButtonResult(String.format("%s (%d classes)", djpa.getRoot().getName(), djpa.getConceptCount()), djpa));
                }

                return results;
            }
            
            public void resultSelected(SearchButtonResult o) {
                DisjointPartialArea parea = ((DisjointPartialArea)o.getResult());
                
                // TODO: Focus on parea result
            }
        });
    }
    
    */
}
