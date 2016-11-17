/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.njit.cs.saboc.blu.sno.gui.main;

import edu.njit.cs.saboc.blu.core.gui.frame.AbnSelectionFrameFactory;
import edu.njit.cs.saboc.blu.core.gui.frame.BLUFrame;
import edu.njit.cs.saboc.blu.sno.gui.abnselection.SCTDisplayFrameListener;
import edu.njit.cs.saboc.blu.sno.gui.abnselection.SCTLoaderPanel;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;

/**
 *
 * @author hl395
 */
public class SCTSelectionFrame implements AbnSelectionFrameFactory{

    @Override
    public JInternalFrame returnSelectionFrame(BLUFrame jFrame) {
        JInternalFrame jif = new JInternalFrame();
        jif.setSize(1400, 700);
        JPanel jp= new SCTLoaderPanel(jFrame, new SCTDisplayFrameListener(jFrame) {
            
            @Override
            public void displayFrame(JInternalFrame frame) {
                jFrame.add(frame);
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });
        jif.add(jp);
        jif.setVisible(true);
        return jif;
    }


    
}
