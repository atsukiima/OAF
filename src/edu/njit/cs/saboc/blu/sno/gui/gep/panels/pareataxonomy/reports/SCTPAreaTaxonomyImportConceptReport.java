
package edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy.reports;

import SnomedShared.Concept;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.reports.AbNConceptLocationReportPanel;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.reports.entry.ImportedConceptNodeReport;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTPArea;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTPAreaTaxonomy;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy.configuration.SCTPAreaTaxonomyConfiguration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

/**
 *
 * @author Chris O
 */
public class SCTPAreaTaxonomyImportConceptReport extends AbNConceptLocationReportPanel<SCTPAreaTaxonomy, SCTPArea, Concept> {

    public SCTPAreaTaxonomyImportConceptReport(SCTPAreaTaxonomyConfiguration config) {
        super(config);
    }

    @Override
    protected ArrayList<ImportedConceptNodeReport<SCTPArea, Concept>> getConceptGroups(ArrayList<String> conceptIds) {
        SCTPAreaTaxonomyConfiguration configuration = (SCTPAreaTaxonomyConfiguration)getConfiguration();
        
        SCTPAreaTaxonomy taxonomy = configuration.getDataConfiguration().getPAreaTaxonomy();
        
        ArrayList<Long> sortedIds = new ArrayList<>();
        
        conceptIds.forEach((String idStr) -> {
            try {
                long id = Long.parseLong(idStr);

                sortedIds.add(id);
            } catch (NumberFormatException nfe) {
                
            }
        });

        Collections.sort(sortedIds);

        ArrayList<Concept> foundConcepts = new ArrayList<>();

        HashSet<Concept> hierarchyConcepts = taxonomy.getConceptHierarchy().getConceptsInHierarchy();
        
        hierarchyConcepts.forEach( (Concept c) -> {
            if(Collections.binarySearch(sortedIds, c.getId()) > -1) {
                foundConcepts.add(c);
            }
        });
        
        HashSet<SCTPArea> pareas = taxonomy.getGroupHierarchy().getNodesInHierarchy();
        
        ArrayList<ImportedConceptNodeReport<SCTPArea, Concept>> results = new ArrayList<>();
        
        foundConcepts.forEach((Concept concept) -> {
            HashSet<SCTPArea> conceptPAreas = new HashSet<>();
            
            pareas.forEach((SCTPArea parea) -> {
               if(parea.getHierarchy().getNodesInHierarchy().contains(concept)) {
                   conceptPAreas.add(parea);
               }
            });
            
            results.add(new ImportedConceptNodeReport<>(concept, conceptPAreas));
        });
        
        return results;
    }
}
