
package edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy.area;

import SnomedShared.Concept;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.pareataxonomy.AreaPanel;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTArea;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTPArea;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy.configuration.SCTPAreaTaxonomyConfiguration;

/**
 *
 * @author Chris O
 */
public class SCTAreaPanel extends AreaPanel<SCTArea, SCTPArea, Concept, SCTPAreaTaxonomyConfiguration> {
    
    private final SCTPAreaTaxonomyConfiguration config;

    public SCTAreaPanel(SCTPAreaTaxonomyConfiguration config) {
        super(new SCTAreaDetailsPanel(config), 
                new SCTAreaPAreaListPanel(config), 
                new SCTAreaDisjointMetricsPanel(config),
                config);

        this.config = config;
    }

    @Override
    protected String getNodeTitle(SCTArea area) {
        if(area.getRelationships().isEmpty()) {
            return "root area (no attribute relationships)";
        } else {
            return config.getTextConfiguration().getContainerName(area);
        }
    }
}
