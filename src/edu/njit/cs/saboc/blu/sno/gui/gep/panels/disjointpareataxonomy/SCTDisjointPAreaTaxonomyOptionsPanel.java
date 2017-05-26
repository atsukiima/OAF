package edu.njit.cs.saboc.blu.sno.gui.gep.panels.disjointpareataxonomy;

import edu.njit.cs.saboc.blu.core.abn.disjoint.DisjointAbstractionNetwork;
import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.PAreaTaxonomy;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.AbNOptionsPanel;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.optionbuttons.abn.AbNHelpButton;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.optionbuttons.abn.ExportAbNButton;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.optionbuttons.abn.PopoutAbNDetailsButton;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.optionbuttons.abn.SavePNGButton;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.optionbuttons.abn.ShowSourceHierarchyButton;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.optionbuttons.abn.ViewDisjointAbNSubsetButton;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.disjointpareataxonomy.configuration.SCTDisjointPAreaTaxonomyConfiguration;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy.configuration.SCTPAreaTaxonomyConfiguration;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy.configuration.SCTPAreaTaxonomyConfigurationFactory;

/**
 *
 * @author Chris O
 */
public class SCTDisjointPAreaTaxonomyOptionsPanel extends AbNOptionsPanel<DisjointAbstractionNetwork> {
    
    public SCTDisjointPAreaTaxonomyOptionsPanel(SCTDisjointPAreaTaxonomyConfiguration config) {
        
        AbNHelpButton helpBtn = new AbNHelpButton(config);
        ExportAbNButton exportBtn = new ExportAbNButton("Export Disjoint Partial-area Taxonomy", config);
        SavePNGButton pngBtn = new SavePNGButton(config);
        PopoutAbNDetailsButton popoutBtn = new PopoutAbNDetailsButton(config);
        
        
        PAreaTaxonomy taxonomy = (PAreaTaxonomy)config.getAbstractionNetwork().getParentAbstractionNetwork();
                
        SCTPAreaTaxonomyConfiguration parentConfig = new SCTPAreaTaxonomyConfigurationFactory().createConfiguration(
                                config.getRelease(),
                                taxonomy, 
                                config.getUIConfiguration().getAbNDisplayManager(), 
                                config.getUIConfiguration().getFrameManager(),
                                false);
        
        ViewDisjointAbNSubsetButton viewSubsetBtn = new ViewDisjointAbNSubsetButton(config,  
                (disjointAbN) -> {
                    config.getUIConfiguration().getAbNDisplayManager().displayDisjointPAreaTaxonomy(disjointAbN);
                },
                parentConfig);
        
                
        ShowSourceHierarchyButton showHierarchyBtn = new ShowSourceHierarchyButton();

        showHierarchyBtn.setCurrentAbN(config.getAbstractionNetwork());
        
        viewSubsetBtn.setCurrentAbN(config.getAbstractionNetwork());
        helpBtn.setCurrentAbN(config.getAbstractionNetwork());
        exportBtn.setCurrentAbN(config.getAbstractionNetwork());
        pngBtn.setCurrentAbN(config.getAbstractionNetwork());
        popoutBtn.setCurrentEntity(config.getAbstractionNetwork());
        
        
        super.addOptionButton(showHierarchyBtn);
        super.addOptionButton(viewSubsetBtn);
        super.addOptionButton(helpBtn);
        super.addOptionButton(exportBtn);
        super.addOptionButton(pngBtn);
        super.addOptionButton(popoutBtn);
    }
}