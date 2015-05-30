package edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local;

import SnomedShared.Concept;
import SnomedShared.pareataxonomy.InheritedRelationship;
import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.GenericRegion;
import edu.njit.cs.saboc.blu.sno.datastructure.hierarchy.SCTConceptHierarchy;

/**
 *
 * @author Chris O
 */
public class SCTRegion extends GenericRegion<Concept, InheritedRelationship, SCTConceptHierarchy, SCTPArea> {

    public SCTRegion(SCTPArea firstPArea) {
        super(firstPArea);
    }
}
