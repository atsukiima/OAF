package edu.njit.cs.saboc.blu.sno.abn.generator;

import SnomedShared.Concept;
import SnomedShared.pareataxonomy.InheritedRelationship;
import SnomedShared.pareataxonomy.InheritedRelationship.InheritanceType;
import edu.njit.cs.saboc.blu.core.abn.GroupHierarchy;
import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.PAreaTaxonomyGenerator;
import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.RelationshipEquality;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTArea;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTPArea;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTPAreaTaxonomy;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTRegion;
import edu.njit.cs.saboc.blu.sno.datastructure.hierarchy.SCTConceptHierarchy;
import edu.njit.cs.saboc.blu.sno.localdatasource.load.ConceptRelationshipsRetriever;
import edu.njit.cs.saboc.blu.sno.sctdatasource.SCTDataSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;

/**
 *
 * @author Chris
 */
public class SCTPAreaTaxonomyGenerator extends PAreaTaxonomyGenerator<SCTPAreaTaxonomy, SCTPArea, 
        SCTArea, SCTRegion, Concept, InheritedRelationship, SCTConceptHierarchy> {

    private SCTConceptHierarchy hierarchy;
    
    private HashMap<Concept, HashSet<InheritedRelationship>> definingConceptRels = new HashMap<Concept, HashSet<InheritedRelationship>>();
    
    private SCTDataSource dataSource;
    
    private Concept sctRootConcept;
    
    private HashMap<Long, String> relNames = new HashMap<Long, String>();

    public SCTPAreaTaxonomyGenerator(
            Concept sctRootConcept, 
            SCTDataSource dataSource, 
            SCTConceptHierarchy conceptHierarchy, 
            ConceptRelationshipsRetriever conceptRelsRetriever) {
        
        this.sctRootConcept = sctRootConcept;
        
        this.hierarchy =  conceptHierarchy;
        
        this.dataSource = dataSource;
        
        HashMap<Concept, HashSet<Long>> definingRels = conceptRelsRetriever.getDefiningRelationships(hierarchy);
        
        HashSet<Long> relsInHierarchy = new HashSet<Long>();
        
        for(Entry<Concept, HashSet<Long>> entry : definingRels.entrySet()) {
            Concept concept = entry.getKey();
            
            HashSet<Long> conceptRels = entry.getValue();
            
            HashSet<Concept> parents = conceptHierarchy.getParents(concept);
            
            HashSet<InheritedRelationship> conceptRelInheritance = new HashSet<InheritedRelationship>();
            
            HashSet<Long> parentRels = new HashSet<Long>();
            
            for(Concept parent : parents) {
                parentRels.addAll(definingRels.get(parent));
            }
            
            for(long conceptRel : conceptRels) {
                if(parentRels.contains(conceptRel)) {
                    conceptRelInheritance.add(new InheritedRelWithHash(InheritanceType.INHERITED, conceptRel));
                } else {
                    conceptRelInheritance.add(new InheritedRelWithHash(InheritanceType.INTRODUCED, conceptRel));
                }
                
                relsInHierarchy.add(conceptRel);
            }
            
            definingConceptRels.put(concept, conceptRelInheritance);
        }
        
        for(long relId : relsInHierarchy) {
            String relName = dataSource.getConceptFromId(relId).getName();
            relName = relName.substring(0, relName.lastIndexOf("(")).trim();
            
            relNames.put(relId, relName);
        }
    }
        
    protected SCTConceptHierarchy getConceptHierarchy() {
        return hierarchy;
    }
    
    @Override
    protected RelationshipEquality<InheritedRelationship> getRelationshipEquality() {
        return new RelationshipEquality<InheritedRelationship>() {
            public boolean equalsNoInheritance(HashSet<InheritedRelationship> a, HashSet<InheritedRelationship> b) {
                HashSet<Long> aRelIds = new HashSet<Long>();
                
                for(InheritedRelationship rel : a) {
                    aRelIds.add(rel.getRelationshipTypeId());
                }
                
                HashSet<Long> bRelIds = new HashSet<Long>();
                
                for(InheritedRelationship rel : b) {
                    bRelIds.add(rel.getRelationshipTypeId());
                }
                
                
                return aRelIds.equals(bRelIds);
            }

            public boolean equalsInheritance(HashSet<InheritedRelationship> a, HashSet<InheritedRelationship> b) {
                return a.equals(b);
            }
        };
    }

    @Override
    protected HashSet<InheritedRelationship> getDefiningConceptRelationships(Concept concept) {
        return definingConceptRels.get(concept);
    }

    @Override
    protected SCTConceptHierarchy initPAreaConceptHierarchy(Concept root) {
        return new SCTConceptHierarchy(root);
    }

    @Override
    protected SCTPArea createPArea(int id, SCTConceptHierarchy pareaHierarchy, HashSet<Integer> parentIds, HashSet<InheritedRelationship> relationships) {
        return new SCTPArea(id, (SCTConceptHierarchy)pareaHierarchy, parentIds, relationships);
    }

    @Override
    protected SCTArea createArea(int id, HashSet<InheritedRelationship> relationships) {
        return new SCTArea(id, relationships);
    }

    @Override
    protected SCTPAreaTaxonomy createPAreaTaxonomy(SCTConceptHierarchy conceptHierarchy, SCTPArea rootPArea, 
            ArrayList<SCTArea> areas, HashMap<Integer, SCTPArea> pareas, 
            GroupHierarchy<SCTPArea> pareaHierarchy) {
        
        return new SCTPAreaTaxonomy(
            sctRootConcept,
            dataSource.getSelectedVersion(),
            dataSource,
            (SCTConceptHierarchy)conceptHierarchy,
            rootPArea,
            areas,
            pareas,
            pareaHierarchy,
            relNames);
    }
}
