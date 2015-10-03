package edu.njit.cs.saboc.blu.sno.gui.gep.panels.optionbuttons.tan;

import SnomedShared.Concept;
import SnomedShared.overlapping.CommonOverlapSet;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.optionbuttons.ExportButton;
import edu.njit.cs.saboc.blu.sno.abn.tan.local.SCTCluster;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.exportabn.ExportAbNUtilities;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.tan.configuration.SCTTANConfiguration;
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
public class SCTExportBandButton extends ExportButton {
    
    private Optional<CommonOverlapSet> currentBand = Optional.empty();
    
    private final SCTTANConfiguration config;

    public SCTExportBandButton(SCTTANConfiguration config) {
        super("Export Band's Concepts");
        
        this.config = config;
    }
        
    public void setCurrentBand(CommonOverlapSet band) {
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
        
        ArrayList<SCTCluster> bandClusters = config.getDataConfiguration().convertClusterSummaryList(currentBand.get().getAllClusters());
        
        HashSet<Concept> allConcepts = new HashSet<>();
        
        bandClusters.forEach((SCTCluster cluster) -> {
            allConcepts.addAll(cluster.getConcepts());
        });
        
        ArrayList<Concept> concepts = new ArrayList<>(allConcepts);
        Collections.sort(concepts, new ConceptNameComparator());

        try (PrintWriter writer = new PrintWriter(file)) {
            concepts.forEach((Concept c) -> {
                writer.println(String.format("%d\t%s", c.getId(), c.getName()));
            });
        } catch (FileNotFoundException fnfe) {
            
        }
    }
    
    private void exportBandClusters(File file) {
        ArrayList<SCTCluster> clusters = config.getDataConfiguration().convertClusterSummaryList(currentBand.get().getAllClusters());
        
        try (PrintWriter writer = new PrintWriter(file)) {
            clusters.forEach( (SCTCluster cluster) -> {
                HashSet<Concept> concepts = cluster.getConcepts();
                
                concepts.forEach( (Concept c) -> {
                    writer.println(String.format("%d\t%s\t%s",
                            c.getId(),
                            c.getName(),
                            String.format("%s (%d)", config.getTextConfiguration().getGroupName(cluster), cluster.getConceptCount())));
                });
            });

        } catch (FileNotFoundException fnfe) {
            
        }
    }
}