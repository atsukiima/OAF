package edu.njit.cs.saboc.blu.sno.conceptbrowser.gui.panels;

import SnomedShared.Concept;
import SnomedShared.OutgoingLateralRelationship;
import SnomedShared.PAreaDetailsForConcept;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.PAreaTaxonomy;
import edu.njit.cs.saboc.blu.sno.conceptbrowser.FocusConcept;
import edu.njit.cs.saboc.blu.sno.conceptbrowser.SnomedConceptBrowser;
import edu.njit.cs.saboc.blu.sno.graph.PAreaBluGraph;
import edu.njit.cs.saboc.blu.sno.gui.dialogs.ConceptGroupDetailsDialog;
import edu.njit.cs.saboc.blu.sno.gui.graphframe.PAreaInternalGraphFrame;
import edu.njit.cs.saboc.blu.sno.localdatasource.load.PAreaTaxonomyGenerator;
import edu.njit.cs.saboc.blu.sno.sctdatasource.SCTDataSource;
import edu.njit.cs.saboc.blu.sno.sctdatasource.SCTLocalDataSource;
import edu.njit.cs.saboc.blu.sno.sctdatasource.middlewareproxy.MiddlewareAccessorProxy;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

/**
 *
 * @author Chris
 */
public class AbstractionNetworkPanel extends BaseNavPanel {

    private JTabbedPane tabbedPane;
    private BaseNavPanel partialAreaPanel;
    private BaseNavPanel tribalANPanel;
    private ANDetailsPanel pareaDetailsPanel = new ANDetailsPanel();

