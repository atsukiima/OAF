/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.njit.cs.saboc.blu.sno.gui.main;

import edu.njit.cs.saboc.blu.core.gui.frame.AbnSelectionFrameFactory;
import edu.njit.cs.saboc.blu.core.gui.frame.OAFMainFrame;
import edu.njit.cs.saboc.blu.sno.gui.abnselection.SCTAbNFrameManager;
import edu.njit.cs.saboc.blu.sno.gui.abnselection.SCTLoaderPanel;
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
        
        JPanel jp = new SCTLoaderPanel(parentFrame,
                new SCTAbNFrameManager(parentFrame, (frame) -> {
                    parentFrame.addInternalFrame(frame);
                }));

        
        jif.add(jp);
        jif.setVisible(true);
        
        return jif;
    }
    
}
