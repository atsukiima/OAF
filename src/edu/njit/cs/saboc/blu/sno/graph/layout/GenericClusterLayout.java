package edu.njit.cs.saboc.blu.sno.graph.layout;

import SnomedShared.generic.GenericContainerPartition;
import SnomedShared.overlapping.ClusterSummary;
import SnomedShared.overlapping.CommonOverlapSet;
import SnomedShared.overlapping.EntryPoint;
import SnomedShared.overlapping.EntryPoint.InheritanceType;
import SnomedShared.overlapping.EntryPointSet;
import SnomedShared.overlapping.OverlapInheritancePartition;
import edu.njit.cs.saboc.blu.core.graph.BluGraph;
import edu.njit.cs.saboc.blu.core.graph.edges.GraphEdge;
import edu.njit.cs.saboc.blu.core.graph.edges.GraphGroupLevel;
import edu.njit.cs.saboc.blu.core.graph.edges.GraphLevel;
import edu.njit.cs.saboc.blu.core.graph.layout.BluGraphLayout;
import edu.njit.cs.saboc.blu.core.graph.nodes.GenericGroupEntry;
import edu.njit.cs.saboc.blu.sno.abn.tan.TribalAbstractionNetwork;
import edu.njit.cs.saboc.blu.sno.graph.tan.BluCluster;
import edu.njit.cs.saboc.blu.sno.graph.tan.BluCommonOverlapSet;
import edu.njit.cs.saboc.blu.sno.graph.tan.BluOverlapPartition;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import javax.swing.JLabel;

/**
 *
 * @author Chris
 */
public abstract class GenericClusterLayout extends BluGraphLayout<CommonOverlapSet, BluCommonOverlapSet, BluCluster> {

    protected TribalAbstractionNetwork hierarchyData;

    protected ArrayList<CommonOverlapSet> commonOverlapSets;

    public GenericClusterLayout(BluGraph graph, TribalAbstractionNetwork hierarchyData) {
        super(graph);

        this.hierarchyData = hierarchyData;
        this.commonOverlapSets = hierarchyData.getBands();
    }

    public void doLayout() {
        ArrayList<CommonOverlapSet> sortedSets = new ArrayList<CommonOverlapSet>();    // Used for generating the graph
        ArrayList<CommonOverlapSet> levelSets = new ArrayList<CommonOverlapSet>();     // Used for generating the graph

        ArrayList<CommonOverlapSet> tempSets = commonOverlapSets;

        CommonOverlapSet lastSet = null;

        Collections.sort(tempSets, new Comparator<CommonOverlapSet>() {    // Sort the areas based on the number of their relationships.

            public int compare(CommonOverlapSet a, CommonOverlapSet b) {
                return a.getSetEntryPoints().size() - b.getSetEntryPoints().size();
            }
        });

        for (CommonOverlapSet set : tempSets) {

            if (lastSet != null && lastSet.getSetEntryPoints().size() != set.getSetEntryPoints().size()) {
                Collections.sort(levelSets, new Comparator<CommonOverlapSet>() {    // Sort the areas based on the number of their relationships.

                    public int compare(CommonOverlapSet a, CommonOverlapSet b) {
                        return a.getClusterConceptCountSum() - b.getClusterConceptCountSum();
                    }
                });

                int c = 0;

                for (c = 0; c < levelSets.size(); c += 2) {
                    sortedSets.add(levelSets.get(c));
                }

                if (levelSets.size() % 2 == 0) {
                    c = levelSets.size() - 1;
                } else {
                    c = levelSets.size() - 2;
                }

                for (; c >= 1; c -= 2) {
                    sortedSets.add(levelSets.get(c));
                }

                levelSets.clear();
            }

            levelSets.add(set);

            lastSet = set;
        }

        Collections.sort(levelSets, new Comparator<CommonOverlapSet>() {    // Sort the areas based on the number of their relationships.

            public int compare(CommonOverlapSet a, CommonOverlapSet b) {
                return a.getClusterConceptCountSum() - b.getClusterConceptCountSum();
            }
        });

        int c = 0;

        for (c = 0; c < levelSets.size(); c += 2) {
            sortedSets.add(levelSets.get(c));
        }

        if (levelSets.size() % 2 == 0) {
            c = levelSets.size() - 1;
        } else {
            c = levelSets.size() - 2;
        }

        for (; c >= 1; c -= 2) {
            sortedSets.add(levelSets.get(c));
        }

        lastSet = null;
        layoutGroupContainers = sortedSets;
    }

    public ArrayList<CommonOverlapSet> getLayoutAreas() {
        return layoutGroupContainers;
    }

