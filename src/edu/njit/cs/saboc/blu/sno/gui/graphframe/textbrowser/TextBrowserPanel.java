package edu.njit.cs.saboc.blu.sno.gui.graphframe.textbrowser;

import SnomedShared.Concept;
import SnomedShared.pareataxonomy.InheritedRelationship;
import SnomedShared.pareataxonomy.InheritedRelationship.InheritanceType;
import SnomedShared.pareataxonomy.GroupParentInfo;
import edu.njit.cs.saboc.blu.sno.abn.SCTAbstractionNetwork;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTArea;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTPArea;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTPAreaTaxonomy;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTRegion;
import edu.njit.cs.saboc.blu.sno.graph.PAreaBluGraph;
import edu.njit.cs.saboc.blu.sno.gui.utils.models.AreaTableModel;
import edu.njit.cs.saboc.blu.sno.gui.utils.models.ChildPAreaTableModel;
import edu.njit.cs.saboc.blu.sno.gui.utils.models.HistoryTableModel;
import edu.njit.cs.saboc.blu.sno.gui.utils.models.PAreaTableModel;
import edu.njit.cs.saboc.blu.sno.gui.utils.models.ParentPAreaTableModel;
import edu.njit.cs.saboc.blu.sno.gui.utils.models.RelationshipRenderer;
import edu.njit.cs.saboc.blu.sno.utils.BrowserLauncher;
import edu.njit.cs.saboc.blu.sno.utils.UtilityMethods;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author Chris
 */
public class TextBrowserPanel extends JPanel {
    private PAreaBluGraph graph;

    private JTable areaTable;
    private JPanel regionPanel;

    private JTabbedPane tabbedPane;
    
    private JEditorPane selectedPAreaPane;
    private JEditorPane conceptPane;

    private ParentPAreaTableModel parentModel;
    private JTable selectedPAreaParentTable;

    private ChildPAreaTableModel childModel;
    private JTable selectedPAreaChildTable;

    private HistoryTableModel historyModel;
    private JTable historyTable;

    private ArrayList<JTable> pareaTables = new ArrayList<JTable>();

    private SCTPArea currentPArea;

