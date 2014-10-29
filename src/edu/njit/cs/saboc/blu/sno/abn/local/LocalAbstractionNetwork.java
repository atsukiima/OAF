
package edu.njit.cs.saboc.blu.sno.abn.local;

import SnomedShared.Concept;
import edu.njit.cs.saboc.blu.sno.datastructure.hierarchy.SCTConceptHierarchy;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Chris
 */
public interface LocalAbstractionNetwork {
    public HashMap<Long, Concept> getConcepts();
    
    public SCTConceptHierarchy getConceptHierarchy();
    
    public ArrayList<Concept> searchConcepts(String term);
    
    public int getHierarchyConceptCount();
}
