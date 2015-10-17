package edu.njit.cs.saboc.blu.sno.utils.filterable.entry;

import SnomedShared.Concept;
import edu.njit.cs.saboc.blu.core.utils.filterable.list.Filterable;
import edu.njit.cs.saboc.blu.sno.localdatasource.concept.LocalLateralRelationship;
import edu.njit.cs.saboc.blu.sno.localdatasource.concept.LocalSCTConceptStated;
import edu.njit.cs.saboc.blu.sno.utils.comparators.ConceptNameComparator;
import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author Chris
 */
public class FilterableStatedAncestorEntry extends Filterable<LocalSCTConceptStated> implements NavigableEntry {

    private LocalSCTConceptStated concept;

    private boolean showConceptIds = true;
    
    private String statedRelsStr = "";

    public FilterableStatedAncestorEntry(LocalSCTConceptStated c) {
        this.concept = c;
        
        ArrayList<LocalLateralRelationship> statedRels = c.getStatedRelationships();
        
        if (!statedRels.isEmpty()) {
            ArrayList<Concept> relationshipConcepts = new ArrayList<Concept>();

            for (LocalLateralRelationship statedRel : statedRels) {
                relationshipConcepts.add(statedRel.getRelationship());
            }

            Collections.sort(relationshipConcepts, new ConceptNameComparator());
            
            String relNames = "";
            
            for(Concept relConcept : relationshipConcepts) {
                String relName = relConcept.getName();
                relName = relName.substring(0, relName.lastIndexOf("(") - 1);
                
                relNames += (relName + ", ");
            }
            
            relNames = relNames.substring(0, relNames.length() - 2);
            
            this.statedRelsStr = relNames;
        }
    }
    
    private String getStatedRelStr(String str) {
        if(!concept.getStatedRelationships().isEmpty()) {
            return String.format(" --- (<font color='red'>%s</font>)", str);
        } 
        
        return "";
    }
    
    public void setShowConceptIds(boolean showConceptIds) {
    	this.showConceptIds = showConceptIds;
    }
    
    public LocalSCTConceptStated getObject() {
        return concept;
    }
    
    public Concept getNavigateConcept() {
        return getNavigableConcept();
    }

    public Concept getNavigableConcept() {
        if(concept.getId() != -1) {
            return concept;
        }

        return null;
    }
    
    private String createEntryStr(String conceptName, String conceptId, String introducedRels) {
        if (showConceptIds) {
            return String.format(
                    "<html>%s <font color='blue'>(%s)</font> %s",
                    conceptName, conceptId, introducedRels);
        } else {
            return String.format(
                    "<html>%s %s",
                    conceptName, introducedRels);
        }
    }
    
    private String createEntryStrWithPrimitive(String conceptName, String conceptId, String introducedRels) {
        if (showConceptIds) {
            return String.format(
                    "<html>%s <font color ='purple'>[primitive]</font> <font color='blue'>(%s)</font> %s",
                    conceptName, conceptId, introducedRels);
        } else {
            return String.format(
                    "<html>%s <font color ='purple'>[primitive]</font> %s",
                    conceptName, introducedRels);
        }
    }

    public String getInitialText() {
        if(concept.primitiveSet() && concept.isPrimitive()) {
            return createEntryStrWithPrimitive(concept.getName(), Long.toString(concept.getId()), getStatedRelStr(statedRelsStr));
        } else {
            return createEntryStr(concept.getName(), Long.toString(concept.getId()), getStatedRelStr(statedRelsStr));
        }
    }
    
    public String getFilterText(String filter) {
        if (!filter.isEmpty()) {
            if (concept.primitiveSet() && concept.isPrimitive()) {
                return createEntryStrWithPrimitive(
                        filter(concept.getName(), filter), 
                        filter(Long.toString(concept.getId()), filter), 
                        getStatedRelStr(filter(statedRelsStr, filter)));
            } else {
                return createEntryStr(
                        filter(concept.getName(), filter), 
                        filter(Long.toString(concept.getId()), filter), 
                        getStatedRelStr(filter(statedRelsStr, filter)));
            }

        } else {
            return getInitialText();
        }
    }
    
    public boolean containsFilter(String filter) {
        return concept.getName().toLowerCase().contains(filter) ||
                Long.toString(concept.getId()).contains(filter) ||
                statedRelsStr.toLowerCase().contains(filter);
    }
    
    @Override
    public String getClipboardText() {
        return String.format("%s\t%d\t%s\t%s", concept.getName(), concept.getId(), concept.isPrimitive(), getStatedRelStr(statedRelsStr));
    }
}
