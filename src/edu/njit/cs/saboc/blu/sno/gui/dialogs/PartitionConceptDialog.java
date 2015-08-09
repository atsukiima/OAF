package edu.njit.cs.saboc.blu.sno.gui.dialogs;

import SnomedShared.Concept;
import SnomedShared.generic.GenericConceptGroup;
import SnomedShared.generic.GenericContainerPartition;
import SnomedShared.overlapping.OverlapInheritancePartition;
import SnomedShared.pareataxonomy.InheritedRelationship;
import edu.njit.cs.saboc.blu.core.abn.AbstractionNetwork;
import edu.njit.cs.saboc.blu.sno.abn.SCTAbstractionNetwork;
import edu.njit.cs.saboc.blu.sno.abn.disjointpareataxonomy.DisjointPAreaTaxonomy;
import edu.njit.cs.saboc.blu.sno.abn.disjointpareataxonomy.SCTDisjointPAreaTaxonomyGenerator;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTArea;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTPArea;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTPAreaTaxonomy;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTRegion;
import edu.njit.cs.saboc.blu.sno.abn.tan.TribalAbstractionNetwork;
import edu.njit.cs.saboc.blu.sno.datastructure.hierarchy.SCTMultiRootedConceptHierarchy;
import edu.njit.cs.saboc.blu.sno.gui.abnselection.SCTDisplayFrameListener;
import edu.njit.cs.saboc.blu.sno.gui.dialogs.disjointpareataxonomy.DisjointPAreaTaxonomyGraphPanel;
import edu.njit.cs.saboc.blu.sno.utils.BrowserLauncher;
import edu.njit.cs.saboc.blu.sno.utils.UtilityMethods;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author Chris
 */
public class PartitionConceptDialog extends JDialog {

