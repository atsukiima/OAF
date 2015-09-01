package edu.njit.cs.saboc.blu.sno.graph.layout;

import SnomedShared.overlapping.ClusterSummary;
import SnomedShared.overlapping.CommonOverlapSet;
import SnomedShared.overlapping.EntryPointSet;
import SnomedShared.overlapping.OverlapInheritancePartition;
import edu.njit.cs.saboc.blu.core.graph.BluGraph;
import edu.njit.cs.saboc.blu.core.graph.edges.GraphGroupLevel;
import edu.njit.cs.saboc.blu.core.graph.edges.GraphLane;
import edu.njit.cs.saboc.blu.core.graph.edges.GraphLevel;
import edu.njit.cs.saboc.blu.core.graph.layout.GraphLayoutConstants;
import edu.njit.cs.saboc.blu.core.graph.nodes.GenericGroupEntry;
import edu.njit.cs.saboc.blu.core.graph.options.GraphOptions;
import edu.njit.cs.saboc.blu.sno.abn.tan.TribalAbstractionNetwork;
import edu.njit.cs.saboc.blu.sno.graph.tan.BluCluster;
import edu.njit.cs.saboc.blu.sno.graph.tan.BluCommonOverlapSet;
import edu.njit.cs.saboc.blu.sno.graph.tan.BluOverlapPartition;
import edu.njit.cs.saboc.blu.sno.utils.UtilityMethods;
import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * TODO: This has to be converted to use the "CREATE PARTITION LABEL" approach, as used in NoPartition Layout
 * @author Chris
 */
public class ClusterPartitionLayout extends GenericClusterLayout {

    public ClusterPartitionLayout(BluGraph graph) {
        super(graph, (TribalAbstractionNetwork) graph.getAbstractionNetwork());
    }

