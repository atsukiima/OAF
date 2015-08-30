package edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy.parea.aggregate;

import SnomedShared.Concept;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.AbstractAggregatedGroupsPanel;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTAggregatePArea;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTPArea;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.SCTConceptList;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.SCTPAreaList;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.SCTPAreaTaxonomyConfiguration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;

/**
 *
 * @author Chris O
 */
public class SCTAggregatedPAreasPanel extends AbstractAggregatedGroupsPanel<SCTAggregatePArea, SCTPArea, Concept> {

    public SCTAggregatedPAreasPanel(SCTPAreaTaxonomyConfiguration config) {
        super(new SCTPAreaList(config), 
                new SCTConceptList(), 
                config);
    }
    
    @Override
    protected ArrayList<SCTPArea> getSortedAggregatedGroupList(HashSet<SCTPArea> groups) {
        ArrayList<SCTPArea> pareas = new ArrayList<>(groups);
        
        Collections.sort(pareas, new Comparator<SCTPArea>() {
            public int compare(SCTPArea a, SCTPArea b) {
                if(a.getRelationships().size() == b.getRelationships().size()) {
                    if(a.getConceptCount() == b.getConceptCount()) {
                        String aRootLabel = a.getRoot().getName();
                        String bRootLabel = b.getRoot().getName();
                        
                        return aRootLabel.compareToIgnoreCase(bRootLabel);
                    } else {
                        return a.getConceptCount() - b.getConceptCount();
                    }
                } else {
                    return a.getRelationships().size() - b.getRelationships().size();
                }
            }
        });
        
        return pareas;
    }
}
