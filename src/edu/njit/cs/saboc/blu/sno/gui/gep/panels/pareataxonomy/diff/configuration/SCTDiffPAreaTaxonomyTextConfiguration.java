package edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy.diff.configuration;

import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.Area;
import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.PArea;
import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.PAreaTaxonomy;
import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.diff.DiffPAreaTaxonomy;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.pareataxonomy.diff.configuration.DiffPAreaTaxonomyTextConfiguration;
import edu.njit.cs.saboc.blu.sno.localdatasource.concept.SCTEntityNameUtils;

/**
 *
 * @author Chris O
 */
public class SCTDiffPAreaTaxonomyTextConfiguration extends DiffPAreaTaxonomyTextConfiguration {

    public SCTDiffPAreaTaxonomyTextConfiguration(DiffPAreaTaxonomy taxonomy) {
        super(taxonomy);
    }

    @Override
    public String getContainerHelpDescription(Area area) {
        return "[SCT DIFF AREA HELP DESCRIPTION]";
    }

    @Override
    public String getConceptTypeName(boolean plural) {
        return SCTEntityNameUtils.getConceptTypeName(plural);
    }

    @Override
    public String getPropertyTypeName(boolean plural) {
        return SCTEntityNameUtils.getPropertyTypeName(plural);
    }

    @Override
    public String getParentConceptTypeName(boolean plural) {
        return SCTEntityNameUtils.getParentConceptTypeName(plural);
    }

    @Override
    public String getChildConceptTypeName(boolean plural) {
        return SCTEntityNameUtils.getChildConceptTypeName(plural);
    }
    
    @Override
    public String getNodeHelpDescription(PArea parea) {
        return "[SCT DIFF PAREA HELP DESCRIPTION]";
    }

    private String getDiffPAreaTaxonomySummary() {
        return "[OWL DIFF PAREA TAXONOMY SUMMARY]";
    }
    
    @Override
    public String getAbNSummary() {
        
        PAreaTaxonomy taxonomy = super.getPAreaTaxonomy();
        
        String summary = "";
        

        summary += "<p><b>Help / Description:</b><br>";
        summary += getAbNHelpDescription();
        
        return summary;
    }
    
    @Override
    public String getAbNHelpDescription() {
        return "[SCT DIFF PAREA TAXONOMY HELP INFORMATION]";
    }
}
