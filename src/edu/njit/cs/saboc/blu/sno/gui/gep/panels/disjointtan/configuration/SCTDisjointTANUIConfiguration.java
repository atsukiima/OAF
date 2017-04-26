package edu.njit.cs.saboc.blu.sno.gui.gep.panels.disjointtan.configuration;

import edu.njit.cs.saboc.blu.core.abn.disjoint.DisjointNode;
import edu.njit.cs.saboc.blu.core.abn.tan.Cluster;
import edu.njit.cs.saboc.blu.core.gui.dialogs.concepthierarchy.ConceptPainter;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.AbNOptionsPanel;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.NodeOptionsPanel;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.disjointabn.tan.DisjointTANUIConfiguration;
import edu.njit.cs.saboc.blu.core.gui.graphframe.AbNDisplayManager;
import edu.njit.cs.saboc.blu.sno.gui.abnselection.SCTAbNFrameManager;
import edu.njit.cs.saboc.blu.sno.gui.dialogs.panels.concepthierarchy.SCTConceptPainter;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.disjointtan.SCTDisjointClusterOptionsPanel;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.disjointtan.SCTDisjointClusterTANOptionsPanel;

/**
 *
 * @author Chris O
 */
public class SCTDisjointTANUIConfiguration extends DisjointTANUIConfiguration {
    
    private final SCTAbNFrameManager frameManager;

    public SCTDisjointTANUIConfiguration(
            SCTDisjointTANConfiguration config,
            AbNDisplayManager displayListener,
            SCTAbNFrameManager frameManager) {
        
        super(config, displayListener, new SCTDisjointTANListenerConfiguration(config));
    
        this.frameManager = frameManager;
    }
    
    public SCTAbNFrameManager getFrameManager() {
        return frameManager;
    }
    
    @Override
    public SCTDisjointTANConfiguration getConfiguration() {
        return (SCTDisjointTANConfiguration)super.getConfiguration();
    }
    
    @Override
    public NodeOptionsPanel<DisjointNode<Cluster>> getNodeOptionsPanel() {
        return new SCTDisjointClusterOptionsPanel(getConfiguration(), getConfiguration().getAbstractionNetwork().isAggregated());
    }

    @Override
    public AbNOptionsPanel getAbNOptionsPanel() {
        return new SCTDisjointClusterTANOptionsPanel(getConfiguration());
    }

    @Override
    public ConceptPainter getConceptHierarchyPainter() {
        return new SCTConceptPainter();
    }
}
