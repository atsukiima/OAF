package edu.njit.cs.saboc.blu.sno.gui.gep.panels.disjointpareataxonomy.configuration;

import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.disjointabn.pareataxonomy.DisjointPAreaTaxonomyListenerConfiguration;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.listeners.EntitySelectionListener;
import edu.njit.cs.saboc.blu.core.ontology.Concept;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.listeners.SCTDisplayNATListener;
import edu.njit.cs.saboc.blu.sno.sctdatasource.SCTRelease;

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
        SCTDisjointPAreaTaxonomyConfiguration config = (SCTDisjointPAreaTaxonomyConfiguration)super.getConfiguration();
        
        return new SCTDisplayNATListener(config.getUIConfiguration().getFrameManager(), config.getRelease());
    }
}
