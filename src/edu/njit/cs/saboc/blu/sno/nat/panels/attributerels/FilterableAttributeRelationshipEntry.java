
package edu.njit.cs.saboc.blu.sno.nat.panels.attributerels;

import edu.njit.cs.saboc.blu.core.utils.filterable.list.Filterable;
import edu.njit.cs.saboc.blu.sno.localdatasource.concept.AttributeRelationship;

/**
 *
 * @author Chris O
 */
public class FilterableAttributeRelationshipEntry extends Filterable<AttributeRelationship> {
    
    private final AttributeRelationship rel;
    
    public FilterableAttributeRelationshipEntry(AttributeRelationship rel) {
        this.rel = rel;
    }

    @Override
    public boolean containsFilter(String filter) {
        return rel.getType().getName().toLowerCase().contains(filter) || 
                rel.getTarget().getName().toLowerCase().contains(filter);
    }

    @Override
    public AttributeRelationship getObject() {
        return rel;
    }

    @Override
    public String getToolTipText() {
        
        return String.format("== %s ==> %s",
                rel.getType().getName(), 
                rel.getTarget().getName());
        
    }

    @Override
    public String getClipboardText() {
        
        return String.format("%s\t%s\t%s", 
                rel.getType().getName(),
                rel.getTarget().getName(),
                rel.getTarget().getIDAsString());
    }
}
