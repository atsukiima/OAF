package edu.njit.cs.saboc.blu.sno.utils.filterable.entry;

import SnomedShared.Concept;
import edu.njit.cs.saboc.blu.core.utils.filterable.list.Filterable;
import java.util.ArrayList;

/**
 *
 * @author Chris O
 */
public class FilterablePathEntry extends Filterable<ArrayList<Concept>> {
    
    private ArrayList<Concept> path;
    
    private String pathStr;
    
    public FilterablePathEntry(ArrayList<Concept> path) {
        this.path = path;
        
        String pathStr = path.get(0).getName();

        if (path.size() > 1) {
            pathStr += " ... ";
        }

        if (path.size() > 2) {
            pathStr += path.get(path.size() - 2).getName() + ", ";
        }

        pathStr += path.get(path.size() - 1).getName();
        pathStr += " (Path Length: " + path.size() +")";
        
        this.pathStr = pathStr;
    }
    
    public boolean containsFilter(String filter) {
        return true;
    }
    
    public ArrayList<Concept> getObject() {
        return path;
    }

    public Concept getNavigableConcept() {
        return null;
    }
    
    public String getInitialText() {
        return pathStr;
    }
    
    public String getFilterText(String filter) {
        return getInitialText();
    }
    
    public ArrayList<Concept> getPath() {
        return path;
    }
}
