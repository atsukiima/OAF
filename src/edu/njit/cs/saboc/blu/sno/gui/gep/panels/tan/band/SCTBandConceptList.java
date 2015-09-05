package edu.njit.cs.saboc.blu.sno.gui.gep.panels.tan.band;

import SnomedShared.Concept;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.AbstractEntityList;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.entry.ContainerConceptEntry;
import edu.njit.cs.saboc.blu.sno.abn.tan.local.SCTCluster;
import java.util.ArrayList;
import java.util.Optional;

/**
 *
 * @author Chris O
 */
public class SCTBandConceptList extends AbstractEntityList<ContainerConceptEntry<Concept, SCTCluster>> {
    
    public SCTBandConceptList() {
        super(new SCTBandConceptTableModel());
    }

    @Override
    protected String getBorderText(Optional<ArrayList<ContainerConceptEntry<Concept, SCTCluster>>> entities) {
        
        if(entities.isPresent()) {
            int overlapping = 0;
            
            ArrayList<ContainerConceptEntry<Concept, SCTCluster>> classes = entities.get();
            
            for(ContainerConceptEntry<Concept, SCTCluster> entry : classes) {
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
