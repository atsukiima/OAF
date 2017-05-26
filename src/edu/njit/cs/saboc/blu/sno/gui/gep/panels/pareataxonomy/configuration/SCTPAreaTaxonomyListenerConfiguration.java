
package edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy.configuration;

import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.listeners.EntitySelectionListener;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.pareataxonomy.configuration.PAreaTaxonomyListenerConfiguration;
import edu.njit.cs.saboc.blu.core.ontology.Concept;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.listeners.SCTDisplayNATListener;
import edu.njit.cs.saboc.blu.sno.sctdatasource.SCTRelease;

/**
 *
 * @author Chris O
 */
public class SCTPAreaTaxonomyListenerConfiguration extends PAreaTaxonomyListenerConfiguration {

    public SCTPAreaTaxonomyListenerConfiguration(SCTPAreaTaxonomyConfiguration config) {
        super(config);
    }
    
    @Override
    public EntitySelectionListener<Concept> getGroupConceptListListener() {
        SCTPAreaTaxonomyConfiguration config = (SCTPAreaTaxonomyConfiguration)super.getConfiguration();
        SCTRelease release = config.getRelease();
        
        return new SCTDisplayNATListener(config.getUIConfiguration().getFrameManager(), release);
    }
}
