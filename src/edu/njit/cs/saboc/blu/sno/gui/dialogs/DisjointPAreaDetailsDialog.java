package edu.njit.cs.saboc.blu.sno.gui.dialogs;

import SnomedShared.Concept;
import SnomedShared.pareataxonomy.PAreaSummary;
import edu.njit.cs.saboc.blu.core.gui.dialogs.panels.GroupDetailsPanel;
import edu.njit.cs.saboc.blu.sno.abn.SCTAbstractionNetwork;
import edu.njit.cs.saboc.blu.sno.abn.disjointpareataxonomy.DisjointPAreaTaxonomy;
import edu.njit.cs.saboc.blu.sno.abn.disjointpareataxonomy.DisjointPartialArea;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTPArea;
import edu.njit.cs.saboc.blu.sno.gui.abnselection.SCTDisplayFrameListener;
import edu.njit.cs.saboc.blu.sno.gui.dialogs.panels.SCTConceptGroupDetailsPanel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import javax.swing.JDialog;
import javax.swing.JTabbedPane;

/**
 *
 * @author Chris
 */
public class DisjointPAreaDetailsDialog extends JDialog {

    public DisjointPAreaDetailsDialog(SCTAbstractionNetwork abstractionNetwork, DisjointPartialArea parea,
            DisjointPAreaTaxonomy djpaTaxonomy, SCTDisplayFrameListener displayFrameListener) {

        SCTConceptGroupDetailsPanel detailsPanel = 
                new SCTConceptGroupDetailsPanel(abstractionNetwork, GroupDetailsPanel.GroupType.DisjointPArea, displayFrameListener);

        Concept selectedGroupRoot = parea.getRoot();

        ArrayList<Concept> conceptsInGroup = parea.getConceptsAsList();

        StringBuilder builder = new StringBuilder();

        builder.append("<html>");
        builder.append("<font size=4 face=\"Arial\">");

        ArrayList<SCTPArea> overlaps = new ArrayList<SCTPArea>(parea.getOverlaps());
        Collections.sort(overlaps, new Comparator<SCTPArea>() {
           public int compare(SCTPArea a, SCTPArea b) {
               return a.getRoot().getName().compareTo(b.getRoot().getName());
           }
        });

        HashMap<Concept, DisjointPartialArea> parents = parea.getParents();

        for (Entry<Concept, DisjointPartialArea> parent : parents.entrySet()) {
            Concept parentConcept = parent.getKey();
            DisjointPartialArea parentDisjointPArea = parent.getValue();

            builder.append(String.format("<b>Disjoint Partial-area Root's Parent Concept(s):</b> <i>%s</i> "
                    + "(<a href=\"%d\">%d</a>)<br>", parentConcept.getName(), parentConcept.getId(), parentConcept.getId()));

            builder.append("<b>Parent Disjoint Partial-area:</b> ");

            builder.append(parentDisjointPArea.getRoot().getName());
            builder.append("<br>");

            builder.append("<b>Parent Disjoint Partial-area Overlaps: </b>");
            builder.append("<br>");

            HashSet<SCTPArea> parentOverlaps = parentDisjointPArea.getOverlaps();

            if(parentOverlaps.size() == 1) {
                builder.append("&nbsp&nbsp&nbsp ");
                builder.append("(None, basis)");
                builder.append("<br>");
            } else {
                for (SCTPArea parentOverlap : parentOverlaps) {   // Otherwise derive the title from its relationships.
                    builder.append("&nbsp&nbsp&nbsp ");
                    builder.append(parentOverlap.getRoot().getName());
                    builder.append("<br>");
                }
            }


            builder.append("<br>");
        } // End ForEach Parent


        detailsPanel.setParentText(builder.toString());

        builder = new StringBuilder();

        builder.append("<html>");
        builder.append("<font size=4 face=\"Arial\">");

        for (Concept c : conceptsInGroup) {
            builder.append(c.getName());
            
            if (c.isPrimitive()) {
                builder.append(" <b><font color ='purple'>[primitive]</font></b> ");
            }
            
            builder.append("&nbsp ");
            builder.append(String.format("<b>(<a href=\"%d\">%d</a>)</b>", c.getId(), c.getId()));
            builder.append("<br>");
        }

        detailsPanel.setConceptsText(builder.toString());

        builder = new StringBuilder();

        builder.append("<html>");
        builder.append("<font size=4 face=\"Arial\">");

        HashSet<DisjointPartialArea> children = djpaTaxonomy.getHierarchy().getChildren(parea);

        for (DisjointPartialArea child : children) {
            builder.append(String.format("%s (%d concepts) (<a href=\"%d\">%d</a>)<br>", child.getRoot().getName(),
                    child.getConceptCount(), child.getRoot().getId(), child.getRoot().getId()));

            HashSet<SCTPArea> childOverlaps = child.getOverlaps();

            for(SCTPArea childOverlap : childOverlaps) {
                builder.append("&nbsp&nbsp&nbsp ");
                builder.append(childOverlap.getRoot().getName());
                builder.append("<br>");
            }

            builder.append("<br>");
        }
        
        int primitiveConceptCount = 0;
        
        for(Concept c : conceptsInGroup) {
            if(c.isPrimitive()) {
                primitiveConceptCount++;
            }
        }
        
        detailsPanel.setChildrenText(builder.toString());

        setTitle("Disjoint Partial-area Information For: " + selectedGroupRoot.getName());

        builder = new StringBuilder();

        builder.append("<html>");
        builder.append("<font size=4 face=\"Arial\">");
        builder.append(String.format("<b>Disjoint Partial-area Name:</b> <i>%s</i><br>", selectedGroupRoot.getName()));
        builder.append(String.format("<b>Disjoint Partial-area Root Concept ID:</b> <i>"
                + "<a href=\"%d\">%d</a></i><br>", selectedGroupRoot.getId(), selectedGroupRoot.getId()));

        builder.append(String.format("<b>Total Concepts in Disjoint Partial-area:</b> <i>%s</i><br>", conceptsInGroup.size()));
        builder.append(String.format("<b>Total Primitive Concepts in Disjoint Partial-area:</b> <i>%s</i><br>", primitiveConceptCount));
        builder.append(String.format("<b>Total Parent Disjoint Partial Areas:</b> <i>%s</i><br>", parents.keySet().size()));
        builder.append(String.format("<b>Total Child Disjoint Partial Areas:</b> <i>%s</i><br>", children.size()));

        builder.append("<b>Disjoint Partial-area Overlaps Between: </b><br>");

        HashSet<SCTPArea> overlappingPAreas = parea.getOverlaps();

        for (SCTPArea overlappingPArea : overlappingPAreas) {   // Otherwise derive the title from its relationships.
            builder.append("&nbsp&nbsp&nbsp ");
            builder.append(overlappingPArea.getRoot().getName());
            builder.append("<br>");
        }

        detailsPanel.setSummaryText(builder.toString());

        JTabbedPane tabbedPane = new JTabbedPane();

        tabbedPane.addTab("Disjoint Partial-area Details", detailsPanel);

        add(tabbedPane);

        setSize(768, 600);
        setLocationRelativeTo(null);
        
        setResizable(true);
        setModal(true);

        setVisible(true);
    }
}