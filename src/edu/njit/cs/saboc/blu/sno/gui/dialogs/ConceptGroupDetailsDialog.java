
package edu.njit.cs.saboc.blu.sno.gui.dialogs;

import SnomedShared.Concept;
import SnomedShared.generic.GenericConceptGroup;
import SnomedShared.overlapping.ClusterSummary;
import SnomedShared.overlapping.EntryPoint;
import SnomedShared.pareataxonomy.InheritedRelationship;
import SnomedShared.pareataxonomy.InheritedRelationship.InheritanceType;
import SnomedShared.pareataxonomy.GroupParentInfo;
import edu.njit.cs.saboc.blu.core.abn.AbstractionNetwork;
import edu.njit.cs.saboc.blu.core.gui.dialogs.panels.GroupDetailsPanel;
import edu.njit.cs.saboc.blu.core.gui.iconmanager.IconManager;
import edu.njit.cs.saboc.blu.core.utils.filterable.list.Filterable;
import edu.njit.cs.saboc.blu.core.utils.filterable.list.FilterableListModel;
import edu.njit.cs.saboc.blu.sno.abn.SCTAbstractionNetwork;
import edu.njit.cs.saboc.blu.sno.abn.tan.TribalAbstractionNetwork;
import edu.njit.cs.saboc.blu.sno.gui.abnselection.SCTDisplayFrameListener;
import edu.njit.cs.saboc.blu.core.gui.dialogs.concepthierarchy.HierarchyPanelClickListener;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTPArea;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTPAreaTaxonomy;
import edu.njit.cs.saboc.blu.sno.gui.dialogs.panels.SCTConceptGroupDetailsPanel;
import edu.njit.cs.saboc.blu.sno.gui.dialogs.panels.concepthierarchy.SCTConceptHierarchyViewPanel;
import edu.njit.cs.saboc.blu.sno.utils.filterable.entry.FilterableConceptEntry;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 *
 * @author Chris
 */
public class ConceptGroupDetailsDialog<T extends AbstractionNetwork> extends JDialog {

    public static enum DialogType {
        PartialArea,
        Cluster,
    }
    
    final JList resultsList;
    protected FilterableListModel conceptModel;
    
    private JPanel filterPanel = new JPanel();
    private JTextField filterField = new JTextField();
    private JButton closeButton = new JButton();

