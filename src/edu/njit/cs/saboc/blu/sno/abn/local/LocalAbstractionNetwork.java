
package edu.njit.cs.saboc.blu.sno.abn.local;

import SnomedShared.Concept;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Chris
 */
public interface LocalAbstractionNetwork {
    public HashMap<Long, Concept> getConcepts();
        
    public ArrayList<Concept> searchConcepts(String term);
    
    public int getHierarchyConceptCount();
}
