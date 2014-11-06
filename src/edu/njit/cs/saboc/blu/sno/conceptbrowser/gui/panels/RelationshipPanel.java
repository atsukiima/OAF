package edu.njit.cs.saboc.blu.sno.conceptbrowser.gui.panels;

import SnomedShared.Concept;
import SnomedShared.OutgoingLateralRelationship;
import edu.njit.cs.saboc.blu.core.gui.iconmanager.IconManager;
import edu.njit.cs.saboc.blu.core.utils.filterable.list.Filterable;
import edu.njit.cs.saboc.blu.sno.conceptbrowser.FocusConcept;
import edu.njit.cs.saboc.blu.sno.conceptbrowser.SnomedConceptBrowser;
import edu.njit.cs.saboc.blu.sno.conceptbrowser.utils.ButtonTabbedPaneUI;
import edu.njit.cs.saboc.blu.sno.sctdatasource.SCTDataSource;
import edu.njit.cs.saboc.blu.sno.utils.filterable.entry.FilterableConceptEntry;
import edu.njit.cs.saboc.blu.sno.utils.filterable.entry.FilterableLateralRelationshipEntry;
import edu.njit.cs.saboc.blu.sno.utils.filterable.list.SCTFilterableList;
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
 * A class that displays lateral relationships of the Focus Concept.
 * Siblings have their own tab on this panel.  Concept relationships can be
 * further deconstructed into Term Relationships, which also have their own
 * tab.
 */
public class RelationshipPanel extends BaseNavPanel {
    private SCTFilterableList conRelList;
    private SCTFilterableList siblingList;
    private SCTFilterableList statedConRelList;

    private JTabbedPane tabbedPane;
    private BaseNavPanel conRelPanel;
    private BaseNavPanel siblingPanel;
    private BaseNavPanel statedConRelPanel;

    private final int CON_REL_IDX = 0;
    private final int STATED_CON_REL_IDX = 1;
    private final int SIBLING_IDX = 2;

