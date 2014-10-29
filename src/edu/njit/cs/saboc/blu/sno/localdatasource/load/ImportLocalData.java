/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.njit.cs.saboc.blu.sno.localdatasource.load;

import edu.njit.cs.saboc.blu.sno.datastructure.hierarchy.SCTConceptHierarchy;
import edu.njit.cs.saboc.blu.sno.localdatasource.concept.Description;
import edu.njit.cs.saboc.blu.sno.localdatasource.concept.LocalLateralRelationship;
import edu.njit.cs.saboc.blu.sno.localdatasource.concept.LocalSnomedConcept;
import edu.njit.cs.saboc.blu.sno.sctdatasource.SCTLocalDataSource;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 *
 * @author Chris
 */
public class ImportLocalData {
    
    private LocalLoadStateMonitor loadMonitor = new LocalLoadStateMonitor();
    
    public LocalLoadStateMonitor getLoadStateMonitor() {
        return loadMonitor;
    }
        
    //Constructs a series of data structures to hold raw taxonomy data    
    public SCTLocalDataSource loadLocalSnomedRelease(final File directory, final String version, final LocalLoadStateMonitor loadMonitor) throws IOException {
        File releaseDirectory = directory;
        
        File conceptsFile = null;
        File descFile = null;
        File relFile = null;
        
        for (File child : releaseDirectory.listFiles()) {
            if (child.getName().contains("Concepts")) {
                conceptsFile = child;
            } else if(child.getName().contains("Descriptions")) {
                descFile = child;
            } else if(child.getName().contains("Relationships")) {
                relFile = child;
            }
        }
        
        long startingUsedMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        
        loadMonitor.setCurrentProcess("Loading Concepts", 0);

        final HashMap<Long, LocalSnomedConcept> concepts = loadConcepts(conceptsFile);
        
        System.gc();
        
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
            
        }
        
        long conceptsMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        
        System.out.println("Concepts Memory: " + (conceptsMemory - startingUsedMemory));

        loadMonitor.setCurrentProcess("Loading Descriptions", 25);
        
        loadDescriptions(descFile, concepts);
        
        System.gc();
        
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
            
        }
        
        long descriptionMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        
        System.out.println("Descriptions Memory: " + (descriptionMemory - conceptsMemory));

        loadMonitor.setCurrentProcess("Loading Relationships", 50);

        SCTConceptHierarchy hierarchy = loadRelationships(relFile, concepts);

        System.gc();

        try {
            Thread.sleep(1000);
        } catch (Exception e) {

        }

        long relationshipsMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();

        System.out.println("Relationships Memory: " + (relationshipsMemory - descriptionMemory));

        loadMonitor.setCurrentProcess("Building Search Index", 75);
        
        SCTLocalDataSource localDS = new SCTLocalDataSource(concepts, hierarchy, true, version);
        
        System.gc();

        try {
            Thread.sleep(1000);
        } catch (Exception e) {

        }

        long indexMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();

        System.out.println("Index Memory: " + (indexMemory - relationshipsMemory));

        loadMonitor.setCurrentProcess("Complete", 100);

        return localDS;
    }

    private SCTConceptHierarchy loadRelationships (
            File relationshipsFile, HashMap<Long, LocalSnomedConcept> concepts) throws IOException {

        Scanner scanner = new Scanner(relationshipsFile);

        if (scanner.hasNext()) {
            scanner.nextLine();
        }
        
        SCTConceptHierarchy hierarchy = new SCTConceptHierarchy(concepts.get(138875005l));
        
        Map<Long, ArrayList<LocalLateralRelationship>> attributeRels
                = new HashMap<Long, ArrayList<LocalLateralRelationship>>();
        
        int processedRelationships = 0;

        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            String[] parts = line.split("\t");

            long relType = Long.parseLong(parts[2]);

            if (relType == 116680003l) {
                long concept1 = Long.parseLong(parts[1]);
                long concept2 = Long.parseLong(parts[3]);
                
                LocalSnomedConcept child = concepts.get(concept1);
                LocalSnomedConcept parent = concepts.get(concept2);
                
                hierarchy.addIsA(child, parent);
            } else {
                long sourceId = Long.parseLong(parts[1]);
                long targetId = Long.parseLong(parts[3]);

                if (!attributeRels.containsKey(sourceId)) {
                    attributeRels.put(sourceId, new ArrayList<LocalLateralRelationship>());
                }

                attributeRels.get(sourceId).add(new LocalLateralRelationship(
                        concepts.get(relType),
                        concepts.get(targetId),
                        Integer.parseInt(parts[6]),
                        Integer.parseInt(parts[4])));
            }
            
            processedRelationships++;
            
            double loadProgress = (processedRelationships / 1600000.0);
            
            loadMonitor.setOverallProgress(50 + Math.min((int)(loadProgress * 20), 20));
        }
        
        for(LocalSnomedConcept concept : concepts.values()) {
            if(attributeRels.containsKey(concept.getId())) {
                concept.setRels(attributeRels.get(concept.getId()));
            } else {
                concept.setRels(new ArrayList<LocalLateralRelationship>());
            }
        }

        scanner.close();

        return hierarchy;
    }
    
    private HashMap<Long, LocalSnomedConcept> loadConcepts(File conceptsFile) throws FileNotFoundException {
        Scanner scanner = new Scanner(conceptsFile);
        
        if (scanner.hasNext()) {
            scanner.nextLine();
        }
        
        HashMap<Long, LocalSnomedConcept> concepts = new HashMap<Long, LocalSnomedConcept>();
        
        int processedConcepts = 0;
        
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            
            String[] parts = line.split("\t");
            boolean prim = false;
            long id = Long.parseLong(parts[0]);
            
            String fsn = parts[2];
            
            if (!parts[5].equals("0")) {
                prim = true;
            }
            
            concepts.put(id, new LocalSnomedConcept(id, null, prim));
            
            processedConcepts++;
            
            double loadProgress = (processedConcepts / 450000.0);
            
            loadMonitor.setOverallProgress(Math.min((int)(loadProgress * 25), 25));
        }
        
        return concepts;
    }

    private void loadDescriptions(File descriptionsFile, 
            HashMap<Long, LocalSnomedConcept> concepts) throws FileNotFoundException {
        
        HashMap<Long, ArrayList<Description>> descriptions = new HashMap<Long, ArrayList<Description>>();
        
        Scanner scanner = new Scanner(descriptionsFile);
        
        if (scanner.hasNext()) {
            scanner.nextLine();
        }
        
        int processedDescriptions = 0;
        
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            String[] parts = line.split("\t");
            
            long conceptId = Long.parseLong(parts[2]);
            
            Description d = new Description(parts[3], Integer.parseInt(parts[5]));
            
            if (descriptions.containsKey(conceptId)) {
                descriptions.get(conceptId).add(d);
            } else {
                descriptions.put(conceptId, new ArrayList<Description>());
                descriptions.get(conceptId).add(d);
            }
            
            processedDescriptions++;
            
            double loadProgress = (processedDescriptions / 1200000.0);
            
            loadMonitor.setOverallProgress(25 + Math.min((int)(loadProgress * 20), 20));
        }
        
        for(LocalSnomedConcept c : concepts.values()) {
            if(descriptions.containsKey(c.getId())) {
                c.setDescriptions(descriptions.get(c.getId()));
            } else {
                c.setDescriptions(new ArrayList<Description>());
            }
        }
    }
}
