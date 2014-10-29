package edu.njit.cs.saboc.blu.sno.graph.layout;

import SnomedShared.pareataxonomy.Area;
import SnomedShared.pareataxonomy.InheritedRelationship;
import SnomedShared.pareataxonomy.PAreaSummary;
import SnomedShared.pareataxonomy.Region;
import edu.njit.cs.saboc.blu.core.graph.BluGraph;
import edu.njit.cs.saboc.blu.core.graph.edges.GraphGroupLevel;
import edu.njit.cs.saboc.blu.core.graph.edges.GraphLane;
import edu.njit.cs.saboc.blu.core.graph.edges.GraphLevel;
import edu.njit.cs.saboc.blu.core.graph.layout.GraphLayoutConstants;
import edu.njit.cs.saboc.blu.core.graph.nodes.GenericGroupEntry;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.PAreaTaxonomy;
import edu.njit.cs.saboc.blu.sno.graph.options.GraphOptions;
import edu.njit.cs.saboc.blu.sno.graph.pareataxonomy.BluArea;
import edu.njit.cs.saboc.blu.sno.graph.pareataxonomy.BluPArea;
import edu.njit.cs.saboc.blu.sno.graph.pareataxonomy.BluRegion;
import edu.njit.cs.saboc.blu.sno.sctdatasource.middlewareproxy.MiddlewareAccessorProxy;
import edu.njit.cs.saboc.blu.sno.utils.UtilityMethods;
import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.JLabel;

/**
 *
 * @author Chris
 */
public class RegionsLayout extends GenericPAreaLayout {
    public RegionsLayout(BluGraph graph, PAreaTaxonomy hierarchyData) {
        super(graph, hierarchyData, true);
    }

