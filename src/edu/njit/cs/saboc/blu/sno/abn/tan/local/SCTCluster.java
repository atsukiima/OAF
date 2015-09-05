/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.njit.cs.saboc.blu.sno.abn.tan.local;

import SnomedShared.Concept;
import SnomedShared.overlapping.ClusterSummary;
import SnomedShared.overlapping.EntryPointSet;
import edu.njit.cs.saboc.blu.core.abn.GenericParentGroupInfo;
import edu.njit.cs.saboc.blu.sno.abn.local.LocalConceptGroup;
import edu.njit.cs.saboc.blu.sno.datastructure.hierarchy.SCTConceptHierarchy;
import java.util.ArrayList;
import java.util.HashSet;

/**
 *
 * @author Chris
 */
public class SCTCluster extends ClusterSummary implements LocalConceptGroup {
    
    private SCTConceptHierarchy conceptHierarchy;
    
    private ArrayList<GenericParentGroupInfo<Concept, SCTCluster>> parentInformation;
    
    public SCTCluster (
            int id, 
            Concept root, 
            SCTConceptHierarchy conceptHierarchy, 
            HashSet<Integer> parentClusters, 
            EntryPointSet epSet) {
        
        super(id, root, conceptHierarchy.getConceptsInHierarchy().size(), parentClusters, epSet);
        
        this.conceptHierarchy = conceptHierarchy;
    }
    
    public HashSet<Concept> getConcepts() {
        return conceptHierarchy.getConceptsInHierarchy();
    }
    
    public void setParentGroupInformation(ArrayList<GenericParentGroupInfo<Concept, SCTCluster>> parentInformation) {
        this.parentInformation = parentInformation;
    }
    
    public ArrayList<GenericParentGroupInfo<Concept, SCTCluster>> getParentGroupInformation() {
        return parentInformation;
    }
    
    public SCTConceptHierarchy getConceptHierarchy() {
        return conceptHierarchy;
    }
}
