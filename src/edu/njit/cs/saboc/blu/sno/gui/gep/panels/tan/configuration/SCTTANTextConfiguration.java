package edu.njit.cs.saboc.blu.sno.gui.gep.panels.tan.configuration;

import SnomedShared.Concept;
import SnomedShared.overlapping.CommonOverlapSet;
import SnomedShared.overlapping.EntryPoint;
import SnomedShared.overlapping.EntryPointSet;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.configuration.text.BLUPartitionedAbNTextConfiguration;
import edu.njit.cs.saboc.blu.sno.abn.tan.local.SCTCluster;
import edu.njit.cs.saboc.blu.sno.abn.tan.local.SCTTribalAbstractionNetwork;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

/**
 *
 * @author Chris O
 */
public class SCTTANTextConfiguration implements BLUPartitionedAbNTextConfiguration<SCTTribalAbstractionNetwork, CommonOverlapSet, SCTCluster, Concept> {

    private final SCTTribalAbstractionNetwork tan;

    public SCTTANTextConfiguration(SCTTribalAbstractionNetwork tan) {
        this.tan = tan;
    }
    
    @Override
    public String getAbNTypeName(boolean plural) {
        if(plural) {
            return "Tribal Abstraction Networks";
        } else {
            return "Tribal Abstraction Network";
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
    public String getContainerTypeName(boolean plural) {
        if (plural) {
            return "Bands";
        } else {
            return "Band";
        }
    }

    private String getCommaSeparatedBandName(HashSet<Long> tribeIds) {

        if (tribeIds.isEmpty()) {
            return "";
        }

        ArrayList<String> tribeNames = new ArrayList<>();

        tribeIds.forEach((Long tribeId) -> {
            tribeNames.add(tan.getConcepts().get(tribeId).getName());
        });

        Collections.sort(tribeNames);

        String bandName = tribeNames.get(0);

        for (int c = 1; c < tribeNames.size(); c++) {
            bandName += ", " + tribeNames.get(c);
        }

        return bandName;
    }

    @Override
    public String getContainerName(CommonOverlapSet band) {
        HashSet<Long> tribes = band.getSetEntryPoints();

        if (tribes.isEmpty()) {
            return "";
        } else {
            return getCommaSeparatedBandName(band.getSetEntryPoints());
        }
    }

    @Override
    public String getGroupsContainerName(SCTCluster group) {
        EntryPointSet entryPoints = group.getEntryPointSet();

        HashSet<Long> entryPointIds = new HashSet<>();

        entryPoints.forEach((EntryPoint ep) -> {
            entryPointIds.add(ep.getEntryPointConceptId());
        });

        if (entryPoints.isEmpty()) {
            return "";
        } else {
            return getCommaSeparatedBandName(entryPointIds);
        }
    }

    @Override
    public String getGroupTypeName(boolean plural) {
        if (plural) {
            return "Clusters";
        } else {
            return "Cluster";
        }
    }

    @Override
    public String getGroupName(SCTCluster cluster) {
        return cluster.getRoot().getName();
    }

    @Override
    public String getContainerHelpDescription(CommonOverlapSet container) {
        StringBuilder helpDescription = new StringBuilder();

        helpDescription.append("A <b>band</b> summarizes the set of all concepts which belong to the intersection of the"
                + " exact same subhierarchies. "
                + "That is, the concepts summarized by a band are all descendants of the same patriarch concepts (shown below)."
                + "Each concept belongs to exactly one band.");

        return helpDescription.toString();
    }

    @Override
    public String getGroupHelpDescriptions(SCTCluster group) {
        StringBuilder helpDescription = new StringBuilder();

        helpDescription.append("A <b>cluster</b> summarizes the subhierarchy of concepts at a specific point of intersection between two or more "
                + "subhierarchies.");

        return helpDescription.toString();
    }

    @Override
    public String getAbNName() {
        return tan.getTANName() + " Tribal Abstraction Network (TAN)";
    }

    @Override
    public String getAbNSummary() {
        String tanName = tan.getTANName();

        int conceptCount = tan.getConceptHierarchy().getConceptsInHierarchy().size();

        int bandCount = tan.getBands().size();
        int clusterCount = tan.getClusters().values().size();

        HashSet<Concept> intersectionConcepts = new HashSet<>();

        tan.getClusters().values().forEach((SCTCluster cluster) -> {
            if (cluster.getEntryPointSet().size() > 1) {
                intersectionConcepts.addAll(cluster.getConcepts());
            }
        });

        ArrayList<SCTCluster> patriarchClusters = tan.getHierarchyEntryPoints();

        ArrayList<SCTCluster> nonintersectingPatriarchClusters = tan.getNonOverlappingEntryPoints();

        String result = String.format("The <b>%s</b> Tribal Abstraction Network (TAN) summarizes %d concepts in %d band(s) and %d cluster(s). "
                + "There are a total of %d patriarch clusters that intersect. A total of %d concepts are descendants of more than one tribe.",
                tanName,
                conceptCount,
                bandCount,
                clusterCount,
                (patriarchClusters.size() - nonintersectingPatriarchClusters.size()),
                intersectionConcepts.size()
        );

        if (!nonintersectingPatriarchClusters.isEmpty()) {
            ArrayList<String> nonOverlappingClusterNames = new ArrayList<>();

            nonintersectingPatriarchClusters.forEach((SCTCluster cluster) -> {
                nonOverlappingClusterNames.add(
                        String.format("%s (%d)", cluster.getRoot().getName(), cluster.getConceptCount()));
            });

            Collections.sort(nonOverlappingClusterNames);

            String nonOverlappingClustersStr = nonOverlappingClusterNames.get(0);

            for (String s : nonOverlappingClusterNames) {
                nonOverlappingClustersStr += String.format("<br>%s", s);
            }

            result += String.format("<p>The following %d patriarch cluster(s) don't intersect with any other patriarch cluster: %s",
                    nonintersectingPatriarchClusters.size(), nonOverlappingClustersStr);
        }

        result += "<p><b>Help / Description:</b><br>";
        result += this.getAbNHelpDescription();

        return result;
    }

    @Override
    public String getAbNHelpDescription() {
        String result = "A <b>Tribal Abstraction Network (TAN)</b> is an abstraction network which summarizes the major points of intersection within a "
                + "SNOMED CT hierarchy. Given a hierarchy of SNOMED CT concepts, the children of the root of the hierarchy are defined as <i>patriarchs</i>. "
                + "Patriarchs are root concepts of subhierarchies within the overall hierarchy. The subhierarchies rooted at the patriarchs may intersect. "
                + "A given concept in the overall hierarchy may be a descendant of multiple patriarchs."
                + "<p>"
                + "The TAN is composed of two kinds of nodes: Bands and Clusters.<p>"
                + "A <b>band</b> summarizes the set of all concepts that are descendants of the exact same patriarchs. Bands are organized into "
                + "color coded levels according to the number of patriarchs their concepts are descendnats of (e.g., green is two). Bands are labeled "
                + "with this set of patriarchs, the total number of concepts which belong to the band, and the total number of clusters in the band."
                + "<p>"
                + "A <b>cluster</b> summarizes the subhierarchy of concepts that exists at one intersection point between two or more subhierarchies. "
                + "Clusters are shown as white boxes within each band. Clusters are named after the concept which is at the exact point of intersection. "
                + "The total number of concepts summarized by a cluster is shown in parenthesis."
                + "<p>"
                + "A TAN also summarizes the <i>Is a</i> hierarchy between concepts. Clicking on a cluster will show you the root's parents (in blue) "
                + "and child clusters, which summarize descendants of one or more additional patriarchs, in purple.";

        return result;
    }
}
