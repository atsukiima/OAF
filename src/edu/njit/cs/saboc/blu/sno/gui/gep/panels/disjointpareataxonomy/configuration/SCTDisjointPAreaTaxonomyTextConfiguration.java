package edu.njit.cs.saboc.blu.sno.gui.gep.panels.disjointpareataxonomy.configuration;

import edu.njit.cs.saboc.blu.core.abn.disjoint.DisjointAbstractionNetwork;
import edu.njit.cs.saboc.blu.core.abn.disjoint.DisjointNode;
import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.PArea;
import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.PAreaTaxonomy;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.disjointabn.pareataxonomy.DisjointPAreaTaxonomyTextConfiguration;
import edu.njit.cs.saboc.blu.sno.localdatasource.concept.SCTEntityNameConfiguration;

/**
 *
 * @author Chris O
 */
public class SCTDisjointPAreaTaxonomyTextConfiguration extends DisjointPAreaTaxonomyTextConfiguration {

    public SCTDisjointPAreaTaxonomyTextConfiguration(
            DisjointAbstractionNetwork<DisjointNode<PArea>, PAreaTaxonomy<PArea>, PArea> disjointTaxonomy) {
        
        super(new SCTEntityNameConfiguration(), disjointTaxonomy);
    }
}
