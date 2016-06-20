package edu.njit.cs.saboc.blu.sno.gui.graphframe.buttons;

import edu.njit.cs.saboc.blu.core.graph.edges.GraphEdge;
import edu.njit.cs.saboc.blu.core.gui.graphframe.buttons.PopupToggleButton;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTPArea;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTPAreaTaxonomy;
import edu.njit.cs.saboc.blu.sno.gui.graphframe.PAreaInternalGraphFrame;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

/**
 *
 * @author Chris
 */
public class GraphOptionsButton extends PopupToggleButton {

    public GraphOptionsButton(JFrame parent, final PAreaInternalGraphFrame igf, final SCTPAreaTaxonomy data) {
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
                ArrayList<GraphEdge> edges = graph.getEdges();

                igf.replaceInternalFrameDataWith(data, false);

                for (GraphEdge edge : edges) {
                    graph.drawRoutedEdge(edge.getSource(), edge.getTarget());
                }
            }
        });

        btnRegions.setToolTipText("Separate PAreas by how they obtained their relationship(s).");

        JRadioButton btnNoRegions = new JRadioButton("No Regions");
        btnNoRegions.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                ArrayList<GraphEdge> edges = graph.getEdges();

                igf.replaceInternalFrameDataWith(data, true);

                for (GraphEdge edge : edges) {
                    graph.drawRoutedEdge(edge.getSource(), edge.getTarget());
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
        pareaThresholdPanel.setBorder(BorderFactory.createTitledBorder("Create Aggregate Partial-area Taxonomy with Bound"));

        pareaThresholdPanel.add(new JLabel("Min: "));

        final JTextField txtMin = new JTextField(6);
        pareaThresholdPanel.add(txtMin);
        txtMin.setText("1");
        txtMin.setHorizontalAlignment(JTextField.RIGHT);

        JButton btnOkay = new JButton("OK");
        btnOkay.setPreferredSize(new Dimension(64, 24));

        btnOkay.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                int minThresh = 0;

                if(!txtMin.getText().trim().isEmpty()) {
                    minThresh = Integer.parseInt(txtMin.getText());
                }
                
                if(minThresh < 1) {
                    txtMin.setText("1");
                    return;
                }
                
                SCTPAreaTaxonomy aggregateTaxonomy = data.getReduced(minThresh);

                igf.replaceInternalFrameDataWith(aggregateTaxonomy, graph.getIsAreaGraph());
            }
        });

        pareaThresholdPanel.add(btnOkay);

        popupPanel.add(pareaThresholdPanel);


        this.setPopupContent(popupPanel);
    }
}
