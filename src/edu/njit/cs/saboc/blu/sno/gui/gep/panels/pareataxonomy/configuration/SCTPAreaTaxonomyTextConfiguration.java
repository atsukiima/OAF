package edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy.configuration;

import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.PAreaTaxonomy;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.pareataxonomy.configuration.PAreaTaxonomyTextConfiguration;
import edu.njit.cs.saboc.blu.sno.localdatasource.concept.SCTEntityNameConfiguration;

/**
 *
 * @author Chris O
 */
public class SCTPAreaTaxonomyTextConfiguration extends PAreaTaxonomyTextConfiguration {

    public SCTPAreaTaxonomyTextConfiguration(PAreaTaxonomy taxonomy) {
        super(new SCTEntityNameConfiguration(), taxonomy);
    }
    
}
