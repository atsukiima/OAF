
package edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy.parea.aggregate;

import SnomedShared.Concept;
import edu.njit.cs.saboc.blu.core.abn.GenericParentGroupInfo;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.models.ParentNodeTableModel;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTAggregatePArea;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy.configuration.SCTPAreaTaxonomyConfiguration;

/**
 *
 * @author Chris O
 */
public class SCTAggregateParentPAreaTableModel extends 
        ParentNodeTableModel<Concept, SCTAggregatePArea, GenericParentGroupInfo<Concept, SCTAggregatePArea>> {
    
    private final SCTPAreaTaxonomyConfiguration config;
            
    public SCTAggregateParentPAreaTableModel (SCTPAreaTaxonomyConfiguration config) {
        super(new String[] {
            "Parent Concept", 
            "Parent Concept ID", 
            "Parent Partial-area", 
            "# Concepts in Parent Aggregate Partial-area", 
            "# Aggregate Partial-areas",
            "Area"
        });
        
        this.config = config;
    }

    @Override
    protected Object[] createRow( GenericParentGroupInfo<Concept, SCTAggregatePArea> item) {
        String parentArea = config.getTextConfiguration().getGroupsContainerName(item.getParentGroup()).replaceAll(", ", "\n");

        return new Object[] {
            item.getParentConcept().getName(),
            item.getParentConcept().getId(),
            config.getTextConfiguration().getGroupName(item.getParentGroup()),
            item.getParentGroup().getAllGroupsConcepts().size(),
            item.getParentGroup().getAggregatedGroups().size(),
            parentArea
        };
    }
}
