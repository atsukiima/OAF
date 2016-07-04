package edu.njit.cs.saboc.blu.sno.gui.gep.panels.disjointpareataxonomy.configuration;

import edu.njit.cs.saboc.blu.core.abn.node.Node;
import edu.njit.cs.saboc.blu.core.abn.ParentNodeDetails;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.configuration.DisjointAbNListenerConfiguration;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.listeners.EntitySelectionAdapter;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.listeners.EntitySelectionListener;
import edu.njit.cs.saboc.blu.core.ontology.Concept;

/**
 *
 * @author Chris O
 */
public class SCTDisjointPAreaTaxonomyListenerConfiguration extends DisjointAbNListenerConfiguration {
    
    private final SCTDisjointPAreaTaxonomyConfiguration config;
    
    public SCTDisjointPAreaTaxonomyListenerConfiguration(SCTDisjointPAreaTaxonomyConfiguration config) {
        this.config = config;
    }

    @Override
    public EntitySelectionListener<Concept> getGroupConceptListListener() {
        return new EntitySelectionAdapter<>();
    }

    @Override
    public EntitySelectionListener<Node> getChildGroupListener() {
        return new EntitySelectionAdapter<>();
    }

    @Override
    public EntitySelectionListener<ParentNodeDetails> getParentGroupListener() {
        return new EntitySelectionAdapter<>();
    }
}
