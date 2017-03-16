package edu.njit.cs.saboc.blu.sno.gui.dialogs.panels.concepthierarchy;

import edu.njit.cs.saboc.blu.core.gui.dialogs.concepthierarchy.ConceptEntry;
import edu.njit.cs.saboc.blu.core.gui.dialogs.concepthierarchy.ConceptPainter;
import edu.njit.cs.saboc.blu.sno.localdatasource.concept.SCTConcept;
import java.awt.Color;

/**
 * A concept hierarchy painter that paints primitive concepts
 * as purple
 * 
 * @author Chris O
 */
public class SCTConceptPainter extends ConceptPainter {
    
    @Override
    public Color getEntryColor(ConceptEntry entry) {
        SCTConcept concept = (SCTConcept)entry.getConcept();
        
        if (concept.isPrimitive()) {
            return new Color(128, 100, 128);
        } else {
            return super.getEntryColor(entry);
        }
    }
}
