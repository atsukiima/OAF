package edu.njit.cs.saboc.blu.sno.gui.abnselection;

import edu.njit.cs.saboc.blu.core.datastructure.hierarchy.Hierarchy;
import edu.njit.cs.saboc.blu.core.gui.panels.abnderivationwizard.targetbased.TargetAbNDerivationWizardPanel;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.SCTInheritableProperty;
import edu.njit.cs.saboc.blu.sno.gui.abnselection.createanddisplay.CreateAndDisplaySCTPAreaTaxonomy;
import edu.njit.cs.saboc.blu.sno.gui.abnselection.createanddisplay.CreateAndDisplaySCTTAN;
import edu.njit.cs.saboc.blu.sno.gui.abnselection.createanddisplay.CreateAndDisplayTargetAbN;
import edu.njit.cs.saboc.blu.sno.gui.abnselection.wizard.AttributeRelationshipRootSelectionPanel;
import edu.njit.cs.saboc.blu.sno.gui.abnselection.wizard.SCTDiffPAreaTaxonomyWizardPanel;
import edu.njit.cs.saboc.blu.sno.gui.abnselection.wizard.SCTInheritablePropertyRetriever;
import edu.njit.cs.saboc.blu.sno.gui.abnselection.wizard.SCTPAreaTaxonomyWizardPanel;
import edu.njit.cs.saboc.blu.sno.gui.abnselection.wizard.SCTTANDerivationWizardPanel;
import edu.njit.cs.saboc.blu.sno.gui.abnselection.wizard.SCTTargetHierarchyRetriever;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.tan.configuration.SCTTANConfiguration;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.tan.configuration.SCTTANConfigurationFactory;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.targetabn.configuration.SCTTargetAbNConfiguration;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.targetabn.configuration.SCTTargetAbNConfigurationFactory;
import edu.njit.cs.saboc.blu.sno.localdatasource.concept.SCTConcept;
import edu.njit.cs.saboc.blu.sno.sctdatasource.SCTRelease;
import edu.njit.cs.saboc.blu.sno.sctdatasource.SCTReleaseWithStated;
import java.awt.BorderLayout;
import java.awt.Color;
import java.util.Optional;
import java.util.Set;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

/**
 *
 * @author Chris O
 */
public class SCTAbNWizardPanel extends JPanel {
    
    private Optional<SCTRelease> optCurrentRelease = Optional.empty();
    
    private final JTabbedPane abnSelectionTabs;
    
    
    
    private final SCTPAreaTaxonomyWizardPanel pareaTaxonomyDerivationWizardPanel;
    
    private final SCTDiffPAreaTaxonomyWizardPanel diffPAreaTaxonomyDerivationWizardPanel;
    
    private final SCTTANDerivationWizardPanel tanDerivationWizardPanel;
    
    private final TargetAbNDerivationWizardPanel targetAbNDerivationWizardPanel;
    
    
    
