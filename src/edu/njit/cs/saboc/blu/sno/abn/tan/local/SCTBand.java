package edu.njit.cs.saboc.blu.sno.abn.tan.local;

import SnomedShared.Concept;
import edu.njit.cs.saboc.blu.core.abn.tan.nodes.Band;
import java.util.HashSet;

/**
 *
 * @author Chris O
 */
public class SCTBand extends Band<Concept, SCTCluster> {
    public SCTBand(int id, HashSet<Concept> patriarchs) {
        super(id, patriarchs);
    }
}
