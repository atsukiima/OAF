package edu.njit.cs.saboc.blu.sno.gui.gep.panels.disjointtan.configuration;

import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.disjointabn.tan.DisjointTANListenerConfiguration;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.listeners.EntitySelectionListener;
import edu.njit.cs.saboc.blu.core.ontology.Concept;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.listeners.SCTDisplayNATListener;

/**
 *
 * @author Chris O
 */
public class SCTDisjointTANListenerConfiguration extends DisjointTANListenerConfiguration {
    public SCTDisjointTANListenerConfiguration(SCTDisjointTANConfiguration config) {
        super(config);
    }
    
    @Override
    public EntitySelectionListener<Concept> getGroupConceptListListener() {
        SCTDisjointTANConfiguration config = (SCTDisjointTANConfiguration)super.getConfiguration();
        
        return new SCTDisplayNATListener(
                config.getUIConfiguration().getFrameManager(), 
                config.getRelease());
    }
}