    public AbstractionNetworkPanel(final SnomedConceptBrowser mainPanel, final SCTDataSource dataSource) {
        super(mainPanel, dataSource);

        setBackground(mainPanel.getNeighborhoodBGColor());
        setLayout(new BorderLayout());

        partialAreaPanel = new BaseNavPanel(mainPanel, dataSource) {
            public void dataPending() {
                pareaDetailsPanel.displayLabel("Please wait...");
            }

            public void dataEmpty() {
                pareaDetailsPanel.displayLabel("");
            }

            public void dataReady() {
                ArrayList<PAreaDetailsForConcept> details =
                        (ArrayList<PAreaDetailsForConcept>) focusConcept.getConceptList(FocusConcept.Fields.PARTIALAREA);

                pareaDetailsPanel.displayDetails(details);
            }
        };

        partialAreaPanel.setLayout(new BorderLayout());
        partialAreaPanel.add(new JScrollPane(pareaDetailsPanel), BorderLayout.CENTER);

        tribalANPanel = new BaseNavPanel(mainPanel, dataSource) {
            public void dataPending() {
            }

            public void dataEmpty() {
            }

            public void dataReady() {
            }
        };

        tribalANPanel.setLayout(new BorderLayout());

        tabbedPane = new JTabbedPane();
        
        if(dataSource instanceof SCTLocalDataSource) {
            JPanel subtaxonomyOptionsPanel = new JPanel(new GridLayout(2, 1, 2, 2));
            
            JButton descTaxonomyBtn = new JButton("Create Descendent Taxonomy");
            
            descTaxonomyBtn.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    Concept c = focusConcept.getConcept();

                    PAreaTaxonomyGenerator generator = new PAreaTaxonomyGenerator();

                    PAreaTaxonomy taxonomy = generator.createPAreaTaxonomy(c, (SCTLocalDataSource) dataSource);

                    mainPanel.getDisplayFrameListener().addNewPAreaGraphFrame(taxonomy, true, false);
                }
            });

            JPanel descBtnPanel = new JPanel();
            descBtnPanel.add(descTaxonomyBtn);
            
            subtaxonomyOptionsPanel.add(descBtnPanel);
            
            JButton focusTaxonomyBtn = new JButton("Create Focus Taxonomy");
            
            focusTaxonomyBtn.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    Concept c = focusConcept.getConcept();
                    
                    PAreaTaxonomyGenerator generator = new PAreaTaxonomyGenerator();
                    PAreaTaxonomy taxonomy = generator.createFocusTaxonomy(c, (SCTLocalDataSource)dataSource);
                    
                    mainPanel.getDisplayFrameListener().addNewPAreaGraphFrame(taxonomy, true, false);
                }
            });
            
            JPanel focusBtnPanel = new JPanel();
            focusBtnPanel.add(focusTaxonomyBtn);
            
            subtaxonomyOptionsPanel.add(focusBtnPanel);
            
            
            tabbedPane.addTab("Focus Concept Taxonomies", subtaxonomyOptionsPanel);
        }
        
        tabbedPane.addTab("Partial-Area Details", partialAreaPanel);

        focusConcept.addDisplayPanel(FocusConcept.Fields.TRIBALAN, tribalANPanel);
        focusConcept.addDisplayPanel(FocusConcept.Fields.PARTIALAREA, partialAreaPanel);


        add(tabbedPane, BorderLayout.CENTER);
    }

    private class ANDetailsPanel extends JPanel {

        private JLabel detailsLabel = new JLabel();
        private JPanel individualDetailsListPanel = new JPanel();

        public ANDetailsPanel() {
            this.setBackground(Color.WHITE);

            this.setLayout(new BorderLayout());

            this.add(detailsLabel, BorderLayout.NORTH);

            individualDetailsListPanel.setLayout(new BoxLayout(individualDetailsListPanel, BoxLayout.Y_AXIS));
            individualDetailsListPanel.setOpaque(false);

            individualDetailsListPanel.setVisible(false);
            this.add(individualDetailsListPanel, BorderLayout.CENTER);
        }

        public void displayLabel(String text) {
            individualDetailsListPanel.setVisible(false);
            individualDetailsListPanel.removeAll();

            detailsLabel.setVisible(true);
            detailsLabel.setText(text);
        }

        public void displayDetails(ArrayList<PAreaDetailsForConcept> details) {
            if (details.isEmpty()) {
                displayLabel("");
                return;
            }

            detailsLabel.setVisible(false);

            
            // TODO: There are 11 concepts that overlap between two hierarchies.
            PAreaDetailsForConcept firstDetail = details.get(0);

            String labelText = "<html>";
            labelText += "<b>Hierarchy:</b> " + firstDetail.getHierarchyRoot().getName() + "<br>";

            labelText += "<b>Area:</b><br>";

            if (firstDetail.getPAreaRelationships().isEmpty()) {
                labelText += "&nbsp;&nbsp; None; root.<br>";
            } else {
                for (OutgoingLateralRelationship rel : firstDetail.getPAreaRelationships()) {
                    String relName = rel.getRelationship().getName();

                    relName = relName.substring(0, relName.lastIndexOf("("));

                    labelText += "&nbsp;&nbsp; " + relName + "<br>";
                }
            }

            labelText += "Partial-areas:";
            
            JPanel labelPanel = new JPanel(new BorderLayout());
            labelPanel.setOpaque(true);
            labelPanel.setBackground(Color.WHITE);
            
            labelPanel.add(new JLabel(labelText));

            individualDetailsListPanel.add(labelPanel);

            for (PAreaDetailsForConcept detail : details) {
                individualDetailsListPanel.add(new PAreaDetailsEntry(detail));
            }

            individualDetailsListPanel.setVisible(true);

            this.repaint();
        }

        private class PAreaDetailsEntry extends JPanel {

            private PAreaDetailsForConcept details;

            public PAreaDetailsEntry(PAreaDetailsForConcept details) {
                this.details = details;
               
                this.setBackground(new Color(250, 250, 250));

                this.setOpaque(false);
                this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
                this.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.GRAY));

                String labelText = "<html>&nbsp;" + details.getPAreaRoot().getName() + "<br>&nbsp;" + details.getConceptCount() + " concepts   ";

                final JButton btnSummary = new JButton("Details");
                final JButton btnView = new JButton("View");
                
                btnSummary.setMaximumSize(new Dimension(40, 30));
                
                btnSummary.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent ae) {
                        btnSummary.setText("Loading...");
                        
                        SwingUtilities.invokeLater(new Runnable() {
                            public void run() {
                                displayGroupSummary();
                                btnSummary.setText("Details");
                            }
                        });
                    }
                });
                
                btnView.setMaximumSize(new Dimension(40, 30));
                
                btnView.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent ae) {
                        btnView.setText("Loading...");
                        
                        SwingUtilities.invokeLater(new Runnable() {
                            public void run() {
                                displayAN();
                                btnView.setText("View");
                            }
                        });
                    }
                });


                this.add(new JLabel(labelText));
                this.add(btnSummary);
                this.add(Box.createHorizontalStrut(2));
                this.add(btnView);
            }
            
            private void displayGroupSummary() {
                String version = 
                        MiddlewareAccessorProxy.getProxy().getSnomedVersionAtIndex(focusConcept.getAssociatedBrowser().getLogoPanel().getSelectedVersion());

                PAreaTaxonomy data = 
                        MiddlewareAccessorProxy.getProxy().getPAreaHierarchyData(version, details.getHierarchyRoot());
                
                ConceptGroupDetailsDialog dialog = new ConceptGroupDetailsDialog(
                        data.getPAreaFromRootConceptId(details.getPAreaRoot().getId()), 
                        data,
                        ConceptGroupDetailsDialog.DialogType.PartialArea,
                        mainPanel.getDisplayFrameListener());
            }

            private void displayAN() {
                String version = MiddlewareAccessorProxy.getProxy().getSnomedVersionAtIndex(focusConcept.getAssociatedBrowser().getLogoPanel().getSelectedVersion());

                PAreaTaxonomy data = MiddlewareAccessorProxy.getProxy().getPAreaHierarchyData(version, details.getHierarchyRoot());
                
                PAreaInternalGraphFrame igf = mainPanel.getDisplayFrameListener().addNewPAreaGraphFrame(data, true, false);

                PAreaBluGraph graph = (PAreaBluGraph) igf.getGraph();

                igf.focusOnComponent(graph.getGroupEntries().get(graph.getPAreaTaxonomy().getPAreaFromRootConceptId(
                        details.getHierarchyRoot().getId()).getId()));

            }
        }
    }
}
