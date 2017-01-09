package edu.njit.cs.saboc.blu.sno.gui.gep.panels.disjointtan.configuration;

import edu.njit.cs.saboc.blu.core.abn.disjoint.DisjointNode;
import edu.njit.cs.saboc.blu.core.abn.tan.Cluster;
import edu.njit.cs.saboc.blu.core.gui.dialogs.concepthierarchy.ConceptPainter;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.NodeOptionsPanel;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.disjointabn.tan.DisjointTANUIConfiguration;
import edu.njit.cs.saboc.blu.core.gui.graphframe.AbNDisplayManager;
import edu.njit.cs.saboc.blu.sno.gui.abnselection.SCTAbNFrameManager;
import edu.njit.cs.saboc.blu.sno.gui.dialogs.panels.concepthierarchy.SCTConceptPainter;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.disjointtan.SCTDisjointTANOptionsPanel;

/**
 *
 * @author Chris O
 */
public class SCTDisjointTANUIConfiguration extends DisjointTANUIConfiguration {
    
    private final SCTDisjointTANConfiguration config;
    
    public SCTDisjointTANUIConfiguration(
            SCTDisjointTANConfiguration config,
            AbNDisplayManager displayListener) {
        
        super(config, displayListener, new SCTDisjointTANListenerConfiguration(config));
        
        this.config = config;
    }
    
    @Override
    public NodeOptionsPanel<DisjointNode<Cluster>> getNodeOptionsPanel() {
        return new SCTDisjointTANOptionsPanel(config, config.getAbstractionNetwork().isAggregated());
    }

    @Override
    public ConceptPainter getConceptHierarchyPainter() {
        return new SCTConceptPainter();
    }
}
