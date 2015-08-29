package edu.njit.cs.saboc.blu.sno.gui.gep.panels.optionbuttons;

import SnomedShared.Concept;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.optionbuttons.ExportButton;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTPArea;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Optional;
import javax.swing.JOptionPane;

/**
 *
 * @author Chris O
 */
public class SCTExportPAreaButton extends ExportButton {
    
    private Optional<SCTPArea> currentPArea = Optional.empty();

    public SCTExportPAreaButton() {
        super("Export Partial-area's Concepts");
    }
        
    public void setCurrentPArea(SCTPArea parea) {
        currentPArea = Optional.ofNullable(parea);
    }
    
    @Override
    public void exportAction() {

        if (currentPArea.isPresent()) {
            Optional<File> exportFile = ExportAbNUtilities.displayFileSelectDialog();

            if (exportFile.isPresent()) {
                String[] choices = {"Concept ID and Concept Name", "Concept Name Only", "Concept ID Only"};
                
                String input = (String) JOptionPane.showInputDialog(null, "Select Partial-area Export Option",
                        "Choose an Export Option",
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        choices, 
                        choices[0]);

                ArrayList<Concept> concepts = currentPArea.get().getConceptsInPArea();

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
