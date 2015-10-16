package edu.njit.cs.saboc.blu.sno.graph;

import edu.njit.cs.saboc.blu.core.graph.BluGraph;
import edu.njit.cs.saboc.blu.core.gui.gep.utils.drawing.GroupEntryLabelCreator;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTPArea;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTPAreaTaxonomy;
import edu.njit.cs.saboc.blu.sno.graph.layout.NoRegionsLayout;
import edu.njit.cs.saboc.blu.sno.graph.layout.RegionsLayout;
import edu.njit.cs.saboc.blu.sno.graph.layout.SCTGraphLayoutFactory;
import edu.njit.cs.saboc.blu.sno.gui.abnselection.SCTDisplayFrameListener;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy.configuration.SCTPAreaTaxonomyConfiguration;
import javax.swing.JFrame;

/**
 *
 * @author Chris
 */
public class PAreaBluGraph extends BluGraph {
    
    private SCTDisplayFrameListener displayListener;
    
    private final SCTPAreaTaxonomyConfiguration config;

    public PAreaBluGraph(
            JFrame parentFrame, 
            SCTPAreaTaxonomy hierarchyData, 
            boolean areaGraph,
            SCTDisplayFrameListener displayListener, 
            GroupEntryLabelCreator<SCTPArea> labelCreator,
            SCTPAreaTaxonomyConfiguration config) {
        
        super(hierarchyData, areaGraph, hierarchyData.getDataSource().isLocalDataSource(), labelCreator);
        
        this.displayListener = displayListener;
        this.config = config;

        if (areaGraph) {
            layout = SCTGraphLayoutFactory.createNoRegionsPAreaLayout(this);
            ((NoRegionsLayout) layout).doLayout(showConceptCountLabels);

        } else {
            layout = SCTGraphLayoutFactory.createRegionsPAreaLayout(this);
            ((RegionsLayout) layout).doLayout(showConceptCountLabels);
        }
    }
    
    public SCTPAreaTaxonomyConfiguration getConfiguration() {
        return config;
    }

    public SCTPAreaTaxonomy getPAreaTaxonomy() {
        return (SCTPAreaTaxonomy)getAbstractionNetwork();
    }
    
    public SCTDisplayFrameListener getDisplayFrameListener() {
        return displayListener;
    }
}
