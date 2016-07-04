package edu.njit.cs.saboc.blu.sno.gui.gep.panels.optionbuttons.tan;

import edu.njit.cs.saboc.blu.core.abn.tan.Band;
import edu.njit.cs.saboc.blu.core.abn.tan.Cluster;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.optionbuttons.ExportButton;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.exportabn.ExportAbNUtilities;
import edu.njit.cs.saboc.blu.core.ontology.Concept;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.tan.configuration.SCTTANConfiguration;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import javax.swing.JOptionPane;

/**
 *
 * @author Chris O
 */
public class SCTExportBandButton extends ExportButton {
    
    private Optional<Band> currentBand = Optional.empty();
    
    private final SCTTANConfiguration config;

    public SCTExportBandButton(SCTTANConfiguration config) {
        super("Export Band's Concepts");
        
        this.config = config;
    }
        
    public void setCurrentBand(Band band) {
        currentBand = Optional.ofNullable(band);
    }
    
    @Override
    public void exportAction() {

        if (currentBand.isPresent()) {
            Optional<File> exportFile = ExportAbNUtilities.displayFileSelectDialog();

            if (exportFile.isPresent()) {
                String[] choices = {"Concept ID and Concept Name", "Concept Id, Concept Name, and Cluster"};
                
                String input = (String) JOptionPane.showInputDialog(null, "Select Band Export Option",
                        "Choose an Export Option",
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        choices, 
                        choices[0]);
                
                if(input.equals(choices[0])) {
                    exportBandConcepts(exportFile.get());
                } else if(input.equals(choices[1])) {
                    exportBandClusters(exportFile.get());
                }
            }
        }
    }
    
    private void exportBandConcepts(File file) {
        ArrayList<Concept> concepts = new ArrayList<>(currentBand.get().getConcepts());
        concepts.sort( (a, b) -> {
            return a.getName().compareToIgnoreCase(b.getName());
        });

        try (PrintWriter writer = new PrintWriter(file)) {
            concepts.forEach((Concept c) -> {
                writer.println(String.format("%d\t%s", c.getID(), c.getName()));
            });
        } catch (FileNotFoundException fnfe) {
            
        }
    }
    
    private void exportBandClusters(File file) {
        Set<Cluster> clusters = currentBand.get().getClusters();
        
        try (PrintWriter writer = new PrintWriter(file)) {
            clusters.forEach( (cluster) -> {
                Set<Concept> concepts = cluster.getConcepts();
                
                concepts.forEach( (Concept c) -> {
                    writer.println(String.format("%d\t%s\t%s",
                            c.getID(),
                            c.getName(),
                            String.format("%s (%d)", cluster.getName(), cluster.getConceptCount())));
                });
            });

        } catch (FileNotFoundException fnfe) {
            
        }
    }
}