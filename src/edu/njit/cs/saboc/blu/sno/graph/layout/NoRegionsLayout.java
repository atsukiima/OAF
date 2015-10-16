package edu.njit.cs.saboc.blu.sno.graph.layout;

import SnomedShared.Concept;
import SnomedShared.generic.GenericContainerPartition;
import SnomedShared.pareataxonomy.InheritedRelationship;
import edu.njit.cs.saboc.blu.core.graph.BluGraph;
import edu.njit.cs.saboc.blu.core.graph.edges.GraphGroupLevel;
import edu.njit.cs.saboc.blu.core.graph.edges.GraphLevel;
import edu.njit.cs.saboc.blu.core.graph.pareataxonomy.GenericNoRegionsPAreaGraphLayout;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTArea;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTPArea;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTPAreaTaxonomy;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTRegion;
import edu.njit.cs.saboc.blu.sno.datastructure.hierarchy.SCTConceptHierarchy;
import edu.njit.cs.saboc.blu.sno.graph.pareataxonomy.BluArea;
import edu.njit.cs.saboc.blu.sno.graph.pareataxonomy.BluPArea;
import edu.njit.cs.saboc.blu.sno.graph.pareataxonomy.BluRegion;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy.configuration.SCTPAreaTaxonomyConfiguration;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import javax.swing.JLabel;


/**
 *
 * @author Chris
 */
public class NoRegionsLayout extends GenericNoRegionsPAreaGraphLayout<
        SCTPAreaTaxonomy,
        SCTArea,
        SCTPArea,
        SCTRegion, 
        BluArea,
        BluPArea,
        BluRegion,
        Concept,
        InheritedRelationship,
        SCTConceptHierarchy> {

    public NoRegionsLayout(BluGraph graph, SCTPAreaTaxonomy hierarchyData, SCTPAreaTaxonomyConfiguration config) {
        super(graph, hierarchyData, config);        
    }
    
    @Override
    protected BluPArea createPAreaPanel(SCTPArea p, BluRegion parent, int x, int y, int pAreaX, GraphGroupLevel pAreaLevel) {
        return PAreaTaxonomyLayoutUtils.createPAreaPanel(graph, p, parent, x, y, pAreaX, pAreaLevel);
    }

    @Override
    protected BluArea createAreaPanel(SCTArea a, int x, int y, int width, int height, Color c, int areaX, GraphLevel parentLevel) {
        return PAreaTaxonomyLayoutUtils.createAreaPanel(graph, a, x, y, width, height, c, areaX, parentLevel);
    }

    @Override
    protected BluRegion createRegionPanel(SCTRegion region, String regionName, BluArea ap, int x, int y, int width, int height, Color c, boolean treatRegionAsArea, JLabel label) {
        return PAreaTaxonomyLayoutUtils.createRegionPanel(graph, region, regionName, ap, x, y, width, height, c, treatRegionAsArea, label);
    }

    @Override
    protected ArrayList<String> getAreaRelationshipNames(SCTArea a) {
        ArrayList<String> names = new ArrayList<>();
        
        HashSet<Long> relIds = a.getRelationshipIds();
        
        HashMap<Long, String> relNames = taxonomy.getLateralRelsInHierarchy();
        
        for(long relId : relIds) {
            names.add(relNames.get(relId));
        }
        
        Collections.sort(names);
        
        return names;
    }
    
    @Override
    public JLabel createPartitionLabel(GenericContainerPartition partition, int width) {
        SCTRegion region = (SCTRegion)partition;
        
        String countStr;
        
        if (graph.showingConceptCountLabels()) {
            int conceptCount = taxonomy.getDataSource().getConceptCountInPAreaHierarchy(taxonomy, region.getPAreasInRegion());

            if (conceptCount == 1) {
                countStr = "(1 Concept)";
            } else {
                countStr = String.format("(%d Concepts)", conceptCount);
            }
        } else {
            if (region.getPAreasInRegion().size() == 1) {
                countStr = "(1 Partial-area)";
            } else {
                countStr = String.format("(%d Partial-areas)", region.getPAreasInRegion().size());
            }
        }
                
        return this.createRegionLabel(taxonomy, region.getPAreasInRegion().get(0).getRelationships(), countStr, width, -1);
    
    }

    @Override
    protected JLabel createRegionLabel(SCTPAreaTaxonomy taxonomy, HashSet<InheritedRelationship> relationships, String countString, int width, int maxRels) {
        Canvas canvas = new Canvas();
        FontMetrics fontMetrics = canvas.getFontMetrics(new Font("SansSerif", Font.BOLD, 14));
        
        HashMap<Long, String> relAbbrevs = taxonomy.getLateralRelsInHierarchy();
        
        String[] entries;

        if (relationships.isEmpty()) {
            entries = new String[]{"\u2205", countString};
        } else {
            entries = new String[relationships.size()];

            int c = 0;

            int longestRelNameWidth = -1;

            for (InheritedRelationship rel : relationships) {
                String relName = relAbbrevs.get(rel.getRelationshipTypeId());

                int relNameWidth = fontMetrics.stringWidth(relName);

                if (relNameWidth > longestRelNameWidth) {
                    longestRelNameWidth = relNameWidth;
                }

                entries[c++] = relName;
            }

            Arrays.sort(entries);
            
            entries = Arrays.copyOf(entries, entries.length + 1);

            entries[entries.length - 1] = countString;

            if (fontMetrics.stringWidth(countString) > longestRelNameWidth) {
                longestRelNameWidth = fontMetrics.stringWidth(countString);
            }

            if (relationships.size() > 1) {
                longestRelNameWidth += fontMetrics.charWidth(',');
            }

            if (longestRelNameWidth > width) {
                width = longestRelNameWidth + 4;
            }
        }
        
        return this.createFittedPartitionLabel(entries, width, fontMetrics);
    }
}
