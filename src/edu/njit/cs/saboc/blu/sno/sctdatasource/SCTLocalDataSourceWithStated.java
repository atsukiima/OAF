package edu.njit.cs.saboc.blu.sno.sctdatasource;

import SnomedShared.Concept;
import edu.njit.cs.saboc.blu.sno.datastructure.hierarchy.SCTConceptHierarchy;
import edu.njit.cs.saboc.blu.sno.localdatasource.concept.LocalSnomedConcept;
import edu.njit.cs.saboc.blu.sno.utils.comparators.ConceptNameComparator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;

/**
 *
 * @author Chris
 */
public class SCTLocalDataSourceWithStated extends SCTLocalDataSource {
    
    private SCTConceptHierarchy statedHierarchy;
    
    public SCTLocalDataSourceWithStated(Map<Long, LocalSnomedConcept> concepts,
           SCTConceptHierarchy conceptHierarchy, boolean processDescriptions, String version,
           SCTConceptHierarchy statedHierarchy) {
        
        super(concepts, conceptHierarchy, processDescriptions, version);
        
        this.statedHierarchy = statedHierarchy;
    }
    
    public ArrayList<Concept> getStatedParents(Concept c) {
        ArrayList<Concept> statedParents = new ArrayList<Concept>(statedHierarchy.getParents(c));
       
        Collections.sort(statedParents, new ConceptNameComparator());
        
        return statedParents;
    }
    
    public ArrayList<Concept> getStatedChildren(Concept c) {
        ArrayList<Concept> statedChildren = new ArrayList<Concept>(statedHierarchy.getChildren(c));
       
        Collections.sort(statedChildren, new ConceptNameComparator());
        
        return statedChildren;
    }
    
    public ArrayList<Concept> getStatedSiblings(Concept c) {
        ArrayList<Concept> parents = this.getStatedParents(c);
        
        HashSet<Concept> siblings = new HashSet<Concept>();
        
        for(Concept parent : parents) {
            siblings.addAll(this.getStatedChildren(parent));
        }
        
        siblings.remove(c);
        
        ArrayList<Concept> statedSiblings = new ArrayList<Concept>(siblings);
        
        Collections.sort(statedSiblings, new ConceptNameComparator());
        
        return statedSiblings;
    }
    
    public ArrayList<Concept> getStatedAncestors(Concept c) {
        return createAncestorList(statedHierarchy, c);
    }
    
    public SCTConceptHierarchy getStatedHierarchy() {
        return statedHierarchy;
    }
    
    public boolean supportsStatedRelationships() {
        return true;
    }
}
