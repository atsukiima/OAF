/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.njit.cs.saboc.blu.sno.abn.tan.local;

import SnomedShared.Concept;
import SnomedShared.overlapping.ClusterSummary;
import SnomedShared.overlapping.EntryPointSet;
import SnomedShared.pareataxonomy.GroupParentInfo;
import edu.njit.cs.saboc.blu.sno.abn.local.LocalConceptGroup;
import edu.njit.cs.saboc.blu.sno.datastructure.hierarchy.SCTConceptHierarchy;
import java.util.ArrayList;
import java.util.HashSet;

/**
 *
 * @author Chris
 */
public class LocalCluster extends ClusterSummary implements LocalConceptGroup {
    
    private SCTConceptHierarchy conceptHierarchy;
    
    private ArrayList<GroupParentInfo> parentInformation;
    
    public LocalCluster (
            int id, 
            Concept root, 
            SCTConceptHierarchy conceptHierarchy, 
            HashSet<Integer> parentClusters, 
            EntryPointSet epSet,
            ArrayList<GroupParentInfo> parentInformation) {
        
        super(id, root, conceptHierarchy.getConceptsInHierarchy().size(), parentClusters, epSet);
        
        this.conceptHierarchy = conceptHierarchy;
        this.parentInformation = parentInformation;
    }
    
    public HashSet<Concept> getConcepts() {
        return conceptHierarchy.getConceptsInHierarchy();
    }
    
    public ArrayList<GroupParentInfo> getParentGroupInformation() {
        return parentInformation;
    }
    
    public SCTConceptHierarchy getConceptHierarchy() {
        return conceptHierarchy;
    }
}
