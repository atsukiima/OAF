package edu.njit.cs.saboc.blu.sno.conceptbrowser.gui.panels;

import SnomedShared.Concept;
import edu.njit.cs.saboc.blu.core.gui.iconmanager.IconManager;
import edu.njit.cs.saboc.blu.core.utils.filterable.list.Filterable;
import edu.njit.cs.saboc.blu.sno.conceptbrowser.FocusConcept;
import edu.njit.cs.saboc.blu.sno.conceptbrowser.SnomedConceptBrowser;
import edu.njit.cs.saboc.blu.sno.conceptbrowser.utils.ButtonTabbedPaneUI;
import edu.njit.cs.saboc.blu.sno.conceptbrowser.utils.filterablelist.SCTNavigableFilterableList;
import edu.njit.cs.saboc.blu.sno.sctdatasource.SCTDataSource;
import edu.njit.cs.saboc.blu.sno.utils.filterable.entry.FilterableConceptEntry;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.JTabbedPane;


/**
 * A class that displays the parents or children of the Focus Concept.  The
 * top middle and bottom middle NAT panels are instances of this class.
 */
public class ParentChildPanel extends BaseNavPanel implements ActionListener {
    
    private SCTNavigableFilterableList inferredList;
    private SCTNavigableFilterableList statedList;

    private BaseNavPanel inferredPanel;
    private BaseNavPanel statedPanel;
    
    private JTabbedPane tabbedPane;
    
    private final int INFERRED_IDX = 0;
    private final int STATED_IDX = 1;
    
    public enum PanelType {
        PARENT, CHILD
    }
    
    private PanelType panelType;
    
    private String inferredTabName;
    private String statedTabName;
    
    private FocusConcept.Fields inferredField;
    private FocusConcept.Fields statedField;
    
    public ParentChildPanel(final SnomedConceptBrowser mainPanel, PanelType panelType, SCTDataSource dataSource) {
        super(mainPanel, dataSource);
        
        this.setLayout(new BorderLayout());
        
        this.setBackground(mainPanel.getNeighborhoodBGColor());
        
        this.inferredList = new SCTNavigableFilterableList(mainPanel.getFocusConcept(), mainPanel.getOptions());
        this.statedList = new SCTNavigableFilterableList(mainPanel.getFocusConcept(), mainPanel.getOptions());

        this.panelType = panelType;
        
        if(this.panelType == PanelType.PARENT) {
            inferredField = FocusConcept.Fields.PARENTS;
            statedField = FocusConcept.Fields.STATEDPARENTS;
            inferredTabName = "INFERRED PARENTS";
            statedTabName = "STATED PARENTS";
        }
        else {
            inferredField = FocusConcept.Fields.CHILDREN;
            statedField = FocusConcept.Fields.STATEDCHILDREN;
            inferredTabName = "INFERRED CHILDREN";
            statedTabName = "STATED CHILDREN";
        }
        
        this.inferredPanel = new BaseNavPanel(mainPanel, dataSource) {
            public void dataReady() {
                ArrayList<Concept> concepts = (ArrayList<Concept>) focusConcept.getConceptList(inferredField);
                
                ArrayList<Filterable> conceptEntries = new ArrayList<Filterable>();

                for (Concept c : concepts) {
                    conceptEntries.add(new FilterableConceptEntry(c));
                }

                int count = concepts.size();

                tabbedPane.setTitleAt(INFERRED_IDX, String.format("%s (%d)", inferredTabName, count));

                inferredList.setContents(conceptEntries);
            }

            public void dataPending() {
                tabbedPane.setTitleAt(INFERRED_IDX, inferredTabName);
                inferredList.showPleaseWait();
            }

            public void dataEmpty() {
                tabbedPane.setTitleAt(INFERRED_IDX, inferredTabName);
                inferredList.showDataEmpty();
            }
        };
        
        inferredPanel.setLayout(new BorderLayout());
        inferredPanel.add(inferredList, BorderLayout.CENTER);
        
        this.statedPanel = new BaseNavPanel(mainPanel, dataSource) {
            public void dataReady() {
                ArrayList<Concept> concepts = (ArrayList<Concept>) focusConcept.getConceptList(statedField);
                
                ArrayList<Filterable> conceptEntries = new ArrayList<Filterable>();

                for (Concept c : concepts) {
                    conceptEntries.add(new FilterableConceptEntry(c));
                }

                int count = concepts.size();

                tabbedPane.setTitleAt(STATED_IDX, String.format("%s (%d)", statedTabName, count));

                statedList.setContents(conceptEntries);
            }

            public void dataPending() {
                tabbedPane.setTitleAt(STATED_IDX, statedTabName);
                statedList.showPleaseWait();
            }

            public void dataEmpty() {
                tabbedPane.setTitleAt(STATED_IDX, statedTabName);
                statedList.showDataEmpty();
            }
        };
        
        statedPanel.setLayout(new BorderLayout());
        statedPanel.add(statedList, BorderLayout.CENTER);
       
        this.tabbedPane = new JTabbedPane();
               
        final ButtonTabbedPaneUI tabbedUI = new ButtonTabbedPaneUI() {

            protected ButtonTabbedPaneUI.TabButton createFilterTabButton(int tabIndex) {
                ButtonTabbedPaneUI.TabButton button = new ButtonTabbedPaneUI.TabButton(tabIndex);
                button.setIcon(IconManager.getIconManager().getIcon("filter.png"));
                button.setPreferredSize(new Dimension(16, 16));
                button.setPadding(new Insets(0, 2, 3, 0));
                button.setBorder(null);
                button.setContentAreaFilled(false);
                button.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        ((ButtonTabbedPaneUI.TabButton)e.getSource()).setContentAreaFilled(true);
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        ((ButtonTabbedPaneUI.TabButton)e.getSource()).setContentAreaFilled(false);
                    }
                });

                button.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        switch(tabbedPane.getSelectedIndex()) {
                            case INFERRED_IDX:
                                inferredList.toggleFilterPanel();
                                return;
                            case STATED_IDX:
                                statedList.toggleFilterPanel();
                                return;
                        }
                    }
                });

                return button;
            }
        };
        
        tabbedPane.setUI(tabbedUI);
        
        tabbedPane.addTab(inferredTabName, inferredPanel);
        focusConcept.addDisplayPanel(inferredField, inferredPanel);
        
        if(dataSource.supportsStatedRelationships()) {
            tabbedPane.addTab(statedTabName, statedPanel);
            focusConcept.addDisplayPanel(statedField, statedPanel);
        }
  
        add(tabbedPane, BorderLayout.CENTER);
    }

    public void actionPerformed(ActionEvent e) {

    }
}
