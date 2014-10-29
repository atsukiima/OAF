package edu.njit.cs.saboc.blu.sno.abn.export;

import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

/**
 *
 * @author Den
 */
public class TaxonomyFileChooser extends JFileChooser {

    public TaxonomyFileChooser() {
        this.setFileFilter(new FileFilter() {
            public boolean accept(File f) {
                if (f.isDirectory()) {
                    return true;
                }
                
                if (f.getName().endsWith(".csv")) {
                    return true;
                } else {
                    return false;
                }
            }
            
            public String getDescription() {
                return "BLUSNO Taxonomy Tab-delimited CSV (.csv)";
            }
        });
    }
}
