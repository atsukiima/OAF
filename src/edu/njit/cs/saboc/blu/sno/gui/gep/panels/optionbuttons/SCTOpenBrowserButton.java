
package edu.njit.cs.saboc.blu.sno.gui.gep.panels.optionbuttons;

import edu.njit.cs.saboc.blu.core.abn.node.SinglyRootedNode;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.configuration.AbNConfiguration;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.optionbuttons.node.OpenBrowserButton;
import edu.njit.cs.saboc.blu.sno.gui.abnselection.SCTAbNFrameManager;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.listeners.SCTDisplayNATListener;
import edu.njit.cs.saboc.blu.sno.sctdatasource.SCTRelease;

/**
 *
 * @author Chris O
 * @param <T>
 */
public class SCTOpenBrowserButton<T extends SinglyRootedNode> extends OpenBrowserButton<T> {
    
    private final AbNConfiguration config;
    private final SCTAbNFrameManager frameManager;
    private final SCTRelease release;
    
    public SCTOpenBrowserButton(
            AbNConfiguration config, 
            SCTRelease release,
            SCTAbNFrameManager frameManager) {
        
        super(config.getTextConfiguration().getNodeTypeName(false));
        
        this.config = config;
        this.release = release;
        this.frameManager = frameManager;
    }

    @Override
    public void displayBrowserWindowAction() {

        SCTDisplayNATListener displayNATListener = new SCTDisplayNATListener(
                frameManager, 
                release);
        
        displayNATListener.entityDoubleClicked(this.getCurrentEntity().get().getRoot());
    }
}