    public void doLayout(GraphOptions options, boolean showConceptCounts) {

        HashMap<Long, String> hierarchyEntryPointNames = hierarchyData.getPatriarchNames();

        ArrayList<CommonOverlapSet> overlapSets =
                (ArrayList<CommonOverlapSet>)hierarchyData.getBands().clone();

        for(int c = overlapSets.size() - 1; c >= 0; c--) {
            if(overlapSets.get(c).getSetEntryPoints().size() == 1) {
                overlapSets.remove(c);
            }
        }


        ArrayList<ClusterSummary> entryPoints = hierarchyData.getHierarchyEntryPoints();
        HashSet<Long> fakeEPSet = new HashSet<Long>();
        fakeEPSet.add(0l);

        CommonOverlapSet entryPointSet = new CommonOverlapSet(-1, fakeEPSet);

        for(ClusterSummary cluster : entryPoints) {
            ClusterSummary newCluster = new ClusterSummary(cluster.getId(), cluster.getHeaderConcept(),
                    cluster.getConceptCount(), cluster.getParentIds(), new EntryPointSet());

            entryPointSet.addClusterToSet(newCluster);
        }
        
        overlapSets.add(entryPointSet);

        commonOverlapSets = overlapSets;

        super.doLayout();

        CommonOverlapSet lastSet = null;   // Used for generating the graph - this is the data version of an area
        BluCommonOverlapSet currentSet = null;    // Used for generating the graph - this is the graphical representation of an area
        BluOverlapPartition currentPartition = null;
        GraphLevel currentLevel = null; // This is used as a temporary variable in this method to hold the current level.
        GraphGroupLevel currentClusterLevel = null; // Used for generating the graph

        // These are a set of styles such that each new row is given a different color.
        Color[] background = {
            new Color(255, 255, 255),
            new Color(200, 200, 200),
            new Color(55, 213, 102),
            new Color(121, 212, 250),
            new Color(242, 103, 103),
            new Color(232, 255, 114),
            Color.cyan,
            Color.orange,
            Color.pink,
            Color.green,
            Color.yellow
        };

        int areaX = 0;  // The first area on each line is given an areaX value of 0.
        int areaY = 0;  // The first row of areas is given an areaY value of 0.
        int pAreaX, pAreaY;
        int x = 0, y = 20, width = 0, height = 0, maxHeight = 0, maxRowWidth;
        int style = 0;

        currentLevel = null; // This is used as a temporary variable in this method to hold the current level.
        currentSet = null;
        currentPartition = null;
        currentClusterLevel = null;

        addGraphLevel(new GraphLevel(0, graph, new ArrayList<GraphLane>())); // Add the first level of areas (the single pArea 0-relationship level) to the data representation of the graph.

        HashMap<OverlapInheritancePartition, String> regionLabels = new HashMap<OverlapInheritancePartition, String>();

        for (CommonOverlapSet a : layoutGroupContainers) {  // Loop through the areas and generate the diagram for each of them
            BluCommonOverlapSet setEntry;
            ArrayList<OverlapInheritancePartition> partitions;

            int maxRows, x2, y2, regionX, regionBump;

            int[] areaPAreaX;

            if (lastSet != null && lastSet.getSetEntryPoints().size() != a.getSetEntryPoints().size()) { // If a new row should be created...

                x = 0;  // Reset the x coordinate to the left
                y += maxHeight + GraphLayoutConstants.CONTAINER_ROW_HEIGHT; // Add the height of the tallest area to the y coordinate plus the areaRowHeight variable which defines how
                // much space should be between rows of areas.

                areaY++;    // Update the areaY variable to reflect the new row.
                areaX = 0;  // Reset the areaX variable.

                maxHeight = 0;  // Reset the maxHeight variable since this is a new row.
                style++;    // Update the style variable which is used to display different colors for the different rows.

                addGraphLevel(new GraphLevel(areaY, graph,
                        generateUpperRowLanes(-5, GraphLayoutConstants.CONTAINER_ROW_HEIGHT - 7, 3, null))); // Add a level object to the arrayList in the dataGraph object
            }

            width = 0;
            height = 0;
            maxRows = 0;

            partitions = a.getOverlapPartitions();

            for (OverlapInheritancePartition partition : partitions) { // Loop through regions to calculate the necessary size for the area.

                int clusterCount = 0;

                if (options != null) {
                    for (ClusterSummary parea : partition.getClusters()) {
                        int conceptCount = parea.getConceptCount();

                        if (conceptCount <= options.pareaMaxThreshold && conceptCount >= options.pareaMinThreshold) {
                            clusterCount++;
                        }
                    }
                } else {
                    clusterCount = partition.getClusters().size();
                }

                int clustersWide;

                if(a.getId() == -1) {
                    clustersWide = clusterCount;
                } else {
                    clustersWide = (int) Math.ceil(Math.sqrt(clusterCount));
                }
               
                int regionWidth = clustersWide * (GenericGroupEntry.ENTRY_WIDTH + GraphLayoutConstants.GROUP_CHANNEL_WIDTH);

                String regionName = "";

                if(a.getId() >= 0) {
                    regionName = UtilityMethods.getOverlapPartitionName(partition.getEntryPointSet(), hierarchyEntryPointNames);
                } else {

                    if(a.getId() == -2) {
                        regionName = "Hierarchy Root";
                    } else if(a.getId() == -1) {
                        regionName = "Cluster Abstraction Network Entry Points";
                    }
                }
                 
                if (showConceptCounts) {
                    ArrayList<Integer> pareaIds = new ArrayList<Integer>();

                    ArrayList<ClusterSummary> clusters = partition.getClusters();

                    for (ClusterSummary cluster : clusters) {
                        pareaIds.add(cluster.getId());
                    }

                    int conceptCount = 0; 
                    
                    if (conceptCount == 1) {
                        regionName += " (1 Concept)";
                    } else {
                        regionName += " (" + conceptCount + " Concepts)";
                    }

                } else {
                    if (partition.getClusters().size() == 1) {
                        regionName += " (1 Cluster)";
                    } else {
                        regionName += " (" + partition.getClusters().size() + " Clusters)";
                    }
                }

                regionLabels.put(partition, regionName);

                if (regionName.length() > (regionWidth / 7 * 2)) // Assuming ~7 pixels per character, this checks to see if the label text can fit on
                {                                                // two lines. If not, it widens the cell to make it fit.
                    regionWidth = regionName.length() * 7 / 2;
                }

                width += regionWidth + 20;

                height = Math.max(height, ((int) (Math.ceil((double) clusterCount / clustersWide)))
                        * (GenericGroupEntry.ENTRY_HEIGHT + GraphLayoutConstants.GROUP_ROW_HEIGHT));  // Set the height to the greater of (a) the current height or (b) the number of regions in a column times the height of each pArea and the space between them.

                maxRows = Math.max(maxRows, (int) Math.ceil(Math.sqrt(clusterCount))); // Update the maxRows variable.
            }

            width += 20;
            height += 60 + GraphLayoutConstants.GROUP_ROW_HEIGHT;

            if (height > maxHeight) // Keeps track of the tallest cell on a row so that it knows how much lower to position the next row to avoid overlap.
            {
                maxHeight = height;
            }

            currentLevel = levels.get(areaY);

            Color color = background[style % background.length];

            setEntry = createCommonOverlapSetPanel(a, x, y, width, height, color, areaX, currentLevel); // Create the area

            containerEntries.put(a.getId(), setEntry);

            currentLevel.addContainerEntry(setEntry);    // Add a data representation for this new area to the current area Level obj.
            addColumn(areaX, currentLevel.getLevelY(), generateColumnLanes(-3,
                    GraphLayoutConstants.CONTAINER_CHANNEL_WIDTH - 5, 3, null)); // Generates a column of lanes to the left of this area.


            currentSet = (BluCommonOverlapSet) currentLevel.getContainerEntries().get(areaX);

            regionX = GraphLayoutConstants.PARTITION_CHANNEL_WIDTH;

            areaPAreaX = new int[maxRows];
            regionBump = 0;

            for (OverlapInheritancePartition part : partitions) { // Actually draw the regions inside the newly created area.

                BluOverlapPartition r;
                int horizontalPAreas, regionWidth;

                String regionName;

                int pareaCount = 0;

                if (options != null) {
                    for (ClusterSummary cluster : part.getClusters()) {
                        int conceptCount = cluster.getConceptCount();

                        if (conceptCount <= options.pareaMaxThreshold && conceptCount >= options.pareaMinThreshold) {
                            pareaCount++;
                        }
                    }
                } else {
                    pareaCount = part.getClusters().size();
                }

                x2 = (int) (1.5 * GraphLayoutConstants.GROUP_CHANNEL_WIDTH);
                y2 = GraphLayoutConstants.PARTITION_ROW_HEIGHT;

                pAreaX = 0;
                pAreaY = 0;

                if(a.getId() == -1) {
                    horizontalPAreas = pareaCount;
                } else {
                    horizontalPAreas = (int) Math.ceil(Math.sqrt(pareaCount));
                }
                
                regionWidth = horizontalPAreas * (GenericGroupEntry.ENTRY_WIDTH + GraphLayoutConstants.GROUP_CHANNEL_WIDTH);

                String fullRegionName = regionLabels.get(part);
                regionName = fullRegionName;
                
                if (regionName.length() > (regionWidth / 7 * 2)) // Assuming ~7 pixels per character, this checks to see if the label text can fit on two lines. If not, it widens the cell to make it fit.
                {
                    regionWidth = regionName.length() * 7 / 2;
                }

                r = createOverlapPartitionPanel(part, regionName, setEntry,
                        regionX - regionBump, 10, regionWidth + GraphLayoutConstants.GROUP_CHANNEL_WIDTH + 10,
                        height - 20, color, false, null);

                regionBump++;
                currentPartition = (BluOverlapPartition)currentSet.addPartitionEntry(r);
                currentPartition.addGroupLevel(new GraphGroupLevel(0, currentPartition)); // Add a new pAreaLevel to the data representation of the current Area object.
                
                currentSet.addRow(0, generateUpperRowLanes(-4,
                        GraphLayoutConstants.GROUP_ROW_HEIGHT - 5, 3, currentSet));

                int i = 0;

                ArrayList<ClusterSummary> clusters = part.getSortedClusters();

                for (ClusterSummary p : clusters) { // Draw the pArease inside this region
                    BluCluster pAreaPanel;

                    currentClusterLevel = currentPartition.getGroupLevels().get(pAreaY);

                    pAreaPanel = createClusterPanel(p, r, x2, y2, pAreaX, currentClusterLevel);

                    if (options != null) {
                        int conceptCount = p.getConceptCount();

                        if (conceptCount > options.pareaMaxThreshold || conceptCount < options.pareaMinThreshold) {
                            r.getHiddenGroups().add(pAreaPanel);
                            continue;
                        }
                    }

                    r.getVisibleGroups().add(pAreaPanel);

                    currentPartition.addColumn(areaPAreaX[pAreaY], generateColumnLanes(-3,
                            GraphLayoutConstants.GROUP_CHANNEL_WIDTH - 2, 3, currentSet));

                    groupEntries.put(p.getId(), pAreaPanel);    // Store it in a map keyed by its ID...

                    currentClusterLevel.addGroupEntry(pAreaPanel);

                    if ((i + 1) % horizontalPAreas == 0 && i < pareaCount - 1) {
                        y2 += GenericGroupEntry.ENTRY_HEIGHT + GraphLayoutConstants.GROUP_ROW_HEIGHT;
                        x2 = (int) (1.5 * GraphLayoutConstants.GROUP_CHANNEL_WIDTH);
                        pAreaX = 0;
                        areaPAreaX[pAreaY]++;
                        pAreaY++;

                        if (currentPartition.getGroupLevels().size() <= pAreaY) {
                            currentPartition.addGroupLevel(new GraphGroupLevel(pAreaY, currentPartition)); // Add a new pAreaLevel to the data representation of the current Area object.
                            currentSet.addRow(pAreaY, generateUpperRowLanes(-4,
                                    GraphLayoutConstants.GROUP_ROW_HEIGHT - 5, 3, currentSet));
                        }
                    } else {
                        x2 += (GenericGroupEntry.ENTRY_WIDTH + GraphLayoutConstants.GROUP_CHANNEL_WIDTH);
                        pAreaX++;
                        areaPAreaX[pAreaY]++;
                    }

                    i++;
                }

                regionX += regionWidth + 20;
            }

            x += width + 40;  // Set x to a position after the newly created area and the appropriate space after that given the set channel width.
            areaX++;
            lastSet = a;
        }
        
        this.centerGraphLevels(this.getGraphLevels());
    }
}
