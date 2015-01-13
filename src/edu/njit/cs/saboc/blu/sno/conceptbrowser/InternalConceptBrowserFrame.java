package edu.njit.cs.saboc.blu.sno.conceptbrowser;

import SnomedShared.Concept;
import edu.njit.cs.saboc.blu.sno.gui.abnselection.SCTDisplayFrameListener;
import edu.njit.cs.saboc.blu.sno.sctdatasource.SCTDataSource;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;

/**
 *
 * @author Chris
 */
public class InternalConceptBrowserFrame extends JInternalFrame {

    private SnomedConceptBrowser browser;
    
    public InternalConceptBrowserFrame(JFrame parentFrame, SCTDataSource dataSource, SCTDisplayFrameListener displayFrameListener) {
        super("BLUSNO Concept-centric Browser",
                true, //resizable
                true, //closable
                true, //maximizable
                true);//iconifiable
        
        browser = new SnomedConceptBrowser(parentFrame, dataSource, displayFrameListener);

        setSize(1200, 550);

        this.add(browser);
        this.setVisible(true);
    }

    public void nagivateTo(Concept c) {
        browser.navigateTo(c);
    }

    public void navigateTo(long conceptId) {
        browser.navigateTo(conceptId);
    }
}
