package edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy.area;

import SnomedShared.Concept;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.AbstractAbNNodeEntityList;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.entry.ContainerConceptEntry;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTArea;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTPArea;
import java.util.ArrayList;
import java.util.Optional;

/**
 *
 * @author Chris O
 */
public class SCTAreaConceptList extends AbstractAbNNodeEntityList<SCTArea, ContainerConceptEntry<Concept, SCTPArea>> {
    
    public SCTAreaConceptList() {
        super(new SCTAreaConceptTableModel());
    }

    @Override
    protected String getBorderText(Optional<ArrayList<ContainerConceptEntry<Concept, SCTPArea>>> entities) {
        
        if(entities.isPresent()) {
            int overlapping = 0;
            
            ArrayList<ContainerConceptEntry<Concept, SCTPArea>> classes = entities.get();
            
            for(ContainerConceptEntry<Concept, SCTPArea> entry : classes) {
                if(entry.getGroups().size() > 1) {
                    overlapping++;
                }
            }

            return String.format("Concepts (%d total, %d overlapping)", entities.get().size(), overlapping);
        } else {
            return "Concepts";
        }
    }
}
