package edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local;

import SnomedShared.Concept;
import SnomedShared.pareataxonomy.InheritedRelationship;
import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.GenericArea;
import edu.njit.cs.saboc.blu.sno.datastructure.hierarchy.SCTConceptHierarchy;
import java.util.HashSet;

/**
 *
 * @author Chris O
 */
public class SCTArea extends GenericArea<Concept, InheritedRelationship, SCTConceptHierarchy, SCTPArea, SCTRegion> {

    private boolean isImplicit = false;
            
    public SCTArea(int id, HashSet<InheritedRelationship> relationships) {
        super(id, relationships);
    }
    
    public SCTArea(int id, HashSet<InheritedRelationship> relationships, boolean isImplicit) {
        this(id, relationships);
        
        this.isImplicit = isImplicit;
    }

    protected SCTRegion createRegion(SCTPArea firstPArea) {
        return new SCTRegion(firstPArea);
    }
    
    public boolean isImplicit() {
        return isImplicit;
    }
    
    public HashSet<Long> getRelationshipIds() {
        HashSet<Long> relIds = new HashSet<Long>();
        
        for(InheritedRelationship rel : this.relationships) {
            relIds.add(rel.getRelationshipTypeId());
        }
        
        return relIds;
    }
}
