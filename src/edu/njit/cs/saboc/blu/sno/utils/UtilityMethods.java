/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.njit.cs.saboc.blu.sno.utils;

import SnomedShared.Concept;
import SnomedShared.overlapping.EntryPoint;
import SnomedShared.overlapping.EntryPointSet;
import SnomedShared.pareataxonomy.InheritedRelationship;
import SnomedShared.pareataxonomy.InheritedRelationship.InheritanceType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;

public class UtilityMethods {
    public static String getPrintableVersionName(String version) {
        if(version.isEmpty()) {
            return version;
        }
        
        String versionName = Character.toUpperCase(version.charAt(0)) + version.substring(1).replace("_", " ");
        
        if(versionName.endsWith("us")) {
            versionName = versionName.substring(0, versionName.length() - 2) + " US Extension";
        } else {
            versionName += " International";
        }
        
        return versionName;
    }

    public static String getRegionName(ArrayList<InheritedRelationship> relationships, HashMap<Long, String> relMap) {
        String regionName = "";

        if (relationships.isEmpty()) {
            regionName = "\u2205";    // If it is the root concept, set the title to the null character.
        } else {
            for (InheritedRelationship rel : relationships) {   // Otherwise derive the title from its relationships.
                String relStr = relMap.get(rel.getRelationshipTypeId());
                relStr += rel.getInheritanceType() == InheritanceType.INHERITED ? "*" : "+";
                regionName += relStr + ", ";
            }
        }

        return regionName;
    }

    public static String getOverlapPartitionName(EntryPointSet epSet, final HashMap<Long, String> epNames) {

        ArrayList<EntryPoint> entryPoints = new ArrayList<EntryPoint>(epSet);

        Collections.sort(entryPoints, new Comparator<EntryPoint>() {
           public int compare(EntryPoint a, EntryPoint b) {
               return epNames.get(a.getEntryPointConceptId()).compareTo(epNames.get(b.getEntryPointConceptId()));
           }
        });

        String regionName = "";

        for (EntryPoint ep : entryPoints) {   // Otherwise derive the title from its relationships.
            String relStr = epNames.get(ep.getEntryPointConceptId());
            relStr += ep.getInheritanceType() == EntryPoint.InheritanceType.INHERITED ? "*" : "+";
            regionName += relStr + "; ";
        }

        return regionName;
    }

    public static String getRegionNameNewlineHTML(ArrayList<InheritedRelationship> relationships, HashMap<Long, String> relMap) {
        String regionName = "<html>";

        if (relationships.isEmpty()) {
            regionName = "\u2205";    // If it is the root concept, set the title to the null character.
        } else {
            for (InheritedRelationship rel : relationships) {   // Otherwise derive the title from its relationships.
                String relStr = relMap.get(rel.getRelationshipTypeId());
                relStr += rel.getInheritanceType() == InheritanceType.INHERITED ? "*" : "+";
                regionName += relStr + "<br>";
            }
        }

        return regionName;
    }
    
    public static HashMap<Concept, HashSet<Concept>> convertALMapToHSMap(HashMap<Concept, ArrayList<Concept>> map) {
        HashMap<Concept, HashSet<Concept>> result = new HashMap<Concept, HashSet<Concept>>();
        
        for(Entry<Concept, ArrayList<Concept>> entries : map.entrySet()) {
            result.put(entries.getKey(), new HashSet<Concept>(entries.getValue()));
        }
        
        return result;
    }
}
