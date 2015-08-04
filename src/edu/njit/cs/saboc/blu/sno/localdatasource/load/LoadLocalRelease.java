/*
 * This unholy mess is currently under construction
 */
package edu.njit.cs.saboc.blu.sno.localdatasource.load;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;

/**
 *
 * @author Chris Castellano
 */
public class LoadLocalRelease {

    public static ArrayList<File> findReleaseFolders(File parentFile) { //parentFile is the snomed directory, not checked at the moment
        ArrayList<File> dirList = findSub(parentFile, new ArrayList<File>(), 0);//finds all subdirectories, disincludes files
        
        return dirList;
    }
    
    public static File getStatedRelationshipsFile(File releaseDirectory) {
        String dirName = releaseDirectory.getAbsolutePath();
        
        if(dirName.contains("RF1Release")) {
            String topLevelDir = dirName.substring(0, dirName.lastIndexOf("RF1Release") + "RF1Release".length());
            
            String statedRelationshipsDir = topLevelDir + "\\OtherResources\\StatedRelationships\\";
            
            File statedRelsDir = new File(statedRelationshipsDir);
            
            if(statedRelsDir.exists() && statedRelsDir.isDirectory()) {
                File [] filesList = statedRelsDir.listFiles();
                
                if(filesList.length == 1) {
                    return filesList[0];
                }
            }
        }
        
        return null;
    }

    public static ArrayList<String> getReleaseFileNames(ArrayList<File> releaseDirectories) {
        
        ArrayList<String> releaseNames = new ArrayList<String>();

        for (File dir : releaseDirectories) {
            String dirName = dir.getAbsolutePath();
            
            String releaseName;
            
            if(dirName.contains("_RF2Release_")) {
                releaseName = dirName.substring(dirName.lastIndexOf("_RF2Release_"), dirName.lastIndexOf("\\Snapshot\\Terminology"));
                
            } else if (dirName.contains("RF1")) {
                if (dirName.contains("_RF1Release")) {
                    releaseName = dirName.substring(dirName.lastIndexOf("RF1Release"), dirName.lastIndexOf("\\Terminology\\Content"));
                } else {
                    releaseName = dirName.substring(dirName.lastIndexOf("SnomedCT_"), dirName.lastIndexOf("\\RF1"));
                }
                
            } else {
                if(dirName.contains("Essential Resources")) {
                    releaseName = dirName.substring(dirName.lastIndexOf("\\SNOMED_CT_Essential_") + 21, dirName.lastIndexOf("\\Essential Resources"));
                } else {
                    if(dirName.contains("RF2Release")) {
                        releaseName = dirName.substring(dirName.lastIndexOf("SnomedCT_"), dirName.lastIndexOf("\\RF2Release"));
                    } else {
                        releaseName = dirName.substring(dirName.lastIndexOf("SnomedCT_"), dirName.lastIndexOf("\\Terminology\\Content"));
                    }
                }
            }

            if (releaseName.contains("Release_")) {
                releaseName = releaseName.substring(releaseName.lastIndexOf("Release_") + 8);
            } else if (releaseName.contains("SnomedCT_")) {
                releaseName = releaseName.substring(releaseName.lastIndexOf("SnomedCT_") + 9);
            }
            
            if(dirName.contains("RF2")) {
                releaseName += (" (RF2)");
            }
            
            releaseName = releaseName.replace("_", " ");
            
            releaseNames.add(releaseName);
        }
        
        return releaseNames;
    }
    
    private static ArrayList<File> findSub(File directory, ArrayList<File> dirList, int currentDepth) {

        if (!directory.isDirectory()) { //if the called file isn't a directory, simply return the arrayList
            return dirList;
        } else {
            File[] children = directory.listFiles(); //gets a list of all files in the selected directory

            for (File child : children) {
                if (child.isDirectory()) { //if the child file is a directory, add it to the list, and continue in 

                    File[] folderContents = child.listFiles();

                    HashSet<String> fileNames = new HashSet<>();

                    for (File f : folderContents) {
                        if (f.isFile()) {
                            fileNames.add(f.getName());
                        }
                    }
                    
                    if (fileNames.size() >= 3) {
                        if (potentialFileMatch(fileNames, "concept")
                                && potentialFileMatch(fileNames, "relationship") 
                                && potentialFileMatch(fileNames, "description")) {

                            String pathName = child.getAbsolutePath();
                            
                            if(pathName.toLowerCase().contains("terminology\\content")
                                    || pathName.toLowerCase().contains("snomed ct terminology\\content")
                                    || pathName.toLowerCase().contains("snapshot\\terminology")) {
                                
                                if(!pathName.toLowerCase().contains("usdrugextension")) {
                                    dirList.add(child);
                                }
                            }
                        }
                    }
                    
                    if(currentDepth < 8) {
                        findSub(child, dirList, currentDepth + 1);
                    }
                }
            }
        }

        return dirList;
    }
    
    private static boolean potentialFileMatch(HashSet<String> fileNames, String match) {
        for(String s : fileNames) {
            if(s.toLowerCase().contains(match.toLowerCase())) {
                return true;
            }
        }
        
        return false;
    }
}
