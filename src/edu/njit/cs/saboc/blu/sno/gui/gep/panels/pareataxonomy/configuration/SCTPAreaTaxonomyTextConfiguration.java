package edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy.configuration;

import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.aggregate.AggregatePArea;
import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.Area;
import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.PArea;
import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.PAreaTaxonomy;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.pareataxonomy.configuration.PAreaTaxonomyTextConfiguration;
import edu.njit.cs.saboc.blu.sno.localdatasource.concept.SCTEntityNameConfiguration;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Chris O
 */
public class SCTPAreaTaxonomyTextConfiguration extends PAreaTaxonomyTextConfiguration {

    public SCTPAreaTaxonomyTextConfiguration(PAreaTaxonomy taxonomy) {
        super(new SCTEntityNameConfiguration(), taxonomy);
    }

    private String getRegularPAreaTaxonomySummary() {
        PAreaTaxonomy taxonomy = super.getPAreaTaxonomy();

        String rootName = taxonomy.getRootPArea().getName();

        int pareaCount = taxonomy.getNodeCount();
        int areaCount = taxonomy.getAreaTaxonomy().getNodeCount();

        int conceptCount = taxonomy.getSourceHierarchy().size();

        String result = String.format("<html>The <b>%s</b> partial-area taxonomy "
                + "summarizes a total of %d concept(s) in %d area(s) and %d partial-areas(s).",
                
                rootName, conceptCount, areaCount, pareaCount);

        return result;
    }

    private String getAggregatePAreaTaxonomySummary() {
        PAreaTaxonomy taxonomy = super.getPAreaTaxonomy();
        
        String rootName = taxonomy.getRootPArea().getName();

        int areaCount = taxonomy.getAreaTaxonomy().getNodeCount();
        int conceptCount = taxonomy.getSourceHierarchy().size();
        
        HashSet<AggregatePArea> aggregatePAreas = new HashSet<>();

        HashSet<AggregatePArea> regularPAreas = new HashSet<>();
        
        Set<Area> areas = taxonomy.getAreas();

        areas.forEach((area) -> {
            
            Set<PArea> pareas = area.getPAreas();

            pareas.forEach((parea) -> {
                AggregatePArea aggregatePArea = (AggregatePArea) parea;

                if (aggregatePArea.getAggregatedNodes().isEmpty()) {
                    regularPAreas.add(aggregatePArea);
                } else {
                    aggregatePAreas.add(aggregatePArea);
                }
            });
        });

        HashSet<PArea> removedPAreas = new HashSet<>();

        aggregatePAreas.forEach((parea) -> {
            removedPAreas.addAll(parea.getAggregatedNodes());
        });

        String result = String.format("<html>The <b>%s</b> aggregate partial-area "
                + "taxonomy summarizes a total of %d concept(s) in %d area(s), %d "
                + "aggregate partial-area(s), %d regular partial-area(s), and %d removed partial-area(s).",
                rootName,
                conceptCount,
                areaCount,
                aggregatePAreas.size(),
                regularPAreas.size(),
                removedPAreas.size());

        return result;
    }

    @Override
    public String getAbNSummary() {
        PAreaTaxonomy taxonomy = super.getPAreaTaxonomy();
        
        String summary;

        if (taxonomy.isAggregated()) {
            summary = getAggregatePAreaTaxonomySummary();
        } else {
            summary = getRegularPAreaTaxonomySummary();
        }

        summary += "<p><b>Help / Description:</b><br>";
        summary += getAbNHelpDescription();

        return summary;
    }
}
