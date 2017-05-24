package edu.njit.cs.saboc.blu.sno.gui.main;

import edu.njit.cs.saboc.blu.core.gui.frame.OAFMainFrame;

/**
 *
 * @author hl395
 */
public class Main {

    public static void main(String[] args) {
        
        if(args.length > 0 && args[0].equals("-natOnly")) {
            NATOnlyMainFrame frame = new NATOnlyMainFrame();
            
        } else {
            OAFMainFrame frame = new OAFMainFrame(new SCTSelectionFrame());
        }
        
    }  
}