    public TextBrowserPanel(final PAreaBluGraph graph) {
        this.graph = graph;

        this.setLayout(new BorderLayout());
        
        final RelationshipRenderer relRenderer = new RelationshipRenderer();

        areaTable = new JTable(new AreaTableModel(graph.getPAreaTaxonomy())) {

            public TableCellRenderer getCellRenderer(int row, int column) {
                if (column == 0) {
                    return relRenderer;
                }

                return super.getCellRenderer(row, column);
            }
        };

        ListSelectionModel listSelectionModel = areaTable.getSelectionModel();

        listSelectionModel.addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent e) {
                ListSelectionModel lsm = (ListSelectionModel) e.getSource();

                if (lsm.isSelectionEmpty()) {
                    return;
                }

                if (areaTable.getRowSelectionAllowed()) {
                    final int selectedIndex = areaTable.getSelectedRow();

                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            SCTArea a = graph.getPAreaTaxonomy().getExplicitHierarchyAreas().get(selectedIndex);

                            regionPanel.removeAll();

                            regionPanel.add(new JScrollPane(getPAreaLists(a)), BorderLayout.CENTER);

                            regionPanel.validate();
                            regionPanel.repaint();
                        }
                    });
                }
            }
        });

        areaTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        areaTable.getColumnModel().getColumn(0).setPreferredWidth(256);

        JPanel selectionPanel = new JPanel(new GridLayout(2, 0));

        JPanel areaSelectPanel = new JPanel(new BorderLayout());
        areaSelectPanel.setBorder(BorderFactory.createTitledBorder("Areas in Hierarchy"));
        areaSelectPanel.add(new JScrollPane(areaTable), BorderLayout.CENTER);

        selectionPanel.add(areaSelectPanel);

        regionPanel = new JPanel(new BorderLayout());
        regionPanel.setBorder(BorderFactory.createTitledBorder("Regions/PAreas in Selected Area"));

        selectionPanel.add(regionPanel);

        selectionPanel.validate();

        JPanel hierarchyPanel = new JPanel(new GridLayout(3, 0));

        JPanel focusPAreaPanel = new JPanel(new BorderLayout());
        focusPAreaPanel.setBorder(BorderFactory.createTitledBorder("Selected Partial-area Information"));

        tabbedPane = new JTabbedPane();

        selectedPAreaPane = new JEditorPane();
        selectedPAreaPane.setContentType("text/html");
        selectedPAreaPane.setEditable(false);
        selectedPAreaPane.addHyperlinkListener(new BrowserLauncher(graph.getDisplayFrameListener(),
                (SCTAbstractionNetwork)graph.getAbstractionNetwork()));

        tabbedPane.addTab("Partial-area Information", new JScrollPane(selectedPAreaPane));

        conceptPane = new JEditorPane();
        conceptPane.setContentType("text/html");
        conceptPane.setEditable(false);
        conceptPane.addHyperlinkListener(new BrowserLauncher(graph.getDisplayFrameListener(),
                (SCTAbstractionNetwork)graph.getAbstractionNetwork()));

        tabbedPane.addTab("Concepts In Partial-area", new JScrollPane(conceptPane));

        JPanel centerPanel = new JPanel(new BorderLayout());

        focusPAreaPanel.add(tabbedPane);

        historyTable = new JTable(historyModel = new HistoryTableModel(graph.getPAreaTaxonomy()));

        historyTable.addMouseListener(new MouseAdapter() {

            public void mouseClicked(MouseEvent e) {

                if(e.getClickCount() != 2) {
                    return;
                }

                if (historyTable.getRowSelectionAllowed()) {
                    final int selectedIndex = historyTable.getSelectedRow();

                    if(selectedIndex < 0) {
                        return;
                    }

                    navigateTo(historyModel.getHistoryEntries().get(selectedIndex));
                }
            }
        });

        historyTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        historyTable.getColumnModel().getColumn(0).setPreferredWidth(128);
        historyTable.getColumnModel().getColumn(1).setPreferredWidth(16);
        historyTable.getColumnModel().getColumn(2).setPreferredWidth(16);
        historyTable.getColumnModel().getColumn(3).setPreferredWidth(16);

        JPanel historyPanel = new JPanel(new BorderLayout());
        historyPanel.setBorder(BorderFactory.createTitledBorder("History"));

        historyPanel.add(new JScrollPane(historyTable), BorderLayout.CENTER);

        centerPanel.add(focusPAreaPanel, BorderLayout.CENTER);
        centerPanel.add(historyPanel, BorderLayout.EAST);

        JPanel parentPanel = new JPanel(new BorderLayout());
        parentPanel.setBorder(BorderFactory.createTitledBorder("Selected Partial-area Parents"));

        parentModel = new ParentPAreaTableModel(graph.getPAreaTaxonomy(), new ArrayList<GroupParentInfo>());

        selectedPAreaParentTable = new JTable(parentModel) {
            public TableCellRenderer getCellRenderer(int row, int column) {
                if (column == 2) {
                    return relRenderer;
                }

                return super.getCellRenderer(row, column);
            }
        };

        selectedPAreaParentTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent me) {
                if(me.getClickCount() == 2) {
                    if(selectedPAreaParentTable.getSelectedRow() >= 0) {
                        GroupParentInfo parent = parentModel.getParents().get(selectedPAreaParentTable.getSelectedRow());
                        SCTPArea parea = graph.getPAreaTaxonomy().getPAreaFromRootConceptId(parent.getParentPAreaRootId());
                        navigateTo(parea);
                    }
                }
            }
        });

        selectedPAreaParentTable.getColumnModel().getColumn(0).setPreferredWidth(150);
        selectedPAreaParentTable.getColumnModel().getColumn(1).setPreferredWidth(150);
        selectedPAreaParentTable.getColumnModel().getColumn(2).setPreferredWidth(256);
        
        parentPanel.add(new JScrollPane(selectedPAreaParentTable), BorderLayout.CENTER);

        JPanel childPanel = new JPanel(new BorderLayout());
        childPanel.setBorder(BorderFactory.createTitledBorder("Selected Partial-area Children"));

        childModel = new ChildPAreaTableModel(graph.getPAreaTaxonomy(), new ArrayList<SCTPArea>());

        selectedPAreaChildTable = new JTable(childModel) {

            public TableCellRenderer getCellRenderer(int row, int column) {
                if (column == 1) {
                    return relRenderer;
                }

                return super.getCellRenderer(row, column);
            }
        };

        selectedPAreaChildTable.addMouseListener(new MouseAdapter() {

            public void mouseClicked(MouseEvent me) {
                if (me.getClickCount() == 2) {
                    if (selectedPAreaChildTable.getSelectedRow() >= 0) {
                        SCTPArea parea = childModel.getPAreas().get(selectedPAreaChildTable.getSelectedRow());
                        navigateTo(parea);
                    }
                }
            }
        });

        selectedPAreaChildTable.getColumnModel().getColumn(0).setPreferredWidth(256);
        selectedPAreaChildTable.getColumnModel().getColumn(1).setPreferredWidth(256);

        childPanel.add(new JScrollPane(selectedPAreaChildTable), BorderLayout.CENTER);

        hierarchyPanel.add(parentPanel);
        hierarchyPanel.add(centerPanel);
        hierarchyPanel.add(childPanel);

        this.add(selectionPanel, BorderLayout.WEST);
        this.add(hierarchyPanel, BorderLayout.CENTER);
    }

    private JPanel getPAreaLists(SCTArea a) {
        JPanel pareaPanel = new JPanel();
        pareaPanel.setLayout(new BoxLayout(pareaPanel, BoxLayout.Y_AXIS));

        pareaTables.clear();

        for (SCTRegion r : a.getRegions()) {

            final SCTRegion region = r;
            
            ArrayList<InheritedRelationship> rels = new ArrayList<InheritedRelationship>(r.getRelationships());

            pareaPanel.add(new JLabel(UtilityMethods.getRegionNameNewlineHTML(rels,
                    graph.getPAreaTaxonomy().getLateralRelsInHierarchy())));

            pareaPanel.add(Box.createVerticalStrut(4));

            final JTable pareaTable = new JTable(new PAreaTableModel(graph.getPAreaTaxonomy(), r.getPAreasInRegion()));

            pareaTable.addMouseListener(new MouseAdapter () {

                public void mouseClicked(MouseEvent e) {

                    if(e.getClickCount() != 1) {
                        return;
                    }

                    if (pareaTable.getRowSelectionAllowed()) {
                        int selectedIndex = pareaTable.getSelectedRow();

                        final SCTPArea parea = region.getPAreasInRegion().get(selectedIndex);

                        for(JTable table : pareaTables) {
                            if(table != pareaTable) {
                                table.clearSelection();
                            }
                        }

                        navigateTo(parea);
                    }
                }
            });

            pareaTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            pareaTable.getColumnModel().getColumn(0).setPreferredWidth(200);
            pareaTable.getColumnModel().getColumn(1).setPreferredWidth(16);
            pareaTable.getColumnModel().getColumn(2).setPreferredWidth(16);
            pareaTable.getColumnModel().getColumn(3).setPreferredWidth(16);

            pareaPanel.add(pareaTable.getTableHeader());
            pareaPanel.add(pareaTable);
            pareaTables.add(pareaTable);
        }

        return pareaPanel;
    }

    public void navigateTo(SCTPArea parea) {
        this.setSelectedPAreaInformation(parea);
        this.setPAreaParentsTable(parea);
        this.setPAreaChildrenTable(parea);
        this.setConceptsInPArea(parea);

        tabbedPane.setSelectedIndex(0);

        historyModel.addEntry(parea);
    }

    private void setSelectedPAreaInformation(SCTPArea parea) {
        StringBuilder builder = new StringBuilder();

        Concept root = parea.getRoot();

        builder.append("<html>");
        builder.append("<font size=4 face=\"Arial\">");
        builder.append(String.format("<b>Partial-area Name:</b> <i>%s</i><br>", root.getName()));
        builder.append(String.format("<b>Root Concept ID:</b> <i>"
                + "<a href=\"%d\">%d</a></i><br>",
                root.getId(), root.getId()));
        builder.append(String.format("<b>Total Concepts in Partial-area:</b> <i>%s</i><br>", parea.getConceptCount()));
        builder.append(String.format("<b>Total Parent PAreas:</b> <i>%s</i><br>", parea.getParentIds().size()));

        HashSet<SCTPArea> childrenPAreaIds = graph.getPAreaTaxonomy().getChildGroups(parea);

        builder.append(String.format("<b>Total Child PAreas:</b> <i>%s</i><br>", (childrenPAreaIds == null ? 0 : childrenPAreaIds.size())));
        builder.append("<b>Partial-area Relationships: </b><br>");

        for (InheritedRelationship rel : parea.getRelationships()) {   // Otherwise derive the title from its relationships.
            String relStr = graph.getPAreaTaxonomy().getLateralRelsInHierarchy().get(rel.getRelationshipTypeId());
            relStr += rel.getInheritanceType() == InheritanceType.INHERITED ? "*" : "+";

            builder.append("&nbsp&nbsp&nbsp ");
            builder.append(relStr);
            builder.append("<br>");
        }

        selectedPAreaPane.setText(builder.toString());

        selectedPAreaPane.setSelectionStart(0);
        selectedPAreaPane.setSelectionEnd(0);
    }

    private void setPAreaParentsTable(final SCTPArea parea) {

        parentModel.setData(graph.getPAreaTaxonomy(), new ArrayList<GroupParentInfo>());

        new Thread(new Runnable() {

            public void run() {
                final SCTPAreaTaxonomy taxonomy = graph.getPAreaTaxonomy();

                final ArrayList<GroupParentInfo> parents = 
                        taxonomy.getDataSource().getPAreaParentInfo(taxonomy, parea);

                Collections.sort(parents, new Comparator<GroupParentInfo>() {
                    public int compare(GroupParentInfo a, GroupParentInfo b) {
                        return a.getParentConcept().getName().compareToIgnoreCase(b.getParentConcept().getName());
                    }
                });

                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        parentModel.setData(taxonomy, parents);
                    }
                });
            }
        }).start();
    }

    private void setPAreaChildrenTable(SCTPArea parea) {
        ArrayList<SCTPArea> childPAreas = new ArrayList<>(graph.getPAreaTaxonomy().getChildGroups(parea));

        Collections.sort(childPAreas, new Comparator<SCTPArea>() {
            public int compare(SCTPArea a, SCTPArea b) {
                return a.getRoot().getName().compareToIgnoreCase(b.getRoot().getName());
            }
        });
        
        childModel.setData(graph.getPAreaTaxonomy(), childPAreas);
    }

    private void setConceptsInPArea(final SCTPArea parea) {
        final SCTPAreaTaxonomy taxonomy = graph.getPAreaTaxonomy();
        
        conceptPane.setText("");

        new Thread(new Runnable() {

            public void run() {
                ArrayList<Concept> conceptsInPArea = taxonomy.getDataSource().getConceptsInPArea(taxonomy, parea);

                StringBuilder builder = new StringBuilder();

                builder.append("<html>");
                builder.append("<font size=4 face=\"Arial\">");

                if (!conceptsInPArea.isEmpty()) {
                    for (Concept c : conceptsInPArea) {
                        builder.append(c.getName());
                        builder.append("&nbsp ");
                        builder.append(String.format("<b>(<a href=\"%d\">%d</a>)</b>", c.getId(), c.getId()));
                        builder.append("<br>");
                    }

                    conceptPane.setText(builder.toString());
                } else {
                    conceptPane.setText("");
                }

                conceptPane.setSelectionStart(0);
                conceptPane.setSelectionEnd(0);
            }
        }).start();
    }
}
