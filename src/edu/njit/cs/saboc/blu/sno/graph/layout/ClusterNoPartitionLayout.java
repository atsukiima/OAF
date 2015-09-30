package edu.njit.cs.saboc.blu.sno.graph.layout;

import SnomedShared.overlapping.CommonOverlapSet;
import SnomedShared.overlapping.EntryPointSet;
import SnomedShared.overlapping.OverlapInheritancePartition;
import edu.njit.cs.saboc.blu.core.graph.BluGraph;
import edu.njit.cs.saboc.blu.core.graph.edges.GraphGroupLevel;
import edu.njit.cs.saboc.blu.core.graph.edges.GraphLane;
import edu.njit.cs.saboc.blu.core.graph.edges.GraphLevel;
import edu.njit.cs.saboc.blu.core.graph.layout.GraphLayoutConstants;
import edu.njit.cs.saboc.blu.core.graph.nodes.GenericGroupEntry;
import edu.njit.cs.saboc.blu.sno.abn.tan.local.SCTCluster;
import edu.njit.cs.saboc.blu.sno.abn.tan.local.SCTTribalAbstractionNetwork;
import edu.njit.cs.saboc.blu.sno.graph.tan.BluCluster;
import edu.njit.cs.saboc.blu.sno.graph.tan.BluCommonOverlapSet;
import edu.njit.cs.saboc.blu.sno.graph.tan.BluOverlapPartition;
import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import javax.swing.JLabel;

/**
 *
 * @author Chris
 */
public class ClusterNoPartitionLayout extends GenericClusterLayout {

    public ClusterNoPartitionLayout(BluGraph graph) {
        super(graph, (SCTTribalAbstractionNetwork) graph.getAbstractionNetwork());
    }

