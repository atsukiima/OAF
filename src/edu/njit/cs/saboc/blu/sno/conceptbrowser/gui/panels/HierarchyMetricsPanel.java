package edu.njit.cs.saboc.blu.sno.conceptbrowser.gui.panels;

import SnomedShared.Concept;
import edu.njit.cs.saboc.blu.core.utils.filterable.entry.FilterableStringEntry;
import edu.njit.cs.saboc.blu.core.utils.filterable.list.Filterable;
import edu.njit.cs.saboc.blu.sno.conceptbrowser.FocusConcept;
import edu.njit.cs.saboc.blu.sno.conceptbrowser.SnomedConceptBrowser;
import edu.njit.cs.saboc.blu.sno.localdatasource.concept.LocalSCTConceptStated;
import edu.njit.cs.saboc.blu.sno.localdatasource.conceptdata.HierarchyMetrics;
import edu.njit.cs.saboc.blu.sno.sctdatasource.SCTDataSource;
import edu.njit.cs.saboc.blu.sno.utils.filterable.entry.FilterableConceptEntry;
import edu.njit.cs.saboc.blu.sno.utils.filterable.entry.FilterableStatedAncestorEntry;
import edu.njit.cs.saboc.blu.sno.utils.filterable.list.SCTFilterableList;
import java.awt.BorderLayout;
import java.util.ArrayList;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;


/**
 *
 * @author Chris
 */
public class HierarchyMetricsPanel extends BaseNavPanel {

    private class HierarchyMetricsTableModel extends AbstractTableModel {

        private String[] columnNames = new String[]{"Metric", "#"};
        private Object[][] data;

        public HierarchyMetricsTableModel() {
            clearData();
        }
        
        public final void clearData() {
            this.data = new Object[1][2];
            
            this.data[0][0] = "";
            this.data[0][1] = "";
            
            this.fireTableDataChanged();
        }
        
        public final void setData(HierarchyMetrics metrics) {
            this.data = new Object[6][2];
            
            String hierarchies = metrics.getHierarchies().isEmpty() ? "[None, SNOMED CT Root]" : metrics.getHierarchies().get(0).getName();
            
            for(int c = 1; c < metrics.getHierarchies().size(); c++) {
                hierarchies += (", " + metrics.getHierarchies().get(c));
            }

            this.data[0][0] = "Hierarchy";
            this.data[0][1] = hierarchies;
            
            
            this.data[1][0] = "Ancestors";
            this.data[1][1] = metrics.getAncestorCount();
            
            this.data[2][0] = "Descendants";
            this.data[2][1] = "[Disabled]";
            
            this.data[3][0] = "Parents";
            this.data[3][1] = metrics.getParentCount();
            
            this.data[4][0] = "Children";
            this.data[4][1] = metrics.getChildCount();
            
            this.data[5][0] = "Siblings";
            this.data[5][1] = metrics.getSiblingCount();
            
            this.fireTableDataChanged();
        }

        public int getColumnCount() {
            return columnNames.length;
        }

        public int getRowCount() {
            return data.length;
        }

        public String getColumnName(int col) {
            return columnNames[col];
        }

        public Object getValueAt(int row, int col) {
            return data[row][col];
        }

        public Class getColumnClass(int c) {
            return getValueAt(0, c).getClass();
        }
    }

    private final int METRICS_IDX = 0;
    private final int ANCESTOR_IDX = 1;
    private final int STATED_ANCESTOR_IDX = 2;
    private final int DESCENDANT_IDX = 3;
    private final int PATH_IDX = 4;

    private HierarchyMetricsTableModel metricsModel;

    private JTabbedPane tabbedPane;

    private BaseNavPanel metricsPanel;
    private BaseNavPanel ancestorsPanel;
    private BaseNavPanel statedAncestorsPanel;
    private BaseNavPanel descendantsPanel;
    private BaseNavPanel allPathsPanel;
    

