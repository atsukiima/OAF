package edu.njit.cs.saboc.blu.sno.gui.gep.panels.disjointpareataxonomy;

import SnomedShared.Concept;
import edu.njit.cs.saboc.blu.core.abn.GenericParentGroupInfo;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.configuration.BLUDisjointAbNConfiguration;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.listeners.EntitySelectionListener;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.listeners.NavigateToGroupListener;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.listeners.ParentGroupSelectedListener;
import edu.njit.cs.saboc.blu.sno.abn.disjointpareataxonomy.DisjointPAreaTaxonomy;
import edu.njit.cs.saboc.blu.sno.abn.disjointpareataxonomy.DisjointPartialArea;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTPArea;
import edu.njit.cs.saboc.blu.sno.datastructure.hierarchy.SCTConceptHierarchy;
import edu.njit.cs.saboc.blu.sno.gui.abnselection.SCTDisplayFrameListener;
import edu.njit.cs.saboc.blu.sno.gui.gep.configuration.SCTDisjointPAreaTaxonomyGEPConfiguration;
import edu.njit.cs.saboc.blu.sno.gui.gep.configuration.listener.DisplayConceptBrowserListener;
import java.util.ArrayList;

/**
 *
 * @author Chris O
 */
public class SCTDisjointPAreaTaxonomyConfiguration implements BLUDisjointAbNConfiguration<Concept, SCTPArea, SCTConceptHierarchy, DisjointPartialArea> {

    private final DisjointPAreaTaxonomy disjointTaxonomy;
    
    private final SCTDisjointPAreaTaxonomyGEPConfiguration gepConfiguration;
    
    private final SCTDisplayFrameListener displayListener;
    
    public SCTDisjointPAreaTaxonomyConfiguration(
            DisjointPAreaTaxonomy disjointTaxonomy, 
            SCTDisplayFrameListener displayListener, 
            SCTDisjointPAreaTaxonomyGEPConfiguration gepConfiguration) {

        this.disjointTaxonomy = disjointTaxonomy;
        
        this.gepConfiguration = gepConfiguration;
        
        this.displayListener = displayListener;
    }
    
    public SCTDisjointPAreaTaxonomyGEPConfiguration getDisjointGEPConfiguration() {
        return gepConfiguration;
    }
    
    public DisjointPAreaTaxonomy getDisjointPAreaTaxonomy() {
        return disjointTaxonomy;
    }
    
    public SCTDisplayFrameListener getDisplayListener() {
        return displayListener;
    }
    
    public SCTDisjointPAreaTaxonomyGEPConfiguration getGEPConfiguration() {
        return gepConfiguration;
    }
    
    @Override
    public String getGroupTypeName(boolean plural) {
        if(plural) {
            return "Disjoint Partial-areas";
        } else {
            return "Disjoint Partial-area";
        }
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
    public String getOverlappingGroupTypeName(boolean plural) {
        if(plural) {
            return "Partial-areas";
        } else {
            return "Partial-area";
        }
    }

    @Override
    public String getOverlappingGroupName(SCTPArea parea) {
        return parea.getRoot().getName();
    }

    @Override
    public String getConceptName(Concept concept) {
        return concept.getName();
    }

    @Override
    public String getGroupName(DisjointPartialArea group) {
        return group.getRoot().getName();
    }

    @Override
    public ArrayList<Concept> getSortedConceptList(DisjointPartialArea group) {
        return group.getConceptsAsList();
    }

    @Override
    public EntitySelectionListener<Concept> getGroupConceptListListener() {
        return new DisplayConceptBrowserListener(this.getDisplayListener(), this.getDisjointPAreaTaxonomy().getSourcePAreaTaxonomy().getDataSource());
    }

    @Override
    public EntitySelectionListener<DisjointPartialArea> getChildGroupListener() {
        return new NavigateToGroupListener<>(this.getGEPConfiguration().getGEP());
    }
    
    @Override
    public EntitySelectionListener<GenericParentGroupInfo<Concept, DisjointPartialArea>> getParentGroupListener() {
        return new ParentGroupSelectedListener<>(this.getGEPConfiguration().getGEP());
    }
    
    @Override
    public String getGroupHelpDescriptions(DisjointPartialArea group) {
        StringBuilder helpDescription = new StringBuilder();
        
        helpDescription.append("A <b>disjoint partial-area</b> represents a summary of a disjoint hierarchy of concepts within a partial-area taxonomy area. ");
        
        helpDescription.append("A <b>basis</b> (non-overlapping) disjoint partial-area is a disjoint partial-area that summarizes all of the concepts "
                + "that are summarized by exactly one partial-area in the complete partial-area taxonomy. "
                + "Basis disjoint partial-areas are assigned one color and are named after this partial-area.");
        
        helpDescription.append("An <b>overlapping</b> disjoint partial-area is a disjoint partial-area that summarizes a set of concepts that are summarized "
                + "by two or more partial-areas in the complete partial-area taxonomy. Overlapping disjoint partial-areas are color coded according to the "
                + "partial-areas in which their concepts overlap and are named after the concept which is the point of intersection between the partial-areas. "
                + "There may be multiple points of intersection, thus, there may be many similarly color coded overlapping disjoint partial-areas.");
        
        return helpDescription.toString();
    }
    
    @Override
    public String getAbNName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getAbNSummary() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public String getAbNHelpDescription() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
