package edu.njit.cs.saboc.blu.sno.gui.main;

import edu.njit.cs.saboc.blu.core.gui.frame.AbnSelectionFrameFactory;
import edu.njit.cs.saboc.blu.core.gui.frame.OAFMainFrame;
import edu.njit.cs.saboc.blu.sno.gui.abnselection.SCTAbNFrameManager;
import edu.njit.cs.saboc.blu.sno.gui.abnselection.SCTMainPanel;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;

/**
 *
 * @author hl395
 */
public class SCTSelectionFrame implements AbnSelectionFrameFactory {

    @Override
    public JInternalFrame createAbNSelectionFrame(OAFMainFrame parentFrame) {
        
        JInternalFrame jif = new JInternalFrame();
        
        jif.setSize(1400, 700);
        
        JPanel jp = new SCTMainPanel(
                new SCTAbNFrameManager(parentFrame, (frame) -> {
                    parentFrame.addInternalFrame(frame);
                }));

        jif.add(jp);
        jif.setVisible(true);
        
        return jif;
    }
}
