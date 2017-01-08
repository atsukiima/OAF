package edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy.configuration;

import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.aggregate.AggregatePArea;
import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.Area;
import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.PArea;
import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.PAreaTaxonomy;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.pareataxonomy.configuration.PAreaTaxonomyTextConfiguration;
import edu.njit.cs.saboc.blu.sno.localdatasource.concept.SCTEntityNameUtils;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Chris O
 */
public class SCTPAreaTaxonomyTextConfiguration extends PAreaTaxonomyTextConfiguration {

    public SCTPAreaTaxonomyTextConfiguration(PAreaTaxonomy taxonomy) {
        super(taxonomy);
    }

    @Override
    public String getContainerHelpDescription(Area node) {
        PAreaTaxonomy taxonomy = super.getPAreaTaxonomy();
        
        StringBuilder helpDesc = new StringBuilder();

        if (taxonomy.isAggregated()) {
            helpDesc.append("An <b>aggregate area</b> represents a set of concepts (shown below) which are defined using the "
                    + " exact same set of attribute relationships types."
                    + "<p><p>"
                    + "Additionally, an aggregate area will summarize one or more concepts "
                    + "from one or more areas with additional attribute relationships if this area"
                    + "has at least one aggregate partial-area. This information is viewable in the <i>aggregated partial-areas</i> tab.");
        } else {
            helpDesc.append("An <b>area</b> represents a set of concepts (shown below) which are defined using the exact same set of attribute relationship "
                    + "types.");
        }

        return helpDesc.toString();
    }

    @Override
    public String getNodeHelpDescription(PArea parea) {
        
        PAreaTaxonomy taxonomy = super.getPAreaTaxonomy();
        
        StringBuilder helpDesc = new StringBuilder();

        if (taxonomy.isAggregated()) {
            helpDesc.append("A <b>regular partial-area</b> summarizes a subhierarchy of structurally and semantically similar concepts in an ontology. "
                    + "All of the concepts in a regular partial-area are defined using the same types of attribute relationships."
                    + "All of the concepts summarized by a regular partial-area are descendants of the same concepts, called the root, "
                    + "which is the introduction point for the set of attribute relationships. "
                    + "A partial-area is named after the root and is labeled with the total number of concepts summarized by the partial-area (in parenthesis).");

            helpDesc.append("<p><p>");

            helpDesc.append("An <b>aggregate partial-area</b> summarizes the same content as a regular partial-area and "
                    + "one or more removed descendant partial-areas which were below the chosen minimum threshold and are defined using one or more additional "
                    + "attribute relationships in relation to the aggregated partial-area. "
                    + "To view the aggregated removed partial-areas, and which areas they came from, see the <i>aggregated partial-area</i> tab.");
        } else {
            helpDesc.append("A <b>partial-area</b> summarizes a subhierarchy of structurally and semantically similar concepts in SNOMED CT. "
                    + "All of the concepts in a partial-area are defined using the exact same types of attribute relationships. "
                    + "All of the concepts summarized by a partial-area are descendants of the same concept, called the root, "
                    + "which is the introduction point for the set of attribute relationships. "
                    + "A partial-area is named after the root and is labeled with the total number "
                    + "of concepts summarized by the partial-area (in parenthesis).");
        }

        return helpDesc.toString();
    }

    @Override
    public String getAbNName() {
        PAreaTaxonomy taxonomy = super.getPAreaTaxonomy();
        
        if (taxonomy.isAggregated()) {
            return String.format("%s Aggregate Partial-area Taxonomy", 
                    taxonomy.getRootPArea().getName());
        } else {
            return String.format("%s Partial-area Taxonomy", 
                     taxonomy.getRootPArea().getName());
        }
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

    @Override
    public String getAbNHelpDescription() {
        
        PAreaTaxonomy taxonomy = super.getPAreaTaxonomy();

        String pareaTaxonomyDesc = "A partial-area taxonomy is a type of abstraction network that summarizes structurally similar "
                + "and semantically similar concepts. The partial-area taxonomy reveals and highlights "
                + "high-level structural and hierarchical aggregation patterns in SNOMED CT. "
                + "<p>"
                + "A partial-area taxonomy is composed of two kinds of nodes. Areas summarize disjoint sets of concepts that are modeled using "
                + "the exact same types of attribute relationships. "
                + "The concepts summarized by an area are all structurally similar in that they summarize concepts that have the same relationship structure. "
                + "Areas are shown as colored boxes and are labeled with their set of attribute relationship types, the number of concepts summarized, "
                + "and the number of partial-areas."
                + "Areas are organized into color-coded levels according to their number of attribute relationship types."
                + "<p>"
                + "The second type of node, called the partial-area, summarizes subhierarchies of semantically similar concepts within each area. "
                + "Within each area there may be multiple such subhierarchies. Partial-areas are shown as white boxes "
                + "within each area. Each partial-area is named after the concept that is the root of the subhierarchy "
                + "and is labeled with the total number of concepts summarized by the partial-area in parenthesis. "
                + "<p>"
                + "The <i>Is a</i> hierarchy between concepts is also summarized by the partial-area taxonomy. When a given partial-area is selected then its "
                + "parent partial-areas are shown in blue and its child partial-areas are shown in purple.";

        if (taxonomy.isAggregated()) {
            String aggregateTaxonomyDesc = "An <b>aggregate partial-area taxonomy</b> is a partial-area taxonomy where the small partial-areas, which summarize "
                    + "a number of concepts below a chosen threshold, are not shown. The small removed partial-areas are aggregated into and summarized by"
                    + "their closest ancestor partial-area(s) which are above the chosen threshold. "
                    + "An aggregate partial-area, shown as a rounded rectangle, summarizes at least one removed partial-area. Regular partial-areas "
                    + "are partial-areas which summarize no removed partial-areas in an aggregate partial-area taxonomy.";

            return String.format("%s<p>%s", pareaTaxonomyDesc, aggregateTaxonomyDesc);
        } else {
            return pareaTaxonomyDesc;
        }
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
}
