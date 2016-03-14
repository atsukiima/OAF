package edu.njit.cs.saboc.blu.sno.abn.tan.local;

import SnomedShared.Concept;
import edu.njit.cs.saboc.blu.core.abn.tan.nodes.GenericBand;
import java.util.HashSet;

/**
 *
 * @author Chris O
 */
public class SCTBand extends GenericBand<Concept, SCTCluster> {
    public SCTBand(int id, HashSet<Concept> patriarchs) {
        super(id, patriarchs);
    }
}