    public HierarchyMetricsPanel(final SnomedConceptBrowser mainPanel, SCTDataSource dataSource) {
        super(mainPanel, dataSource);

        this.setLayout(new BorderLayout());
        this.setBackground(mainPanel.getNeighborhoodBGColor());

        tabbedPane = new JTabbedPane();

        tabbedPane.addTab("Hierarchy Metrics", createMetricsPanel());
        tabbedPane.addTab("All Ancestors", createAncestorsPanel());
        
        if(dataSource.supportsStatedRelationships()) {
            tabbedPane.addTab("Add Stated Ancestors", createStatedAncestorsPanel());
        }
        
        //tabbedPane.addTab("All Descendants", createDescendantsPanel());
        //tabbedPane.addTab("All Paths", createAllPathsPanel());

        this.add(tabbedPane, BorderLayout.CENTER);
    }

    private BaseNavPanel createMetricsPanel() {
        JTable table = new JTable(metricsModel = new HierarchyMetricsTableModel());

        metricsPanel = new BaseNavPanel(mainPanel, dataSource) {

            public void dataPending() {
                metricsModel.clearData();
            }

            public void dataEmpty() {
                metricsModel.clearData();
            }

            public void dataReady() {
                HierarchyMetrics metrics = (HierarchyMetrics)focusConcept.getConceptList(FocusConcept.Fields.HIERARCHYMETRICS);
                metricsModel.setData(metrics);
            }
        };

        metricsPanel.setLayout(new BorderLayout());
        metricsPanel.add(table, BorderLayout.CENTER);

        focusConcept.addDisplayPanel(FocusConcept.Fields.HIERARCHYMETRICS, metricsPanel);

        return metricsPanel;
    }

    private BaseNavPanel createAncestorsPanel() {

        final SCTFilterableList ancestorsList = new SCTFilterableList(mainPanel.getFocusConcept(), mainPanel.getOptions(), true, true);

        ancestorsPanel = new BaseNavPanel(mainPanel, dataSource) {
            public void dataPending() {
                tabbedPane.setTitleAt(ANCESTOR_IDX, "All Ancestors");
                ancestorsList.showPleaseWait();
            }

            public void dataEmpty() {
                tabbedPane.setTitleAt(ANCESTOR_IDX, "All Ancestors");
                ancestorsList.showDataEmpty();
            }

            public void dataReady() {
                ArrayList<Concept> allAncestors = (ArrayList<Concept>)focusConcept.getConceptList(FocusConcept.Fields.ALLANCESTORS);

                ArrayList<Filterable> conceptEntries = new ArrayList<Filterable>();

                for(Concept c : allAncestors) {
                    conceptEntries.add(new FilterableConceptEntry(c));
                }
                
                ancestorsList.setContents(conceptEntries);
                
                tabbedPane.setTitleAt(ANCESTOR_IDX, String.format("All Ancestors (%d)", allAncestors.size()));
            }
        };

        ancestorsPanel.setLayout(new BorderLayout());
        ancestorsPanel.add(ancestorsList);

        focusConcept.addDisplayPanel(FocusConcept.Fields.ALLANCESTORS, ancestorsPanel);

        return ancestorsPanel;
    }
    
    private BaseNavPanel createStatedAncestorsPanel() {
        final SCTFilterableList statedAncestorsList = new SCTFilterableList(mainPanel.getFocusConcept(), mainPanel.getOptions(), true, true);

        statedAncestorsPanel = new BaseNavPanel(mainPanel, dataSource) {
            
            public void dataPending() {
                tabbedPane.setTitleAt(STATED_ANCESTOR_IDX, "All Stated Ancestors");
                statedAncestorsList.showPleaseWait();
            }

            public void dataEmpty() {
                tabbedPane.setTitleAt(STATED_ANCESTOR_IDX, "All Stated Ancestors");
                statedAncestorsList.showDataEmpty();
            }

            public void dataReady() {
                ArrayList<Concept> allStatedAncestors = (ArrayList<Concept>) focusConcept.getConceptList(FocusConcept.Fields.STATEDANCESTORS);

                ArrayList<Filterable> conceptEntries = new ArrayList<Filterable>();

                for (Concept c : allStatedAncestors) {
                    LocalSCTConceptStated statedConcept = (LocalSCTConceptStated)c;
                    
                    conceptEntries.add(new FilterableStatedAncestorEntry(statedConcept));
                }

                statedAncestorsList.setContents(conceptEntries);

                tabbedPane.setTitleAt(STATED_ANCESTOR_IDX, String.format("All Stated Ancestors (%d)", allStatedAncestors.size()));
            }
        };

        statedAncestorsPanel.setLayout(new BorderLayout());
        statedAncestorsPanel.add(statedAncestorsList);

        focusConcept.addDisplayPanel(FocusConcept.Fields.STATEDANCESTORS, statedAncestorsPanel);

        return statedAncestorsPanel;
    }
    
