package edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy.parea;

import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.models.ChildNodeTableModel;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTPArea;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy.configuration.SCTPAreaTaxonomyConfiguration;

/**
 *
 * @author Chris O
 */
public class SCTChildPAreaTableModel extends ChildNodeTableModel<SCTPArea> {

    private final SCTPAreaTaxonomyConfiguration config;
    
    public SCTChildPAreaTableModel(SCTPAreaTaxonomyConfiguration config) {
        
        super(new String[] {
            "Child Partial-area", 
            "# Concepts", 
            "Area"
        });
        
        this.config = config;
    }

    @Override
    protected Object[] createRow(SCTPArea parea) {       

        return new Object[] {
            parea.getRoot().getName(),
            parea.getConceptCount(),
            config.getTextConfiguration().getGroupsContainerName(parea).replaceAll(", ", "\n")
        };
    }
}
