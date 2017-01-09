
package edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy.diff.configuration;

import edu.njit.cs.saboc.blu.core.gui.dialogs.concepthierarchy.ConceptPainter;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.NodeDashboardPanel;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.NodeOptionsPanel;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.abn.AbstractAbNDetailsPanel;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.pareataxonomy.diff.configuration.DiffPAreaTaxonomyUIConfiguration;
import edu.njit.cs.saboc.blu.core.gui.graphframe.AbNDisplayManager;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.diffpareataxonomy.SCTDescriptiveDiffPAreaTaxonomy;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy.diff.DescriptiveDiffPAreaTaxonomyReportPanel;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy.diff.SCTDescriptiveDiffAreaPanel;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy.diff.SCTDescriptiveDiffPAreaPanel;

/**
 *
 * @author Chris O
 */
public class SCTDiffPAreaTaxonomyUIConfiguration extends DiffPAreaTaxonomyUIConfiguration {

    public SCTDiffPAreaTaxonomyUIConfiguration(
            SCTDiffPAreaTaxonomyConfiguration config, 
            SCTDiffPAreaTaxonomyListenerConfiguration listenerConfig,
            AbNDisplayManager displayListener) {

        super(config, displayListener, listenerConfig, new SCTDiffPAreaChangeExplanationFactory(config));
    }
    
    
    public SCTDiffPAreaTaxonomyUIConfiguration(SCTDiffPAreaTaxonomyConfiguration config, AbNDisplayManager displayListener) {
        
        this(config, new SCTDiffPAreaTaxonomyListenerConfiguration(config), displayListener);
    }
    
    @Override
    public SCTDiffPAreaTaxonomyConfiguration getConfiguration() {
        return (SCTDiffPAreaTaxonomyConfiguration)super.getConfiguration();
    }
    
    @Override
    public SCTDiffPAreaChangeExplanationFactory getChangeExplanationEntryFactory() {
        return (SCTDiffPAreaChangeExplanationFactory)super.getChangeExplanationEntryFactory();
    }

    @Override
    public AbstractAbNDetailsPanel createAbNDetailsPanel() {
        if(this.getConfiguration().getPAreaTaxonomy() instanceof SCTDescriptiveDiffPAreaTaxonomy) { 
            return new DescriptiveDiffPAreaTaxonomyReportPanel(this.getConfiguration());
        } else {
            return super.createAbNDetailsPanel();
        }
    }

    @Override
    public NodeDashboardPanel createContainerDetailsPanel() {
        if(this.getConfiguration().getPAreaTaxonomy() instanceof SCTDescriptiveDiffPAreaTaxonomy) { 
            return new SCTDescriptiveDiffAreaPanel(this.getConfiguration());
        } else {
            return super.createContainerDetailsPanel();
        }
    }

    @Override
    public NodeDashboardPanel createGroupDetailsPanel() {
        if(this.getConfiguration().getPAreaTaxonomy() instanceof SCTDescriptiveDiffPAreaTaxonomy) { 
            return new SCTDescriptiveDiffPAreaPanel(this.getConfiguration(), getChangeExplanationEntryFactory());
        } else {
            return super.createGroupDetailsPanel();
        }
    }
    
    @Override
    public NodeOptionsPanel getPartitionedNodeOptionsPanel() {
        return new NodeOptionsPanel();
    }

    @Override
    public NodeOptionsPanel getNodeOptionsPanel() {
        return new NodeOptionsPanel();
    }

    @Override
    public ConceptPainter getConceptHierarchyPainter() {
        return new ConceptPainter();
    }
}
