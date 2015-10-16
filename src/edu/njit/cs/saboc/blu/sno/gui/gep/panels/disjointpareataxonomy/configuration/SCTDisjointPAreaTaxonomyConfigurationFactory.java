
package edu.njit.cs.saboc.blu.sno.gui.gep.panels.disjointpareataxonomy.configuration;

import edu.njit.cs.saboc.blu.sno.abn.disjointpareataxonomy.DisjointPAreaTaxonomy;
import edu.njit.cs.saboc.blu.sno.gui.abnselection.SCTDisplayFrameListener;

/**
 *
 * @author Chris O
 */
public class SCTDisjointPAreaTaxonomyConfigurationFactory {
    public SCTDisjointPAreaTaxonomyConfiguration createConfiguration(DisjointPAreaTaxonomy disjointTaxonomy, SCTDisplayFrameListener displayListener) {
        
        SCTDisjointPAreaTaxonomyConfiguration disjointConfiguration = new SCTDisjointPAreaTaxonomyConfiguration();
        disjointConfiguration.setDataConfiguration(new SCTDisjointPAreaTaxonomyDataConfiguration(disjointTaxonomy));
        disjointConfiguration.setUIConfiguration(new SCTDisjointPAreaTaxonomyUIConfiguration(disjointConfiguration, displayListener));
        disjointConfiguration.setTextConfiguration(new SCTDisjointPAreaTaxonomyTextConfiguration(disjointTaxonomy));
        
        return disjointConfiguration;
    }
}
