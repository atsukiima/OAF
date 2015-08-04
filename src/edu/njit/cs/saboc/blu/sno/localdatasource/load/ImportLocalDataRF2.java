package edu.njit.cs.saboc.blu.sno.localdatasource.load;

import edu.njit.cs.saboc.blu.sno.datastructure.hierarchy.SCTConceptHierarchy;
import edu.njit.cs.saboc.blu.sno.localdatasource.concept.Description;
import edu.njit.cs.saboc.blu.sno.localdatasource.concept.LocalLateralRelationship;
import edu.njit.cs.saboc.blu.sno.localdatasource.concept.LocalSCTConceptStated;
import edu.njit.cs.saboc.blu.sno.localdatasource.concept.LocalSnomedConcept;
import edu.njit.cs.saboc.blu.sno.sctdatasource.SCTLocalDataSourceWithStated;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Chris O
 */
public class ImportLocalDataRF2 {
    
    protected LocalLoadStateMonitor loadMonitor = new LocalLoadStateMonitor();

    public LocalLoadStateMonitor getLoadStateMonitor() {
        return loadMonitor;
    }

    public SCTLocalDataSourceWithStated loadLocalSnomedRelease(final File directory, final String version, final LocalLoadStateMonitor loadMonitor) throws IOException {
        File releaseDirectory = directory;
        
        File conceptsFile = null;
        File descFile = null;
        File relFile = null;
        File statedRelFile = null;
        
        for (File child : releaseDirectory.listFiles()) {
            if (child.getName().toLowerCase().contains("sct2_concept")) {
                conceptsFile = child;
            } else if(child.getName().toLowerCase().contains("sct2_description")) {
                descFile = child;
            } else if(child.getName().toLowerCase().contains("sct2_relationship")) {
                relFile = child;
            } else if(child.getName().toLowerCase().contains("sct2_statedrelationship")) {
                statedRelFile = child;
            }
        }
               
        loadMonitor.setCurrentProcess("Loading Concepts", 0);

        final HashMap<Long, LocalSnomedConcept> concepts = loadConcepts(conceptsFile);
        
        loadMonitor.setCurrentProcess("Loading Descriptions", 25);
       
        loadDescriptions(descFile, concepts);

        loadMonitor.setCurrentProcess("Loading Relationships", 50);

        SCTConceptHierarchy hierarchy = loadRelationships(relFile, concepts);

        loadMonitor.setCurrentProcess("Loading Stated Relationships", 75);

        SCTConceptHierarchy statedHierarchy = loadStatedRelationships(statedRelFile, concepts);

        loadMonitor.setCurrentProcess("Building Search Index", 85);
        
        SCTLocalDataSourceWithStated localDS = new SCTLocalDataSourceWithStated(concepts, hierarchy, true, version, statedHierarchy);

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

        Map<Long, ArrayList<LocalLateralRelationship>> statedAttributeRelationships = new HashMap<>();

        try(BufferedReader in = new BufferedReader(new FileReader(relationshipsFile))) {

            String line;

            in.readLine();
            
            int processedRelationships = 0;

            while ((line = in.readLine()) != null) {
                String[] parts = line.split("\t");
                
                int active = Integer.parseInt(parts[2]);
                
                if(active == 0) {
                    continue;
                }

                long relType = Long.parseLong(parts[7]);
                
                long sourceId = Long.parseLong(parts[4]);
                long targetId = Long.parseLong(parts[5]);

                if (relType == 116680003l) {
                    LocalSnomedConcept child = concepts.get(sourceId);
                    LocalSnomedConcept parent = concepts.get(targetId);

                    hierarchy.addIsA(child, parent);
                } else {

                    if (!statedAttributeRelationships.containsKey(sourceId)) {
                        statedAttributeRelationships.put(sourceId, new ArrayList<>());
                    }
                    
                    int relationshipGroup = Integer.parseInt(parts[6]);
                    
                    int relCharacteristicType = 1;
                    
                    if(Long.parseLong(parts[8]) == 900000000000010007l) {
                        relCharacteristicType = 0;
                    }

                    statedAttributeRelationships.get(sourceId).add(new LocalLateralRelationship(
                            concepts.get(relType),
                            concepts.get(targetId),
                            relationshipGroup,
                            relCharacteristicType));
                }

                processedRelationships++;

                double loadProgress = (processedRelationships / 1600000.0);

                loadMonitor.setOverallProgress(50 + Math.min((int) (loadProgress * 20), 20));
            }

            ArrayList<LocalLateralRelationship> emptyArrayList = new ArrayList<>();

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
        }

        return hierarchy;
    }

