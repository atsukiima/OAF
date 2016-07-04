
package edu.njit.cs.saboc.blu.sno.gui.gep.panels.optionbuttons.pareataxonomy;

import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.Area;
import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.PArea;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.optionbuttons.ExportButton;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.exportabn.ExportAbNUtilities;
import edu.njit.cs.saboc.blu.core.ontology.Concept;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy.configuration.SCTPAreaTaxonomyConfiguration;
import edu.njit.cs.saboc.blu.sno.localdatasource.concept.SCTConcept;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Set;
import javax.swing.JOptionPane;

/**
 *
 * @author Chris O
 */
public class SCTExportAreaButton extends ExportButton {
    
    private Optional<Area> currentArea = Optional.empty();
    
    private final SCTPAreaTaxonomyConfiguration config;

    public SCTExportAreaButton(SCTPAreaTaxonomyConfiguration config) {
        super("Export Area's Concepts");
        
        this.config = config;
    }
        
    public void setCurrentArea(Area area) {
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
        concepts.sort((a,b) -> {
            return a.getName().compareToIgnoreCase(b.getName());
        });

        try (PrintWriter writer = new PrintWriter(file)) {
            concepts.forEach((c) -> {
                writer.println(String.format("%d\t%s", c.getID(), c.getName()));
            });
        } catch (FileNotFoundException fnfe) {
            
        }
    }
    
    private void exportAreaPartialAreas(File file) {
        Set<PArea> pareas = currentArea.get().getPAreas();
        
        try (PrintWriter writer = new PrintWriter(file)) {
            pareas.forEach( (parea) -> {
                ArrayList<Concept> concepts = new ArrayList<>(parea.getConcepts());
                concepts.sort((a,b) -> {
                    return a.getName().compareToIgnoreCase(b.getName());
                });
                
                concepts.forEach( (c) -> {
                    writer.println(String.format("%d\t%s\t%s",
                            c.getID(),
                            c.getName(),
                            String.format("%s (%d)", parea.getName(), parea.getConceptCount())));
                });
            });

        } catch (FileNotFoundException fnfe) {
            
        }
    }
}