    public ConceptGroupDetailsDialog(
            final GenericConceptGroup group, 
            final SCTAbstractionNetwork<T> sctAbN, 
            final DialogType dialogType,
            final SCTDisplayFrameListener displayFrameListener) {
        
        this.resultsList = new JList();
        this.conceptModel = new FilterableListModel();

        SCTConceptGroupDetailsPanel detailsPanel;
        String dialogTypeStr;

        if(dialogType == DialogType.PartialArea) {
            dialogTypeStr = "Partial-area";
            detailsPanel = new SCTConceptGroupDetailsPanel(sctAbN, GroupDetailsPanel.GroupType.PartialArea, displayFrameListener);
        } else {
            dialogTypeStr = "Cluster";
            detailsPanel = new SCTConceptGroupDetailsPanel(sctAbN, GroupDetailsPanel.GroupType.Cluster, displayFrameListener);
        }
        
        Concept selectedGroupRoot = group.getRoot();

        ArrayList<Concept> conceptsInGroup;

        if (dialogType == DialogType.PartialArea) {
            conceptsInGroup = sctAbN.getDataSource().getConceptsInPArea(
                    (SCTPAreaTaxonomy)sctAbN.getAbstractionNetwork(), (SCTPArea)group);
        } else {
            conceptsInGroup = sctAbN.getDataSource().getConceptsInCluster(
                    (TribalAbstractionNetwork)sctAbN.getAbstractionNetwork(), (ClusterSummary)group);
        }
        
        int primitiveConceptCount = 0;
        
        for(Concept c : conceptsInGroup) {            
            if(c.isPrimitive()) {
                primitiveConceptCount++;
            }
        }
        
        HashSet<Integer> childrenGroupIds = sctAbN.getAbstractionNetwork().getGroupChildren(group.getId());

        StringBuilder builder = new StringBuilder();

        builder.append("<html>");
        builder.append("<font size=4 face=\"Arial\">");

        HashMap<Integer, ? extends GenericConceptGroup> groups;

        if(dialogType == DialogType.PartialArea) {
            groups = ((SCTPAreaTaxonomy)sctAbN.getAbstractionNetwork()).getPAreas();
        } else {
            groups = ((TribalAbstractionNetwork)sctAbN.getAbstractionNetwork()).getClusters();
        }

        ArrayList<GroupParentInfo> groupParentInfo;

        if (dialogType == DialogType.PartialArea) {
            groupParentInfo = sctAbN.getDataSource().getPAreaParentInfo(
                    (SCTPAreaTaxonomy)sctAbN.getAbstractionNetwork(), (SCTPArea)group);
        } else {
            groupParentInfo = sctAbN.getDataSource().getClusterParentInfo(
                    (TribalAbstractionNetwork)sctAbN.getAbstractionNetwork(), (ClusterSummary)group);
        }
        
        /**
         * For "Partial Area Root Concepts (Parent)" panel
         */        
        for (GroupParentInfo ppi : groupParentInfo) {
            Concept parent = ppi.getParentConcept();
            
            GenericConceptGroup parentGroup = null;
         
            for(int id : groups.keySet()) {
                GenericConceptGroup summary = groups.get(id);

                if(summary.getRoot().getId() == ppi.getParentPAreaRootId()) {
                    parentGroup = summary;
                    break;
                }
            }

            if(parentGroup == null) {
                continue;
            }

            if(dialogType == DialogType.PartialArea) {
                builder.append(String.format("<b>Root Parent Concept:</b> <i>%s</i> "
                        + "(<a href=\"%d\">%d</a>)<br>", parent.getName(), parent.getId(), parent.getId()));

                builder.append("<b>Parent Partial-area:</b> ");
            } else {
                builder.append(String.format("<b>Root Parent Concept:</b> <i>%s</i> "
                        + "(<a href=\"%d\">%d</a>)<br>", parent.getName(), parent.getId(), parent.getId()));

                builder.append("<b>Parent Cluster</b> ");
            }

            builder.append(parentGroup.getRoot().getName());
            builder.append("<br>");

            if(dialogType == DialogType.PartialArea) {
                builder.append("<b>Parent Partial-area Relationships: </b>");
                builder.append("<br>");

                HashSet<InheritedRelationship> relationships = ((SCTPArea)parentGroup).getRelationships();

                for (InheritedRelationship rel : relationships) {   // Otherwise derive the title from its relationships.
                    String relStr = ((SCTPAreaTaxonomy)sctAbN.getAbstractionNetwork()).getLateralRelsInHierarchy().get(rel.getRelationshipTypeId());
                    relStr += rel.getInheritanceType() == InheritanceType.INHERITED ? "*" : "+";

                    builder.append("&nbsp&nbsp&nbsp ");
                    builder.append(relStr);
                    builder.append("<br>");
                }
            } else {
                builder.append("<b>Parent Cluster Tribes: </b>");
                builder.append("<br>");

                HashMap<Long, String> entryPointNames = 
                        ((TribalAbstractionNetwork)sctAbN.getAbstractionNetwork()).getPatriarchNames();
                
                ArrayList<EntryPoint> entryPoints = 
                        ((ClusterSummary) parentGroup).getEntryPointSet().getSortedEntryPointSet(entryPointNames);

                for (EntryPoint ep : entryPoints) {   // Otherwise derive the title from its relationships.
                    String relStr = entryPointNames.get(ep.getEntryPointConceptId());
                    relStr += ep.getInheritanceType() == EntryPoint.InheritanceType.INHERITED ? "*" : "+";

                    builder.append("&nbsp&nbsp&nbsp ");
                    builder.append(relStr);
                    builder.append("<br>");
                }
            }

            builder.append("<br>");
        } // End ForEach ParentInfo

        detailsPanel.setParentText(builder.toString());

        /**
         * For "Concepts in Partial-area (Alphabetical)" panel (filterable list)
         */
        
        detailsPanel.setConceptsComponent(createFilterableConceptListPanel(conceptsInGroup));
        
        /**
         * For "Child Partial-area" panel
         */
        builder = new StringBuilder();

        builder.append("<html>");
        builder.append("<font size=4 face=\"Arial\">");

        if (childrenGroupIds != null) {

            ArrayList<GenericConceptGroup> childrenGroups = new ArrayList<GenericConceptGroup>();

            for (int child : childrenGroupIds) {
                childrenGroups.add(groups.get(child));
            }

            Collections.sort(childrenGroups, new Comparator<GenericConceptGroup>() {
                public int compare(GenericConceptGroup a, GenericConceptGroup b) {
                    return a.getRoot().getName().compareToIgnoreCase(b.getRoot().getName());
                }
            });

            for (GenericConceptGroup childGroup : childrenGroups) {
                builder.append(String.format("%s (%d concepts) (<a href=\"%d\">%d</a>)<br>", childGroup.getRoot().getName(),
                        childGroup.getConceptCount(), childGroup.getRoot().getId(), childGroup.getRoot().getId()));

                if(dialogType == DialogType.PartialArea) {
                    HashSet<InheritedRelationship> relationships = ((SCTPArea)childGroup).getRelationships(); // child
                    HashSet<InheritedRelationship> parent_relationships = ((SCTPArea)group).getRelationships(); // parent
                    
                    HashMap<Long, String> relNameMap = 
                            ((SCTPAreaTaxonomy)sctAbN.getAbstractionNetwork()).getLateralRelsInHierarchy();

                    for (InheritedRelationship rel : relationships) {   // Otherwise derive the title from its relationships.
                        
                        int count_identical = 0;
                        
                        for (InheritedRelationship parent_rel : parent_relationships)
                        {
                            if (rel.getRelationshipTypeId() == parent_rel.getRelationshipTypeId())
                            {
                                count_identical++;
                                break;                                
                            }
                        }
                                                                       
                        String relStr = relNameMap.get(rel.getRelationshipTypeId());
                        relStr += rel.getInheritanceType() == InheritanceType.INHERITED ? "*" : "+";

                        builder.append("&nbsp&nbsp&nbsp ");
                        if(count_identical == 0)    // new relationship found in child, so print it purple
                        {
                            builder.append(String.format("<font size=4 face=\"Arial\" color=#800080> %s </font>", relStr));
                        }
                        else
                        {
                            builder.append(relStr);
                        }
                        builder.append("<br>");
                    }
                } else {
                    HashMap<Long, String> entryPointNames = 
                            ((TribalAbstractionNetwork)sctAbN).getPatriarchNames();
                    
                    ArrayList<EntryPoint> entryPoints = 
                            ((ClusterSummary)childGroup).getEntryPointSet().getSortedEntryPointSet(entryPointNames);

                    for (EntryPoint ep : entryPoints) {   // Otherwise derive the title from its relationships.
                        String relStr = entryPointNames.get(ep.getEntryPointConceptId());
                        relStr += ep.getInheritanceType() == EntryPoint.InheritanceType.INHERITED ? "*" : "+";

                        builder.append("&nbsp&nbsp&nbsp ");
                        builder.append(relStr);
                        builder.append("<br>");
                    }
                }

                builder.append("<br>");
            }
        }

        detailsPanel.setChildrenText(builder.toString());

        
        /**
         * For the top Panel for Summary
         */
        if(dialogType == DialogType.PartialArea) {
            setTitle("Partial-area Information For: " + selectedGroupRoot.getName());
        } else {
            setTitle("Cluster Information For: " + selectedGroupRoot.getName());
        }

        builder = new StringBuilder();

        builder.append("<html>");
        builder.append("<font size=4 face=\"Arial\">");
        builder.append(String.format("<b>%s Name:</b> <i>%s</i><br>", dialogTypeStr, selectedGroupRoot.getName()));
        builder.append(String.format("<b>%s Concept ID:</b> <i>"
                + "<a href=\"%d\">%d</a></i><br>",
                dialogType == DialogType.PartialArea ? "Root" : "Header", 
                selectedGroupRoot.getId(), selectedGroupRoot.getId()));
        
        builder.append(String.format("<b>Total Concepts in %s:</b> <i>%s</i><br>", dialogTypeStr, conceptsInGroup.size()));
        builder.append(String.format("<b>Total Primitive Concepts in %s:</b> <i>%d</i><br>", dialogTypeStr, primitiveConceptCount));
        builder.append(String.format("<b>Total Parent %ss:</b> <i>%s</i><br>", dialogTypeStr, groupParentInfo.size()));
        builder.append(String.format("<b>Total Child %ss:</b> <i>%s</i><br>", dialogTypeStr, (childrenGroupIds == null ? 0 : childrenGroupIds.size())));

        if(dialogType == DialogType.PartialArea) {
            builder.append("<b>Partial-area Relationships: </b><br>");
        } else {
            builder.append("<b>Cluster Tribes: </b><br>");
        }

        if(dialogType == DialogType.PartialArea) {
            HashSet<InheritedRelationship> relationships = ((SCTPArea)group).getRelationships();

            for (InheritedRelationship rel : relationships) {   // Otherwise derive the title from its relationships.
                String relStr = ((SCTPAreaTaxonomy)sctAbN).getLateralRelsInHierarchy().get(rel.getRelationshipTypeId());
                relStr += rel.getInheritanceType() == InheritanceType.INHERITED ? "*" : "+";

                builder.append("&nbsp&nbsp&nbsp ");
                builder.append(relStr);
                builder.append("<br>");
            }
        } else {
            HashMap<Long, String> entryPointNames = 
                    ((TribalAbstractionNetwork)sctAbN).getPatriarchNames();
            
            ArrayList<EntryPoint> entryPoints = 
                    ((ClusterSummary)group).getEntryPointSet().getSortedEntryPointSet(entryPointNames);

            for (EntryPoint ep : entryPoints) {   // Otherwise derive the title from its relationships.
                String relStr = entryPointNames.get(ep.getEntryPointConceptId());
                relStr += ep.getInheritanceType() == EntryPoint.InheritanceType.INHERITED ? "*" : "+";

                builder.append("&nbsp&nbsp&nbsp ");
                builder.append(relStr);
                builder.append("<br>");
            }
        }

        detailsPanel.setSummaryText(builder.toString());

        JTabbedPane tabbedPane = new JTabbedPane();
        
        String groupType;

        if(dialogType == DialogType.PartialArea) {
            tabbedPane.addTab("Partial-area Details", detailsPanel);
            groupType = "Partial-area";
        } else {
            tabbedPane.addTab("Cluster Details", detailsPanel);
            groupType = "Cluster";
        }
        
        SCTConceptHierarchyViewPanel viewPanel = new SCTConceptHierarchyViewPanel(group,
                sctAbN,
                groupType,
                new HierarchyPanelClickListener<Concept>() {
                    public void conceptDoubleClicked(Concept c) {
                        displayFrameListener.addNewBrowserFrame(c, sctAbN.getDataSource());
                    }
                });
        
        viewPanel.setGroup(group);
            
        tabbedPane.addTab("Hierarchical View (Graphical)", new JScrollPane(viewPanel));
        
        add(tabbedPane);

        setResizable(true);

        setSize(768, 600);
        setLocationRelativeTo(null);
        setVisible(true);
    }
    