    public PartitionConceptDialog(final JFrame parentFrame, final GenericContainerPartition partition, 
            final SCTAbstractionNetwork sctAbN, boolean treatAsContainer, final SCTDisplayFrameListener displayFrameListener) {
        
        super(parentFrame);

        final boolean isPartialAreaDialog = (sctAbN instanceof SCTPAreaTaxonomy);
        
        ArrayList<GenericConceptGroup> groups = partition.getGroups();
        
        final HashMap<Long, GenericConceptGroup> rootIdMap = new HashMap<Long, GenericConceptGroup>();
        ArrayList<Long> groupRootIds = new ArrayList<Long>();
        
        HashMap<Long, ArrayList<Concept>> concepts;
        
        for (GenericConceptGroup group : groups) {
            rootIdMap.put(group.getRoot().getId(), group);
            groupRootIds.add(group.getRoot().getId());
        }
        
        if (isPartialAreaDialog) {
            concepts = sctAbN.getDataSource().getConceptsInPAreaSet(
                    (SCTPAreaTaxonomy)sctAbN.getAbstractionNetwork(), ((SCTRegion)partition).getPAreasInRegion());
        } else {
            concepts = sctAbN.getDataSource().getConceptsInClusterSet(
                    (TribalAbstractionNetwork)sctAbN.getAbstractionNetwork(), ((OverlapInheritancePartition)partition).getClusters());
        }

        StringBuilder builder = new StringBuilder();

        String partitionName = "";
        ArrayList<String> partitionNameArray = new ArrayList<String>();
        ArrayList<SCTRegion> regionsArray = new ArrayList<SCTRegion>();

        if(isPartialAreaDialog) {
            SCTRegion region = (SCTRegion)partition;
            SCTPAreaTaxonomy taxonomy = (SCTPAreaTaxonomy)sctAbN.getAbstractionNetwork();
            
            if(treatAsContainer) {
                ArrayList<InheritedRelationship> regionRels = new ArrayList<InheritedRelationship>(region.getPAreasInRegion().get(0).getRelationships());
                
                partitionName = UtilityMethods.getRegionName(regionRels,
                    taxonomy.getLateralRelsInHierarchy()).replaceAll("\\*", "").replaceAll("\\+", "");
            } else {
                ArrayList<InheritedRelationship> regionRels = new ArrayList<InheritedRelationship>(region.getRelationships());
                
                partitionName = UtilityMethods.getRegionName(regionRels,
                    taxonomy.getLateralRelsInHierarchy());
                
                partitionNameArray.add(partitionName);      // Name of the region that was clicked on
                
                /* find the area that this region belongs to */
                for(int i=0; i < ((SCTPAreaTaxonomy)sctAbN.getAbstractionNetwork()).getExplicitHierarchyAreas().size(); i++) {     // for each Area
                    SCTArea currentArea = taxonomy.getExplicitHierarchyAreas().get(i);
                    
                    for(int j=0; j < currentArea.getRegions().size(); j++) {                                        // for each Region within that Area
                        SCTRegion currentRegion = currentArea.getRegions().get(j);
                        
                        if((currentRegion.getPAreasInRegion().get(0)).getId() ==
                                (((SCTRegion)partition).getPAreasInRegion().get(0)).getId()) {

                            regionsArray = currentArea.getRegions();

                            break;
                        }
                    }
                }
                
                String otherPartitionName;
                
                for(int i=0; i < regionsArray.size(); i++) {
                    SCTRegion r = (SCTRegion)regionsArray.get(i);
                    
                    ArrayList<InheritedRelationship> rRels = new ArrayList<InheritedRelationship>(r.getRelationships());

                    otherPartitionName = UtilityMethods.getRegionName(rRels, taxonomy.getLateralRelsInHierarchy());
                    
                    if(!otherPartitionName.equals(partitionNameArray.get(0))) {    // avoid repeating the first region name (one that was clicked on)
                        partitionNameArray.add(otherPartitionName);
                    }
                }
            }
        } else {
            OverlapInheritancePartition inheritancePartition = (OverlapInheritancePartition)partition;
            TribalAbstractionNetwork tan = (TribalAbstractionNetwork)sctAbN.getAbstractionNetwork();

            if(treatAsContainer) {
                partitionName = UtilityMethods.getOverlapPartitionName(inheritancePartition.getEntryPointSet(),
                        tan.getPatriarchNames());
            } else {
                partitionName = UtilityMethods.getOverlapPartitionName(inheritancePartition.getEntryPointSet(),
                        tan.getPatriarchNames());
            }
        }
        
        final HashMap<Concept, ArrayList<Long>> overlappingConcepts = new HashMap<Concept, ArrayList<Long>>();
        
        HashMap<Long, Integer> groupPrimitiveConceptCounts = new HashMap<Long, Integer>();

        for(long id : concepts.keySet()) {
            ArrayList<Concept> conceptsInPArea = concepts.get(id);
            
            groupPrimitiveConceptCounts.put(id, 0);

            for(Concept c : conceptsInPArea) {
                if(!overlappingConcepts.containsKey(c)) {
                    overlappingConcepts.put(c, new ArrayList<Long>());
                }
                
                if(c.isPrimitive()) {
                    groupPrimitiveConceptCounts.put(id, groupPrimitiveConceptCounts.get(id) + 1);
                }

                overlappingConcepts.get(c).add(id);
            }
        }

        final StringBuilder shadowBuilder = new StringBuilder();

        builder.append("<html>");
        builder.append("<font size=4 face=\"Arial\">");

        builder.append(String.format("<b> %s </b>", partitionName));
        shadowBuilder.append(partitionName);

        builder.append("<p>");
        shadowBuilder.append("\n\n");
        
        int uniquePrimitiveConcepts = 0;
        
        for(Concept c : overlappingConcepts.keySet()) {
            if(c.isPrimitive()) {
                uniquePrimitiveConcepts++;
            }
        }

        String uniqueConceptsStr = String.format("Total Unique Concepts in %s: %d, Total Unique Primitive Concepts: %d",
                (treatAsContainer ? "Area" : "Region"), overlappingConcepts.keySet().size(), uniquePrimitiveConcepts);

        builder.append(uniqueConceptsStr);
        shadowBuilder.append(uniqueConceptsStr);
        
        builder.append("<p>");
        shadowBuilder.append("\n\n");

        class TextBlock {
            public int textStart;
            public int textEnd;

            public TextBlock(int start, int end) {
                this.textStart = start;
                this.textEnd = end;
            }
            
            public boolean containsOffset(int offset) {
                return offset >= textStart && offset <= textEnd;
            }
        }

        final HashMap<Concept, ArrayList<TextBlock>> textBounds = new HashMap<Concept, ArrayList<TextBlock>>();

        /* Print info about what other PAreas does each concept belong to */
        for (long pareaRootId : groupRootIds) {
            GenericConceptGroup group = rootIdMap.get(pareaRootId);

            String pareaConceptCountStr = String.format("%s (%d concepts, %d primitives)", group.getRoot().getName(), group.getConceptCount(),
                    groupPrimitiveConceptCounts.get(pareaRootId));

            builder.append(pareaConceptCountStr);
            builder.append("<br>");
            shadowBuilder.append(pareaConceptCountStr);
            shadowBuilder.append("\n");

            for (Concept c : concepts.get(pareaRootId)) {
                builder.append("&nbsp&nbsp&nbsp ");
                shadowBuilder.append("    ");
                
                builder.append(c.getName());
                shadowBuilder.append(c.getName());
                
                if(c.isPrimitive()) {
                    builder.append(" <b><font color ='purple'>[primitive]</font></b> ");
                    shadowBuilder.append(" [primitive] ");
                }

                builder.append(String.format("&nbsp <b>(<a href=\"%d\">%d</a>)</b>", c.getId(), c.getId()));
                shadowBuilder.append(String.format("  (%d)", c.getId()));

                if(overlappingConcepts.get(c).size() > 1) {
                    builder.append("&nbsp&nbsp ");
                    shadowBuilder.append("   ");

                    int startIndex = shadowBuilder.length();

                    if(isPartialAreaDialog) {
                        builder.append(String.format("<font color=\"red\">Concept in %d Other Partial-area(s)</font>",
                                overlappingConcepts.get(c).size() - 1));

                        shadowBuilder.append(String.format("Concept in %d Other Partial-area(s)",
                                overlappingConcepts.get(c).size() - 1));
                    } else {
                        builder.append(String.format("<font color=\"red\">Concept in %d Other Cluster(s)</font>",
                                overlappingConcepts.get(c).size() - 1));

                        shadowBuilder.append(String.format("Concept in %d Other Cluster(s)",
                                overlappingConcepts.get(c).size() - 1));
                    }

                    int endIndex = shadowBuilder.length();

                    if(!textBounds.containsKey(c)) {
                        textBounds.put(c, new ArrayList<TextBlock>());
                    }
                    
                    textBounds.get(c).add(new TextBlock(startIndex, endIndex));
                }

                builder.append("<br>");
                shadowBuilder.append("\n");
            }

            builder.append("<br>");
            shadowBuilder.append("\n");
        }

        setResizable(true);

        if(isPartialAreaDialog) {
            if(treatAsContainer) {
                setTitle("Concepts in Selected Area");
            } else {
                setTitle("Concepts in Selected Region");
            }
        } else {
            if(treatAsContainer) {
                setTitle("Concepts in Selected Common Overlap Set");
            } else {
                setTitle("Concepts in Selected Overlap Inheritance Partition");
            }
        }

        JEditorPane conceptsPane = new JEditorPane() {
            public String getToolTipText(MouseEvent e) {
                int start = this.viewToModel(e.getPoint());

                for(Entry<Concept, ArrayList<TextBlock>> entry : textBounds.entrySet()) {

                    ArrayList<TextBlock> blocks = entry.getValue();

                    for (TextBlock block : blocks) {
                        if (block.containsOffset(start)) {
                            StringBuilder toolTip = new StringBuilder();

                            toolTip.append("<html>");
                            
                            if(isPartialAreaDialog) {
                                toolTip.append(String.format("<b>%s</b> is in the following Partial Areas:<br>", entry.getKey().getName()));
                            } else {
                                toolTip.append(String.format("<b>%s</b> is in the following Clusters:<br>", entry.getKey().getName()));
                            }

                            ArrayList<Long> groupRoots = overlappingConcepts.get(entry.getKey());

                            for(long rootConceptId : groupRoots) {
                                GenericConceptGroup group = rootIdMap.get(rootConceptId);
                                
                                toolTip.append(String.format("&nbsp %s<br>", group.getRoot().getName()));
                            }

                            return toolTip.toString();
                        }
                    }
                }
                
                return null;

            }
        };

        /* If Regions is turned on, then print the rest of the regions in the Area that holds the current selected region */
        if(!treatAsContainer) {
            builder.append("<br>");
            shadowBuilder.append("\n");

            builder.append("<b> <font color=\"#04B431\" size=5> Other Regions in this Area: </font> </b>");
            shadowBuilder.append("Other Regions in this Area:");

            builder.append("<br><br>");
            shadowBuilder.append("\n\n");

            for (int i = 0; i < regionsArray.size(); i++) {
                if (regionsArray.get(i).equals(partition)) {
                    continue;
                }

                groups = ((GenericContainerPartition) regionsArray.get(i)).getGroups();

                final HashMap<Long, GenericConceptGroup> genericRootIdMap = new HashMap<Long, GenericConceptGroup>();
                groupRootIds = new ArrayList<Long>();

                if (isPartialAreaDialog) {
                    concepts = sctAbN.getDataSource().getConceptsInPAreaSet(
                            (SCTPAreaTaxonomy) sctAbN.getAbstractionNetwork(), ((SCTRegion) partition).getPAreasInRegion());
                } else {
                    concepts = sctAbN.getDataSource().getConceptsInClusterSet(
                            (TribalAbstractionNetwork) sctAbN.getAbstractionNetwork(), ((OverlapInheritancePartition) partition).getClusters());
                }

                final HashMap<Concept, ArrayList<Long>> genericOverlappingConcepts = new HashMap<Concept, ArrayList<Long>>();

                groupPrimitiveConceptCounts = new HashMap<Long, Integer>();

                for(long id : concepts.keySet()) {
                    ArrayList<Concept> conceptsInPArea = concepts.get(id);

                    groupPrimitiveConceptCounts.put(id, 0);

                    for(Concept c : conceptsInPArea) {
                        if(!genericOverlappingConcepts.containsKey(c)) {
                            genericOverlappingConcepts.put(c, new ArrayList<Long>());
                        }

                        if(c.isPrimitive()) {
                            groupPrimitiveConceptCounts.put(id, groupPrimitiveConceptCounts.get(id) + 1);
                        }

                        genericOverlappingConcepts.get(c).add(id);
                    }
                }
                
                builder.append(String.format("<b> %s </b>", partitionNameArray.get(i)));
                shadowBuilder.append(partitionNameArray.get(i));

                builder.append("<p>");
                shadowBuilder.append("\n\n");

                uniquePrimitiveConcepts = 0;

                for(Concept c : genericOverlappingConcepts.keySet()) {
                    if(c.isPrimitive()) {
                        uniquePrimitiveConcepts++;
                    }
                }

                uniqueConceptsStr = String.format("Total Unique Concepts in %s: %d, Total Unique Primitive Concepts: %d",
                        (treatAsContainer ? "Area" : "Region"), genericOverlappingConcepts.keySet().size(), uniquePrimitiveConcepts);

                builder.append(uniqueConceptsStr);
                shadowBuilder.append(uniqueConceptsStr);
                
                builder.append("<p>");
                shadowBuilder.append("\n\n");

                /* Print info about what other PAreas does each concept belong to */
                for (long pareaRootId : groupRootIds) {
                    GenericConceptGroup group = genericRootIdMap.get(pareaRootId);
                    
                    String pareaConceptCountStr = String.format("%s (%d concepts, %d primitives)", group.getRoot().getName(), group.getConceptCount(),
                            groupPrimitiveConceptCounts.get(pareaRootId));

                    builder.append(pareaConceptCountStr);
                    builder.append("<br>");
                    shadowBuilder.append(pareaConceptCountStr);
                    shadowBuilder.append("\n");

                    for (Concept c : concepts.get(pareaRootId)) {
                        builder.append("&nbsp&nbsp&nbsp ");
                        shadowBuilder.append("    ");

                        builder.append(c.getName());
                        shadowBuilder.append(c.getName());

                        if(c.isPrimitive()) {
                            builder.append(" <b><font color ='purple'>[primitive]</font></b> ");
                            shadowBuilder.append(" [primitive] ");
                        }

                        builder.append(String.format("&nbsp <b>(<a href=\"%d\">%d</a>)</b>", c.getId(), c.getId()));
                        shadowBuilder.append(String.format("  (%d)", c.getId()));

                        if(genericOverlappingConcepts.get(c).size() > 1) {
                            builder.append("&nbsp&nbsp ");
                            shadowBuilder.append("   ");

                            int startIndex = shadowBuilder.length();

                            if(isPartialAreaDialog) {
                                builder.append(String.format("<font color=\"red\">Concept in %d Other Partial-area(s)</font>",
                                        genericOverlappingConcepts.get(c).size() - 1));

                                shadowBuilder.append(String.format("Concept in %d Other Partial-area(s)",
                                        genericOverlappingConcepts.get(c).size() - 1));
                            } else {
                                builder.append(String.format("<font color=\"red\">Concept in %d Other Cluster(s)</font>",
                                        genericOverlappingConcepts.get(c).size() - 1));

                                shadowBuilder.append(String.format("Concept in %d Other Cluster(s)",
                                        genericOverlappingConcepts.get(c).size() - 1));
                            }

                            int endIndex = shadowBuilder.length();

                            if(!textBounds.containsKey(c)) {
                                textBounds.put(c, new ArrayList<TextBlock>());
                            }

                            textBounds.get(c).add(new TextBlock(startIndex, endIndex));
                        }

                        builder.append("<br>");
                        shadowBuilder.append("\n");
                    }

                    builder.append("<br>");
                    shadowBuilder.append("\n");
                }
            }
        }
        
        conceptsPane.setToolTipText("");

        conceptsPane.setEditable(false);
        conceptsPane.setContentType("text/html");
        conceptsPane.setText(builder.toString());
        conceptsPane.setEditable(false);
        conceptsPane.addHyperlinkListener(new BrowserLauncher(displayFrameListener, sctAbN));

        conceptsPane.setSelectionStart(0);
        conceptsPane.setSelectionEnd(0);
        
        boolean hasOverlappingConcepts = false;
        for(Concept c : overlappingConcepts.keySet()) {
            if(overlappingConcepts.get(c).size() > 1) {     // determine if the hierarchy has any overlapping concepts
                hasOverlappingConcepts = true;
                break;
            }
        }

        final JTabbedPane tabbedPane = new JTabbedPane();
        
        tabbedPane.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent changeEvent) {
                int index = tabbedPane.getSelectedIndex();
                
                if (index == 1 && tabbedPane.getTabComponentAt(index) == null) { 
                    DisjointPAreaTaxonomy taxonomy;
                    
                    //if(sctAbN instanceof RootSubtaxonomy) {
                        //taxonomy = createDisjointPAreaRootSubtaxonomy(sctAbN, partition);
                    //} else {
                        taxonomy = createDisjointPAreaTaxonomy(sctAbN, partition);
                    //}
                    
                    DisjointPAreaTaxonomyGraphPanel djpaPanel = new DisjointPAreaTaxonomyGraphPanel(parentFrame, taxonomy, displayFrameListener);
                    
                    tabbedPane.setComponentAt(1, djpaPanel);
                }
            }
        });
        
        if(isPartialAreaDialog) {
            tabbedPane.addTab((treatAsContainer ? "Area Concept List" : "Region Concept List"), new JScrollPane(conceptsPane));
            
            if(hasOverlappingConcepts == true) {    // Display disjoint Partial-areas only if there are overlapping concepts in the hierarchy
                tabbedPane.addTab("Disjoint Partial-areas", null);
            }

        } else {
            tabbedPane.addTab((treatAsContainer ? "Common Overlap Set Concept List" : "Overlap Inheritance Partition Concept List"), new JScrollPane(conceptsPane));
        }
       
        final GenericContainerPartition tempPartition = partition;          // Declared 'final' to..
        final AbstractionNetwork tempHierarchyData = sctAbN.getAbstractionNetwork();       // .. use ..
        final HashMap<Long, ArrayList<Concept>> tempConcepts = concepts;    // .. inside the ..
        final boolean tempHasOverlappingConcepts = hasOverlappingConcepts;  // .. MouseListner
        
        tabbedPane.addMouseListener(new MouseListener() {
            boolean repeat = false;
            
            public void mouseExited(MouseEvent e) {
                
            }

            public void mouseClicked(MouseEvent e) {

            }

            public void mousePressed(MouseEvent e) {
            }

            public void mouseReleased(MouseEvent e) {
            }

            public void mouseEntered(MouseEvent e) {
            }
        });

        add(tabbedPane);

        setSize(800, 600);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private DisjointPAreaTaxonomy createDisjointPAreaTaxonomy(SCTAbstractionNetwork abn, GenericContainerPartition partition) {
        ArrayList<SCTPArea> pareas = ((SCTRegion)partition).getPAreasInRegion();
        
        ArrayList<Long> pareaRootIds = new ArrayList<Long>();

        HashSet<Concept> roots = new HashSet<Concept>();

        // Get the roots of the partial-areas in the area
        for (SCTPArea parea : pareas) {
            pareaRootIds.add(parea.getRoot().getId());
            roots.add(parea.getRoot());
        }

        SCTMultiRootedConceptHierarchy hierarchy = abn.getDataSource().getRegionConceptHierarchy((SCTPAreaTaxonomy) abn.getAbstractionNetwork(), pareas);

        SCTDisjointPAreaTaxonomyGenerator generator = new SCTDisjointPAreaTaxonomyGenerator();
        
        return generator.generateDisjointAbstractionNetwork((SCTPAreaTaxonomy)abn, new HashSet<>(pareas), hierarchy);
    }
    
    /*
    private DisjointPAreaRootSubtaxonomy createDisjointPAreaRootSubtaxonomy(SCTAbstractionNetwork abn, GenericContainerPartition partition) {
        ArrayList<SCTPArea> topLevelPAreas = null;
        
        SCTPAreaTaxonomy topLevelTaxonomy = ((RootSubtaxonomy)abn.getAbstractionNetwork()).getTopLevelTaxonomy();
        
        ArrayList<SCTArea> topLevelAreas = topLevelTaxonomy.getHierarchyAreas();
        
        ArrayList<InheritedRelationship> areaRelationships = 
                new ArrayList<InheritedRelationship>(((SCTRegion)partition).getPAreasInRegion().get(0).getRelationships());
        
        ArrayList<Long> rels = new ArrayList<Long>();
        
        for(InheritedRelationship rel : areaRelationships) {
            rels.add(rel.getRelationshipTypeId());
        }
        
        for(SCTArea area : topLevelAreas) {
            if(area.getRelationshipIds().equals(rels)) {
                topLevelPAreas = area.getAllPAreas();
                
                break;
            }
        }
        
        SCTMultiRootedConceptHierarchy hierarchy = abn.getDataSource().getRegionConceptHierarchy((SCTPAreaTaxonomy) abn, topLevelPAreas);

        DisjointPAreaRootSubtaxonomy djpaTaxonomy = new DisjointPAreaRootSubtaxonomy(
                    new HashSet<SCTPArea>(topLevelPAreas),
                    new HashSet<SCTPArea>(partition.getGroups()),
                    hierarchy,
                    (SCTPAreaTaxonomy) abn);
        
        return djpaTaxonomy;
    }
    */
    
    /**
     * Print detailed Concept info based on its probability of being erroneous.
     * @param c
     * @param color
     * @param overlappingConcepts
     * @param rootIdMap 
     */
    public String printSuspectConcept(Concept c, String color, 
            HashMap<Concept, ArrayList<Long>> overlappingConcepts, HashMap<Long, GenericConceptGroup> rootIdMap) {
        
        StringBuilder builder = new StringBuilder();
        
        builder.append("&nbsp&nbsp&nbsp");
        builder.append(String.format("<b> <font color=\"%s\"> %s </font> </b>", color, c.getName()));
        if(c.isPrimitive()) {
            builder.append(" <b><font color ='purple'>[primitive]</font></b> ");
        }
        builder.append(String.format("&nbsp <b>(<a href=\"%d\">%d</a>)</b>", c.getId(), c.getId()));

        String pareaName = "";
        int counter = overlappingConcepts.get(c).size();
        for(long id : overlappingConcepts.get(c)) {
            GenericConceptGroup currentGroup = rootIdMap.get(id);
            pareaName += currentGroup.getRoot().getName();

            counter--;
            if(counter != 0) {
                pareaName += ", ";
            }
        }
        builder.append("&nbsp");
        builder.append(String.format(" in <font color=\"#848484\"> %s </font> <br>", pareaName));
        
        return builder.toString();
    }
    
    /**
     * Collect all combinations of r from n
     * @param n
     * @param r
     * @param allCombinations
     * @param totalCombinations : stores the number of possible combinations for the given pair of (n, r)
     */
    public void searchCombinations(int n, int r, ArrayList<ArrayList<Integer>> allCombinations, int totalCombinations) {
        ArrayList<Integer> currentCombination = new ArrayList<Integer>();
        
        for(int i=0; i < r; i++) {
            currentCombination.add(i);
        }

        allCombinations.add(new ArrayList<Integer>(currentCombination));    // store the first combination

        for(int i=1; i < totalCombinations; i++) {  // for the remaining combinations
            int j = r - 1;
            int max_value = n - 1;
            
            while(j >= 0 && currentCombination.get(j) == max_value) {   // find the rightmost element that is not at the max-value of that position
                j--;
                max_value--;
            }
            
            if(j >= 0) {
                int previousValue = currentCombination.get(j);
                currentCombination.remove(j);
                currentCombination.add(j, previousValue + 1);

                for(int k = j+1; k < r; k++) {
                    currentCombination.remove(k);
                    currentCombination.add(k, currentCombination.get(k-1) + 1);
                }
            }

            allCombinations.add(new ArrayList<Integer>(currentCombination));
        }
    }
}
