/*
 * This unholy mess is currently under construction
 */
package edu.njit.cs.saboc.blu.sno.localdatasource.load;

import java.io.File;
import java.util.ArrayList;

/**
 *
 * @author Chris Castellano
 */
public class LoadLocalRelease {

    public static ArrayList<File> findReleaseFolders(File parentFile) { //parentFile is the snomed directory, not checked at the moment
        ArrayList<File> dirList = findSub(parentFile, new ArrayList<File>());//finds all subdirectories, disincludes files
        
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
            
            if (dirName.contains("RF1")) {
                
                if (dirName.contains("_RF1Release")) {
                    releaseName = dirName.substring(dirName.lastIndexOf("RF1Release"), dirName.lastIndexOf("\\Terminology\\Content"));
                } else {
                    releaseName = dirName.substring(dirName.lastIndexOf("SnomedCT_"), dirName.lastIndexOf("\\RF1"));
                }
                
            } else {
                
                if(dirName.contains("Essential Resources")) {
                    releaseName = dirName.substring(dirName.lastIndexOf("\\SNOMED_CT_Essential_") + 21, dirName.lastIndexOf("\\Essential Resources"));
                } else {
                    releaseName = dirName.substring(dirName.lastIndexOf("SnomedCT_"), dirName.lastIndexOf("\\Terminology\\Content"));
                }
            }

            if(releaseName.contains("Release")) {
                if(releaseName.contains("RF1Release")) {
                    releaseName = releaseName.substring("_RF1Release".length());
                } else {
                    releaseName = releaseName.substring("SnomedCT_Release_".length());
                }
            } else {
                if(releaseName.contains("SNOMEDCT_")) {
                    releaseName = releaseName.substring("SnomedCT_".length());
                }
            }
            
            releaseName = releaseName.replace("_", " ");
            
            releaseNames.add(releaseName);
        }
        
        return releaseNames;
    }
    
    

    private static ArrayList<File> findSub(File directory, ArrayList<File> dirList) {

        if (!directory.isDirectory()) { //if the called file isn't a directory, simply return the arrayList
            return dirList;
        } else {
            File[] children = directory.listFiles(); //gets a list of all files in the selected directory

            for (File child : children) {
                if (child.isDirectory()) { //if the child file is a directory, add it to the list, and continue in 
                    if (child.getAbsolutePath().contains("\\Content")) {
                        File [] folderContents = child.listFiles();
                        
                        // TODO: Check to see if concepts, relationships, descriptions available
                        if (folderContents.length == 3 && 
                                !child.getAbsolutePath().contains("USDrugExtension")) {
                            
                            dirList.add(child);
                        }
                    }
                    
                    findSub(child, dirList);
                }
            }
        }

        return dirList;
    }
}
