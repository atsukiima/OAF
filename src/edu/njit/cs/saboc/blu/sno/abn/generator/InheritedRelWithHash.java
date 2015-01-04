
package edu.njit.cs.saboc.blu.sno.abn.generator;

import SnomedShared.pareataxonomy.InheritedRelationship;

/**
 *
 * @author Chris O
 */
public class InheritedRelWithHash extends InheritedRelationship {

    public InheritedRelWithHash(InheritedRelationship.InheritanceType inheritance, long relId) {
        super(inheritance, relId);
    }

    public int hashCode() {
        return String.format("%d %s", this.getRelationshipTypeId(), this.getInheritanceType().name()).hashCode();
    }
}
