/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local;

import SnomedShared.Concept;
import SnomedShared.pareataxonomy.Area;
import SnomedShared.pareataxonomy.PAreaSummary;
import edu.njit.cs.saboc.blu.sno.abn.local.LocalAbstractionNetwork;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.PAreaRootSubtaxonomy;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.PAreaTaxonomy;
import edu.njit.cs.saboc.blu.sno.datastructure.hierarchy.SCTConceptHierarchy;
import edu.njit.cs.saboc.blu.sno.sctdatasource.SCTLocalDataSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 *
 * @author Chris
 */
public class LocalPAreaTaxonomy extends PAreaTaxonomy implements LocalAbstractionNetwork {
    
    private SCTConceptHierarchy conceptHierarchy;
    
    public LocalPAreaTaxonomy(
            SCTConceptHierarchy conceptHierarchy,
            PAreaSummary rootPArea,
            ArrayList<Area> areas,
            HashMap<Integer, PAreaSummary> pareas,
            HashMap<Integer, HashSet<Integer>> pareaHierarchy,
            HashMap<Long, String> lateralRels,
            String version,
            SCTLocalDataSource dataSource) {
        
        super(conceptHierarchy.getRoot(), rootPArea, areas, pareas, 
                pareaHierarchy, lateralRels, version, dataSource);
        
        this.conceptHierarchy = conceptHierarchy;
    }
    
    public ArrayList<Concept> searchConcepts(String s) {
        return new ArrayList<Concept>();
    }
    
    public SCTConceptHierarchy getConceptHierarchy() {
        return conceptHierarchy;
    }
    
    
    public HashMap<Long, Concept> getConcepts() {
        return new HashMap<Long, Concept>();
    }
    
    public int getHierarchyConceptCount() {
        return conceptHierarchy.getConceptsInHierarchy().size();
    }
    
    
    protected PAreaSummary createRootSubtaxonomyPArea(PAreaSummary originalPArea, HashSet<Integer> subtaxonomyParents) {
        LocalPArea localPArea = (LocalPArea)originalPArea;
        
        return new LocalPArea(localPArea, subtaxonomyParents, this);
    }
    
        
    public PAreaTaxonomy getRootSubtaxonomy(PAreaSummary parea) {
        PAreaRootSubtaxonomy taxonomy = (PAreaRootSubtaxonomy)super.getRootSubtaxonomy(parea);
        
        SCTConceptHierarchy subHierarchy = new SCTConceptHierarchy(parea.getRoot(), conceptHierarchy);
        
        return new LocalPAreaRootSubtaxonomy(
                subHierarchy, 
                taxonomy.getRootPArea(),  
                taxonomy.getHierarchyAreas(), 
                taxonomy.getPAreas(), 
                taxonomy.getPAreaHierarchy(), 
                taxonomy.getLateralRelsInHierarchy(), 
                this.dataSource.getSelectedVersion(), 
                (SCTLocalDataSource)this.dataSource,
                (LocalPAreaTaxonomy)taxonomy.getTopLevelTaxonomy());       
    }
    
    public PAreaTaxonomy getRelationshipSubtaxonomy(ArrayList<Long> relationships) {
        PAreaTaxonomy taxonomy = super.getRelationshipSubtaxonomy(relationships);
        
        SCTConceptHierarchy subHierarchy = new SCTConceptHierarchy(taxonomy.getRootPArea().getRoot());
        
        for(PAreaSummary parea : taxonomy.getPAreas().values()) {
            LocalPArea localPArea = (LocalPArea)parea;
            subHierarchy.addAllHierarchicalRelationships(localPArea.getConceptHierarchy());
        }
        
        return new LocalPAreaTaxonomy(
                subHierarchy, 
                taxonomy.getRootPArea(),  
                taxonomy.getHierarchyAreas(), 
                taxonomy.getPAreas(), 
                taxonomy.getPAreaHierarchy(), 
                taxonomy.getLateralRelsInHierarchy(), 
                this.dataSource.getSelectedVersion(), 
                (SCTLocalDataSource)this.dataSource);     
    }
    
    public PAreaTaxonomy getImplicitRelationshipSubtaxonomy() {
        PAreaTaxonomy taxonomy = super.getImplicitRelationshipSubtaxonomy();

        SCTConceptHierarchy subHierarchy = new SCTConceptHierarchy(taxonomy.getRootPArea().getRoot());

        for (PAreaSummary parea : taxonomy.getPAreas().values()) {
            LocalPArea localPArea = (LocalPArea) parea;
            subHierarchy.addAllHierarchicalRelationships(localPArea.getConceptHierarchy());
        }

        return new LocalPAreaTaxonomy(
                subHierarchy,
                taxonomy.getRootPArea(),
                taxonomy.getHierarchyAreas(),
                taxonomy.getPAreas(),
                taxonomy.getPAreaHierarchy(),
                taxonomy.getLateralRelsInHierarchy(),
                this.dataSource.getSelectedVersion(),
                (SCTLocalDataSource) this.dataSource);
    }
}
