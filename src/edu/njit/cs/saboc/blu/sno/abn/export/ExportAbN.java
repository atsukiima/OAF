package edu.njit.cs.saboc.blu.sno.abn.export;

import SnomedShared.Concept;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import javax.swing.JFileChooser;

/**
 *
 * @author Chris
 */
public class ExportAbN {

    private static File displayFileSelectDialog() {
        final TaxonomyFileChooser chooser = new TaxonomyFileChooser();

        int returnVal = chooser.showSaveDialog(null);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();

            if (!file.isDirectory()) {
                return file;
            }
        }
        
        return null;
    }

    public static void exportAbNGroups(HashMap<Long, ArrayList<Concept>> groupMembers, String type) {
        File file = displayFileSelectDialog();
        
        if(file == null ) {
            // TODO: Report error to user
            
            return;
        }
        
        if(!file.getName().endsWith(".csv")) {
            file = new File(file.getAbsolutePath() + ".csv");
        }
        
        if(file == null) {
            return;
        }
        
        try { 
            PrintWriter writer = new PrintWriter(file);
            
            writer.println(String.format("%s\t%s", type.toUpperCase() + "ROOTCONCEPTID", "CONCEPTID"));
            
            for(Entry<Long, ArrayList<Concept>> group : groupMembers.entrySet()) {
                for(Concept c : group.getValue()) {
                    writer.println(String.format("%d\t%d", group.getKey(), c.getId()));
                }
            }
            
            writer.close();
            
        } catch(FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        }
    }
}
