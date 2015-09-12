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
}
