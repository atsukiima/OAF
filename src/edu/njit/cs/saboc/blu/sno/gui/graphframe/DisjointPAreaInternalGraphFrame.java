package edu.njit.cs.saboc.blu.sno.gui.graphframe;

import edu.njit.cs.saboc.blu.core.graph.BluGraph;
import edu.njit.cs.saboc.blu.core.gui.gep.utils.drawing.DisjointAbNPainter;
import edu.njit.cs.saboc.blu.core.gui.gep.utils.drawing.GroupEntryLabelCreator;
import edu.njit.cs.saboc.blu.core.gui.graphframe.GenericInternalGraphFrame;
import edu.njit.cs.saboc.blu.sno.abn.disjointpareataxonomy.DisjointPAreaTaxonomy;
import edu.njit.cs.saboc.blu.sno.abn.disjointpareataxonomy.DisjointPartialArea;
import edu.njit.cs.saboc.blu.sno.graph.DisjointPAreaBluGraph;
import edu.njit.cs.saboc.blu.sno.gui.abnselection.SCTDisplayFrameListener;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.disjointpareataxonomy.configuration.SCTDisjointPAreaTaxonomyConfigurationFactory;
import edu.njit.cs.saboc.blu.sno.gui.graphframe.buttons.search.DisjointPAreaInternalSearchButton;
import edu.njit.cs.saboc.blu.sno.utils.UtilityMethods;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 *
 * @author Chris O
 */
public class DisjointPAreaInternalGraphFrame extends GenericInternalGraphFrame {

    private final SCTDisplayFrameListener displayListener;
    
    private final DisjointPAreaInternalSearchButton searchBtn;

    public DisjointPAreaInternalGraphFrame(final JFrame parentFrame, final DisjointPAreaTaxonomy data, SCTDisplayFrameListener displayListener) {
        
        super(parentFrame, "SNOMED CT Disjoint Partial-area Taxonomy");
        
        this.displayListener = displayListener;
        
        this.searchBtn = new DisjointPAreaInternalSearchButton(parentFrame, this);
        
        super.addToggleableButtonToMenu(searchBtn);

        String frameTitle = UtilityMethods.getPrintableVersionName(data.getParentAbstractionNetwork().getSCTVersion()) + " | Hierarchy: " + data.getParentAbstractionNetwork().getSCTRootConcept().getName();

        this.setTitle(frameTitle);

        replaceInternalFrameDataWith(data);
    }

    public DisjointPAreaBluGraph getGraph() {
        return (DisjointPAreaBluGraph)super.getGraph();
    }

    public void replaceInternalFrameDataWith(final DisjointPAreaTaxonomy data) {
        
        Thread loadThread = new Thread(() -> {
            gep.showLoading();
            
            GroupEntryLabelCreator labelCreator = new GroupEntryLabelCreator<DisjointPartialArea>() {
                public String getRootNameStr(DisjointPartialArea parea) {
                    int lastIndex = parea.getRoot().getName().lastIndexOf(" (");

                    if (lastIndex == -1) {
                        return parea.getRoot().getName();
                    } else {
                        return parea.getRoot().getName().substring(0, lastIndex);
                    }
                }
            };

            BluGraph graph = new DisjointPAreaBluGraph(parentFrame, data, displayListener, labelCreator);

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