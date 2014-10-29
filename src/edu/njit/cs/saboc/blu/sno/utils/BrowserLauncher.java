package edu.njit.cs.saboc.blu.sno.utils;

import edu.njit.cs.saboc.blu.sno.abn.SCTAbstractionNetwork;
import edu.njit.cs.saboc.blu.sno.gui.abnselection.SCTDisplayFrameListener;
import javax.swing.SwingUtilities;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkEvent.EventType;
import javax.swing.event.HyperlinkListener;

/**
 *
 * @author Chris
 */
public class BrowserLauncher implements HyperlinkListener {

    private SCTAbstractionNetwork abstractionNetwork;
    
    private SCTDisplayFrameListener displayListener;
    
    public BrowserLauncher(SCTDisplayFrameListener displayListener, SCTAbstractionNetwork abstractionNetwork) {
        this.displayListener = displayListener;
        this.abstractionNetwork = abstractionNetwork;
    }

    public void hyperlinkUpdate(HyperlinkEvent he) {

        if (he.getEventType() != EventType.ACTIVATED) {
            return;
        }

        final String conceptIdStr = he.getDescription().trim();

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                long conceptId = Long.parseLong(conceptIdStr);
                
                displayListener.addNewBrowserFrame(conceptId, abstractionNetwork.getSCTDataSource());
            }
        });
    }
}
