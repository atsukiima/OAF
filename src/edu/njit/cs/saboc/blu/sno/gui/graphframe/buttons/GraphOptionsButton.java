package edu.njit.cs.saboc.blu.sno.gui.graphframe.buttons;

import SnomedShared.pareataxonomy.PAreaSummary;
import edu.njit.cs.saboc.blu.core.graph.edges.GraphEdge;
import edu.njit.cs.saboc.blu.core.gui.graphframe.buttons.PopupToggleButton;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.PAreaTaxonomy;
import edu.njit.cs.saboc.blu.sno.graph.options.GraphOptions;
import edu.njit.cs.saboc.blu.sno.gui.graphframe.PAreaInternalGraphFrame;
import edu.njit.cs.saboc.blu.sno.properties.AuditReportProperties;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author Chris
 */
public class GraphOptionsButton extends PopupToggleButton {

    public GraphOptionsButton(JFrame parent, final PAreaInternalGraphFrame igf, final PAreaTaxonomy data) {
        super(parent, "Options");
        
        JPanel popupPanel  = new JPanel();
        popupPanel.setLayout(new BoxLayout(popupPanel, BoxLayout.Y_AXIS));

        popupPanel.setBorder(BorderFactory.createEtchedBorder());

        JPanel regionOptionsPanel = new JPanel();
        regionOptionsPanel.setBorder(BorderFactory.createTitledBorder("Partition PAreas by Inheritance Type (Regions)"));

        ButtonGroup bg = new ButtonGroup();

        JRadioButton btnRegions = new JRadioButton("Regions");
        btnRegions.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                AuditReportProperties.getAuditReportProperties().setRegionsOn(true);
                
                ArrayList<GraphEdge> edges = graph.getEdges();

                igf.replaceInternalFrameDataWith(data, false, graph.showingConceptCountLabels(), null);

                for (GraphEdge edge : edges) {
                    graph.drawRoutedEdge(edge.getSourceID(), edge.getTargetID());
                }
            }
        });

        btnRegions.setToolTipText("Separate PAreas by how they obtained their relationship(s).");

        JRadioButton btnNoRegions = new JRadioButton("No Regions");
        btnNoRegions.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                AuditReportProperties.getAuditReportProperties().setRegionsOn(false);
                
                ArrayList<GraphEdge> edges = graph.getEdges();

                igf.replaceInternalFrameDataWith(data, true, graph.showingConceptCountLabels(), null);

                for (GraphEdge edge : edges) {
                    graph.drawRoutedEdge(edge.getSourceID(), edge.getTargetID());
                }
            }
        });
        
        btnNoRegions.setToolTipText("Group PAreas with same relationship(s) together regardless of inheritance types.");

        bg.add(btnRegions);
        bg.add(btnNoRegions);

        btnNoRegions.setSelected(true);

        regionOptionsPanel.add(btnRegions);
        regionOptionsPanel.add(btnNoRegions);

        popupPanel.add(regionOptionsPanel);

        JPanel edgeOptionsPanel = new JPanel();
        edgeOptionsPanel.setBorder(BorderFactory.createTitledBorder("Edge Options"));

        JButton drawEdges = new JButton("Draw All Edges");

        if (data.getPAreaCount() > 200) {
            drawEdges.setEnabled(false);
            drawEdges.setToolTipText("The current graph is too large. To draw all edges you need a graph with at most 200 PAreas.");
        } else {
            drawEdges.setToolTipText("NOTE: In graphs with many edges this may take some time.");
        }

        JButton clearEdges = new JButton("Remove All Edges");
        clearEdges.setToolTipText("Remove all edges from the graph.");

        drawEdges.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                
            }
        });

        clearEdges.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                graph.clearAllEdges();
            }
        });

        edgeOptionsPanel.add(drawEdges);
        edgeOptionsPanel.add(clearEdges);

        popupPanel.add(edgeOptionsPanel);

        JPanel pareaThresholdPanel = new JPanel();
        pareaThresholdPanel.setBorder(BorderFactory.createTitledBorder("Display PAreas With # of Concepts Between"));
        pareaThresholdPanel.add(new JLabel("Max: "));

        HashMap<Integer, PAreaSummary> pareas = data.getPAreas();

        int max = 0;

        for(PAreaSummary summary : pareas.values()) {
            if(summary.getConceptCount() > max) {
                max = summary.getConceptCount();
            }
        }

        final JTextField txtMax = new JTextField(6);
        pareaThresholdPanel.add(txtMax);
        txtMax.setHorizontalAlignment(JTextField.RIGHT);
        txtMax.setText(Integer.toString(max));

        pareaThresholdPanel.add(new JLabel("Min: "));

        final JTextField txtMin = new JTextField(6);
        pareaThresholdPanel.add(txtMin);
        txtMin.setText("1");
        txtMin.setHorizontalAlignment(JTextField.RIGHT);

        JButton btnOkay = new JButton("OK");
        btnOkay.setPreferredSize(new Dimension(64, 24));

        btnOkay.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                int maxThresh = 0;
                int minThresh = 0;

                if(!txtMax.getText().trim().isEmpty()) {
                    maxThresh = Integer.parseInt(txtMax.getText());
                }

                if(!txtMin.getText().trim().isEmpty()) {
                    minThresh = Integer.parseInt(txtMin.getText());
                }

                if (minThresh > maxThresh) {
                    JOptionPane.showMessageDialog(null,
                            "You have entered a minimum value that is greater than the entered maximum value.",
                            "ERROR: Minumum Greater than Maximum",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                GraphOptions options = new GraphOptions();

                options.pareaMaxThreshold = maxThresh;
                options.pareaMinThreshold = minThresh;

                igf.replaceInternalFrameDataWith(data, graph.getIsAreaGraph(), graph.showingConceptCountLabels(), options);
            }
        });

        pareaThresholdPanel.add(btnOkay);

        popupPanel.add(pareaThresholdPanel);

        JPanel regionLabelOptionsPanel = new JPanel();
        regionLabelOptionsPanel.setBorder(BorderFactory.createTitledBorder("Select Area/Region Label Type"));

        ButtonGroup regionBg = new ButtonGroup();

        JRadioButton btnPAreaCount = new JRadioButton("# PAreas");
        btnPAreaCount.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                ArrayList<GraphEdge> edges = graph.getEdges();

                igf.replaceInternalFrameDataWith(data, graph.getIsAreaGraph(), false, null);

                for (GraphEdge edge : edges) {
                    graph.drawRoutedEdge(edge.getSourceID(), edge.getTargetID());
                }
            }
        });

        btnPAreaCount.setToolTipText("Display the number of Partial Areas in the area/region.");

        JRadioButton btnConceptCount = new JRadioButton("# Unique Concepts");
        btnConceptCount.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                ArrayList<GraphEdge> edges = graph.getEdges();

                igf.replaceInternalFrameDataWith(data, graph.getIsAreaGraph(), true, null);

                for (GraphEdge edge : edges) {
                    graph.drawRoutedEdge(edge.getSourceID(), edge.getTargetID());
                }
            }
        });

        btnConceptCount.setToolTipText("Display the number of unique concepts in the area/region.");

        regionBg.add(btnPAreaCount);
        regionBg.add(btnConceptCount);

        btnPAreaCount.setSelected(true);

        regionLabelOptionsPanel.add(btnPAreaCount);
        regionLabelOptionsPanel.add(btnConceptCount);

        popupPanel.add(regionLabelOptionsPanel);
        
        
        /**
         * Sliders for Audit Recommendation Settings
         */
        JPanel auditSettingsPanel = new JPanel();
        auditSettingsPanel.setLayout(new BoxLayout(auditSettingsPanel, BoxLayout.Y_AXIS));
        auditSettingsPanel.setBorder(BorderFactory.createTitledBorder("Audit Suggestion Settings"));
        
        int minValue = 0;
        int maxValue = 10;
        int initValue = 6;
        
        // Slider to define a small Partial-area
        JPanel auditMiniPanel1 = new JPanel();
        JLabel settingLabel1 = new JLabel("Small Partial-area (<=)", JLabel.CENTER);
        JSlider smallPAreaSlider = new JSlider(JSlider.HORIZONTAL, minValue, maxValue, initValue);
        smallPAreaSlider.setMajorTickSpacing(5);
        smallPAreaSlider.setMinorTickSpacing(1);
        smallPAreaSlider.setPaintTicks(true);
        smallPAreaSlider.setPaintLabels(true);
        
        smallPAreaSlider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                JSlider source = (JSlider) e.getSource();
                if(!source.getValueIsAdjusting()) {
                    AuditReportProperties.getAuditReportProperties().setSmallPArea(source.getValue());
                }
            }
        });
        
        auditMiniPanel1.setLayout(new BoxLayout(auditMiniPanel1, BoxLayout.LINE_AXIS));
        auditMiniPanel1.add(Box.createHorizontalGlue());
        auditMiniPanel1.add(settingLabel1);
        auditMiniPanel1.add(Box.createRigidArea(new Dimension(33, 0)));
        auditMiniPanel1.add(smallPAreaSlider);
        auditSettingsPanel.add(auditMiniPanel1);
        
        // Slider to set the magnitude that determines what Partial-area has many relationships
        JPanel auditMiniPanel2 = new JPanel();
        initValue = 2;
        JLabel settingLabel2 = new JLabel("Many Relationships (>)", JLabel.CENTER);
        JSlider manyRelationshipsSlider = new JSlider(JSlider.HORIZONTAL, minValue, maxValue, initValue);
        manyRelationshipsSlider.setMajorTickSpacing(5);
        manyRelationshipsSlider.setMinorTickSpacing(1);
        manyRelationshipsSlider.setPaintTicks(true);
        manyRelationshipsSlider.setPaintLabels(true);
        
        manyRelationshipsSlider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                JSlider source = (JSlider) e.getSource();
                if(!source.getValueIsAdjusting()) {
                    AuditReportProperties.getAuditReportProperties().setManyRelationships(source.getValue());
                }
            }
        });
        
        auditMiniPanel2.setLayout(new BoxLayout(auditMiniPanel2, BoxLayout.LINE_AXIS));
        auditMiniPanel2.add(Box.createHorizontalGlue());
        auditMiniPanel2.add(settingLabel2);
        auditMiniPanel2.add(Box.createRigidArea(new Dimension(33, 0)));
        auditMiniPanel2.add(manyRelationshipsSlider);
        auditSettingsPanel.add(auditMiniPanel2);
        
        // Slider to set the magnitude of Partial-areas that determines what Area has few small Partial-areas
        JPanel auditMiniPanel3 = new JPanel();
        initValue = 3;
        JLabel settingLabel3 = new JLabel("Few Small Partial-areas (<=)", JLabel.CENTER);
        JSlider fewSmallPAreaSlider = new JSlider(JSlider.HORIZONTAL, minValue, maxValue, initValue);
        fewSmallPAreaSlider.setMajorTickSpacing(5);
        fewSmallPAreaSlider.setMinorTickSpacing(1);
        fewSmallPAreaSlider.setPaintTicks(true);
        fewSmallPAreaSlider.setPaintLabels(true);
        
        fewSmallPAreaSlider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                JSlider source = (JSlider) e.getSource();
                if(!source.getValueIsAdjusting()) {
                    AuditReportProperties.getAuditReportProperties().setFewSmallPAreas(source.getValue());
                }
            }
        });
        
        auditMiniPanel3.setLayout(new BoxLayout(auditMiniPanel3, BoxLayout.LINE_AXIS));
        auditMiniPanel3.add(Box.createHorizontalGlue());
        auditMiniPanel3.add(settingLabel3);
        auditMiniPanel3.add(fewSmallPAreaSlider);
        auditSettingsPanel.add(auditMiniPanel3);
        
        // Checkbox to determine if the user wants the Audit Recommendations to be displayed as "non-primitives first"
        JPanel auditMiniPanel4 = new JPanel();
        JLabel settingLabel4 = new JLabel("Sort with non-Primitives first", JLabel.CENTER);
        JCheckBox primitivesCheckbox = new JCheckBox();
        primitivesCheckbox.addItemListener(new ItemListener() {
            
            public void itemStateChanged(ItemEvent e) {
                if(e.getStateChange() == ItemEvent.SELECTED) {
                    AuditReportProperties.getAuditReportProperties().setPrimitivesLast(true);
                }
                else {
                    AuditReportProperties.getAuditReportProperties().setPrimitivesLast(false);
                }
            }
            
        });
        
        auditMiniPanel4.add(settingLabel4);
        auditMiniPanel4.add(primitivesCheckbox);
        auditSettingsPanel.add(auditMiniPanel4);
        
        // Checkbox to determine if the user wants the Audit Recommendations to be sorted by regions
        JPanel auditMiniPanel5 = new JPanel();
        JLabel settingLabel5 = new JLabel("Sort by Regions", JLabel.CENTER);
        JCheckBox regionsCheckbox = new JCheckBox();
        regionsCheckbox.addItemListener(new ItemListener() {
            
            public void itemStateChanged(ItemEvent e) {
                if(e.getStateChange() == ItemEvent.SELECTED) {
                    AuditReportProperties.getAuditReportProperties().setStrictRegionsProperty(true);
                }
                else {
                    AuditReportProperties.getAuditReportProperties().setStrictRegionsProperty(false);
                }
            }
        });
        
        auditMiniPanel5.add(settingLabel5);
        auditMiniPanel5.add(regionsCheckbox);
        auditSettingsPanel.add(auditMiniPanel5);
        
        popupPanel.add(auditSettingsPanel);
        
        this.setPopupContent(popupPanel);
    }
}
