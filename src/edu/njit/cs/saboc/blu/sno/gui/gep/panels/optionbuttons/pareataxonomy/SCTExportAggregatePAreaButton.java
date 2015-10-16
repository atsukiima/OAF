package edu.njit.cs.saboc.blu.sno.gui.gep.panels.optionbuttons.pareataxonomy;

import SnomedShared.Concept;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.optionbuttons.ExportButton;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTAggregatePArea;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTPArea;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.exportabn.ExportAbNUtilities;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy.configuration.SCTPAreaTaxonomyConfiguration;
import edu.njit.cs.saboc.blu.sno.utils.comparators.ConceptNameComparator;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import javax.swing.JOptionPane;

/**
 *
 * @author Chris O
 */
public class SCTExportAggregatePAreaButton extends ExportButton {
    
    private Optional<SCTAggregatePArea> currentPArea = Optional.empty();
    
    private final SCTPAreaTaxonomyConfiguration config;

    public SCTExportAggregatePAreaButton(SCTPAreaTaxonomyConfiguration config) {
        super("Export Aggregate Partial-area's Concepts");
        
        this.config = config;
    }
        
    public void setCurrentPArea(SCTAggregatePArea parea) {
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
                    exportClassesOnly(exportFile.get());
                } else if(input.equals(choices[1])) {
                    exportReducedPartialAreas(exportFile.get());
                } else {
                    
                }
            }
        }
    }
    
    private void exportClassesOnly(File file) {
        ArrayList<Concept> concepts = new ArrayList<>(currentPArea.get().getAllGroupsConcepts());
        Collections.sort(concepts, new ConceptNameComparator());
        
        try (PrintWriter writer = new PrintWriter(file)) {
            concepts.forEach((Concept c) -> {
                    writer.println(String.format("%d\t%s",
                            c.getId(),
                            c.getName()));
            });

        } catch (FileNotFoundException fnfe) {

        }
    }
    
    private void exportReducedPartialAreas(File file) {
        HashSet<SCTPArea> pareas = currentPArea.get().getAggregatedGroupHierarchy().getNodesInHierarchy();
        
        try (PrintWriter writer = new PrintWriter(file)) {
            pareas.forEach((SCTPArea parea) -> {
                String areaName = config.getTextConfiguration().getGroupsContainerName(parea);
                
                ArrayList<Concept> pareaConcepts = parea.getConceptsInPArea();
                
                pareaConcepts.forEach( (Concept c) -> {
                    writer.println(String.format("%d\t%s\t%s\t%s",
                            c.getId(),
                            c.getName(),
                            String.format("%s (%d)", config.getTextConfiguration().getGroupName(parea), parea.getConceptCount()),
                            areaName));
                });
            });
        } catch (FileNotFoundException fnfe) {

        }
    }
}