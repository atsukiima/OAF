package edu.njit.cs.saboc.blu.sno.gui.gep.panels.tan.cluster;

import SnomedShared.Concept;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.AbstractNodeDetailsPanel;
import edu.njit.cs.saboc.blu.sno.abn.tan.local.SCTCluster;
import edu.njit.cs.saboc.blu.sno.abn.tan.local.SCTTribalAbstractionNetwork;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.SCTConceptList;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.SCTTANConfiguration;
import edu.njit.cs.saboc.blu.sno.utils.comparators.ConceptNameComparator;
import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author Chris O
 */
public class SCTClusterDetailsPanel extends AbstractNodeDetailsPanel<SCTCluster, Concept> {
    
    protected final SCTTribalAbstractionNetwork tan;
    
    public SCTClusterDetailsPanel(SCTTANConfiguration config) {
        
        super(new SCTClusterSummaryPanel(config), 
               new SCTClusterOptionsPanel(config),
              new SCTConceptList());
        
        this.tan = config.getTribalAbstractionNetwork();
    }
    
    @Override
    protected ArrayList<Concept> getSortedConceptList(SCTCluster cluster) {
        ArrayList<Concept> concepts = new ArrayList<>(cluster.getConcepts());
        Collections.sort(concepts, new ConceptNameComparator());
        
        return concepts;
    }
}
