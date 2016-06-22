
package edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy.parea.aggregate;

import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.models.OAFAbstractTableModel;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTPArea;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy.configuration.SCTPAreaTaxonomyConfiguration;

/**
 *
 * @author Chris O
 */
public class SCTAggregatedPAreaListTableModel extends OAFAbstractTableModel<SCTPArea> {
    
    protected final SCTPAreaTaxonomyConfiguration configuration;
    
    public SCTAggregatedPAreaListTableModel(SCTPAreaTaxonomyConfiguration configuration) {
        super(
                new String[] {
                    "Partial-area",
                    "# Concepts",
                    "Area"
            });
        
        this.configuration = configuration;
    }

    @Override
    protected Object[] createRow(SCTPArea parea) {
        
        String areaName = configuration.getTextConfiguration().getGroupsContainerName(parea);
        
        return new Object [] {
            configuration.getTextConfiguration().getGroupName(parea),
            parea.getConceptCount(),
            areaName.replaceAll(", ", "\n")
        };
    }
}