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
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.LocalPAreaTaxonomy;
import edu.njit.cs.saboc.blu.sno.graph.options.GraphOptions;
import edu.njit.cs.saboc.blu.sno.graph.pareataxonomy.BluArea;
import edu.njit.cs.saboc.blu.sno.graph.pareataxonomy.BluPArea;
import edu.njit.cs.saboc.blu.sno.graph.pareataxonomy.BluRegion;
import edu.njit.cs.saboc.blu.sno.sctdatasource.middlewareproxy.MiddlewareAccessorProxy;
import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.JLabel;


/**
 *
 * @author Chris
 */
public class NoRegionsLayout extends GenericPAreaLayout {
    public NoRegionsLayout(BluGraph graph, PAreaTaxonomy hierarchyData) {
        super(graph, hierarchyData, false);
    }

    public void doLayout(GraphOptions options, boolean showConceptCounts) {
        super.doLayout();

        HashMap<Long, String> areaLateralRels;
        
        if(pareaTaxonomy instanceof LocalPAreaTaxonomy) {
            areaLateralRels = pareaTaxonomy.getLateralRelsInHierarchy();
        } else {
            areaLateralRels = MiddlewareAccessorProxy.getProxy().getRelationshipAbbreviations(pareaTaxonomy.getVersion());
        }

        Area lastArea = null;   // Used for generating the graph - this is the data version of an area
        BluArea currentArea;    // Used for generating the graph - this is the graphical representation of an area
        GraphLevel currentLevel; // This is used as a temporary variable in this method to hold the current level.
        GraphGroupLevel currentPAreaLevel; // Used for generating the graph

        BluRegion currentRegion;

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
        int x = 0;
        int y = 20;
        int maxRegionHeight = 0;
        int style = 0;

        currentLevel = null; // This is used as a temporary variable in this method to hold the current level.
        currentArea = null;
        currentRegion = null;
        currentPAreaLevel = null;

        addGraphLevel(new GraphLevel(0, graph, new ArrayList<GraphLane>())); // Add the first level of areas to the data representation of the graph.
        
        for (Area a : layoutGroupContainers) {  // Loop through the areas and generate the diagram for each of them

            if (lastArea != null && lastArea.getRelationships().size() != a.getRelationships().size()) { // If a new row should be created...

                x = 0;  // Reset the x coordinate to the left
                y += maxRegionHeight + GraphLayoutConstants.CONTAINER_ROW_HEIGHT; // Add the height of the tallest area to the y coordinate plus the areaRowHeight variable which defines how
                // much space should be between rows of areas.

                areaY++;    // Update the areaY variable to reflect the new row.
                areaX = 0;  // Reset the areaX variable.

                maxRegionHeight = 0;  // Reset the maxHeight variable since this is a new row.
                style++;    // Update the style variable which is used to display different colors for the different rows.

                addGraphLevel(new GraphLevel(areaY, graph, generateUpperRowLanes(-5,
                        GraphLayoutConstants.CONTAINER_ROW_HEIGHT - 7, 3, null))); // Add a level object to the arrayList in the dataGraph object
            }

            int areaWidth = 0;
            int pareaCount = 0;

            ArrayList<PAreaSummary> areaPAreas = a.getAllPAreas();

            if (options != null) {
                for (PAreaSummary parea : areaPAreas) {
                    int conceptCount = parea.getConceptCount();

                    if (conceptCount <= options.pareaMaxThreshold && conceptCount >= options.pareaMinThreshold) {
                        pareaCount++;
                    }
                }
            } else {
                pareaCount = areaPAreas.size();
            }

            // Take the number of cells and find the square root of it (rounded up) to
            // find the minimum width required for a square that could hold all the pAreas.
            int columnsWide = (int) Math.ceil(Math.sqrt(pareaCount));

            int regionWidth = columnsWide * (GenericGroupEntry.ENTRY_WIDTH + GraphLayoutConstants.GROUP_CHANNEL_WIDTH);

            String areaName = "";

            ArrayList<Long> areaRels = a.getRelationships();
            
            String countString = "";

            if (areaRels.isEmpty()) {
                areaName = "\u2205";    // If it is the root concept, set the title to the null character.
            } else {

                boolean first = true;

                for (long rel : areaRels) {   // Otherwise derive the title from its relationships.

                    if (!first) {
                        areaName += ", ";
                    } else {
                        first = false;
                    }

                    String relStr = areaLateralRels.get(rel);
                    areaName += relStr;
                }

                if (showConceptCounts) {
                    int conceptCount = pareaTaxonomy.getSCTDataSource().getConceptCountInPAreaHierarchy(pareaTaxonomy, areaPAreas);
                            
                    if (conceptCount == 1) {
                        countString = "(1 Concept)";
                    } else {
                        countString = String.format("(%d Concepts)", conceptCount);
                    }
                } else {
                    if (areaPAreas.size() == 1) {
                        countString = "(1 Partial-area)";
                    } else {
                        countString = String.format("(%d Partial-areas)", areaPAreas.size());
                    }
                }
            }
            
            areaName += (" " + countString);

            JLabel regionLabel = createRegionLabel(pareaTaxonomy, areaPAreas.get(0).getRelationships(), countString, regionWidth, true);
            
            regionWidth = Math.max(regionWidth, regionLabel.getWidth() + 8);
            
            areaWidth += regionWidth + 20;
            
            int areaHeight = regionLabel.getHeight() + (int) (Math.ceil((double) pareaCount / columnsWide)) * (GenericGroupEntry.ENTRY_HEIGHT + GraphLayoutConstants.GROUP_ROW_HEIGHT);  
            
            int maxPAreaRows = (int) Math.ceil(Math.sqrt(pareaCount));

            areaWidth += 20;
            areaHeight += 50 + GraphLayoutConstants.GROUP_ROW_HEIGHT;

            // Keeps track of the tallest cell on a row so that it knows how much lower to position the next row to avoid overlap.
            if (areaHeight > maxRegionHeight) {
                maxRegionHeight = areaHeight;
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

            int regionX = GraphLayoutConstants.PARTITION_CHANNEL_WIDTH;
            int [] areaPAreaX = new int[maxPAreaRows];
            int regionBump = 0;
            
            int labelXPos =  GraphLayoutConstants.PARTITION_CHANNEL_WIDTH + (regionWidth - regionLabel.getWidth()) / 2;
            regionLabel.setLocation(labelXPos, 4);

            if (options != null) {
                for (PAreaSummary parea : areaPAreas) {
                    int conceptCount = parea.getConceptCount();

                    if (conceptCount <= options.pareaMaxThreshold && conceptCount >= options.pareaMinThreshold) {
                        pareaCount++;
                    }
                }
            } else {
                pareaCount = areaPAreas.size();
            }

            int pareaRows = (int) Math.ceil(Math.sqrt(pareaCount));

            int x2 = (int) (1.5 * GraphLayoutConstants.GROUP_CHANNEL_WIDTH);
            int y2 = regionLabel.getHeight() + 30;
            
            int pAreaX = 0;
            int pAreaY = 0;
 
            regionWidth = Math.max(regionWidth, pareaRows * (GenericGroupEntry.ENTRY_WIDTH +
                    GraphLayoutConstants.GROUP_CHANNEL_WIDTH));

            /*
             * Need a fake region to load all of the PAreas into without considering relationships.
             */
            class PseudoRegion extends Region {

                public PseudoRegion(PAreaSummary first) {
                    super(first);
                    this.relationships = new ArrayList<InheritedRelationship>();
                }
            }

            PseudoRegion region = new PseudoRegion(areaPAreas.get(0));

            for(int c = 1; c < areaPAreas.size(); c++) {
                region.addPAreaToRegion(areaPAreas.get(c));
            }

            BluRegion bluRegion = createRegionPanel(region, areaName, area,
                    regionX - regionBump, 10, regionWidth +
                    GraphLayoutConstants.GROUP_CHANNEL_WIDTH + 10, areaHeight - 20, color, true, regionLabel);

            regionBump++;
            currentRegion = (BluRegion)currentArea.addPartitionEntry(bluRegion);
            currentRegion.addGroupLevel(new GraphGroupLevel(0, currentRegion)); // Add a new pAreaLevel to the data representation of the current Area object.
            currentArea.addRow(0, generateUpperRowLanes(-4,
                    GraphLayoutConstants.GROUP_ROW_HEIGHT - 5, 3, currentArea));

            int i = 0;
            
            for (PAreaSummary p : areaPAreas) { // Draw the pArea inside this region

                currentPAreaLevel = currentRegion.getGroupLevels().get(pAreaY);

                BluPArea pAreaPanel = createPAreaPanel(p, bluRegion, x2, y2, pAreaX, currentPAreaLevel);

                if (options != null) {
                    int conceptCount = p.getConceptCount();

                    if (conceptCount > options.pareaMaxThreshold || conceptCount < options.pareaMinThreshold) {
                        bluRegion.getHiddenGroups().add(pAreaPanel);
                        continue;
                    }
                }

                bluRegion.getVisibleGroups().add(pAreaPanel);

                currentRegion.addColumn(areaPAreaX[pAreaY], generateColumnLanes(-3,
                        GraphLayoutConstants.GROUP_CHANNEL_WIDTH - 2, 3, currentArea));
                
                groupEntries.put(p.getId(), pAreaPanel);    // Store it in a map keyed by its ID...

                currentPAreaLevel.addGroupEntry(pAreaPanel);

                if ((i + 1) % pareaRows == 0 && i < pareaCount - 1) {
                    y2 += GenericGroupEntry.ENTRY_HEIGHT + GraphLayoutConstants.GROUP_ROW_HEIGHT;
                    x2 = (int) (1.5 * GraphLayoutConstants.GROUP_CHANNEL_WIDTH);
                    pAreaX = 0;
                    areaPAreaX[pAreaY]++;
                    pAreaY++;

                    if (currentRegion.getGroupLevels().size() <= pAreaY) {
                        currentRegion.addGroupLevel(new GraphGroupLevel(pAreaY, currentRegion)); // Add a new pAreaLevel to the data representation of the current Area object.
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

            x += areaWidth + 40;  // Set x to a position after the newly created area and the appropriate space after that given the set channel width.
            areaX++;
            lastArea = a;
        }
        
        centerGraphLevels(this.getGraphLevels());
    }
}