    protected BluCluster createClusterPanel(ClusterSummary p, BluOverlapPartition parent, int x, int y, int pAreaX, GraphGroupLevel clusterLevel) {
        BluCluster clusterPanel = new BluCluster(p, graph, parent, pAreaX, clusterLevel, new ArrayList<GraphEdge>());

        //Make sure this panel dimensions will fit on the graph, stretch the graph if necessary
        graph.stretchGraphToFitPanel(x, y, GenericGroupEntry.ENTRY_WIDTH, GenericGroupEntry.ENTRY_HEIGHT);

        //Setup the panel's dimensions, etc.
        clusterPanel.setBounds(x, y, GenericGroupEntry.ENTRY_WIDTH, GenericGroupEntry.ENTRY_HEIGHT);

        parent.add(clusterPanel, 0);

        return clusterPanel;
    }


    protected BluCommonOverlapSet createCommonOverlapSetPanel(CommonOverlapSet set, int x, int y, int width, int height, Color c, int areaX, GraphLevel parentLevel) {
        BluCommonOverlapSet setPanel = new BluCommonOverlapSet(set, graph, areaX, parentLevel, new Rectangle(x, y, width, height));

        graph.stretchGraphToFitPanel(x, y, width, height);

        setPanel.setBounds(x, y, width, height);
        setPanel.setBackground(c);

        graph.add(setPanel, 0);

        return setPanel;
    }

    protected BluOverlapPartition createOverlapPartitionPanel(OverlapInheritancePartition partition, String regionName, 
            BluCommonOverlapSet set, int x, int y, int width, int height, Color c, boolean treatPartitonAsOverlapSet, JLabel partitionLabel) {

        BluOverlapPartition overlapPanel = new BluOverlapPartition(partition, regionName,
                width, height, graph, set, c, treatPartitonAsOverlapSet, partitionLabel);

        graph.stretchGraphToFitPanel(x, y, width, height);

        overlapPanel.setBounds(x, y, width, height);

        set.add(overlapPanel, 0);

        return overlapPanel;
    }

    public BluCommonOverlapSet getCommonOverlapSet(int level, int setX) {
        return (BluCommonOverlapSet)getConainterAt(level, setX);
    }

    public BluOverlapPartition getRegion(int level, int setX, int partitionX) {
        return (BluOverlapPartition)getContainerPartitionAt(level, setX, partitionX);
    }

    public BluCluster getCluster(int level, int setX, int partitionX, int clusterY, int clusterX) {
        return (BluCluster)getGroupEntry(level, setX, partitionX, clusterY, clusterX);
    }
    
    protected JLabel createOverlapPartitionLabel(TribalAbstractionNetwork tan, EntryPointSet patriarchs, String countString, int width, boolean treatAsBand) {
       
        Canvas canvas = new Canvas();
        FontMetrics fontMetrics = canvas.getFontMetrics(new Font("SansSerif", Font.BOLD, 14));
        
        HashMap<Long, String> patriarchNames = tan.getPatriarchNames();
        
        String [] entries = new String[patriarchs.size() + 1];
        entries[entries.length - 1] = countString;

        int c = 0;
        
        int longestPatriarch = -1;
        
        for(EntryPoint patriarch : patriarchs) {
            String patriarchName = patriarchNames.get(patriarch.getEntryPointConceptId());
            
            if(!treatAsBand) {
                if(patriarch.getInheritanceType() == InheritanceType.INHERITED) {
                    patriarchName += "*";
                } else {
                    patriarchName += "+";
                }
            }
            
            int relNameWidth = fontMetrics.stringWidth(patriarchName);
            
            if(relNameWidth > longestPatriarch) {
                longestPatriarch = relNameWidth;
            }
            
            entries[c++] = patriarchName;
        }
        
        if(fontMetrics.stringWidth(countString) > longestPatriarch) {
            longestPatriarch = fontMetrics.stringWidth(countString);
        }
        
        if(patriarchs.size() > 1) {
            longestPatriarch += fontMetrics.charWidth(',');
        }
        
        if(treatAsBand) {
            longestPatriarch += fontMetrics.charWidth('+');
        }
        
        if(longestPatriarch > width) {
            width = longestPatriarch + 4;
        }
        
        return this.createFittedPartitionLabel(entries, width, fontMetrics);
    }
    
    public JLabel createPartitionLabel(GenericContainerPartition partition, int width) {
        OverlapInheritancePartition bandPartition = (OverlapInheritancePartition)partition;
        
        String countStr;
        
        if (graph.showingConceptCountLabels()) {
                int conceptCount = hierarchyData.getSCTDataSource().getConceptCountInClusterHierarchy(hierarchyData, bandPartition.getClusters());

                if (conceptCount == 1) {
                    countStr = " (1 Concept)";
                } else {
                    countStr = " (" + conceptCount + " Concepts)";
                }

            } else {
                if (bandPartition.getClusters().size() == 1) {
                    countStr = " (1 Cluster)";
                } else {
                    countStr = " (" + bandPartition.getClusters().size() + " Clusters)";
                }
            }
        
        // TODO: Currently we do not partition bands base on inheritance type, so last Arg is true.
        return this.createOverlapPartitionLabel(hierarchyData, bandPartition.getClusters().get(0).getEntryPointSet(), countStr, width, true);
    }
}
