package edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy.area;

import SnomedShared.Concept;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.AbstractContainerGroupListPanel;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTArea;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTPArea;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.SCTConceptList;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.SCTPAreaList;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.SCTPAreaTaxonomyConfiguration;
import java.util.ArrayList;

/**
 *
 * @author Chris O
 */
public class SCTAreaPAreaListPanel extends AbstractContainerGroupListPanel<SCTArea, SCTPArea, Concept> {

    public SCTAreaPAreaListPanel(SCTPAreaTaxonomyConfiguration config) {
        super(new SCTPAreaList(config), 
                new SCTConceptList(), 
                config);
    }
    
    @Override
    public ArrayList<Concept> getSortedConceptList(SCTPArea parea) {
        return configuration.getSortedConceptList(parea);
    }
}
