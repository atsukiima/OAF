package edu.njit.cs.saboc.blu.sno.gui.graphframe;

import edu.njit.cs.saboc.blu.core.abn.disjoint.DisjointAbstractionNetwork;
import edu.njit.cs.saboc.blu.core.abn.node.Node;
import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.PArea;
import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.PAreaTaxonomy;
import edu.njit.cs.saboc.blu.core.graph.BluGraph;
import edu.njit.cs.saboc.blu.core.graph.disjointabn.DisjointBluGraph;
import edu.njit.cs.saboc.blu.core.gui.gep.utils.drawing.DisjointAbNPainter;
import edu.njit.cs.saboc.blu.core.gui.gep.utils.drawing.SinglyRootedNodeLabelCreator;
import edu.njit.cs.saboc.blu.core.gui.graphframe.GenericInternalGraphFrame;
import edu.njit.cs.saboc.blu.sno.gui.abnselection.SCTDisplayFrameListener;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.disjointpareataxonomy.configuration.SCTDisjointPAreaTaxonomyConfigurationFactory;
import edu.njit.cs.saboc.blu.sno.gui.graphframe.buttons.search.DisjointPAreaInternalSearchButton;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 *
 * @author Chris O
 */
public class DisjointPAreaInternalGraphFrame extends GenericInternalGraphFrame {

    private final SCTDisplayFrameListener displayListener;
    
    private final DisjointPAreaInternalSearchButton searchBtn;

    public DisjointPAreaInternalGraphFrame(
            JFrame parentFrame, 
            DisjointAbstractionNetwork<PAreaTaxonomy, PArea> data, 
            SCTDisplayFrameListener displayListener) {
        
        super(parentFrame, "SNOMED CT Disjoint Partial-area Taxonomy");
        
        this.displayListener = displayListener;
        
        this.searchBtn = new DisjointPAreaInternalSearchButton(parentFrame, this);
        
        super.addToggleableButtonToMenu(searchBtn);

        replaceInternalFrameDataWith(data);
    }

    public final void replaceInternalFrameDataWith(
            DisjointAbstractionNetwork<PAreaTaxonomy, PArea> data) {
        
        Thread loadThread = new Thread(() -> {
            gep.showLoading();
            
            SinglyRootedNodeLabelCreator labelCreator = new SinglyRootedNodeLabelCreator() {
                public String getRootNameStr(Node node) {
                    return node.getName();
                }
            };

            BluGraph graph = new DisjointBluGraph(parentFrame, data, labelCreator);

            searchBtn.setGraph(graph);

            SCTDisjointPAreaTaxonomyConfigurationFactory factory = new SCTDisjointPAreaTaxonomyConfigurationFactory();
            
            SwingUtilities.invokeLater(() -> {
                displayAbstractionNetwork(graph, new DisjointAbNPainter(), factory.createConfiguration(data, displayListener));

                tabbedPane.validate();
                tabbedPane.repaint();
            });
        });
        
        loadThread.start();
    }
}
