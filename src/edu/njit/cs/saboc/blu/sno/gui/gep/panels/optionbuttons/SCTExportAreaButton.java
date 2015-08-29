
package edu.njit.cs.saboc.blu.sno.gui.gep.panels.optionbuttons;

import SnomedShared.Concept;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.optionbuttons.ExportButton;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTArea;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTPArea;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.SCTPAreaTaxonomyConfiguration;
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
public class SCTExportAreaButton extends ExportButton {
    
    private Optional<SCTArea> currentArea = Optional.empty();
    
    private final SCTPAreaTaxonomyConfiguration config;

    public SCTExportAreaButton(SCTPAreaTaxonomyConfiguration config) {
        super("Export Area's Concepts");
        
        this.config = config;
    }
        
    public void setCurrentArea(SCTArea area) {
        currentArea = Optional.ofNullable(area);
    }
    
    @Override
    public void exportAction() {

        if (currentArea.isPresent()) {
            Optional<File> exportFile = ExportAbNUtilities.displayFileSelectDialog();

            if (exportFile.isPresent()) {
                String[] choices = {"Concept ID and Concept Name", "Concept Id, Concept Name, and Partial-area"};
                
                String input = (String) JOptionPane.showInputDialog(null, "Select Area Export Option",
                        "Choose an Export Option",
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        choices, 
                        choices[0]);
                
                if(input.equals(choices[0])) {
                    exportAreaConcepts(exportFile.get());
                } else if(input.equals(choices[1])) {
                    exportAreaPartialAreas(exportFile.get());
                }
            }
        }
    }
    
    private void exportAreaConcepts(File file) {
        
        ArrayList<Concept> concepts = new ArrayList<>(currentArea.get().getConcepts());
        Collections.sort(concepts, new ConceptNameComparator());

        try (PrintWriter writer = new PrintWriter(file)) {
            concepts.forEach((Concept c) -> {
                writer.println(String.format("%d\t%s", c.getId(), c.getName()));
            });
        } catch (FileNotFoundException fnfe) {
            
        }
    }
    
    private void exportAreaPartialAreas(File file) {
        ArrayList<SCTPArea> pareas = currentArea.get().getAllPAreas();
        
        try (PrintWriter writer = new PrintWriter(file)) {
            pareas.forEach( (SCTPArea parea) -> {
                ArrayList<Concept> concepts = parea.getConceptsInPArea();
                
                concepts.forEach( (Concept c) -> {
                    writer.println(String.format("%d\t%s\t%s",
                            c.getId(),
                            c.getName(),
                            String.format("%s (%d)", config.getGroupName(parea), parea.getConceptCount())));
                });
            });

        } catch (FileNotFoundException fnfe) {
            
        }
    }
}
