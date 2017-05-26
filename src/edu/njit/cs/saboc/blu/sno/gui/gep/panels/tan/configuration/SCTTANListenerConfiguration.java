
package edu.njit.cs.saboc.blu.sno.gui.gep.panels.tan.configuration;

import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.listeners.EntitySelectionListener;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.tan.configuration.TANListenerConfiguration;
import edu.njit.cs.saboc.blu.core.ontology.Concept;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.listeners.SCTDisplayNATListener;
import edu.njit.cs.saboc.blu.sno.sctdatasource.SCTRelease;

/**
 *
 * @author Chris O
 */
public class SCTTANListenerConfiguration extends TANListenerConfiguration {

    public SCTTANListenerConfiguration(SCTTANConfiguration config) {
        super(config);
    }
    
    @Override
    public EntitySelectionListener<Concept> getGroupConceptListListener() {
        
        SCTTANConfiguration config = (SCTTANConfiguration)super.getConfiguration();
        SCTRelease release = (SCTRelease)config.getRelease();
        
        return new SCTDisplayNATListener(config.getUIConfiguration().getFrameManager(), release);
        
    }
}