    /* opens (open = true) or closes the filter panel */
    public void toggleFilterPanel() {
        if(!filterPanel.isVisible()) {
            setFilterPanelOpen(true, null);
        }
        else {
            setFilterPanelOpen(false, null);
        }
    }
    
    /*opens the filter panell and uses a KeyEvent if openned by typing */
    public void setFilterPanelOpen(boolean open, KeyEvent e) {
        if(open) {
            if(!filterPanel.isVisible()) {
                filterPanel.setVisible(true);
                if(e != null) {
                    filterField.setText("" + e.getKeyChar());
                }
                else {
                    filterField.setText("");
                }
                filterField.requestFocus();
            }
        }
        else {
            conceptModel.changeFilter("");
            filterPanel.setVisible(false);
            resultsList.grabFocus();
        }
    }
    
    private JPanel createFilterableConceptListPanel(ArrayList<Concept> conceptsInGroup) {
        JPanel conceptPanel = new JPanel(new BorderLayout());
        
        ArrayList<Filterable> filterableResults = new ArrayList<Filterable>();
        
        for (Concept c : conceptsInGroup) {
            filterableResults.add(new FilterableConceptEntry(c));
        }
        
        conceptModel.changeFilter("");
        filterPanel.setVisible(false);
        conceptModel.clear();
        conceptModel.addAll(filterableResults);     // used to populate FilterableListModel object instead of its constructor
        resultsList.setModel(conceptModel);
        
        JButton filterButton = new JButton();
        filterButton.setIcon(IconManager.getIconManager().getIcon("filter.png"));
        filterButton.setToolTipText("Filter these entries");
        filterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                toggleFilterPanel();
            }
        });
        
        JPanel northPanel = new JPanel();
        northPanel.setLayout(new GridBagLayout());
        northPanel.setOpaque(false);
        
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = GridBagConstraints.RELATIVE;
        c.weightx = 1;
        c.anchor = GridBagConstraints.FIRST_LINE_START;
        
        northPanel.add(Box.createHorizontalBox(), c);
        
        c.anchor = GridBagConstraints.FIRST_LINE_END;
        c.weightx = 0;
        
        northPanel.add(filterButton, c);

        conceptPanel.add(northPanel, BorderLayout.NORTH);
        
        closeButton.setIcon(IconManager.getIconManager().getIcon("cross.png"));
        closeButton.setToolTipText("Close");

        filterPanel.setLayout(new BoxLayout(filterPanel, BoxLayout.X_AXIS));
        filterPanel.add(closeButton);
        filterPanel.add(Box.createHorizontalStrut(10));
        filterPanel.add(new JLabel("Filter:  "));
        filterPanel.add(filterField);
        filterPanel.setVisible(false);

        conceptPanel.add(filterPanel, BorderLayout.SOUTH);
        
        filterField.addKeyListener(new KeyAdapter() {       // close filter field upon pressing escape
            @Override
            public void keyPressed(KeyEvent ke) {
                if(ke.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    setFilterPanelOpen(false, null);
                }
            }
        });

        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setFilterPanelOpen(false, null);
            }
        });
        
        filterField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                conceptModel.changeFilter(filterField.getText());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                conceptModel.changeFilter(filterField.getText());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                conceptModel.changeFilter(filterField.getText());
            }
        });
        
        resultsList.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if(e.getKeyChar() != KeyEvent.CHAR_UNDEFINED
                        && (e.getModifiersEx() & KeyEvent.CTRL_DOWN_MASK) != KeyEvent.CTRL_DOWN_MASK) {

                    if(!filterPanel.isVisible()) { // Panel is closed
                        setFilterPanelOpen(true, e);
                    }
                    else { // Panel is open, return focus to it
                        filterField.setText(filterField.getText() + e.getKeyChar());
                        filterField.requestFocus();
                    }
                }
            }
        });
        
        resultsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        conceptPanel.add(new JScrollPane(resultsList));
        
        return conceptPanel;
    }
}
