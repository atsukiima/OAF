package edu.njit.cs.saboc.blu.sno.gui.gep.panels.optionbuttons.pareataxonomy;

import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.PArea;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.optionbuttons.ExportButton;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.exportabn.ExportAbNUtilities;
import edu.njit.cs.saboc.blu.core.ontology.Concept;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Optional;
import java.util.Set;
import javax.swing.JOptionPane;

/**
 *
 * @author Chris O
 */
public class SCTExportPAreaButton extends ExportButton {
    
    private Optional<PArea> currentPArea = Optional.empty();

    public SCTExportPAreaButton() {
        super("Export Partial-area's Concepts");
    }
        
    public void setCurrentPArea(PArea parea) {
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

                Set<Concept> concepts = currentPArea.get().getConcepts();

                try(PrintWriter writer = new PrintWriter(exportFile.get())) {
                    
                    concepts.forEach((Concept c) -> {
                        if (input.equals(choices[0])) {
                            writer.println(String.format("%d\t%s", 
                                    c.getID(), 
                                    c.getName()));
                            
                        } else if (input.equals(choices[1])) {
                            writer.println(c.getName());
                        } else if(input.equals(choices[2])){
                            writer.println(c.getID());
                        } else {
                            
                        }
                    });
                    
                } catch(FileNotFoundException fnfe) {
                    
                }
            }
        }
    }
}
