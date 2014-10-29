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

    public static ArrayList<String> getReleaseFileNames(ArrayList<File> releaseDirectories) {
        
        ArrayList<String> releaseNames = new ArrayList<String>();

        for (File dir : releaseDirectories) {
            String dirName = dir.getAbsolutePath();
            
            String releaseName;
            
            if (dirName.contains("RF1")) {
                releaseName = dirName.substring(dirName.lastIndexOf("SnomedCT_"), dirName.lastIndexOf("\\RF1"));
            } else {
                releaseName = dirName.substring(dirName.lastIndexOf("SnomedCT_"), dirName.lastIndexOf("\\Terminology\\Content"));
            }

            if(releaseName.contains("Release")) {
                releaseName = releaseName.substring("SnomedCT_Release_".length());
            } else {
                releaseName = releaseName.substring("SnomedCT_".length());
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
                    if (child.getAbsolutePath().contains("\\Terminology\\Content")) {
                        File [] folderContents = child.listFiles();
                        
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
