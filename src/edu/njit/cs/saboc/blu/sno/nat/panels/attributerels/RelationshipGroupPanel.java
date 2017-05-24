
package edu.njit.cs.saboc.blu.sno.nat.panels.attributerels;

import edu.njit.cs.saboc.blu.core.graph.pareataxonomy.BasePAreaTaxonomyLayout;
import edu.njit.cs.saboc.blu.core.utils.filterable.list.Filterable;
import edu.njit.cs.saboc.blu.sno.localdatasource.concept.AttributeRelationship;
import edu.njit.cs.saboc.blu.sno.localdatasource.concept.SCTConcept;
import edu.njit.cs.saboc.blu.sno.nat.SCTConceptBrowserDataSource;
import edu.njit.cs.saboc.nat.generic.NATBrowserPanel;
import edu.njit.cs.saboc.nat.generic.gui.filterable.nestedlist.FilterableNestedEntryPanel;
import java.awt.Color;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;

/**
 *
 * @author Chris O
 */
public class RelationshipGroupPanel extends FilterableNestedEntryPanel<FilterableRelationshipGroupEntry> {
    
    public RelationshipGroupPanel(
            NATBrowserPanel<SCTConcept> mainPanel,
            FilterableRelationshipGroupEntry entry) {
        
        super(entry);

        this.setOpaque(false);
        
        RelationshipGroup group = entry.getObject();

        if (group.getGroupId() > 0) {

            // Get some colors, not related to taxonomies, just need colors...
            ArrayList<Color> colors = BasePAreaTaxonomyLayout.getTaxonomyLevelColors();

            this.setBorder(BorderFactory.createTitledBorder(
                    BorderFactory.createLineBorder(
                            colors.get(group.getGroupId() % colors.size()),
                            2,
                            true),
                    String.format("Relationship Group %d", group.getGroupId())));
        }

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        ArrayList<Filterable<AttributeRelationship>> rels = entry.getSubEntries();

        rels.forEach((rel) -> {
            
            if (!rel.getCurrentFilter().isPresent() || rel.containsFilter(rel.getCurrentFilter().get())) {
                AttributeRelationshipEntryPanel relPanel = new AttributeRelationshipEntryPanel(
                        mainPanel, 
                        (FilterableAttributeRelationshipEntry) rel);

                this.add(relPanel);

                super.addSubEntryPanel(relPanel);
            }
            
        });
    }
}
