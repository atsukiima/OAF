
package edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy.configuration;

import edu.njit.cs.saboc.blu.core.abn.node.Node;
import edu.njit.cs.saboc.blu.core.abn.ParentNodeDetails;
import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.InheritableProperty;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.listeners.EntitySelectionAdapter;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.listeners.EntitySelectionListener;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.pareataxonomy.configuration.PAreaTaxonomyListenerConfiguration;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.reports.entry.ContainerReport;
import edu.njit.cs.saboc.blu.core.ontology.Concept;

/**
 *
 * @author Chris O
 */
public class SCTPAreaTaxonomyListenerConfiguration implements PAreaTaxonomyListenerConfiguration {

    private final SCTPAreaTaxonomyConfiguration config;
    
    public SCTPAreaTaxonomyListenerConfiguration(SCTPAreaTaxonomyConfiguration config) {
        this.config = config;
    }

    @Override
    public EntitySelectionListener<InheritableProperty> getGroupRelationshipSelectedListener() {
        return new EntitySelectionAdapter();
    }

    @Override
    public EntitySelectionListener<InheritableProperty> getContainerRelationshipSelectedListener() {
        return new EntitySelectionAdapter();
    }

    @Override
    public EntitySelectionListener<ContainerReport> getContainerReportSelectedListener() {
        return new EntitySelectionAdapter();
    }

    @Override
    public EntitySelectionListener<Concept> getGroupConceptListListener() {
        return new EntitySelectionAdapter();
    }

    @Override
    public EntitySelectionListener<Node> getChildGroupListener() {
        return new EntitySelectionAdapter();
    }

    @Override
    public EntitySelectionListener<ParentNodeDetails> getParentGroupListener() {
        return new EntitySelectionAdapter();
    }
}
