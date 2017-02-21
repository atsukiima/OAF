package edu.njit.cs.saboc.blu.sno.nat.panels.attributerels;

import edu.njit.cs.saboc.blu.sno.localdatasource.concept.AttributeRelationship;
import java.util.ArrayList;

/**
 *
 * @author Chris O
 */
public class RelationshipGroup {

    private final int groupId;
    private final ArrayList<AttributeRelationship> rels;

    public RelationshipGroup(int groupId, ArrayList<AttributeRelationship> rels) {
        this.groupId = groupId;
        this.rels = rels;
    }

    public ArrayList<AttributeRelationship> getAttributeRelationships() {
        return rels;
    }

    public int getGroupId() {
        return groupId;
    }
}
