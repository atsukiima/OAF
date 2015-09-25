package edu.njit.cs.saboc.blu.sno.gui.gep.panels.optionbuttons.tan;

import SnomedShared.Concept;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.optionbuttons.ExportButton;
import edu.njit.cs.saboc.blu.sno.abn.tan.local.SCTCluster;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.exportabn.ExportAbNUtilities;
import edu.njit.cs.saboc.blu.sno.utils.comparators.ConceptNameComparator;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;
import javax.swing.JOptionPane;

/**
 *
 * @author Chris O
 */
public class SCTExportClusterButton extends ExportButton {
    
    private Optional<SCTCluster> currentCluster = Optional.empty();

    public SCTExportClusterButton() {
        super("Export Cluster's Concepts");
    }
        
    public void setCurrentCluster(SCTCluster cluster) {
        currentCluster = Optional.ofNullable(cluster);
    }
    
    @Override
    public void exportAction() {

        if (currentCluster.isPresent()) {
            Optional<File> exportFile = ExportAbNUtilities.displayFileSelectDialog();

            if (exportFile.isPresent()) {
                String[] choices = {"Concept ID and Concept Name", "Concept Name Only", "Concept ID Only"};
                
                String input = (String) JOptionPane.showInputDialog(null, "Select Cluster Export Option",
                        "Choose an Export Option",
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        choices, 
                        choices[0]);

                ArrayList<Concept> concepts = new ArrayList<>(currentCluster.get().getConcepts());
                Collections.sort(concepts, new ConceptNameComparator());

                try(PrintWriter writer = new PrintWriter(exportFile.get())) {
                    
                    concepts.forEach((Concept c) -> {
                        if (input.equals(choices[0])) {
                            writer.println(String.format("%d\t%s", 
                                    c.getId(), 
                                    c.getName()));
                            
                        } else if (input.equals(choices[1])) {
                            writer.println(c.getName());
                        } else if(input.equals(choices[2])){
                            writer.println(c.getId());
                        } else {
                            
                        }
                    });
                    
                } catch(FileNotFoundException fnfe) {
                    
                }
            }
        }
    }
}
