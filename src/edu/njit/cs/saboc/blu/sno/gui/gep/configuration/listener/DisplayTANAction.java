package edu.njit.cs.saboc.blu.sno.gui.gep.configuration.listener;

import edu.njit.cs.saboc.blu.core.abn.tan.ClusterTribalAbstractionNetwork;
import edu.njit.cs.saboc.blu.core.gui.graphframe.AbNDisplayManager;
import edu.njit.cs.saboc.blu.core.gui.listener.DisplayAbNAction;

/**
 * Action for displaying a TAN
 * 
 * @author Chris O
 */
public class DisplayTANAction implements DisplayAbNAction<ClusterTribalAbstractionNetwork> {
    
    private final AbNDisplayManager listener;
    
    public DisplayTANAction(AbNDisplayManager listener) {
        this.listener = listener;
    }

    @Override
    public void displayAbstractionNetwork(ClusterTribalAbstractionNetwork tan) {
        listener.displayTribalAbstractionNetwork(tan);
    }
}
