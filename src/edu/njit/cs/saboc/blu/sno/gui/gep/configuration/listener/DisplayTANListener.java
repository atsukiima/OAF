package edu.njit.cs.saboc.blu.sno.gui.gep.configuration.listener;

import edu.njit.cs.saboc.blu.core.abn.tan.ClusterTribalAbstractionNetwork;
import edu.njit.cs.saboc.blu.core.gui.listener.DisplayAbNListener;
import edu.njit.cs.saboc.blu.sno.gui.abnselection.SCTDisplayFrameListener;

/**
 *
 * @author Chris O
 */
public class DisplayTANListener implements DisplayAbNListener<ClusterTribalAbstractionNetwork> {
    
    private final SCTDisplayFrameListener listener;
    
    public DisplayTANListener(SCTDisplayFrameListener listener) {
        this.listener = listener;
    }

    @Override
    public void displayAbstractionNetwork(ClusterTribalAbstractionNetwork tan) {
        listener.addNewClusterGraphFrame(tan);
    }
}
