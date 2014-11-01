package edu.njit.cs.saboc.blu.sno.gui.dialogs.panels.concepthierarchy;

import SnomedShared.Concept;
import edu.njit.cs.saboc.blu.core.gui.dialogs.concepthierarchy.ConceptEntry;
import edu.njit.cs.saboc.blu.core.gui.dialogs.concepthierarchy.ConceptPainter;
import java.awt.Color;

/**
 *
 * @author Chris
 */
public class SCTConceptPainter extends ConceptPainter<Concept> {
    public Color getEntryColor(ConceptEntry<Concept> entry) {
        if (entry.getConcept().primitiveSet() && entry.getConcept().isPrimitive()) {
            return new Color(128, 100, 128);
        } else {
            return super.getEntryColor(entry);
        }
    }
}
