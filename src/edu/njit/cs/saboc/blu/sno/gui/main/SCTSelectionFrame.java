package edu.njit.cs.saboc.blu.sno.gui.main;

import edu.njit.cs.saboc.blu.core.gui.frame.AbnSelectionFrameFactory;
import edu.njit.cs.saboc.blu.core.gui.frame.OAFMainFrame;
import edu.njit.cs.saboc.blu.core.utils.toolstate.OAFRecentlyOpenedFileManager;
import edu.njit.cs.saboc.blu.core.utils.toolstate.OAFStateFileManager;
import edu.njit.cs.saboc.blu.sno.gui.abnselection.SCTAbNFrameManager;
import edu.njit.cs.saboc.blu.sno.gui.abnselection.SCTMainPanel;
import javax.swing.JInternalFrame;

/**
 *
 * @author hl395
 */
public class SCTSelectionFrame implements AbnSelectionFrameFactory {
    
    private OAFStateFileManager stateFileManager;
    
    @Override
    public JInternalFrame createAbNSelectionFrame(OAFMainFrame parentFrame) {
        
        JInternalFrame internalFrame = new JInternalFrame();
        
        internalFrame.setSize(1400, 700);
        
        try {
            this.stateFileManager = new OAFStateFileManager("BLUSNO");
        } catch (OAFRecentlyOpenedFileManager.RecentlyOpenedFileException rofe) {
            this.stateFileManager = null;
        }
        
        SCTAbNFrameManager sctFrameManager = new SCTAbNFrameManager(parentFrame, (frame) -> {
                    parentFrame.addInternalFrame(frame);
                }, stateFileManager);

        SCTMainPanel sctMainPanel = new SCTMainPanel(sctFrameManager, stateFileManager);

        internalFrame.add(sctMainPanel);
        internalFrame.setVisible(true);
        
        return internalFrame;
    }
}
