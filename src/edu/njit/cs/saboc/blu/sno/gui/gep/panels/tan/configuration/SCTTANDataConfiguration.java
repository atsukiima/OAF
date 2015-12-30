package edu.njit.cs.saboc.blu.sno.gui.gep.panels.tan.configuration;

import SnomedShared.Concept;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.tan.BLUGenericTANDataConfiguration;
import edu.njit.cs.saboc.blu.sno.abn.tan.local.SCTBand;
import edu.njit.cs.saboc.blu.sno.abn.tan.local.SCTCluster;
import edu.njit.cs.saboc.blu.sno.abn.tan.local.SCTTribalAbstractionNetwork;
import edu.njit.cs.saboc.blu.sno.utils.comparators.ConceptNameComparator;
import java.util.Comparator;

/**
 *
 * @author Chris O
 */
public class SCTTANDataConfiguration extends BLUGenericTANDataConfiguration<SCTTribalAbstractionNetwork, SCTBand, SCTCluster, Concept> {

    public SCTTANDataConfiguration(SCTTribalAbstractionNetwork tan) {
        super(tan);
    }
    
    public Comparator<Concept> getConceptNameComparator() {
        return new ConceptNameComparator();
    }
}