    private SCTConceptHierarchy loadRelationships(File relationshipsFile, HashMap<Long, LocalSnomedConcept> concepts) throws IOException {

        SCTConceptHierarchy hierarchy = new SCTConceptHierarchy(concepts.get(138875005l));

        Map<Long, ArrayList<LocalLateralRelationship>> attributeRels = new HashMap<>();

        try(BufferedReader in = new BufferedReader(new FileReader(relationshipsFile))) {

            String line;

            in.readLine();
            
            int processedRelationships = 0;

            while ((line = in.readLine()) != null) {
                String[] parts = line.split("\t");
                
                int active = Integer.parseInt(parts[2]);
                
                if(active == 0) {
                    continue;
                }

                long relType = Long.parseLong(parts[7]);
                
                long sourceId = Long.parseLong(parts[4]);
                long targetId = Long.parseLong(parts[5]);

                if (relType == 116680003l) {
                    LocalSnomedConcept child = concepts.get(sourceId);
                    LocalSnomedConcept parent = concepts.get(targetId);

                    hierarchy.addIsA(child, parent);
                } else {

                    if (!attributeRels.containsKey(sourceId)) {
                        attributeRels.put(sourceId, new ArrayList<>());
                    }
                    
                    int relationshipGroup = Integer.parseInt(parts[6]);
                    
                    int relCharacteristicType = 1;
                    
                    if(Long.parseLong(parts[8]) == 900000000000011006l) {
                        relCharacteristicType = 0;
                    }

                    attributeRels.get(sourceId).add(new LocalLateralRelationship(
                            concepts.get(relType),
                            concepts.get(targetId),
                            relationshipGroup,
                            relCharacteristicType));
                }

                processedRelationships++;

                double loadProgress = (processedRelationships / 1600000.0);

                loadMonitor.setOverallProgress(50 + Math.min((int) (loadProgress * 20), 20));
            }

            ArrayList<LocalLateralRelationship> emptyArrayList = new ArrayList<>();

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
        }
        
        return hierarchy;
    }
    
    private HashMap<Long, LocalSnomedConcept> loadConcepts(File conceptsFile) throws FileNotFoundException {

        HashMap<Long, LocalSnomedConcept> concepts = new HashMap<>();

        try(BufferedReader in = new BufferedReader(new FileReader(conceptsFile))) {
            
            in.readLine();
 
            String line;

            int processedConcepts = 0;

            while ((line = in.readLine()) != null) {
                String[] parts = line.split("\t");

                long id = Long.parseLong(parts[0]);
                
                boolean active = (parts[2].equals("0"));
                
                boolean primative = (parts[4].equals("900000000000074008"));

                concepts.put(id, new LocalSCTConceptStated(id, primative, active));

                processedConcepts++;

                double loadProgress = (processedConcepts / 450000.0);

                loadMonitor.setOverallProgress(Math.min((int) (loadProgress * 25), 25));
            }

            System.out.println("PROCESSED CONCEPTS: " + processedConcepts);

        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        return concepts;
    }

    private void loadDescriptions(File descriptionsFile,
            HashMap<Long, LocalSnomedConcept> concepts) throws FileNotFoundException {

        HashMap<Long, ArrayList<Description>> descriptions = new HashMap<Long, ArrayList<Description>>();

        try(BufferedReader in = new BufferedReader(new FileReader(descriptionsFile))) {

            String line;

            in.readLine();

            int processedDescriptions = 0;

            while ((line = in.readLine()) != null) {

                String[] parts = line.split("\t");
                
                int active = Integer.parseInt(parts[2]);
                
                if(active == 0) {
                    continue;
                }

                long conceptId = Long.parseLong(parts[4]);
                
                long descType = Long.parseLong(parts[6]);
                
                int rf1DescType;
                
                if(descType == 900000000000003001l) {
                    rf1DescType = 3;
                } else {
                    rf1DescType = 0;
                }
                
                Description d = new Description(parts[7], rf1DescType);

                if (descriptions.containsKey(conceptId)) {
                    descriptions.get(conceptId).add(d);
                } else {
                    descriptions.put(conceptId, new ArrayList<>());
                    descriptions.get(conceptId).add(d);
                }

                processedDescriptions++;

                double loadProgress = (processedDescriptions / 1200000.0);

                loadMonitor.setOverallProgress(25 + Math.min((int) (loadProgress * 20), 20));
            }

            System.out.println("PROCESSED DESCRIPTIONS: " + processedDescriptions);

        } catch (IOException ioe) {
            ioe.printStackTrace();
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
