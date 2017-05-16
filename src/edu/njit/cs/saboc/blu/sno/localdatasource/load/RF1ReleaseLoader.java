package edu.njit.cs.saboc.blu.sno.localdatasource.load;

import edu.njit.cs.saboc.blu.core.datastructure.hierarchy.Hierarchy;
import edu.njit.cs.saboc.blu.core.utils.toolstate.OAFStateFileManager;
import edu.njit.cs.saboc.blu.sno.localdatasource.concept.Description;
import edu.njit.cs.saboc.blu.sno.localdatasource.concept.AttributeRelationship;
import edu.njit.cs.saboc.blu.sno.localdatasource.concept.SCTStatedConcept;
import edu.njit.cs.saboc.blu.sno.localdatasource.concept.SCTConcept;
import edu.njit.cs.saboc.blu.sno.nat.SCTConceptBrowserDataSource;
import edu.njit.cs.saboc.blu.sno.sctdatasource.SCTRelease;
import edu.njit.cs.saboc.blu.sno.sctdatasource.SCTReleaseInfo;
import edu.njit.cs.saboc.blu.sno.sctdatasource.SCTReleaseWithStated;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Chris
 */
public class RF1ReleaseLoader {
    
    protected LocalLoadStateMonitor loadMonitor = new LocalLoadStateMonitor();
    
    private final OAFStateFileManager stateFileManager;
    
    public RF1ReleaseLoader(OAFStateFileManager stateFileManager) {
        this.stateFileManager = stateFileManager;
    }
    
    public LocalLoadStateMonitor getLoadStateMonitor() {
        return loadMonitor;
    }
        
    //Constructs a series of data structures to hold raw taxonomy data    
    public SCTRelease loadLocalSnomedRelease(
            final File directory, 
            final SCTReleaseInfo releaseInfo, 
            final LocalLoadStateMonitor loadMonitor) throws IOException {
        
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

        final HashMap<Long, SCTConcept> concepts = loadConcepts(conceptsFile, includesStatedRelationships);
        
        loadMonitor.setCurrentProcess("Loading Descriptions", 25);
        
        loadDescriptions(descFile, concepts);

        loadMonitor.setCurrentProcess("Loading Relationships", 50);

        Hierarchy<SCTConcept> hierarchy = loadRelationships(relFile, concepts);
               
        SCTRelease release;
        
        if(includesStatedRelationships) {
            loadMonitor.setCurrentProcess("Loading Stated Relationships", 75);
            
            Hierarchy<SCTConcept> statedHierarchy = loadStatedRelationships(statedRelationshipsFile, concepts);
            
            loadMonitor.setCurrentProcess("Building Search Index", 85);
            
            release = new SCTReleaseWithStated(releaseInfo, hierarchy, new HashSet<>(concepts.values()), statedHierarchy);
        } else {
            loadMonitor.setCurrentProcess("Building Search Index", 75);
            release = new SCTRelease(releaseInfo, hierarchy, new HashSet<>(concepts.values()));
        }
        
        release.setConceptBrowserDataSource(new SCTConceptBrowserDataSource(release, stateFileManager));
        
        loadMonitor.setCurrentProcess("Complete", 100);

        return release;
    }
    
    /**
     * Basically just copy/pasted loadRelationships here.
     * @param relationshipsFile
     * @param concepts
     * @return
     * @throws IOException 
     */
    private Hierarchy<SCTConcept> loadStatedRelationships(File relationshipsFile, HashMap<Long, SCTConcept> concepts) throws IOException {

        Hierarchy<SCTConcept> hierarchy = new Hierarchy<>(concepts.get(138875005l));

        Map<Long, Set<AttributeRelationship>> statedAttributeRelationships = new HashMap<>();

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

                    SCTConcept child = concepts.get(concept1);
                    SCTConcept parent = concepts.get(concept2);

                    hierarchy.addEdge(child, parent);
                } else {
                    long sourceId = Long.parseLong(parts[1]);
                    long targetId = Long.parseLong(parts[3]);

                    if (!statedAttributeRelationships.containsKey(sourceId)) {
                        statedAttributeRelationships.put(sourceId, new HashSet<>());
                    }

                    statedAttributeRelationships.get(sourceId).add(new AttributeRelationship(
                            concepts.get(relType),
                            concepts.get(targetId),
                            Integer.parseInt(parts[6]),
                            Integer.parseInt(parts[4])));
                }

