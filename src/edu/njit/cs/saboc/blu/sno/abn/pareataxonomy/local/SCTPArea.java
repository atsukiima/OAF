package edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local;

import SnomedShared.Concept;
import SnomedShared.pareataxonomy.InheritedRelationship;
import SnomedShared.pareataxonomy.InheritedRelationship.InheritanceType;
import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.GenericPArea;
import edu.njit.cs.saboc.blu.sno.abn.generator.InheritedRelWithHash;
import edu.njit.cs.saboc.blu.sno.datastructure.hierarchy.SCTConceptHierarchy;
import java.util.HashSet;

/**
 * A Partial-area that was created using a local SNOMED CT release.
 * Unlike a PArea Summary, a LocalPArea stores its hierarchy of concepts and
 * information about its parents.
 * @author 
 */
public class SCTPArea extends GenericPArea<Concept, InheritedRelationship, SCTConceptHierarchy, SCTPArea>  {
        
    public SCTPArea(int id, 
            SCTConceptHierarchy conceptHierarchy, 
            HashSet<Integer> parentIds,
            HashSet<InheritedRelationship> relationships) {
        
        super(id,  conceptHierarchy.getRoot(),  conceptHierarchy, parentIds, relationships);
    }
    
    public HashSet<InheritedRelationship> getRelsWithoutInheritanceInfo() {
        HashSet<InheritedRelationship> rels = new HashSet<InheritedRelationship>();
        
        for(InheritedRelationship rel : this.getRelationships()) {
            rels.add(new InheritedRelWithHash(InheritanceType.INHERITED, rel.getRelationshipTypeId()));
        }
        
        return rels;
    }
}
