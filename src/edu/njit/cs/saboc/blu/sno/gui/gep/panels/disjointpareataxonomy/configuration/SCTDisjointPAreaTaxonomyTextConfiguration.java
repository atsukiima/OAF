package edu.njit.cs.saboc.blu.sno.gui.gep.panels.disjointpareataxonomy.configuration;

import SnomedShared.Concept;
import SnomedShared.pareataxonomy.InheritedRelationship;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.configuration.text.BLUDisjointAbNTextConfiguration;
import edu.njit.cs.saboc.blu.sno.abn.disjointpareataxonomy.DisjointPAreaTaxonomy;
import edu.njit.cs.saboc.blu.sno.abn.disjointpareataxonomy.DisjointPartialArea;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTPArea;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

/**
 *
 * @author Chris O
 */
public class SCTDisjointPAreaTaxonomyTextConfiguration implements BLUDisjointAbNTextConfiguration<DisjointPAreaTaxonomy, DisjointPartialArea, SCTPArea, Concept> {

    private final DisjointPAreaTaxonomy disjointTaxonomy;

    public SCTDisjointPAreaTaxonomyTextConfiguration(DisjointPAreaTaxonomy disjointTaxonomy) {
        this.disjointTaxonomy = disjointTaxonomy;
    }

    @Override
    public String getAbNTypeName(boolean plural) {
        if (plural) {
            return "Disjoint Partial-area Taxonomies";
        } else {
            return "Disjoint Partial-area Taxonomy";
        }
    }

