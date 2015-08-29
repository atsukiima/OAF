package edu.njit.cs.saboc.blu.sno.gui.graphframe;

import edu.njit.cs.saboc.blu.core.graph.BluGraph;
import edu.njit.cs.saboc.blu.core.gui.gep.utils.drawing.DisjointAbNPainter;
import edu.njit.cs.saboc.blu.core.gui.gep.utils.drawing.GroupEntryLabelCreator;
import edu.njit.cs.saboc.blu.core.gui.graphframe.GenericInternalGraphFrame;
import edu.njit.cs.saboc.blu.sno.abn.disjointpareataxonomy.DisjointPAreaTaxonomy;
import edu.njit.cs.saboc.blu.sno.abn.disjointpareataxonomy.DisjointPartialArea;
import edu.njit.cs.saboc.blu.sno.graph.DisjointPAreaBluGraph;
import edu.njit.cs.saboc.blu.sno.graph.PAreaBluGraph;
import edu.njit.cs.saboc.blu.sno.gui.abnselection.SCTDisplayFrameListener;
import edu.njit.cs.saboc.blu.sno.gui.gep.listeners.SCTDisjointPAreaTaxonomyGEPConfiguration;
import edu.njit.cs.saboc.blu.sno.utils.UtilityMethods;
import javax.swing.JFrame;

/**
 *
 * @author Chris O
 */
public class DisjointPAreaInternalGraphFrame extends GenericInternalGraphFrame {

    private SCTDisplayFrameListener displayListener;

    public DisjointPAreaInternalGraphFrame(final JFrame parentFrame, final DisjointPAreaTaxonomy data, SCTDisplayFrameListener displayListener) {
        
        super(parentFrame, "SNOMED CT Disjoint Partial-area Taxonomy");
        
        this.displayListener = displayListener;

        String frameTitle = UtilityMethods.getPrintableVersionName(data.getParentAbstractionNetwork().getSCTVersion()) + " | Hierarchy: " + data.getParentAbstractionNetwork().getSCTRootConcept().getName();

        this.setTitle(frameTitle);

        replaceInternalFrameDataWith(data);
    }

    public PAreaBluGraph getGraph() {
        return (PAreaBluGraph)super.getGraph();
    }

    public void replaceInternalFrameDataWith(final DisjointPAreaTaxonomy data) {
        
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
        
        initializeGraphTabs(graph, new DisjointAbNPainter(), new SCTDisjointPAreaTaxonomyGEPConfiguration(parentFrame, data, displayListener));
        
        tabbedPane.validate();
        tabbedPane.repaint();
    }
}
