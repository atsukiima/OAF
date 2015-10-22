package edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy.configuration;

import SnomedShared.Concept;
import SnomedShared.pareataxonomy.InheritedRelationship;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.pareataxonomy.configuration.BLUGenericPAreaTaxonomyTextConfiguration;
import edu.njit.cs.saboc.blu.sno.abn.disjointpareataxonomy.DisjointPAreaTaxonomy;
import edu.njit.cs.saboc.blu.sno.abn.disjointpareataxonomy.DisjointPartialArea;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTAggregatePArea;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTArea;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTPArea;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTPAreaTaxonomy;
import edu.njit.cs.saboc.blu.sno.datastructure.hierarchy.SCTConceptHierarchy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

/**
 *
 * @author Chris O
 */
public class SCTPAreaTaxonomyTextConfiguration extends BLUGenericPAreaTaxonomyTextConfiguration<
        SCTPAreaTaxonomy, DisjointPAreaTaxonomy, SCTArea, SCTPArea, SCTAggregatePArea, DisjointPartialArea, SCTConceptHierarchy, InheritedRelationship, Concept> {

    private final SCTPAreaTaxonomy taxonomy;

    public SCTPAreaTaxonomyTextConfiguration(SCTPAreaTaxonomy taxonomy) {
        this.taxonomy = taxonomy;
    }

    public SCTPAreaTaxonomy getPAreaTaxonomy() {
        return taxonomy;
    }

    @Override
    public String getDisjointGroupName(DisjointPartialArea group) {
        return group.getRoot().getName();
    }

    private String getRelationshipNamesCommaSeparated(HashSet<InheritedRelationship> rels) {

        if (rels.isEmpty()) {
            return "";
        }

        ArrayList<String> relNames = new ArrayList<>();

        rels.forEach((InheritedRelationship rel) -> {
            relNames.add(taxonomy.getLateralRelsInHierarchy().get(rel.getRelationshipTypeId()));
        });

        Collections.sort(relNames);

        String areaName = relNames.get(0);

        for (int c = 1; c < relNames.size(); c++) {
            areaName += ", " + relNames.get(c);
        }

        return areaName;
    }

    @Override
    public String getContainerName(SCTArea container) {
        if (container.getRelationships().isEmpty()) {
            return "root area (no attribute relationships)";
        } else {
            return getRelationshipNamesCommaSeparated(container.getRelationships());
        }
    }
    
    @Override
    public String getConceptTypeName(boolean plural) {
        if (plural) {
            return "Concepts";
        } else {
            return "Concept";
        }
    }

    @Override
    public String getConceptName(Concept concept) {
        return concept.getName();
    }
    
    @Override
    public String getConceptUniqueIdentifier(Concept concept) {
        return Long.toString(concept.getId());
    }

    @Override
    public String getGroupName(SCTPArea group) {
        return group.getRoot().getName();
    }

    @Override
    public String getContainerHelpDescription(SCTArea container) {
        StringBuilder helpDesc = new StringBuilder();

        if (taxonomy.isReduced()) {
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
    public String getGroupHelpDescriptions(SCTPArea group) {
        StringBuilder helpDesc = new StringBuilder();

        if (taxonomy.isReduced()) {
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
        if (taxonomy.isReduced()) {
            return String.format("%s Aggregate Partial-area Taxonomy", getGroupName(taxonomy.getRootGroup()));
        } else {
            return String.format("%s Partial-area Taxonomy", getGroupName(taxonomy.getRootGroup()));
        }
    }

    private String getRegularPAreaTaxonomySummary() {

        String rootName = getGroupName(taxonomy.getRootGroup());

        int pareaCount = taxonomy.getPAreaCount();
        int areaCount = taxonomy.getAreaCount();
        
        HashSet<Concept> concepts = new HashSet<>();
        
        taxonomy.getGroupHierarchy().getNodesInHierarchy().forEach( (SCTPArea parea) -> {
            concepts.addAll(parea.getConceptsInPArea());
        });
        
        int conceptCount = concepts.size();

        String result = String.format("<html>The <b>%s</b> partial-area taxonomy summarizes a total of %d concept(s) in %d area(s) and %d partial-areas(s).",
                rootName, conceptCount, areaCount, pareaCount);

        return result;
    }

    private String getAggregatePAreaTaxonomySummary() {
        String rootName = getGroupName(taxonomy.getRootGroup());

        int areaCount = taxonomy.getAreaCount();
        
        HashSet<Concept> concepts = new HashSet<>();

        taxonomy.getGroupHierarchy().getNodesInHierarchy().forEach((SCTPArea parea) -> {
            concepts.addAll(parea.getConceptsInPArea());
        });

        int conceptCount = concepts.size();
        
        HashSet<SCTAggregatePArea> aggregatePAreas = new HashSet<>();

        HashSet<SCTAggregatePArea> regularPAreas = new HashSet<>();

        taxonomy.getAreas().forEach((SCTArea area) -> {
            ArrayList<SCTPArea> pareas = area.getAllPAreas();

            pareas.forEach((SCTPArea parea) -> {
                SCTAggregatePArea aggregatePArea = (SCTAggregatePArea) parea;

                if (aggregatePArea.getAggregatedGroups().isEmpty()) {
                    regularPAreas.add(aggregatePArea);
                } else {
                    aggregatePAreas.add(aggregatePArea);
                }
            });
        });

        HashSet<SCTPArea> removedPAreas = new HashSet<>();

        aggregatePAreas.forEach((SCTAggregatePArea parea) -> {
            removedPAreas.addAll(parea.getAggregatedGroups());
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
        String summary;

        if (taxonomy.isReduced()) {
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

        if (taxonomy.isReduced()) {
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
    public String getGroupsContainerName(SCTPArea parea) {
        return this.getRelationshipNamesCommaSeparated(parea.getRelationships());
    }
}