    @Override
    public String getGroupTypeName(boolean plural) {
        if (plural) {
            return "Disjoint Partial-areas";
        } else {
            return "Disjoint Partial-area";
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
    public String getOverlappingGroupTypeName(boolean plural) {
        if (plural) {
            return "Partial-areas";
        } else {
            return "Partial-area";
        }
    }

    @Override
    public String getOverlappingGroupName(SCTPArea parea) {
        return parea.getRoot().getName();
    }

    @Override
    public String getConceptName(Concept concept) {
        return concept.getName();
    }

    @Override
    public String getGroupName(DisjointPartialArea group) {
        return group.getRoot().getName();
    }

    @Override
    public String getGroupHelpDescriptions(DisjointPartialArea group) {
        StringBuilder helpDescription = new StringBuilder();

        helpDescription.append("A <b>disjoint partial-area</b> represents a summary of a disjoint hierarchy of concepts within a partial-area taxonomy area. ");

        helpDescription.append("A <b>basis</b> (non-overlapping) disjoint partial-area is a disjoint partial-area that summarizes all of the concepts "
                + "that are summarized by exactly one partial-area in the complete partial-area taxonomy. "
                + "Basis disjoint partial-areas are assigned one color and are named after this partial-area.");

        helpDescription.append("An <b>overlapping</b> disjoint partial-area is a disjoint partial-area that summarizes a set of concepts that are summarized "
                + "by two or more partial-areas in the complete partial-area taxonomy. Overlapping disjoint partial-areas are color coded according to the "
                + "partial-areas in which their concepts overlap and are named after the concept which is the point of intersection between the partial-areas. "
                + "There may be multiple points of intersection, thus, there may be many similarly color coded overlapping disjoint partial-areas.");

        return helpDescription.toString();
    }

    private String getCommaDelimitedAreaName(HashSet<InheritedRelationship> rels) {
        ArrayList<String> sortedRelLabels = new ArrayList<>();

        HashMap<Long, String> parentTaxonomyRels = disjointTaxonomy.getParentAbstractionNetwork().getLateralRelsInHierarchy();

        rels.forEach((InheritedRelationship rel) -> {
            String relName = parentTaxonomyRels.get(rel.getRelationshipTypeId());

            sortedRelLabels.add(relName);
        });

        Collections.sort(sortedRelLabels);

        String areaLabel = sortedRelLabels.get(0);

        for (int c = 1; c < sortedRelLabels.size(); c++) {
            areaLabel += String.format(", %s", sortedRelLabels.get(c));
        }

        return areaLabel;
    }

    @Override
    public String getAbNName() {
        SCTPArea aPArea = disjointTaxonomy.getOverlappingGroups().iterator().next();

        String areaName = getCommaDelimitedAreaName(aPArea.getRelationships());

        return areaName + " Disjoint Partial-area Taxonomy";
    }

    @Override
    public String getAbNSummary() {
        SCTPArea aPArea = disjointTaxonomy.getOverlappingGroups().iterator().next();

        String areaName = getCommaDelimitedAreaName(aPArea.getRelationships());

        String parentPAreaTaxonomyName = disjointTaxonomy.getParentAbstractionNetwork().getRootGroup().getRoot().getName();

        int overlappingPAreaCount = disjointTaxonomy.getOverlappingGroups().size();

        int disjointPAreaCount = disjointTaxonomy.getGroups().values().size();

        int conceptCount = 0;

        HashSet<DisjointPartialArea> disjointPAreas = disjointTaxonomy.getDisjointGroups();

        HashMap<Integer, HashSet<DisjointPartialArea>> levels = new HashMap<>();

        for (DisjointPartialArea disjointPArea : disjointPAreas) {
            int level = disjointPArea.getOverlaps().size();

            if (!levels.containsKey(level)) {
                levels.put(level, new HashSet<>());
            }

            levels.get(level).add(disjointPArea);

            conceptCount += disjointPArea.getConceptCount();
        }

        ArrayList<Integer> sortedLevels = new ArrayList<>(levels.keySet());
        Collections.sort(sortedLevels);

        String summary = String.format("The {%s} disjoint partial-area taxonomy, "
                + "derived from the %s partial-area taxonomy, "
                + "summarizes the overlap between %d partial-areas. "
                + "The disjoint partial-area taxonomy summarizes %d concepts in %d disjoint partial-areas.",
                areaName,
                parentPAreaTaxonomyName,
                overlappingPAreaCount,
                conceptCount,
                disjointPAreaCount);

        summary += "<p><b>Overlapping Concept Distribution:</b><br>";
        for (Integer level : sortedLevels) {

            if (level == 1) {
                continue;
            }

            HashSet<DisjointPartialArea> levelDisjointPAreas = levels.get(level);

            int levelClassCount = 0;

            for (DisjointPartialArea levelDisjointPArea : levelDisjointPAreas) {
                levelClassCount += levelDisjointPArea.getConceptCount();
            }

            summary += String.format("Level %d: %d Disjoint Partial-areas, %d Concepts<br>", level, levelDisjointPAreas.size(), levelClassCount);
        }

        summary += "<p><b>Help / Description:</b><br>";

        summary += getAbNHelpDescription();

        return summary;
    }

    @Override
    public String getAbNHelpDescription() {
        String description = "A disjoint partial-area taxonomy is an abstraction network, derived from an area in a complete "
                + "partial-area taxonomy, that partitions the selected area's concepts into disjoint groups called disjoint partial-areas. "
                + "The disjoint partial-areas are organized into a hierarchy based on the underlying <i>Is a</i> hierarchy. In comparison to the "
                + "partial-area taxonomy, the disjoint partial-area taxonomy provides a more accurate picture of the concept hierarchy within an area,"
                + "since concepts may be summarized by multiple partial-areas (called <i>overlapping concepts</i>). We note that partial-areas "
                + "that do not have any overlapping concepts are not considered when creating the disjoint partial-area taxonomy."
                + "<p>"
                + "Disjoint partial-areas that do not contain any overlapping concepts (called basis disjoint partial-areas, or non-overlapping disjoint partial-areas), "
                + "are give a single color and are located at the top of the disjoint partial-area taxonomy abstraction network. Overlapping disjoint partial-areas,"
                + "which summarize one or more overlapping concepts, are color coded according to which partial-areas their concepts overlap between. Overlapping "
                + "disjoint partial-areas are organized into levels according to their degree of overlap, i.e., how many partial-areas their concepts are summarized by.";

        return description;
    }
}
