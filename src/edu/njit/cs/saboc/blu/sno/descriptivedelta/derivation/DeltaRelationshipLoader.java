package edu.njit.cs.saboc.blu.sno.descriptivedelta.derivation;

import edu.njit.cs.saboc.blu.sno.descriptivedelta.DeltaRelationship;
import edu.njit.cs.saboc.blu.sno.localdatasource.concept.SCTConcept;
import edu.njit.cs.saboc.blu.sno.sctdatasource.SCTRelease;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

/**
 *
 * @author Chris O
 */
public class DeltaRelationshipLoader {
    
    public DeltaRelationships loadDeltaRelationships(SCTRelease toRelease) {
        
        File releaseTerminologyFolder = toRelease.getReleaseInfo().getReleaseDirectory();
        File fullReleaseFolder = releaseTerminologyFolder.getParentFile().getParentFile();

        File statedDeltaRelFile = getStatedRelationshipDeltaFile(fullReleaseFolder.getAbsolutePath());
        File inferredDeltaRelFile = getRelationshipDeltaFile(fullReleaseFolder.getAbsolutePath());
        
        Map<SCTConcept, Set<DeltaRelationship>> statedDeltaRels = getDeltaRelationships(statedDeltaRelFile, toRelease);
        Map<SCTConcept, Set<DeltaRelationship>> inferredDeltaRels = getDeltaRelationships(inferredDeltaRelFile, toRelease);

        return new DeltaRelationships(statedDeltaRels, inferredDeltaRels);
    }
    
    private Map<SCTConcept, Set<DeltaRelationship>> getDeltaRelationships(File file, SCTRelease release) {
        
        Map<SCTConcept, Set<DeltaRelationship>> rels = new HashMap<>();
        
        try (Scanner relScanner = new Scanner(file)) {
            relScanner.nextLine();

            while (relScanner.hasNext()) {
                String line = relScanner.nextLine();

                String[] parts = line.split("\t");

                boolean active = parts[2].equals("1");

                long sourceId = Long.parseLong(parts[4]);
                long targetId = Long.parseLong(parts[5]);

                int group = Integer.parseInt(parts[6]);

                long typeId = Long.parseLong(parts[7]);

                long characteristicType = Long.parseLong(parts[8]);
                
                SCTConcept source = release.getConceptFromId(sourceId);
                SCTConcept type = release.getConceptFromId(typeId);
                SCTConcept target = release.getConceptFromId(targetId);

                DeltaRelationship rel = new DeltaRelationship(type, target, group, characteristicType, active);

                if (!rels.containsKey(source)) {
                    rels.put(source, new HashSet<>());
                }

                rels.get(source).add(rel);
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        
        return rels;
    }
    
    private File getRelationshipDeltaFile(String rootDir) {
        return getDeltaFile(rootDir, "Relationship");
    }
    
    private File getStatedRelationshipDeltaFile(String rootDir) {
        return getDeltaFile(rootDir, "StatedRelationship");
    }
    
    private File getDeltaFile(String rootDir, String type) {
        String deltaDirStr = rootDir + "\\Delta\\Terminology";
        
        File deltaDirFile = new File(deltaDirStr);
        
        File [] allFiles = deltaDirFile.listFiles();
        
        String typeName = type + "_Delta";
        
        for(File file : allFiles) {
            if(file.getName().contains(typeName)) {
                return file;
            }
        }
        
        return null;
    }
}
