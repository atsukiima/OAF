package edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local;

import SnomedShared.Concept;
import SnomedShared.pareataxonomy.GroupParentInfo;
import SnomedShared.pareataxonomy.PAreaSummary;
import edu.njit.cs.saboc.blu.sno.abn.local.LocalConceptGroup;
import edu.njit.cs.saboc.blu.sno.datastructure.hierarchy.SCTConceptHierarchy;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * A Partial-area that was created using a local SNOMED CT release.
 * Unlike a PArea Summary, a LocalPArea stores its hierarchy of concepts and
 * information about its parents.
 * @author 
 */
public class LocalPArea extends PAreaSummary implements LocalConceptGroup {

    private SCTConceptHierarchy pareaConceptHierarchy;
    
    private ArrayList<GroupParentInfo> parentInformation;
        
    public LocalPArea(int id, Concept root) {
        super(id,  root,  0,  new HashSet<Integer>());
        
        this.pareaConceptHierarchy = new SCTConceptHierarchy(root);
    }
    
    // Copy constructor for root subtaxonomies
    public LocalPArea(LocalPArea parea, HashSet<Integer> parentIds, LocalPAreaTaxonomy sourceTaxonomy) {
        super(parea.getId(), parea.getRoot(), parea.getConceptCount(), parentIds);
        
        ArrayList<GroupParentInfo> parentInfo = new ArrayList<GroupParentInfo>();
        
        for(int parentId : parentIds) {
            PAreaSummary parent = sourceTaxonomy.getPAreas().get(parentId);
            
            for(GroupParentInfo info : parea.getParentGroupInformation()) {
                if(info.getParentPAreaRootId() == parent.getRoot().getId()) {
                    parentInfo.add(info);
                }
            }
        }
        
        this.setParentGroupInformation(parentInfo);
        
        this.pareaConceptHierarchy = parea.pareaConceptHierarchy;
    }
   
    public SCTConceptHierarchy getConceptHierarchy() {
        return pareaConceptHierarchy;
    }
        
    public ArrayList<Concept> getConceptsInPArea() {
        return new ArrayList<Concept>(pareaConceptHierarchy.getConceptsInHierarchy());
    }
    
    public void setParents(ArrayList<Integer> parents){
        super.parentGroupIds.addAll(parents);
    }
    
    public int getConceptCount() {
        return pareaConceptHierarchy.getConceptsInHierarchy().size();
    }
    
    public void setParentGroupInformation(ArrayList<GroupParentInfo> parentInformation) {
        this.parentInformation = parentInformation;
    }
    
    public ArrayList<GroupParentInfo> getParentGroupInformation() {
        return parentInformation;
    }
}