    public void doLayout(GraphOptions options, boolean showConceptCounts) {
        super.doLayout();

        HashMap<Long, String> areaLateralRels =
                MiddlewareAccessorProxy.getProxy().getRelationshipAbbreviations(pareaTaxonomy.getVersion());

        Area lastArea = null;   // Used for generating the graph - this is the data version of an area
        BluArea currentArea = null;    // Used for generating the graph - this is the graphical representation of an area
        BluRegion currentPartition = null;
        GraphLevel currentLevel = null; // This is used as a temporary variable in this method to hold the current level.
        GraphGroupLevel currentPAreaLevel = null; // Used for generating the graph

        // These are a set of styles such that each new row is given a different color.
        Color[] background = {
            new Color(178, 178, 178),
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
        int x = 0, y = 20, maxHeight = 0;
        int style = 0;

        currentLevel = null; // This is used as a temporary variable in this method to hold the current level.
        currentArea = null;
        currentPartition = null;
        currentPAreaLevel = null;

        addGraphLevel(new GraphLevel(0, graph, new ArrayList<GraphLane>())); // Add the first level of areas (the single pArea 0-relationship level) to the data representation of the graph.

        HashMap<Region, JLabel> regionLabels = new HashMap<Region, JLabel>();
        HashMap<Region, String> regionNames = new HashMap<Region, String>();

        for (Area a : layoutGroupContainers) {  // Loop through the areas and generate the diagram for each of them
            int x2; 
            int y2;
            int regionX;
            int regionBump;

            int[] areaPAreaX;

            if (lastArea != null && lastArea.getRelationships().size() != a.getRelationships().size()) { // If a new row should be created...
                // Reset the x coordinate to the left
                x = 0;  
                
                // Add the height of the tallest area to the y coordinate plus the areaRowHeight variable which defines how
                // much space should be between rows of areas.
                y += maxHeight + GraphLayoutConstants.CONTAINER_ROW_HEIGHT; 

                areaY++;    // Update the areaY variable to reflect the new row.
                areaX = 0;  // Reset the areaX variable.

                maxHeight = 0;  // Reset the maxHeight variable since this is a new row.
                style++;    // Update the style variable which is used to display different colors for the different rows.

                addGraphLevel(new GraphLevel(areaY, graph,
                        generateUpperRowLanes(-5, GraphLayoutConstants.CONTAINER_ROW_HEIGHT - 7, 3, null))); // Add a level object to the arrayList in the dataGraph object
            }

            int areaWidth = 0;
            int areaHeight = 0;
            int maxRows = 0;
            
            ArrayList<Region> regions = a.getRegions();

            for (Region region : regions) { // Loop through regions to calculate the necessary size for the area.
                int pareaCount = 0;

                if (options != null) {
                    for (PAreaSummary parea : region.getPAreasInRegion()) {
                        int conceptCount = parea.getConceptCount();

                        if (conceptCount <= options.pareaMaxThreshold && conceptCount >= options.pareaMinThreshold) {
                            pareaCount++;
                        }
                    }
                } else {
                    pareaCount = region.getPAreasInRegion().size();
                }

                // Take the number of cells and find the square root of it (rounded up) to
                // find the minimum width required for a square that could hold all the pAreas.
                int pareasWide = (int) Math.ceil(Math.sqrt(pareaCount)); 

                int regionWidth = pareasWide * (GenericGroupEntry.ENTRY_WIDTH + GraphLayoutConstants.GROUP_CHANNEL_WIDTH);
                
                ArrayList<InheritedRelationship> relationships = region.getRelationships();

                String regionName = UtilityMethods.getRegionName(relationships, areaLateralRels);
                
                String countString;

                if (showConceptCounts) {
                    ArrayList<PAreaSummary> pareas = region.getPAreasInRegion();
                    
                    int conceptCount = pareaTaxonomy.getSCTDataSource().getConceptCountInPAreaHierarchy(pareaTaxonomy, pareas);

                    if (conceptCount == 1) {
                        countString = "(" + conceptCount + " Concept)";
                    } else {
                        countString = "(" + conceptCount + " Concepts)";
                    }

                } else {
                    if (region.getPAreasInRegion().size() == 1) {
                        countString = "(" + region.getPAreasInRegion().size() + " Partial-area)";
                    } else {
                        countString = "(" + region.getPAreasInRegion().size() + " Partial-areas)";
                    }
                }
                
                regionName += (" " + countString);
                
                regionNames.put(region, regionName);
                
                JLabel regionLabel = this.createRegionLabel(pareaTaxonomy, relationships, countString, regionWidth, false);

                regionLabels.put(region, regionLabel);
                
                regionWidth = Math.max(regionWidth, regionLabel.getWidth() + 4);

                areaWidth += regionWidth + 20;
                
                // Set the height to the greater of (a) the current height or (b) the number of regions in a column times the height of each pArea and the space between them.
                areaHeight = Math.max(areaHeight, 
                        regionLabel.getHeight() + (int) (Math.ceil((double) pareaCount / pareasWide))
                        * (GenericGroupEntry.ENTRY_HEIGHT + GraphLayoutConstants.GROUP_ROW_HEIGHT));  

                maxRows = Math.max(maxRows, (int) Math.ceil(Math.sqrt(pareaCount))); // Update the maxRows variable.
            }

            areaWidth += 20;
            areaHeight += 60 + GraphLayoutConstants.GROUP_ROW_HEIGHT;

            if (areaHeight > maxHeight) // Keeps track of the tallest cell on a row so that it knows how much lower to position the next row to avoid overlap.
            {
                maxHeight = areaHeight;
            }

            currentLevel = levels.get(areaY);

            Color color = background[style % background.length];

            if (a.isImplicitArea()) {
                color = new Color(color.getRed() / 2, color.getGreen() / 2, color.getBlue() / 2);
            }

            BluArea area = createAreaPanel(a, x, y, areaWidth, areaHeight, color, areaX, currentLevel); // Create the area

            containerEntries.put(a.getId(), area);

            currentLevel.addContainerEntry(area);    // Add a data representation for this new area to the current area Level obj.
            
            addColumn(areaX, currentLevel.getLevelY(), generateColumnLanes(-3,
                    GraphLayoutConstants.CONTAINER_CHANNEL_WIDTH - 5, 3, null)); // Generates a column of lanes to the left of this area.

            currentArea = (BluArea)currentLevel.getContainerEntries().get(areaX);

            regionX = GraphLayoutConstants.PARTITION_CHANNEL_WIDTH;
            
            areaPAreaX = new int[maxRows];
            regionBump = 0;

            for (Region region : regions) { // Create the regions inside the newly created area.

                JLabel regionLabel = regionLabels.get(region);

                int pareaCount = 0;

                if (options != null) {
                    for (PAreaSummary parea : region.getPAreasInRegion()) {
                        int conceptCount = parea.getConceptCount();

                        if (conceptCount <= options.pareaMaxThreshold && conceptCount >= options.pareaMinThreshold) {
                            pareaCount++;
                        }
                    }
                } else {
                    pareaCount = region.getPAreasInRegion().size();
                }

                x2 = (int) (1.5 * GraphLayoutConstants.GROUP_CHANNEL_WIDTH);
                y2 = regionLabel.getHeight() + 30;

                pAreaX = 0;
                pAreaY = 0;

                int horizontalPAreas = (int) Math.ceil(Math.sqrt(pareaCount));
                int regionWidth = horizontalPAreas * (GenericGroupEntry.ENTRY_WIDTH + GraphLayoutConstants.GROUP_CHANNEL_WIDTH);
                
                regionWidth = Math.max(regionWidth, regionLabel.getWidth() + 4);
                
                int labelXPos =  GraphLayoutConstants.PARTITION_CHANNEL_WIDTH + (regionWidth - regionLabel.getWidth()) / 2;
                regionLabel.setLocation(labelXPos, 4);
                
                BluRegion bluRegion = createRegionPanel(region, regionNames.get(region), area,
                        regionX - regionBump, 10, regionWidth + GraphLayoutConstants.GROUP_CHANNEL_WIDTH + 10,
                        areaHeight - 20, color, false, regionLabel);

                regionBump++;
                currentPartition = (BluRegion)currentArea.addPartitionEntry(bluRegion);
                currentPartition.addGroupLevel(new GraphGroupLevel(0, currentPartition)); // Add a new pAreaLevel to the data representation of the current Area object.
                currentArea.addRow(0, generateUpperRowLanes(-4,
                        GraphLayoutConstants.GROUP_ROW_HEIGHT - 5, 3, currentArea));

                int i = 0;

                for (PAreaSummary p : region.getPAreasInRegion()) { // Draw the pArease inside this region

                    currentPAreaLevel = currentPartition.getGroupLevels().get(pAreaY);

                    BluPArea pAreaPanel = createPAreaPanel(p, bluRegion, x2, y2, pAreaX, currentPAreaLevel);

                    if (options != null) {
                        int conceptCount = p.getConceptCount();

                        if (conceptCount > options.pareaMaxThreshold || conceptCount < options.pareaMinThreshold) {
                            bluRegion.getHiddenGroups().add(pAreaPanel);
                            continue;
                        }
                    }

                    bluRegion.getVisibleGroups().add(pAreaPanel);

                    currentPartition.addColumn(areaPAreaX[pAreaY], generateColumnLanes(-3,
                            GraphLayoutConstants.GROUP_CHANNEL_WIDTH - 2, 3, currentArea));

                    groupEntries.put(p.getId(), pAreaPanel);    // Store it in a map keyed by its ID...

                    currentPAreaLevel.addGroupEntry(pAreaPanel);

                    if ((i + 1) % horizontalPAreas == 0 && i < pareaCount - 1) {
                        y2 += GenericGroupEntry.ENTRY_HEIGHT + GraphLayoutConstants.GROUP_ROW_HEIGHT;
                        x2 = (int) (1.5 * GraphLayoutConstants.GROUP_CHANNEL_WIDTH);
                        pAreaX = 0;
                        areaPAreaX[pAreaY]++;
                        pAreaY++;

                        if (currentPartition.getGroupLevels().size() <= pAreaY) {
                            currentPartition.addGroupLevel(new GraphGroupLevel(pAreaY, currentPartition)); // Add a new pAreaLevel to the data representation of the current Area object.
                            currentArea.addRow(pAreaY, generateUpperRowLanes(-4,
                                    GraphLayoutConstants.GROUP_ROW_HEIGHT - 5, 3, currentArea));
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

            x += areaWidth + 40;  // Set x to a position after the newly created area and the appropriate space after that given the set channel width.
            areaX++;
            lastArea = a;
        }
        
        this.centerGraphLevels(this.getGraphLevels());
    }
}
