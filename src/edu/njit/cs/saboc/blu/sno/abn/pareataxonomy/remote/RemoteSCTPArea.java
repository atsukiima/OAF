
package edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.remote;

import SnomedShared.Concept;
import SnomedShared.pareataxonomy.GroupParentInfo;
import SnomedShared.pareataxonomy.InheritedRelationship;
import SnomedShared.pareataxonomy.PAreaSummary;
import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.GenericParentPAreaInfo;
import edu.njit.cs.saboc.blu.core.datastructure.hierarchy.SingleRootedHierarchy;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTPArea;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTPAreaTaxonomy;
import edu.njit.cs.saboc.blu.sno.datastructure.hierarchy.SCTConceptHierarchy;
import edu.njit.cs.saboc.blu.sno.sctdatasource.SCTRemoteDataSource;
import java.util.ArrayList;
import java.util.HashSet;

/**
 *
 * @author Chris O
 */
public class RemoteSCTPArea extends SCTPArea {

    private SCTRemoteDataSource dataSource;
    
    private SCTPAreaTaxonomy sourceTaxonomy;
    
    private PAreaSummary summary;
    
    public RemoteSCTPArea(SCTRemoteDataSource dataSource, PAreaSummary summary) {
        super(summary.getId(), new SCTConceptHierarchy(summary.getRoot()), summary.getParentIds(), new HashSet<InheritedRelationship>(summary.getRelationships()));
        
        this.dataSource = dataSource;
                
        this.summary = summary;
        
        this.conceptCount = summary.getConceptCount();
    }
    
    public PAreaSummary getSummary() {
        return summary;
    }
    
    public void setSourceTaxonomy(SCTPAreaTaxonomy taxonomy) {
        this.sourceTaxonomy = taxonomy;
    }
    
    public HashSet<GenericParentPAreaInfo<Concept, SCTPArea>> getParentPAreaInfo() {
        ArrayList<GroupParentInfo> parentGroups = dataSource.getPAreaParentInfo(sourceTaxonomy, this);
        
        HashSet<GenericParentPAreaInfo<Concept, SCTPArea>> parentInfo = new HashSet<GenericParentPAreaInfo<Concept, SCTPArea>>();
        
        for(GroupParentInfo parent : parentGroups) {
            parentInfo.add(new GenericParentPAreaInfo<Concept, SCTPArea>(parent.getParentConcept(), 
                    sourceTaxonomy.getPAreaFromRootConceptId(parent.getParentPAreaRootId())));
        }
        
        return parentInfo;
    }

    public SCTConceptHierarchy getHierarchy() {
        return super.getHierarchy(); //To change body of generated methods, choose Tools | Templates.
    }
}
