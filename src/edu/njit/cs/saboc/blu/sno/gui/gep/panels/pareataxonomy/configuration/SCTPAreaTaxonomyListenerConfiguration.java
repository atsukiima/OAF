
package edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy.configuration;

import SnomedShared.Concept;
import SnomedShared.pareataxonomy.InheritedRelationship;
import edu.njit.cs.saboc.blu.core.abn.GenericParentGroupInfo;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.listeners.EntitySelectionAdapter;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.listeners.EntitySelectionListener;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.listeners.NavigateToContainerReportListener;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.listeners.NavigateToNodeListener;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.listeners.ParentGroupSelectedListener;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.pareataxonomy.configuration.PAreaTaxonomyListenerConfiguration;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.reports.entry.ContainerReport;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTArea;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTPArea;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTPAreaTaxonomy;
import edu.njit.cs.saboc.blu.sno.gui.gep.configuration.listener.DisplayConceptBrowserListener;

/**
 *
 * @author Chris O
 */
public class SCTPAreaTaxonomyListenerConfiguration implements PAreaTaxonomyListenerConfiguration<SCTPAreaTaxonomy, SCTArea, SCTPArea, Concept, InheritedRelationship> {


    private final SCTPAreaTaxonomyConfiguration config;
    
    public SCTPAreaTaxonomyListenerConfiguration(SCTPAreaTaxonomyConfiguration config) {
        this.config = config;
    }
    
    @Override
    public EntitySelectionListener<Concept> getGroupConceptListListener() {
        return new DisplayConceptBrowserListener(config.getUIConfiguration().getDisplayFrameListener(), 
                config.getDataConfiguration().getPAreaTaxonomy().getDataSource());
    }

    @Override
    public EntitySelectionListener<SCTPArea> getChildGroupListener() {
        return new NavigateToNodeListener<>(config.getUIConfiguration().getGEP());
    }
    
    @Override
    public EntitySelectionListener<GenericParentGroupInfo<Concept, SCTPArea>> getParentGroupListener() {
        return new ParentGroupSelectedListener<>(config.getUIConfiguration().getGEP());
    }
    
    @Override
    public EntitySelectionListener<InheritedRelationship> getGroupRelationshipSelectedListener() {
        return new EntitySelectionAdapter<>();
    }

    @Override
    public EntitySelectionListener<InheritedRelationship> getContainerRelationshipSelectedListener() {
        return new EntitySelectionAdapter<>();
    }

    @Override
    public EntitySelectionListener<ContainerReport<SCTArea, SCTPArea, Concept>> getContainerReportSelectedListener() {
        return new NavigateToContainerReportListener<>(config.getUIConfiguration().getGEP());
    }
    
}
