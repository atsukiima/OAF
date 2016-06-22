package edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy.area;

import SnomedShared.Concept;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.NodeEntityList;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.entry.PartitionedNodeConceptEntry;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTArea;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTPArea;
import java.util.ArrayList;
import java.util.Optional;

/**
 *
 * @author Chris O
 */
public class SCTAreaConceptList extends NodeEntityList<SCTArea, PartitionedNodeConceptEntry<Concept, SCTPArea>> {
    
    public SCTAreaConceptList() {
        super(new SCTAreaConceptTableModel());
    }

    @Override
    protected String getBorderText(Optional<ArrayList<PartitionedNodeConceptEntry<Concept, SCTPArea>>> entities) {
        
        if(entities.isPresent()) {
            int overlapping = 0;
            
            ArrayList<PartitionedNodeConceptEntry<Concept, SCTPArea>> classes = entities.get();
            
            for(PartitionedNodeConceptEntry<Concept, SCTPArea> entry : classes) {
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
