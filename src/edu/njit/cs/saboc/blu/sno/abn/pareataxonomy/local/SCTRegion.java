package edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local;

import SnomedShared.Concept;
import SnomedShared.pareataxonomy.InheritedRelationship;
import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.GenericRegion;

/**
 *
 * @author Chris O
 */
public class SCTRegion extends GenericRegion<Concept, InheritedRelationship, SCTPArea> {

    public SCTRegion(SCTPArea firstPArea) {
        super(firstPArea);
    }
}
