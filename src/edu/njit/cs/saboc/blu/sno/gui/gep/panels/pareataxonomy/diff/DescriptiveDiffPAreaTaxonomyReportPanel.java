package edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy.diff;

import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.pareataxonomy.diff.DiffPAreaTaxonomyDetailsPanel;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy.diff.configuration.SCTDiffPAreaTaxonomyConfiguration;
import edu.njit.cs.saboc.blu.sno.localdatasource.concept.SCTConcept;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Chris O
 */
public class DescriptiveDiffPAreaTaxonomyReportPanel extends DiffPAreaTaxonomyDetailsPanel {
    
    private final DescriptiveDeltaConceptList ddConceptList;
    
    public DescriptiveDiffPAreaTaxonomyReportPanel(SCTDiffPAreaTaxonomyConfiguration config) {
        super(config);

        this.ddConceptList = new DescriptiveDeltaConceptList(config);
        
        Set<SCTConcept> allConcepts = new HashSet<>();
        
        config.getPAreaTaxonomy().getPAreas().forEach( (diffPArea) -> {
            allConcepts.addAll((Set<SCTConcept>)(Set<?>)diffPArea.getConcepts());
        });
        
        ArrayList<SCTConcept> sortedConcepts = new ArrayList<>(allConcepts);
        
        sortedConcepts.sort( (a,b) -> {
            return a.getName().compareToIgnoreCase(b.getName());
        });
        
        ddConceptList.setContents(sortedConcepts);
        
        this.addDetailsTab("Descriptive Delta Report", ddConceptList);
    }
}