    public RelationshipPanel(final SnomedConceptBrowser mainPanel, SCTDataSource dataSource) {
        super(mainPanel, dataSource);

        conRelList = new SCTFilterableList(mainPanel.getFocusConcept(), mainPanel.getOptions(), true, true);
        siblingList = new SCTFilterableList(mainPanel.getFocusConcept(), mainPanel.getOptions(), true, true);
        statedConRelList = new SCTFilterableList(mainPanel.getFocusConcept(), mainPanel.getOptions(), true, true);

        setBackground(mainPanel.getNeighborhoodBGColor());
        setLayout(new BorderLayout());

        // Concept Relationships Panel
        conRelPanel = new BaseNavPanel(mainPanel, dataSource) {
            @Override
            public void dataPending() {
                tabbedPane.setTitleAt(CON_REL_IDX, "ATTRIBUTE RELATIONSHIPS");
                conRelList.showPleaseWait();
            }

            public void dataEmpty() {
                tabbedPane.setTitleAt(CON_REL_IDX, "ATTRIBUTE RELATIONSHIPS");
                conRelList.showDataEmpty();
            }

            @Override
            public void dataReady() {
                ArrayList<Filterable> entries = new ArrayList<Filterable>();

                ArrayList<OutgoingLateralRelationship> relationships =
                        (ArrayList<OutgoingLateralRelationship>)focusConcept.getConceptList(
                        FocusConcept.Fields.CONCEPTREL);

                for(OutgoingLateralRelationship olr : relationships) {
                    entries.add(new FilterableLateralRelationshipEntry(olr));
                }

                conRelList.setContents(entries);
                int relCount = relationships.size();
                tabbedPane.setTitleAt(CON_REL_IDX, "ATTRIBUTE RELATIONSHIPS (" + relCount + ")");
            }
        };
        
        conRelPanel.setLayout(new BorderLayout());
        conRelPanel.add(conRelList, BorderLayout.CENTER);
        
        statedConRelPanel = new BaseNavPanel(mainPanel, dataSource) {
            @Override
            public void dataPending() {
                tabbedPane.setTitleAt(STATED_CON_REL_IDX, "STATED ATTRIBUTE RELATIONSHIPS");
                statedConRelList.showPleaseWait();
            }

            public void dataEmpty() {
                tabbedPane.setTitleAt(STATED_CON_REL_IDX, "STATED ATTRIBUTE RELATIONSHIPS");
                statedConRelList.showDataEmpty();
            }

            @Override
            public void dataReady() {
                ArrayList<Filterable> entries = new ArrayList<Filterable>();

                ArrayList<OutgoingLateralRelationship> relationships =
                        (ArrayList<OutgoingLateralRelationship>)focusConcept.getConceptList(
                        FocusConcept.Fields.STATEDCONCEPTRELS);

                for(OutgoingLateralRelationship olr : relationships) {
                    entries.add(new FilterableLateralRelationshipEntry(olr));
                }

                statedConRelList.setContents(entries);
                
                int relCount = relationships.size();
                tabbedPane.setTitleAt(STATED_CON_REL_IDX, "STATED ATTRIBUTE RELATIONSHIPS (" + relCount + ")");
            }
        };
        
        statedConRelPanel.setLayout(new BorderLayout());
        statedConRelPanel.add(statedConRelList, BorderLayout.CENTER);

        // Siblings Panel
        siblingPanel = new BaseNavPanel(mainPanel, dataSource) {
            @Override
            public void dataPending() {
                tabbedPane.setTitleAt(SIBLING_IDX, "SIBLINGS");
                siblingList.showPleaseWait();
            }

            public void dataEmpty() {
                tabbedPane.setTitleAt(SIBLING_IDX, "SIBLINGS");
                siblingList.showDataEmpty();
            }

            @Override
            public void dataReady() {
                FocusConcept.Fields field;

                field = FocusConcept.Fields.SIBLINGS;
                
                int count = ((ArrayList<String>)focusConcept.getConceptList(field)).size();
                tabbedPane.setTitleAt(SIBLING_IDX, "SIBLINGS (" + count + ")");

                ArrayList<Concept> siblings = (ArrayList<Concept>)focusConcept.getConceptList(field);
                ArrayList<Filterable> conceptEntries = new ArrayList<Filterable>();

                for(Concept c : siblings) {
                    conceptEntries.add(new FilterableConceptEntry(c));
                }
                
                siblingList.setContents(conceptEntries);
            }
        };
        siblingPanel.setLayout(new BorderLayout());
        siblingPanel.add(siblingList, BorderLayout.CENTER);

        // Tabbed Pane
        tabbedPane = new JTabbedPane();
        
        final ButtonTabbedPaneUI tabbedUI = new ButtonTabbedPaneUI() {

            protected TabButton createFilterTabButton(int tabIndex) {
                TabButton button = new TabButton(tabIndex);
                button.setIcon(IconManager.getIconManager().getIcon("filter.png"));
                button.setPreferredSize(new Dimension(16, 16));
                button.setPadding(new Insets(0, 2, 3, 0));
                button.setBorder(null);
                button.setContentAreaFilled(false);
                button.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        ((TabButton)e.getSource()).setContentAreaFilled(true);
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        ((TabButton)e.getSource()).setContentAreaFilled(false);
                    }
                });

                button.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        switch(tabbedPane.getSelectedIndex()) {
                            case CON_REL_IDX:
                                conRelList.toggleFilterPanel();
                                return;
                            case SIBLING_IDX:
                                siblingList.toggleFilterPanel();
                                return;
                            case STATED_CON_REL_IDX:
                                statedConRelList.toggleFilterPanel();
                                return;
                        }
                    }
                });

                return button;
            }
        };
        
        tabbedPane.setUI(tabbedUI);

        tabbedPane.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }
        });

        tabbedPane.addTab("ATTRIBUTE RELATIONSHIPS", conRelPanel);
        
        if(dataSource.supportsStatedRelationships()) {
            tabbedPane.addTab("STATED ATTRIBUTE RELATIONSHIPS", statedConRelPanel);
            focusConcept.addDisplayPanel(FocusConcept.Fields.STATEDCONCEPTRELS, statedConRelPanel);
        }
        
        tabbedPane.addTab("SIBLINGS", siblingPanel);
        
        add(tabbedPane, BorderLayout.CENTER);

        focusConcept.addDisplayPanel(FocusConcept.Fields.CONCEPTREL, conRelPanel);
        focusConcept.addDisplayPanel(FocusConcept.Fields.SIBLINGS, siblingPanel);
        focusConcept.addDisplayPanel(FocusConcept.Fields.STATEDCONCEPTRELS, statedConRelPanel);
    }
}
