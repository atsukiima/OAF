package edu.njit.cs.saboc.blu.sno.gui.gep.panels.disjointpareataxonomy.configuration;

import edu.njit.cs.saboc.blu.core.abn.ParentNodeDetails;
import edu.njit.cs.saboc.blu.core.abn.disjoint.DisjointNode;
import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.PArea;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.disjointabn.pareataxonomy.DisjointPAreaTaxonomyListenerConfiguration;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.listeners.EntitySelectionAdapter;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.listeners.EntitySelectionListener;
import edu.njit.cs.saboc.blu.core.ontology.Concept;

/**
 *
 * @author Chris O
 */
public class SCTDisjointPAreaTaxonomyListenerConfiguration extends DisjointPAreaTaxonomyListenerConfiguration {
    
    
    public SCTDisjointPAreaTaxonomyListenerConfiguration(SCTDisjointPAreaTaxonomyConfiguration config) {
        super(config);
    }

    @Override
    public EntitySelectionListener<Concept> getGroupConceptListListener() {
        return new EntitySelectionAdapter<>();
    }

    @Override
    public EntitySelectionListener<DisjointNode<PArea>> getChildGroupListener() {
        return new EntitySelectionAdapter<>();
    }

    @Override
    public EntitySelectionListener<ParentNodeDetails<DisjointNode<PArea>>> getParentGroupListener() {
        return new EntitySelectionAdapter<>();
    }
}
