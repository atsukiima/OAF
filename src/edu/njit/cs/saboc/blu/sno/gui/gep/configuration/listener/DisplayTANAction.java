package edu.njit.cs.saboc.blu.sno.gui.gep.configuration.listener;

import edu.njit.cs.saboc.blu.core.abn.tan.ClusterTribalAbstractionNetwork;
import edu.njit.cs.saboc.blu.core.gui.listener.DisplayAbNAction;
import edu.njit.cs.saboc.blu.sno.gui.abnselection.SCTDisplayFrameListener;

/**
 *
 * @author Chris O
 */
public class DisplayTANAction implements DisplayAbNAction<ClusterTribalAbstractionNetwork> {
    
    private final SCTDisplayFrameListener listener;
    
    public DisplayTANAction(SCTDisplayFrameListener listener) {
        this.listener = listener;
    }

    @Override
    public void displayAbstractionNetwork(ClusterTribalAbstractionNetwork tan) {
        listener.addNewClusterGraphFrame(tan);
    }
}
