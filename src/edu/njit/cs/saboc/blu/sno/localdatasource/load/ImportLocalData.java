/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.njit.cs.saboc.blu.sno.localdatasource.load;

import edu.njit.cs.saboc.blu.sno.datastructure.hierarchy.SCTConceptHierarchy;
import edu.njit.cs.saboc.blu.sno.localdatasource.concept.Description;
import edu.njit.cs.saboc.blu.sno.localdatasource.concept.LocalLateralRelationship;
import edu.njit.cs.saboc.blu.sno.localdatasource.concept.LocalSCTConceptStated;
import edu.njit.cs.saboc.blu.sno.localdatasource.concept.LocalSnomedConcept;
import edu.njit.cs.saboc.blu.sno.sctdatasource.SCTLocalDataSource;
import edu.njit.cs.saboc.blu.sno.sctdatasource.SCTLocalDataSourceWithStated;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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
            if (child.getName().toLowerCase().contains("concepts")) {
                conceptsFile = child;
            } else if(child.getName().toLowerCase().contains("descriptions")) {
                descFile = child;
            } else if(child.getName().toLowerCase().contains("relationships")) {
                relFile = child;
            }
        }
        
        File statedRelationshipsFile = LoadLocalRelease.getStatedRelationshipsFile(releaseDirectory);
        
        boolean includesStatedRelationships = (statedRelationshipsFile != null);
               
        loadMonitor.setCurrentProcess("Loading Concepts", 0);

        final HashMap<Long, LocalSnomedConcept> concepts = loadConcepts(conceptsFile, includesStatedRelationships);
        
        loadMonitor.setCurrentProcess("Loading Descriptions", 25);
       
        
        loadDescriptions(descFile, concepts);

        loadMonitor.setCurrentProcess("Loading Relationships", 50);

        SCTConceptHierarchy hierarchy = loadRelationships(relFile, concepts);
               
        SCTLocalDataSource localDS;
        
        if(includesStatedRelationships) {
            loadMonitor.setCurrentProcess("Loading Stated Relationships", 75);
            
            SCTConceptHierarchy statedHierarchy = loadStatedRelationships(statedRelationshipsFile, concepts);
            
            loadMonitor.setCurrentProcess("Building Search Index", 85);
            
            localDS = new SCTLocalDataSourceWithStated(concepts, hierarchy, true, version, statedHierarchy);
        } else {
            
            loadMonitor.setCurrentProcess("Building Search Index", 75);
            localDS = new SCTLocalDataSource(concepts, hierarchy, true, version);
        }
        
        loadMonitor.setCurrentProcess("Complete", 100);

        return localDS;
    }
    
    /**
     * Basically just copy/pasted loadRelationships here.
     * @param relationshipsFile
     * @param concepts
     * @return
     * @throws IOException 
     */
    private SCTConceptHierarchy loadStatedRelationships(File relationshipsFile, HashMap<Long, LocalSnomedConcept> concepts) throws IOException {

        SCTConceptHierarchy hierarchy = new SCTConceptHierarchy(concepts.get(138875005l));

        Map<Long, ArrayList<LocalLateralRelationship>> statedAttributeRelationships
                = new HashMap<Long, ArrayList<LocalLateralRelationship>>();

        BufferedReader in = null;

        try {
            in = new BufferedReader(new FileReader(relationshipsFile));

            String line;

            in.readLine();

            int processedRelationships = 0;

            while ((line = in.readLine()) != null) {

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

                    if (!statedAttributeRelationships.containsKey(sourceId)) {
                        statedAttributeRelationships.put(sourceId, new ArrayList<LocalLateralRelationship>());
                    }

                    statedAttributeRelationships.get(sourceId).add(new LocalLateralRelationship(
                            concepts.get(relType),
                            concepts.get(targetId),
                            Integer.parseInt(parts[6]),
                            Integer.parseInt(parts[4])));
                }

                processedRelationships++;

                double loadProgress = (processedRelationships / 500000.0);

                loadMonitor.setOverallProgress(75 + Math.min((int) (loadProgress * 10), 10));
            }

            ArrayList<LocalLateralRelationship> emptyArrayList = new ArrayList<LocalLateralRelationship>();

            for (LocalSnomedConcept concept : concepts.values()) {
                LocalSCTConceptStated statedConcept = (LocalSCTConceptStated) concept;

                if (statedAttributeRelationships.containsKey(concept.getId())) {
                    statedConcept.setStatedRelationships(statedAttributeRelationships.get(concept.getId()));
                } else {
                    statedConcept.setStatedRelationships(emptyArrayList);
                }
            }

        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
        }

        return hierarchy;
    }

    private SCTConceptHierarchy loadRelationships(File relationshipsFile, HashMap<Long, LocalSnomedConcept> concepts) throws IOException {

        SCTConceptHierarchy hierarchy = new SCTConceptHierarchy(concepts.get(138875005l));

        Map<Long, ArrayList<LocalLateralRelationship>> attributeRels
                = new HashMap<Long, ArrayList<LocalLateralRelationship>>();

        BufferedReader in = null;
        
        try {
            in = new BufferedReader(new FileReader(relationshipsFile));

            String line;

            in.readLine();

            int processedRelationships = 0;

            while ((line = in.readLine()) != null) {
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

                loadMonitor.setOverallProgress(50 + Math.min((int) (loadProgress * 20), 20));
            }

            ArrayList<LocalLateralRelationship> emptyArrayList = new ArrayList<LocalLateralRelationship>();

            for (LocalSnomedConcept concept : concepts.values()) {
                if (attributeRels.containsKey(concept.getId())) {
                    concept.setLateralRelationships(attributeRels.get(concept.getId()));
                } else {
                    concept.setLateralRelationships(emptyArrayList);
                }
            }

            System.out.println("PROCESSED RELS: " + processedRelationships);

        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
        }
        
        return hierarchy;
    }
    
    private HashMap<Long, LocalSnomedConcept> loadConcepts(File conceptsFile, boolean statedRelationships) throws FileNotFoundException {

        HashMap<Long, LocalSnomedConcept> concepts = new HashMap<Long, LocalSnomedConcept>();

        BufferedReader in = null;

        try {
            in = new BufferedReader(new FileReader(conceptsFile));

            String line;

            in.readLine();

            int processedConcepts = 0;

            while ((line = in.readLine()) != null) {
                String[] parts = line.split("\t");
                boolean prim = false;
                long id = Long.parseLong(parts[0]);

                if (!parts[5].equals("0")) {
                    prim = true;
                }

                if (statedRelationships) {
                    concepts.put(id, new LocalSCTConceptStated(id, null, prim));
                } else {
                    concepts.put(id, new LocalSnomedConcept(id, null, prim));
                }

                processedConcepts++;

                double loadProgress = (processedConcepts / 450000.0);

                loadMonitor.setOverallProgress(Math.min((int) (loadProgress * 25), 25));
            }

            System.out.println("PROCESSED CONCEPTS: " + processedConcepts);

        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
        }

        return concepts;
    }

    private void loadDescriptions(File descriptionsFile,
            HashMap<Long, LocalSnomedConcept> concepts) throws FileNotFoundException {

        HashMap<Long, ArrayList<Description>> descriptions = new HashMap<Long, ArrayList<Description>>();

        BufferedReader in = null;

        try {
            in = new BufferedReader(new FileReader(descriptionsFile));

            String line;

            in.readLine();

            int processedDescriptions = 0;

            while ((line = in.readLine()) != null) {

                String[] parts = line.split("\t");

                if (parts.length < 6) {
                    System.err.println(processedDescriptions + "\t" + Runtime.getRuntime().freeMemory() + "\t" + line);
                    System.err.println(Arrays.toString(parts));
                    System.err.println();

                    continue;
                }

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

                loadMonitor.setOverallProgress(25 + Math.min((int) (loadProgress * 20), 20));
            }

            System.out.println("PROCESSED DESCRIPTIONS: " + processedDescriptions);

        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
        }

        for (LocalSnomedConcept c : concepts.values()) {
            if (descriptions.containsKey(c.getId())) {
                c.setDescriptions(descriptions.get(c.getId()));
            } else {
                c.setDescriptions(new ArrayList<Description>());
            }
        }
    }
}
