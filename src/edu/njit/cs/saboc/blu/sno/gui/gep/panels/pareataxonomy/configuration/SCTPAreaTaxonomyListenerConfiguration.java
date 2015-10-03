
package edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy.configuration;

import SnomedShared.Concept;
import edu.njit.cs.saboc.blu.core.abn.GenericParentGroupInfo;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.configuration.ui.listener.BLUAbNListenerConfiguration;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.listeners.EntitySelectionListener;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.listeners.NavigateToGroupListener;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.listeners.ParentGroupSelectedListener;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTPArea;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTPAreaTaxonomy;
import edu.njit.cs.saboc.blu.sno.gui.gep.configuration.listener.DisplayConceptBrowserListener;

/**
 *
 * @author Chris O
 */
public class SCTPAreaTaxonomyListenerConfiguration implements BLUAbNListenerConfiguration<SCTPAreaTaxonomy, SCTPArea, Concept> {
    
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
        return new NavigateToGroupListener<>(config.getUIConfiguration().getGEP());
    }
    
    @Override
    public EntitySelectionListener<GenericParentGroupInfo<Concept, SCTPArea>> getParentGroupListener() {
        return new ParentGroupSelectedListener<>(config.getUIConfiguration().getGEP());
    }
}