                processedRelationships++;

                double loadProgress = (processedRelationships / 500000.0);

                loadMonitor.setOverallProgress(75 + Math.min((int) (loadProgress * 10), 10));
            }

            ArrayList<AttributeRelationship> emptyArrayList = new ArrayList<>();

            for (SCTConcept concept : concepts.values()) {
                SCTStatedConcept statedConcept = (SCTStatedConcept) concept;

                if (statedAttributeRelationships.containsKey(concept.getID())) {
                    statedConcept.setStatedRelationships(statedAttributeRelationships.get(concept.getID()));
                } else {
                    statedConcept.setStatedRelationships(Collections.emptySet());
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

    private Hierarchy<SCTConcept> loadRelationships(File relationshipsFile, HashMap<Long, SCTConcept> concepts) throws IOException {

        Hierarchy<SCTConcept> hierarchy = new Hierarchy<>(concepts.get(138875005l));

        Map<Long, Set<AttributeRelationship>> attributeRels = new HashMap<>();

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

                    SCTConcept child = concepts.get(concept1);
                    SCTConcept parent = concepts.get(concept2);

                    hierarchy.addEdge(child, parent);
                } else {
                    long sourceId = Long.parseLong(parts[1]);
                    long targetId = Long.parseLong(parts[3]);

                    if (!attributeRels.containsKey(sourceId)) {
                        attributeRels.put(sourceId, new HashSet<>());
                    }

                    attributeRels.get(sourceId).add(new AttributeRelationship(
                            concepts.get(relType),
                            concepts.get(targetId),
                            Integer.parseInt(parts[6]),
                            Integer.parseInt(parts[4])));
                }

                processedRelationships++;

                double loadProgress = (processedRelationships / 1600000.0);

                loadMonitor.setOverallProgress(50 + Math.min((int) (loadProgress * 20), 20));
            }


            for (SCTConcept concept : concepts.values()) {
                if (attributeRels.containsKey(concept.getID())) {
                    concept.setLateralRelationships(attributeRels.get(concept.getID()));
                } else {
                    concept.setLateralRelationships(Collections.emptySet());
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
    
    private HashMap<Long, SCTConcept> loadConcepts(File conceptsFile, boolean statedRelationships) throws FileNotFoundException {

        HashMap<Long, SCTConcept> concepts = new HashMap<>();

        BufferedReader in = null;

        try {
            in = new BufferedReader(new FileReader(conceptsFile));

            String line;

            in.readLine();

            int processedConcepts = 0;

            while ((line = in.readLine()) != null) {
                String[] parts = line.split("\t");
                
                boolean prim = (!parts[5].equals("0"));
                
                boolean active = (parts[1].equals("0"));
                
                long id = Long.parseLong(parts[0]);

                if (statedRelationships) {
                    concepts.put(id, new SCTStatedConcept(id, prim, active));
                } else {
                    concepts.put(id, new SCTConcept(id, prim, active));
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

    private void loadDescriptions(File descriptionsFile, HashMap<Long, SCTConcept> concepts) throws FileNotFoundException {

        HashMap<Long, Set<Description>> descriptions = new HashMap<>();

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
                    descriptions.put(conceptId, new HashSet<>());
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

        for (SCTConcept c : concepts.values()) {
            if (descriptions.containsKey(c.getID())) {
                c.setDescriptions(descriptions.get(c.getID()));
            } else {
                c.setDescriptions(Collections.emptySet());
            }
        }
    }
}
