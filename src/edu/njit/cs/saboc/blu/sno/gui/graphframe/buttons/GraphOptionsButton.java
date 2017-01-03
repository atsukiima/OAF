package edu.njit.cs.saboc.blu.sno.gui.graphframe.buttons;

import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.PAreaTaxonomy;
import edu.njit.cs.saboc.blu.core.graph.AbstractionNetworkGraph;
import edu.njit.cs.saboc.blu.core.graph.edges.GraphEdge;
import edu.njit.cs.saboc.blu.core.gui.graphframe.buttons.PopupToggleButton;
import edu.njit.cs.saboc.blu.sno.gui.graphframe.PAreaInternalGraphFrame;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

/**
 *
 * @author Chris
 */
public class GraphOptionsButton extends PopupToggleButton {
    
    public GraphOptionsButton(JFrame parent, 
            PAreaInternalGraphFrame igf, 
            PAreaTaxonomy taxonomy) {
        
        super(parent, "Options");
        
        JPanel popupPanel  = new JPanel();
        popupPanel.setLayout(new BoxLayout(popupPanel, BoxLayout.Y_AXIS));

        popupPanel.setBorder(BorderFactory.createEtchedBorder());

        JPanel regionOptionsPanel = new JPanel();
        regionOptionsPanel.setBorder(BorderFactory.createTitledBorder("Partition PAreas by Inheritance Type (Regions)"));

        ButtonGroup bg = new ButtonGroup();

        JRadioButton btnRegions = new JRadioButton("Regions");
        btnRegions.addActionListener((ae) -> {

            if (igf.getGraph().isPresent()) {
                AbstractionNetworkGraph<PAreaTaxonomy> graph = igf.getGraph().get();

                ArrayList<GraphEdge> edges = graph.getEdges();

                igf.displayPAreaTaxonomy(taxonomy);

                edges.forEach((edge) -> {
                    graph.drawRoutedEdge(edge.getSource(), edge.getTarget());
                });
            }
        });

        btnRegions.setToolTipText("Separate PAreas by how they obtained their relationship(s).");

        JRadioButton btnNoRegions = new JRadioButton("No Regions");
        btnNoRegions.addActionListener((ae) -> {

            if (igf.getGraph().isPresent()) {
                AbstractionNetworkGraph<PAreaTaxonomy> graph = igf.getGraph().get();

                ArrayList<GraphEdge> edges = graph.getEdges();

                igf.displayPAreaTaxonomy(taxonomy);

                edges.forEach((edge) -> {
                    graph.drawRoutedEdge(edge.getSource(), edge.getTarget());
                });
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

        if (taxonomy.getNodeCount() > 200) {
            drawEdges.setEnabled(false);
            drawEdges.setToolTipText("The current graph is too large. To draw all edges you need a graph with at most 200 PAreas.");
        } else {
            drawEdges.setToolTipText("NOTE: In graphs with many edges this may take some time.");
        }

        JButton clearEdges = new JButton("Remove All Edges");
        clearEdges.setToolTipText("Remove all edges from the graph.");

        drawEdges.addActionListener((e) -> {
        });

        clearEdges.addActionListener((e) -> {
            if(igf.getGraph().isPresent()) {
                igf.getGraph().get().clearAllEdges();
            }
        });

        edgeOptionsPanel.add(drawEdges);
        edgeOptionsPanel.add(clearEdges);

        popupPanel.add(edgeOptionsPanel);

        this.setPopupContent(popupPanel);
    }
}
