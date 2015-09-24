
package edu.njit.cs.saboc.blu.sno.gui.gep.panels.optionbuttons.pareataxonomy.disjoint;

import SnomedShared.Concept;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.optionbuttons.ExportButton;
import edu.njit.cs.saboc.blu.sno.abn.disjointpareataxonomy.DisjointPartialArea;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.SCTPAreaTaxonomyConfiguration;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.exportabn.ExportAbNUtilities;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Optional;
import javax.swing.JOptionPane;

/**
 *
 * @author 
 */
public class SCTExportDisjointPAreaButton extends ExportButton {
    
    private Optional<DisjointPartialArea> currentDisjointPArea = Optional.empty();
    
    private final SCTPAreaTaxonomyConfiguration config;

    public SCTExportDisjointPAreaButton(SCTPAreaTaxonomyConfiguration config) {
        super("Export Disjoint Partial-area's Classes");
        
        this.config = config;
    }
        
    public void setCurrentPArea(DisjointPartialArea parea) {
        currentDisjointPArea = Optional.ofNullable(parea);
    }
    
    @Override
    public void exportAction() {

        if (currentDisjointPArea.isPresent()) {
            Optional<File> exportFile = ExportAbNUtilities.displayFileSelectDialog();

            if (exportFile.isPresent()) {
                String[] choices = {"Concept ID and Concept Name", "Concept Name Only", "Concept ID Only"};
                
                String input = (String) JOptionPane.showInputDialog(null, "Select Partial-area Export Option",
                        "Choose an Export Option",
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        choices, 
                        choices[0]);

                ArrayList<Concept> concepts = currentDisjointPArea.get().getConceptsAsList();

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