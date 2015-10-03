package edu.njit.cs.saboc.blu.sno.gui.gep.panels.disjointpareataxonomy.configuration;

import SnomedShared.Concept;
import edu.njit.cs.saboc.blu.core.abn.GenericParentGroupInfo;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.configuration.ui.listener.BLUAbNListenerConfiguration;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.listeners.EntitySelectionListener;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.listeners.NavigateToGroupListener;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.listeners.ParentGroupSelectedListener;
import edu.njit.cs.saboc.blu.sno.abn.disjointpareataxonomy.DisjointPAreaTaxonomy;
import edu.njit.cs.saboc.blu.sno.abn.disjointpareataxonomy.DisjointPartialArea;
import edu.njit.cs.saboc.blu.sno.gui.gep.configuration.listener.DisplayConceptBrowserListener;

/**
 *
 * @author Chris O
 */
public class SCTDisjointPAreaTaxonomyListenerConfiguration implements BLUAbNListenerConfiguration<DisjointPAreaTaxonomy, DisjointPartialArea, Concept> {
    
    private final SCTDisjointPAreaTaxonomyConfiguration config;
    
    public SCTDisjointPAreaTaxonomyListenerConfiguration(SCTDisjointPAreaTaxonomyConfiguration config) {
        this.config = config;
    }
    
    @Override
    public EntitySelectionListener<Concept> getGroupConceptListListener() {
        return new DisplayConceptBrowserListener(config.getUIConfiguration().getDisplayListener(), 
                config.getDataConfiguration().getDisjointPAreaTaxonomy().getSourcePAreaTaxonomy().getDataSource());
    }

    @Override
    public EntitySelectionListener<DisjointPartialArea> getChildGroupListener() {
        return new NavigateToGroupListener<>(config.getUIConfiguration().getGEP());
    }
    
    @Override
    public EntitySelectionListener<GenericParentGroupInfo<Concept, DisjointPartialArea>> getParentGroupListener() {
        return new ParentGroupSelectedListener<>(config.getUIConfiguration().getGEP());
    }
   
}
