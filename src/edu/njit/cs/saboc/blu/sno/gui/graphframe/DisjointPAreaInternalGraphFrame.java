package edu.njit.cs.saboc.blu.sno.gui.graphframe;

import edu.njit.cs.saboc.blu.core.abn.disjoint.DisjointAbstractionNetwork;
import edu.njit.cs.saboc.blu.core.abn.disjoint.DisjointNode;
import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.DisjointPArea;
import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.PArea;
import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.PAreaTaxonomy;
import edu.njit.cs.saboc.blu.core.graph.AbstractionNetworkGraph;
import edu.njit.cs.saboc.blu.core.graph.disjointabn.DisjointBluGraph;
import edu.njit.cs.saboc.blu.core.gui.gep.DisjointAbNInitializer;
import edu.njit.cs.saboc.blu.core.gui.gep.utils.drawing.AggregateDisjointAbNPainter;
import edu.njit.cs.saboc.blu.core.gui.gep.utils.drawing.AggregateSinglyRootedNodeLabelCreator;
import edu.njit.cs.saboc.blu.core.gui.gep.utils.drawing.DisjointAbNPainter;
import edu.njit.cs.saboc.blu.core.gui.gep.utils.drawing.SinglyRootedNodeLabelCreator;
import edu.njit.cs.saboc.blu.core.gui.graphframe.GenericInternalGraphFrame;
import edu.njit.cs.saboc.blu.core.gui.graphframe.buttons.search.AbNSearchButton;
import edu.njit.cs.saboc.blu.sno.gui.abnselection.SCTDisplayFrameListener;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.disjointpareataxonomy.configuration.SCTDisjointPAreaTaxonomyConfiguration;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.disjointpareataxonomy.configuration.SCTDisjointPAreaTaxonomyConfigurationFactory;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.disjointpareataxonomy.configuration.SCTDisjointPAreaTaxonomyTextConfiguration;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy.configuration.SCTPAreaTaxonomyConfiguration;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy.configuration.SCTPAreaTaxonomyConfigurationFactory;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 *
 * @author Chris O
 */
public class DisjointPAreaInternalGraphFrame extends GenericInternalGraphFrame<DisjointAbstractionNetwork<DisjointPArea, PAreaTaxonomy<PArea>, PArea>> {

    private final SCTDisplayFrameListener displayListener;
    
    private final AbNSearchButton searchBtn;

    public DisjointPAreaInternalGraphFrame(
            JFrame parentFrame, 
            DisjointAbstractionNetwork<DisjointNode<PArea>, PAreaTaxonomy<PArea>, PArea> data, 
            SCTDisplayFrameListener displayListener) {
        
        super(parentFrame, "SNOMED CT Disjoint Partial-area Taxonomy");
        
        this.displayListener = displayListener;
        
        this.searchBtn = new AbNSearchButton(parentFrame, new SCTDisjointPAreaTaxonomyTextConfiguration(null));
        
        super.addToggleableButtonToMenu(searchBtn);

        displayDisjointPAreaTaxonomy(data);
    }

    public final void displayDisjointPAreaTaxonomy(
            DisjointAbstractionNetwork<DisjointNode<PArea>, PAreaTaxonomy<PArea>, PArea> disjointPAreaTaxonomy) {
        
        Thread loadThread = new Thread(() -> {
            getAbNExplorationPanel().showLoading();
            
            SinglyRootedNodeLabelCreator labelCreator;

            if (disjointPAreaTaxonomy.isAggregated()) {
                labelCreator = new AggregateSinglyRootedNodeLabelCreator();
            } else {
                labelCreator = new SinglyRootedNodeLabelCreator();
            }
            
            DisjointAbNPainter painter;

            if (disjointPAreaTaxonomy.isAggregated()) {
                painter = new AggregateDisjointAbNPainter();
            } else {
                painter = new DisjointAbNPainter();
            }
            
            AbstractionNetworkGraph newGraph = new DisjointBluGraph(getParentFrame(), disjointPAreaTaxonomy, labelCreator);

            SCTDisjointPAreaTaxonomyConfigurationFactory factory = new SCTDisjointPAreaTaxonomyConfigurationFactory();
            SCTDisjointPAreaTaxonomyConfiguration currentConfiguration = factory.createConfiguration(disjointPAreaTaxonomy, displayListener);
            
            SCTPAreaTaxonomyConfigurationFactory parentTaxonomyConfigFactory = new SCTPAreaTaxonomyConfigurationFactory();
            SCTPAreaTaxonomyConfiguration parentConfig = parentTaxonomyConfigFactory.createConfiguration(
                    disjointPAreaTaxonomy.getParentAbstractionNetwork(), displayListener);
            
            searchBtn.initialize(currentConfiguration);
            
            SwingUtilities.invokeLater(() -> {
                displayAbstractionNetwork(
                        newGraph,
                        painter,
                        currentConfiguration,
                        new DisjointAbNInitializer(
                                currentConfiguration,
                                parentConfig,
                                (bound) -> {
                                    DisjointAbstractionNetwork disjointAbN = getGraph().get().getAbstractionNetwork().getAggregated(bound);
                                    
                                    displayDisjointPAreaTaxonomy(disjointAbN);
                                },
                                (disjointAbN) -> {
                                    displayDisjointPAreaTaxonomy(disjointAbN);
                                }));
            });
        });
        
        loadThread.start();
    }
}
