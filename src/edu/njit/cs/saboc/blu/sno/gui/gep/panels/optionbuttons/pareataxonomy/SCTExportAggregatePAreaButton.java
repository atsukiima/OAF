package edu.njit.cs.saboc.blu.sno.gui.gep.panels.optionbuttons.pareataxonomy;

import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.AggregatePArea;
import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.PArea;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.optionbuttons.ExportButton;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.exportabn.ExportAbNUtilities;
import edu.njit.cs.saboc.blu.core.ontology.Concept;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy.configuration.SCTPAreaTaxonomyConfiguration;
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
public class SCTExportAggregatePAreaButton extends ExportButton {
    
    private Optional<AggregatePArea> currentPArea = Optional.empty();
    
    private final SCTPAreaTaxonomyConfiguration config;

    public SCTExportAggregatePAreaButton(SCTPAreaTaxonomyConfiguration config) {
        super("Export Aggregate Partial-area's Concepts");
        
        this.config = config;
    }
        
    public void setCurrentPArea(AggregatePArea parea) {
        currentPArea = Optional.ofNullable(parea);
    }
    
    @Override
    public void exportAction() {

        if (currentPArea.isPresent()) {
            Optional<File> exportFile = ExportAbNUtilities.displayFileSelectDialog();

            if (exportFile.isPresent()) {
                
                String[] choices = {"Concept ID and Concept Name", "Concept ID, Concept Name, and Removed Partial-area"};
                
                String input = (String) JOptionPane.showInputDialog(null, "Select Aggregate Partial-area Export Option",
                        "Choose an Export Option",
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        choices, 
                        choices[0]);

                if(input.equals(choices[0])) {
                    exportConceptsOnly(exportFile.get());
                } else if(input.equals(choices[1])) {
                    exportAggregatedPartialAreas(exportFile.get());
                } else {
                    
                }
            }
        }
    }
    
    private void exportConceptsOnly(File file) {
        ArrayList<Concept> concepts = new ArrayList<>(currentPArea.get().getConcepts());
        concepts.sort( (a, b) -> {
            return a.getName().compareToIgnoreCase(b.getName());
        });
        
        try (PrintWriter writer = new PrintWriter(file)) {
            concepts.forEach((Concept c) -> {
                    writer.println(String.format("%d\t%s",
                            c.getID(),
                            c.getName()));
            });

        } catch (FileNotFoundException fnfe) {

        }
    }
    
    private void exportAggregatedPartialAreas(File file) {
        Set<PArea> pareas = currentPArea.get().getAggregatedNodes();
        
        try (PrintWriter writer = new PrintWriter(file)) {
            pareas.forEach((parea) -> {
                String areaName = parea.getName();
                
                ArrayList<Concept> pareaConcepts = new ArrayList<>(parea.getConcepts());
                pareaConcepts.sort((a, b) -> { 
                    return a.getName().compareToIgnoreCase(b.getName());
                });
                
                pareaConcepts.forEach( (Concept c) -> {
                    writer.println(String.format("%d\t%s\t%s\t%s",
                            c.getID(),
                            c.getName(),
                            String.format("%s (%d)", parea.getName(), parea.getConceptCount()),
                            areaName));
                });
            });
        } catch (FileNotFoundException fnfe) {

        }
    }
}