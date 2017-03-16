package edu.njit.cs.saboc.blu.sno.nat.panels.attributerels;

import edu.njit.cs.saboc.blu.core.utils.filterable.list.Filterable;
import edu.njit.cs.saboc.blu.sno.localdatasource.concept.AttributeRelationship;
import edu.njit.cs.saboc.nat.generic.gui.filterable.nestedlist.FilterableNestedEntry;
import java.util.ArrayList;

/**
 *
 * @author Chris O
 */
public class FilterableRelationshipGroupEntry extends FilterableNestedEntry<RelationshipGroup, AttributeRelationship> {
    
    public FilterableRelationshipGroupEntry(RelationshipGroup group, 
            ArrayList<Filterable<AttributeRelationship>> attributeRelEntries) {
        
        super(group, attributeRelEntries);
    }

    @Override
    public String getToolTipText() {
        String str = "<html>";
        
        for(Filterable<AttributeRelationship> relEntry : super.getSubEntries()) {
            str += String.format("%s<br>", relEntry.getToolTipText());
        }
        
        return str;
    }

    @Override
    public String getClipboardText() {
        return "";
    }
}
