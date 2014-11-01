package edu.njit.cs.saboc.blu.sno.gui.dialogs.panels.concepthierarchy;

import SnomedShared.Concept;
import edu.njit.cs.saboc.blu.core.gui.dialogs.concepthierarchy.ConceptEntry;

/**
 *
 * @author Chris
 */
public class SCTConceptEntry extends ConceptEntry<Concept> {
    
    public SCTConceptEntry(Concept concept) {
        super(concept);
    }
    
    public String getConceptName(Concept c) {
        return c.getName().substring(0, c.getName().lastIndexOf(" ("));
    }
}
