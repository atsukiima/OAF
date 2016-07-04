package edu.njit.cs.saboc.blu.sno.gui.gep.panels.disjointpareataxonomy.configuration;

import edu.njit.cs.saboc.blu.core.gui.dialogs.concepthierarchy.ConceptPainter;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.configuration.DisjointAbNUIConfiguration;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.AbstractNodeOptionsPanel;
import edu.njit.cs.saboc.blu.sno.gui.abnselection.SCTDisplayFrameListener;

/**
 *
 * @author Chris O
 */
public class SCTDisjointPAreaTaxonomyUIConfiguration extends DisjointAbNUIConfiguration {
    
    private final SCTDisjointPAreaTaxonomyConfiguration config;
    
    private final SCTDisplayFrameListener displayListener;
    
    public SCTDisjointPAreaTaxonomyUIConfiguration(SCTDisjointPAreaTaxonomyConfiguration config, SCTDisplayFrameListener displayListener) {
        
        super(config, new SCTDisjointPAreaTaxonomyListenerConfiguration(config));
        
        this.config = config;
        this.displayListener = displayListener;
    }
    
    public SCTDisplayFrameListener getDisplayListener() {
        return displayListener;
    }

    @Override
    public AbstractNodeOptionsPanel getNodeOptionsPanel() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ConceptPainter getConceptHierarchyPainter() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
