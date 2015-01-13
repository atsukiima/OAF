package edu.njit.cs.saboc.blu.sno.gui.gep.listeners;

import SnomedShared.Concept;
import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.GenericParentPAreaInfo;
import edu.njit.cs.saboc.blu.core.datastructure.hierarchy.ConceptGroupHierarchy;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.GroupOptionsPanelActionListener;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.ReducedSCTPArea;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTArea;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTPArea;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTPAreaTaxonomy;
import edu.njit.cs.saboc.blu.sno.datastructure.hierarchy.SCTConceptHierarchy;
import edu.njit.cs.saboc.blu.sno.gui.abnselection.SCTDisplayFrameListener;
import edu.njit.cs.saboc.blu.sno.gui.graphframe.PAreaInternalGraphFrame;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import javax.swing.JFrame;

/**
 *
 * @author Chris O
 */
public class ReducedPAreaOptionsConfiguration extends PAreaOptionsConfiguration {
    
    public ReducedPAreaOptionsConfiguration(final JFrame parentFrame, 
            final PAreaInternalGraphFrame graphFrame,
            final SCTPAreaTaxonomy taxonomy, 
            final SCTDisplayFrameListener displayListener) {
        
        super(parentFrame, graphFrame, taxonomy, displayListener);
        
        super.enableButtonWithAction(5, new GroupOptionsPanelActionListener<ReducedSCTPArea>() {
            public void actionPerformedOn(ReducedSCTPArea parea) {
                SCTPAreaTaxonomy expandedTaxonomy = createExpandedPAreaTaxonomy(taxonomy, parea);
                
                displayListener.addNewPAreaGraphFrame(expandedTaxonomy, true, false);
            }
        });
    }
    
    private SCTPAreaTaxonomy createExpandedPAreaTaxonomy(SCTPAreaTaxonomy sourceTaxonomy, ReducedSCTPArea reducedPArea) {
        
        ConceptGroupHierarchy<SCTPArea> reducedGroupHierarchy = reducedPArea.getReducedGroupHierarchy();
        
        HashSet<SCTPArea> reducedPAreas = reducedGroupHierarchy.getNodesInHierarchy();
        
        SCTConceptHierarchy conceptHierarchy = new SCTConceptHierarchy(reducedPArea.getRoot());
                
        HashMap<Integer, SCTPArea> pareas = new HashMap<Integer, SCTPArea>();
        
        HashMap<Integer, HashSet<Integer>> expandedHierarchy = new HashMap<Integer, HashSet<Integer>>();
        
        for (SCTPArea parea : reducedPAreas) {
            conceptHierarchy.addAllHierarchicalRelationships(parea.getHierarchy());

            expandedHierarchy.put(parea.getId(), new HashSet<Integer>());
        }
        
        ArrayList<SCTArea> hierarchyAreas = new ArrayList<SCTArea>();
        
        for(SCTPArea parea : reducedPAreas) {

            HashSet<SCTPArea> parents = reducedGroupHierarchy.getParents(parea);
            
            HashSet<Integer> parentIds = new HashSet<Integer>();
            
            for(SCTPArea parentPArea : parents) {
                int parentPAreaId = parentPArea.getId();
                
                parentIds.add(parentPAreaId);
                
                expandedHierarchy.get(parentPAreaId).add(parea.getId());
            }
            
            SCTPArea expandedPArea = new SCTPArea(parea.getId(), (SCTConceptHierarchy)parea.getHierarchy(), parentIds, parea.getRelationships());
            
            HashSet<GenericParentPAreaInfo<Concept, SCTPArea>> pareaParentDetails = parea.getParentPAreaInfo();
            
            HashSet<GenericParentPAreaInfo<Concept, SCTPArea>> expandedParentDetails = 
                    new HashSet<GenericParentPAreaInfo<Concept, SCTPArea>>();
            
            for(GenericParentPAreaInfo<Concept, SCTPArea> parentDetails : pareaParentDetails) {
                if(parents.contains(parentDetails.getParentPArea())) {
                    expandedParentDetails.add(parentDetails);
                }
            }
            
            expandedPArea.setParentPAreaInfo(expandedParentDetails);
            
            pareas.put(parea.getId(), expandedPArea);
            
            boolean areaFound = false;

            for (SCTArea area : hierarchyAreas) {
                if (area.getRelationships().equals(parea.getRelsWithoutInheritanceInfo())) {
                    area.addPArea(parea);
                    areaFound = true;
                    break;
                }
            }

            if (!areaFound) {
                SCTArea newArea = new SCTArea(hierarchyAreas.size(), parea.getRelsWithoutInheritanceInfo());
                newArea.addPArea(parea);
                hierarchyAreas.add(newArea);
            }
        }
        
        return new SCTPAreaTaxonomy(
                sourceTaxonomy.getSCTRootConcept(), 
                sourceTaxonomy.getSCTVersion(),
                sourceTaxonomy.getDataSource(),
                conceptHierarchy,
                pareas.get(reducedPArea.getId()),
                hierarchyAreas,
                pareas,
                expandedHierarchy,
                sourceTaxonomy.getLateralRelsInHierarchy());
    }
}