    public SCTAbNWizardPanel(SCTAbNFrameManager frameManager) {
        this.setLayout(new BorderLayout());
        
        this.abnSelectionTabs = new JTabbedPane();
        this.abnSelectionTabs.setBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(Color.GREEN, 2), 
                "Derive an Abstraction Network"));
        
        
        this.pareaTaxonomyDerivationWizardPanel = new SCTPAreaTaxonomyWizardPanel( 
                
                (release, root, availableProperties, selectedProperties, useStatedRels) -> {
                    CreateAndDisplaySCTPAreaTaxonomy createPAreaTaxonomy = new CreateAndDisplaySCTPAreaTaxonomy(
                            "Creating partial-area taxonomy...",
                            root, 
                            availableProperties,
                            selectedProperties,
                            frameManager,
                            release,
                            useStatedRels);
                    
                    createPAreaTaxonomy.run();
                    
                }, frameManager);
        
        
        JPanel pareaPanel = new JPanel(new BorderLayout());
        pareaPanel.add(pareaTaxonomyDerivationWizardPanel, BorderLayout.CENTER);
        
        
        this.diffPAreaTaxonomyDerivationWizardPanel = new SCTDiffPAreaTaxonomyWizardPanel(frameManager);
        
        
        JPanel diffPAreaPanel = new JPanel(new BorderLayout());
        diffPAreaPanel.add(diffPAreaTaxonomyDerivationWizardPanel, BorderLayout.CENTER);
        
        
        SCTTANConfigurationFactory tanConfigFactory = new SCTTANConfigurationFactory();
        SCTTANConfiguration dummyTANConfig = tanConfigFactory.createConfiguration(null, frameManager, null, false);        
        
        tanDerivationWizardPanel = new SCTTANDerivationWizardPanel(dummyTANConfig, (patriarchs) -> {
            
            CreateAndDisplaySCTTAN creatingTANDialog = new CreateAndDisplaySCTTAN(
                    "Creating Tribal Abstraction Network", 
                    (Set<SCTConcept>)(Set<?>)patriarchs,
                    getTANDerivationPanel().useStatedRelationshipsSelected(),
                    frameManager,
                    optCurrentRelease.get()
                );
            
            creatingTANDialog.run();
        });
        
        JPanel tanPanel = new JPanel(new BorderLayout());
        tanPanel.add(tanDerivationWizardPanel, BorderLayout.CENTER);
        
        SCTTargetAbNConfigurationFactory targetConfigFactory = new SCTTargetAbNConfigurationFactory();
        SCTTargetAbNConfiguration dummyTargetConfig = targetConfigFactory.createConfiguration(null, frameManager, null);       
        
        targetAbNDerivationWizardPanel = new TargetAbNDerivationWizardPanel(dummyTargetConfig,
                (sourceHierarchy, type, targetHierarchy) -> {
                    CreateAndDisplayTargetAbN createRangeAbN = new CreateAndDisplayTargetAbN(
                            "Creating Attribute Relationship Target Abstraction Network",
                            frameManager,
                            (Hierarchy<SCTConcept>) (Hierarchy<?>) sourceHierarchy,
                            (SCTInheritableProperty) type,
                            (Hierarchy<SCTConcept>) (Hierarchy<?>) targetHierarchy,
                            (SCTReleaseWithStated)optCurrentRelease.get()
                    );

                    createRangeAbN.run();
                },
                new AttributeRelationshipRootSelectionPanel<>(dummyTargetConfig));

        JPanel targetPanel = new JPanel(new BorderLayout());
        targetPanel.add(targetAbNDerivationWizardPanel, BorderLayout.CENTER);
        
        abnSelectionTabs.addTab("<html>Partial-area Taxonomy", pareaPanel);
        abnSelectionTabs.addTab("<html>Diff Partial-area Taxonomy (<font color = 'RED'><i>Under development</i></font>)", diffPAreaPanel);
        abnSelectionTabs.addTab("<html>Tribal Abstraction Network (TAN)", tanPanel);
        abnSelectionTabs.addTab("<html>Target Abstraction Network (Target AbN)", targetPanel);

        this.add(abnSelectionTabs, BorderLayout.CENTER);
    }

    public final SCTTANDerivationWizardPanel getTANDerivationPanel() {
        return tanDerivationWizardPanel;
    }
    
    @Override
    public void setEnabled(boolean value) {
        super.setEnabled(value);

        this.abnSelectionTabs.setEnabled(value);

        this.pareaTaxonomyDerivationWizardPanel.setEnabled(value);
        this.tanDerivationWizardPanel.setEnabled(value);

        if (this.optCurrentRelease.isPresent()) {
            this.targetAbNDerivationWizardPanel.setEnabled(value
                    && this.optCurrentRelease.get().supportsStatedRelationships());
        }
    }
    
    public void setCurrentRelease(SCTRelease release) {
        
        this.optCurrentRelease = Optional.of(release);
        
        pareaTaxonomyDerivationWizardPanel.initialize(release);

        diffPAreaTaxonomyDerivationWizardPanel.initialize(release);

        tanDerivationWizardPanel.initialize(release);

        if (release.supportsStatedRelationships()) {
            targetAbNDerivationWizardPanel.initialize(
                    release,
                    release,
                    new SCTInheritablePropertyRetriever((SCTReleaseWithStated) release),
                    new SCTTargetHierarchyRetriever((SCTReleaseWithStated) release));
        }
    }
    
    public void clear() {
        this.optCurrentRelease = Optional.empty();

        pareaTaxonomyDerivationWizardPanel.clearContents();
        tanDerivationWizardPanel.clearContents();
        targetAbNDerivationWizardPanel.clearContents();
        diffPAreaTaxonomyDerivationWizardPanel.clearContents();
    }
}
