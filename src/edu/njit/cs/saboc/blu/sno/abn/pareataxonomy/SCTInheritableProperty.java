
package edu.njit.cs.saboc.blu.sno.abn.pareataxonomy;

import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.InheritableProperty;
import edu.njit.cs.saboc.blu.core.ontology.Concept;
import edu.njit.cs.saboc.blu.sno.localdatasource.concept.SCTConcept;

/**
 *
 * @author Chris O
 */
public class SCTInheritableProperty extends InheritableProperty<Long, Concept> {

    public SCTInheritableProperty(SCTConcept relType, InheritanceType inheritance) {
        super(relType.getID(), relType, inheritance);
    }

    @Override
    public String getName() {
        return super.getPropertyType().getName();
    }

    @Override
    public String getIDAsString() {
        return super.getID().toString();
    }
}
