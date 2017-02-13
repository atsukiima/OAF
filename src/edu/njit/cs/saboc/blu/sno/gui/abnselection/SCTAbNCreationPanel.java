package edu.njit.cs.saboc.blu.sno.gui.abnselection;

import edu.njit.cs.saboc.blu.core.datastructure.hierarchy.Hierarchy;
import edu.njit.cs.saboc.blu.core.gui.panels.abnderivationwizard.targetbased.TargetAbNDerivationWizardPanel;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.SCTInheritableProperty;
import edu.njit.cs.saboc.blu.sno.gui.abnselection.createanddisplay.CreateAndDisplaySCTPAreaTaxonomy;
import edu.njit.cs.saboc.blu.sno.gui.abnselection.createanddisplay.CreateAndDisplaySCTTAN;
import edu.njit.cs.saboc.blu.sno.gui.abnselection.createanddisplay.CreateAndDisplayTargetAbN;
import edu.njit.cs.saboc.blu.sno.gui.abnselection.wizard.SCTInheritablePropertyRetriever;
import edu.njit.cs.saboc.blu.sno.gui.abnselection.wizard.SCTPAreaTaxonomyWizardPanel;
import edu.njit.cs.saboc.blu.sno.gui.abnselection.wizard.SCTTANDerivationWizardPanel;
import edu.njit.cs.saboc.blu.sno.gui.abnselection.wizard.SCTTargetHierarchyRetriever;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.tan.configuration.SCTTANConfiguration;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.tan.configuration.SCTTANConfigurationFactory;
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
 * @author cro3
 */
public class SCTAbNCreationPanel extends JPanel {
    
    private final JTabbedPane abnSelectionTabs;
    
    private final SCTPAreaTaxonomyWizardPanel pareaTaxonomyDerivationWizardPanel;
    
    private final SCTTANDerivationWizardPanel tanDerivationWizardPanel;
    
    private final TargetAbNDerivationWizardPanel targetAbNDerivationWizardPanel;
    
    private Optional<SCTRelease> optCurrentRelease = Optional.empty();
    
    public SCTAbNCreationPanel(SCTAbNFrameManager frameManager) {
        
        super(new BorderLayout());
        
         JPanel centerPanel = new JPanel(new BorderLayout());
        
        abnSelectionTabs = new JTabbedPane();
        abnSelectionTabs.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK), 
                "Derive an Abstraction Network"));
        
        
        this.pareaTaxonomyDerivationWizardPanel = new SCTPAreaTaxonomyWizardPanel( 
                (release, root, availableProperties, selectedProperties) -> {
                    CreateAndDisplaySCTPAreaTaxonomy createPAreaTaxonomy = new CreateAndDisplaySCTPAreaTaxonomy(
                            "Creating partial-area taxonomy...",
                            root, 
                            availableProperties,
                            selectedProperties,
                            frameManager,
                            release);
                    
                    createPAreaTaxonomy.createAbN();
                    
                }, frameManager);
        
        
        JPanel pareaPanel = new JPanel(new BorderLayout());
        pareaPanel.add(pareaTaxonomyDerivationWizardPanel, BorderLayout.CENTER);
        
        /*
        JPanel diffPAreaPanel = new JPanel(new BorderLayout());
        diffPAreaPanel.add(diffPAreaTaxonomyWizardPanel, BorderLayout.CENTER);
        */
        
        SCTTANConfigurationFactory factory = new SCTTANConfigurationFactory();
        SCTTANConfiguration dummyConfig = factory.createConfiguration(null, frameManager);        
        
        tanDerivationWizardPanel = new SCTTANDerivationWizardPanel(dummyConfig, (patriarchs) -> {
            
            CreateAndDisplaySCTTAN creatingTANDialog = new CreateAndDisplaySCTTAN(
                    "Creating Tribal Abstraction Network", 
                    (Set<SCTConcept>)(Set<?>)patriarchs,
                    getTANDerivationPanel().useStatedRelationshipsSelected(),
                    frameManager,
                    optCurrentRelease.get()
                );
            
            creatingTANDialog.createAbN();
        });
        
        JPanel tanPanel = new JPanel(new BorderLayout());
        tanPanel.add(tanDerivationWizardPanel, BorderLayout.CENTER);
        
        targetAbNDerivationWizardPanel = new TargetAbNDerivationWizardPanel(dummyConfig,
                (sourceHierarchy, type, targetHierarchy) -> {
                    CreateAndDisplayTargetAbN createRangeAbN = new CreateAndDisplayTargetAbN(
                            "Creating Range Abstraction Network",
                            frameManager,
                            (Hierarchy<SCTConcept>) (Hierarchy<?>) sourceHierarchy,
                            (SCTInheritableProperty) type,
                            (Hierarchy<SCTConcept>) (Hierarchy<?>) targetHierarchy,
                            (SCTReleaseWithStated)optCurrentRelease.get()
                    );

                    createRangeAbN.createAbN();
                });

        JPanel targetPanel = new JPanel(new BorderLayout());
        targetPanel.add(targetAbNDerivationWizardPanel, BorderLayout.CENTER);
        
        
        abnSelectionTabs.addTab("Partial-area Taxonomy", pareaPanel);
//        abnSelectionTabs.addTab("Diff Partial-area Taxonomy", diffPAreaTaxonomyWizardPanel);
        abnSelectionTabs.addTab("Tribal Abstraction Network", tanPanel);
        abnSelectionTabs.addTab("Target Abstraction Network", targetPanel);

        centerPanel.add(abnSelectionTabs, BorderLayout.CENTER);

        this.add(centerPanel, BorderLayout.CENTER);
    }
    
    private SCTTANDerivationWizardPanel getTANDerivationPanel() {
        return tanDerivationWizardPanel;
    }
    
    @Override
    public void setEnabled(boolean value) {
        super.setEnabled(value);
        
        this.abnSelectionTabs.setEnabled(value);
        
        this.pareaTaxonomyDerivationWizardPanel.setEnabled(value);
        this.tanDerivationWizardPanel.setEnabled(value);
        this.targetAbNDerivationWizardPanel.setEnabled(value);
    }
    
    public void setCurrentRelease(SCTRelease release) {
        this.optCurrentRelease = Optional.of(release);
        
        pareaTaxonomyDerivationWizardPanel.initialize(release);
        
        tanDerivationWizardPanel.initialize(release);
        
        targetAbNDerivationWizardPanel.initialize(
                release, 
                release, 
                new SCTInheritablePropertyRetriever((SCTReleaseWithStated)release), 
                new SCTTargetHierarchyRetriever((SCTReleaseWithStated)release));
    }
    
    public void clear() {
       this.optCurrentRelease = Optional.empty();
    }
    
    public void resetView() {
        
    }
}
