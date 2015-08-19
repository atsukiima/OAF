package edu.njit.cs.saboc.blu.sno.gui.gep.listeners;

import SnomedShared.Concept;
import edu.njit.cs.saboc.blu.core.abn.GenericParentGroupInfo;
import edu.njit.cs.saboc.blu.core.abn.GroupHierarchy;
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
        

    }
    
    private SCTPAreaTaxonomy createExpandedPAreaTaxonomy(SCTPAreaTaxonomy sourceTaxonomy, ReducedSCTPArea reducedPArea) {
        
        GroupHierarchy<SCTPArea> reducedGroupHierarchy = reducedPArea.getReducedGroupHierarchy();
        
        HashSet<SCTPArea> reducedPAreas = reducedGroupHierarchy.getNodesInHierarchy();
        
        SCTConceptHierarchy conceptHierarchy = new SCTConceptHierarchy(reducedPArea.getRoot());
                
        HashMap<Integer, SCTPArea> pareas = new HashMap<Integer, SCTPArea>();

        for (SCTPArea parea : reducedPAreas) {
            conceptHierarchy.addAllHierarchicalRelationships(parea.getHierarchy());
        }
        
        ArrayList<SCTArea> hierarchyAreas = new ArrayList<SCTArea>();
        
        for(SCTPArea parea : reducedPAreas) {

            HashSet<SCTPArea> parents = reducedGroupHierarchy.getParents(parea);
            
            HashSet<Integer> parentIds = new HashSet<Integer>();
            
            for(SCTPArea parentPArea : parents) {
                int parentPAreaId = parentPArea.getId();
                
                parentIds.add(parentPAreaId);
            }
            
            SCTPArea expandedPArea = new SCTPArea(parea.getId(), (SCTConceptHierarchy)parea.getHierarchy(), parentIds, parea.getRelationships());
            
            HashSet<GenericParentGroupInfo<Concept, SCTPArea>> pareaParentDetails = parea.getParentPAreaInfo();
            
            HashSet<GenericParentGroupInfo<Concept, SCTPArea>> expandedParentDetails = 
                    new HashSet<GenericParentGroupInfo<Concept, SCTPArea>>();
            
            for(GenericParentGroupInfo<Concept, SCTPArea> parentDetails : pareaParentDetails) {
                if(parents.contains(parentDetails.getParentGroup())) {
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
        
        GroupHierarchy<SCTPArea> expandedHierarchy = new GroupHierarchy(reducedPArea);
        
        pareas.values().forEach((SCTPArea parea) -> {
            HashSet<Integer> parentIds = parea.getParentIds();
            
            parentIds.forEach( (Integer parentId) -> {
               expandedHierarchy.addIsA(parea, pareas.get(parentId));
            });
        });
        
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