    private BaseNavPanel createDescendantsPanel() {

        final SCTFilterableList descendantList = new SCTFilterableList(mainPanel.getFocusConcept(), mainPanel.getOptions(), true, true);

        descendantsPanel = new BaseNavPanel(mainPanel, dataSource) {
            public void dataPending() {
                tabbedPane.setTitleAt(DESCENDANT_IDX, "All Descendants");
                descendantList.showPleaseWait();
            }

            public void dataEmpty() {
                tabbedPane.setTitleAt(DESCENDANT_IDX, "All Descendants");
                descendantList.showDataEmpty();
            }

            public void dataReady() {
                ArrayList<Concept> allDescendants = (ArrayList<Concept>)focusConcept.getConceptList(FocusConcept.Fields.ALLDESCENDANTS);

                ArrayList<Filterable> conceptEntries = new ArrayList<Filterable>();

                for(Concept c : allDescendants) {
                    conceptEntries.add(new FilterableConceptEntry(c));
                }
                
                descendantList.setContents(conceptEntries);
                
                tabbedPane.setTitleAt(DESCENDANT_IDX, String.format("All Descendants (%d)", allDescendants.size()));
            }
        };

        descendantsPanel.setLayout(new BorderLayout());
        descendantsPanel.add(descendantList);

        focusConcept.addDisplayPanel(FocusConcept.Fields.ALLDESCENDANTS, descendantsPanel);

        return descendantsPanel;
    }

    private BaseNavPanel createAllPathsPanel() {
        final SCTFilterableList pathList = new SCTFilterableList(mainPanel.getFocusConcept(), mainPanel.getOptions(), true, true);
        

        allPathsPanel = new BaseNavPanel(mainPanel, dataSource) {
            public void dataPending() {
                tabbedPane.setTitleAt(PATH_IDX, "All Paths");
                pathList.showPleaseWait();
            }

            public void dataEmpty() {
                tabbedPane.setTitleAt(PATH_IDX, "All Paths");
                pathList.showDataEmpty();
            }

            public void dataReady() {
                ArrayList<ArrayList<Concept>> allPaths = (ArrayList<ArrayList<Concept>>)focusConcept.getConceptList(FocusConcept.Fields.ALLPATHS);
                
                ArrayList<Filterable> pathEntries = new ArrayList<Filterable>();
                
                for(ArrayList<Concept> path : allPaths) {
  
                    String pathStr = path.get(0).getName();
                    
                    if(path.size() > 1) {
                        pathStr += " ... ";
                    }
                    
                    if(path.size() > 2) {
                        pathStr += path.get(path.size() - 2).getName() + ", ";
                    }
                    
                    pathStr += path.get(path.size() - 1).getName();
                    
                    pathEntries.add(new FilterableStringEntry(pathStr));
                }
                
                pathList.setContents(pathEntries);
                
                tabbedPane.setTitleAt(PATH_IDX, String.format("All Paths (%d)", allPaths.size()));
            }
        };

        allPathsPanel.setLayout(new BorderLayout());
        allPathsPanel.add(pathList);

        focusConcept.addDisplayPanel(FocusConcept.Fields.ALLPATHS, allPathsPanel);

        return allPathsPanel;
    }
}
