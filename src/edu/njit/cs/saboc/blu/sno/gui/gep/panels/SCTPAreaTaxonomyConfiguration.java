package edu.njit.cs.saboc.blu.sno.gui.gep.panels;

import SnomedShared.Concept;
import SnomedShared.pareataxonomy.InheritedRelationship;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.pareataxonomy.PAreaTaxonomyConfiguration;
import edu.njit.cs.saboc.blu.sno.abn.disjointpareataxonomy.DisjointPAreaTaxonomy;
import edu.njit.cs.saboc.blu.sno.abn.disjointpareataxonomy.DisjointPartialArea;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTArea;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTPArea;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTPAreaTaxonomy;
import edu.njit.cs.saboc.blu.sno.datastructure.hierarchy.SCTConceptHierarchy;
import edu.njit.cs.saboc.blu.sno.gui.abnselection.SCTDisplayFrameListener;
import edu.njit.cs.saboc.blu.sno.utils.comparators.ConceptNameComparator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 *
 * @author Chris O
 */
public class SCTPAreaTaxonomyConfiguration extends PAreaTaxonomyConfiguration<
        Concept, 
        SCTPArea, 
        SCTArea, 
        SCTPAreaTaxonomy, 
        DisjointPartialArea, 
        InheritedRelationship,
        SCTConceptHierarchy,
        DisjointPAreaTaxonomy> {



    private final SCTPAreaTaxonomy taxonomy;
    
    private final SCTDisplayFrameListener displayListener;
    
    public SCTPAreaTaxonomyConfiguration(SCTPAreaTaxonomy taxonomy, SCTDisplayFrameListener displayListener) {
        this.taxonomy = taxonomy;
        this.displayListener = displayListener;
    }
    
    public SCTPAreaTaxonomy getPAreaTaxonomy() {
        return taxonomy;
    }
    
    public SCTDisplayFrameListener getDisplayListener() {
        return displayListener;
    }
    
    @Override
    public ArrayList<InheritedRelationship> getAreaRelationships(SCTArea area) {
        ArrayList<InheritedRelationship> rels = new ArrayList<>(area.getRelationships());
        
        return rels;
    }
    
    @Override
    public ArrayList<InheritedRelationship> getPAreaRelationships(SCTPArea parea) {
        ArrayList<InheritedRelationship> rels = new ArrayList<>(parea.getRelationships());
        
        return rels;
    }

    @Override
    public Comparator<SCTPArea> getChildPAreaComparator() {
        return new Comparator<SCTPArea>() {
            public int compare(SCTPArea a, SCTPArea b) {
                return a.getRoot().getName().compareToIgnoreCase(b.getRoot().getName());
            }
        };
    }

    @Override
    public String getDisjointGroupName(DisjointPartialArea group) {
        return group.getRoot().getName();
    }

    @Override
    public String getContainerName(SCTArea container) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ArrayList<SCTPArea> getSortedGroupList(SCTArea area) {
        return area.getAllPAreas();
    }

    @Override
    public String getConceptTypeName(boolean plural) {
        if(plural) {
            return "Concepts";
        } else {
            return "Concept";
        }
    }

    @Override
    public String getConceptName(Concept concept) {
        return concept.getName();
    }

    @Override
    public String getGroupName(SCTPArea group) {
        return group.getRoot().getName();
    }

    @Override
    public ArrayList<Concept> getSortedConceptList(SCTPArea group) {
        ArrayList<Concept> pareaConcepts = group.getConceptsInPArea();
        Collections.sort(pareaConcepts, new ConceptNameComparator());
        
        return pareaConcepts;
    }
    
        @Override
    public DisjointPAreaTaxonomy createDisjointAbN(SCTArea container) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
