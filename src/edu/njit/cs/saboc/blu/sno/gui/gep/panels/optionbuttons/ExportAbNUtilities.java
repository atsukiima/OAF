package edu.njit.cs.saboc.blu.sno.gui.gep.panels.optionbuttons;

import java.io.File;
import java.util.Optional;
import javax.swing.JFileChooser;

/**
 *
 * @author Chris O
 */
public class ExportAbNUtilities {
    public static Optional<File> displayFileSelectDialog() {
        final JFileChooser chooser = new JFileChooser();

        int returnVal = chooser.showSaveDialog(null);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();

            if (!file.isDirectory()) {
                return Optional.of(file);
            }
        }
        
        return Optional.empty();
    }
}