    public void doLayout() {

        HashMap<Long, String> hierarchyEntryPointNames = hierarchyData.getPatriarchNames();

        ArrayList<CommonOverlapSet> overlapSets =
                (ArrayList<CommonOverlapSet>)hierarchyData.getBands().clone();

        for(int c = overlapSets.size() - 1; c >= 0; c--) {
            if(overlapSets.get(c).getSetEntryPoints().size() == 1) {
                overlapSets.remove(c);
            }
        }

        ArrayList<SCTCluster> entryPoints = hierarchyData.getHierarchyEntryPoints();
        HashSet<Long> fakeEPSet = new HashSet<Long>();
        fakeEPSet.add(0l);

        CommonOverlapSet entryPointSet = new CommonOverlapSet(-1, fakeEPSet);

        for(SCTCluster cluster : entryPoints) {
            
            SCTCluster newCluster = new SCTCluster(cluster.getId(), cluster.getHeaderConcept(),
                    ((SCTCluster)cluster).getConceptHierarchy(), cluster.getParentIds(), new EntryPointSet());
            newCluster.setParentGroupInformation(new ArrayList<>());

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
        int clusterX, clusterY;
        int x = 0, y = 20, width = 0, maxHeight = 0;
        int style = 0;

        addGraphLevel(new GraphLevel(0, graph, new ArrayList<GraphLane>())); // Add the first level of areas (the single pArea 0-relationship level) to the data representation of the graph.

        for (CommonOverlapSet a : layoutGroupContainers) {  // Loop through the areas and generate the diagram for each of them
            BluCommonOverlapSet setEntry;
            
            
            ArrayList<SCTCluster> clusters = hierarchyData.convertClusters(a.getAllClusters());

            int maxRows, x2, y2, regionX, partitionBump;

            int[] bandClusterX;

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
            maxRows = 0;

            int clusterCount = clusters.size();
            
            int clusterEntriesWide;

            if(a.getId() == -1) {
                clusterEntriesWide = clusterCount;
            } else {
                clusterEntriesWide = (int) Math.ceil(Math.sqrt(clusterCount));
            }

            int setWidth = clusterEntriesWide * (GenericGroupEntry.ENTRY_WIDTH + GraphLayoutConstants.GROUP_CHANNEL_WIDTH);

            String regionName = "";

            if(a.getId() >= 0) {
                HashSet<Long> entryPointIds = a.getSetEntryPoints();

                for(long epId : entryPointIds) {
                    regionName += (hierarchyEntryPointNames.get(epId) + ", ");
                }

                regionName = regionName.substring(0, regionName.length() - 1);
            }
            
            String countString;

            int conceptCount = hierarchyData.getDataSource().getConceptCountInClusterHierarchy(hierarchyData, clusters);

            if (conceptCount == 1) {
                countString = " (1 Concept, 1 Cluster)";
            } else {
                countString = String.format(" (%d Concepts, %d Clusters)", conceptCount, clusterCount);
            }

            regionName += (" " + countString);
            
            JLabel bandLabel = createOverlapPartitionLabel(hierarchyData, a.getOverlapPartitions().get(0).getEntryPointSet(), countString, setWidth, true);

            setWidth = Math.max(setWidth, bandLabel.getWidth() + 8);

            width += setWidth + 20;

            int height = bandLabel.getHeight() + (int) (Math.ceil((double) clusterCount / clusterEntriesWide))
                    * (GenericGroupEntry.ENTRY_HEIGHT + GraphLayoutConstants.GROUP_ROW_HEIGHT);  // Set the height to the greater of (a) the current height or (b) the number of regions in a column times the height of each pArea and the space between them.

            maxRows = Math.max(maxRows, (int) Math.ceil(Math.sqrt(clusterCount))); // Update the maxRows variable.

            width += 20;
            height += 60 + GraphLayoutConstants.GROUP_ROW_HEIGHT;

            // Keeps track of the tallest cell on a row so that it knows how much lower to position the next row to avoid overlap.
            if (height > maxHeight) {
                maxHeight = height;
            }

            currentLevel = levels.get(areaY);

            Color color = background[style % background.length];

            setEntry = createCommonOverlapSetPanel(a, x, y, width, height, color, areaX, currentLevel); // Create the area

            containerEntries.put(a.getId(), setEntry);

            // Add a data representation for this new area to the current area Level obj.
            currentLevel.addContainerEntry(setEntry);    
            
            // Generates a column of lanes to the left of this area.
            addColumn(areaX, currentLevel.getLevelY(), generateColumnLanes(-3,
                    GraphLayoutConstants.CONTAINER_CHANNEL_WIDTH - 5, 3, null)); 

            currentSet = (BluCommonOverlapSet) currentLevel.getContainerEntries().get(areaX);

            regionX = GraphLayoutConstants.PARTITION_CHANNEL_WIDTH;

            bandClusterX = new int[maxRows];
            partitionBump = 0;

            x2 = (int) (1.5 * GraphLayoutConstants.GROUP_CHANNEL_WIDTH);
            y2 = bandLabel.getHeight() + 30;
            
            clusterX = 0;
            clusterY = 0;

            class PseudoPartition extends OverlapInheritancePartition {

                public PseudoPartition() {
                    super(new EntryPointSet());
                }
            }

            PseudoPartition partition = new PseudoPartition();

            for(SCTCluster c : clusters) {
                partition.addClusterToPartition(c);
            }
            
            int labelXPos =  GraphLayoutConstants.PARTITION_CHANNEL_WIDTH + (setWidth - bandLabel.getWidth()) / 2;
            bandLabel.setLocation(labelXPos, 4);

            BluOverlapPartition overlapPartition = createOverlapPartitionPanel(partition, regionName, setEntry,
                    regionX - partitionBump, 10, setWidth + GraphLayoutConstants.GROUP_CHANNEL_WIDTH + 10,
                    height - 20, color, true, bandLabel);

            partitionBump++;
            currentPartition = (BluOverlapPartition)currentSet.addPartitionEntry(overlapPartition);
            currentPartition.addGroupLevel(new GraphGroupLevel(0, currentPartition)); // Add a new pAreaLevel to the data representation of the current Area object.

            currentSet.addRow(0, generateUpperRowLanes(-4,
                    GraphLayoutConstants.GROUP_ROW_HEIGHT - 5, 3, currentSet));

            int i = 0;

            for (SCTCluster p : clusters) { // Draw the pArease inside this region
                BluCluster pAreaPanel;

                currentClusterLevel = currentPartition.getGroupLevels().get(clusterY);

                pAreaPanel = createClusterPanel(p, overlapPartition, x2, y2, clusterX, currentClusterLevel);

                overlapPartition.getVisibleGroups().add(pAreaPanel);

                currentPartition.addColumn(bandClusterX[clusterY], generateColumnLanes(-3,
                        GraphLayoutConstants.GROUP_CHANNEL_WIDTH - 2, 3, currentSet));

                groupEntries.put(p.getId(), pAreaPanel);    // Store it in a map keyed by its ID...

                currentClusterLevel.addGroupEntry(pAreaPanel);

                if ((i + 1) % clusterEntriesWide == 0 && i < clusters.size() - 1) {
                    y2 += GenericGroupEntry.ENTRY_HEIGHT + GraphLayoutConstants.GROUP_ROW_HEIGHT;
                    x2 = (int) (1.5 * GraphLayoutConstants.GROUP_CHANNEL_WIDTH);
                    clusterX = 0;
                    bandClusterX[clusterY]++;
                    clusterY++;

                    if (currentPartition.getGroupLevels().size() <= clusterY) {
                        currentPartition.addGroupLevel(new GraphGroupLevel(clusterY, currentPartition)); // Add a new pAreaLevel to the data representation of the current Area object.
                        currentSet.addRow(clusterY, generateUpperRowLanes(-4,
                                GraphLayoutConstants.GROUP_ROW_HEIGHT - 5, 3, currentSet));
                    }
                } else {
                    x2 += (GenericGroupEntry.ENTRY_WIDTH + GraphLayoutConstants.GROUP_CHANNEL_WIDTH);
                    clusterX++;
                    bandClusterX[clusterY]++;
                }

                i++;
            }

            x += width + 40;  // Set x to a position after the newly created area and the appropriate space after that given the set channel width.
            areaX++;
            lastSet = a;
        }
        
        this.centerGraphLevels(this.getGraphLevels());
    }
}
