package edu.njit.cs.saboc.blu.sno.gui.gep.panels.disjointpareataxonomy.configuration;

import SnomedShared.Concept;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.configuration.data.BLUAbNDataConfiguration;
import edu.njit.cs.saboc.blu.sno.abn.disjointpareataxonomy.DisjointPAreaTaxonomy;
import edu.njit.cs.saboc.blu.sno.abn.disjointpareataxonomy.DisjointPartialArea;
import java.util.ArrayList;

/**
 *
 * @author Chris O
 */
public class SCTDisjointPAreaTaxonomyDataConfiguration implements BLUAbNDataConfiguration<DisjointPAreaTaxonomy, DisjointPartialArea, Concept> {
    
    private final DisjointPAreaTaxonomy disjointTaxonomy;
    
    public SCTDisjointPAreaTaxonomyDataConfiguration(DisjointPAreaTaxonomy disjointTaxonomy) {
        this.disjointTaxonomy = disjointTaxonomy;
    }
    
    public DisjointPAreaTaxonomy getDisjointPAreaTaxonomy() {
        return disjointTaxonomy;
    }
    
    @Override
    public ArrayList<Concept> getSortedConceptList(DisjointPartialArea group) {
        return group.getConceptsAsList();
    }
}
