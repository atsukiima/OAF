package edu.njit.cs.saboc.blu.sno.gui.abnselection;

import edu.njit.cs.saboc.blu.core.datastructure.hierarchy.Hierarchy;
import edu.njit.cs.saboc.blu.core.gui.panels.abnderivationwizard.targetbased.TargetAbNDerivationWizardPanel;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.SCTInheritableProperty;
import edu.njit.cs.saboc.blu.sno.gui.abnselection.createanddisplay.CreateAndDisplaySCTNAT;
import edu.njit.cs.saboc.blu.sno.gui.abnselection.createanddisplay.CreateAndDisplaySCTPAreaTaxonomy;
import edu.njit.cs.saboc.blu.sno.gui.abnselection.createanddisplay.CreateAndDisplaySCTTAN;
import edu.njit.cs.saboc.blu.sno.gui.abnselection.createanddisplay.CreateAndDisplayTargetAbN;
import edu.njit.cs.saboc.blu.sno.gui.abnselection.wizard.SCTInheritablePropertyRetriever;
import edu.njit.cs.saboc.blu.sno.gui.abnselection.wizard.AttributeRelationshipRootSelectionPanel;
import edu.njit.cs.saboc.blu.sno.gui.abnselection.wizard.SCTDiffPAreaTaxonomyWizardPanel;
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
import java.awt.Dimension;
import java.util.Optional;
import java.util.Set;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

/**
 * Panel for creating abstraction networks from a SNOMED CT release.
 * 
 * @author Chris O
 */
public class SCTAbNCreationPanel extends JPanel {
    
    private final JTabbedPane abnSelectionTabs;
    
    private final SCTPAreaTaxonomyWizardPanel pareaTaxonomyDerivationWizardPanel;
    
    private final SCTDiffPAreaTaxonomyWizardPanel diffPAreaTaxonomyDerivationWizardPanel;
    
    private final SCTTANDerivationWizardPanel tanDerivationWizardPanel;
    
    private final TargetAbNDerivationWizardPanel targetAbNDerivationWizardPanel;
    
    private Optional<SCTRelease> optCurrentRelease = Optional.empty();
    
    private JButton openBrowserBtn;
    
    private final SCTAbNFrameManager frameManager;
    
    public SCTAbNCreationPanel(SCTAbNFrameManager frameManager) {
        
        super(new BorderLayout());
        
        this.frameManager = frameManager;
        
        JPanel centerPanel = new JPanel(new BorderLayout());
        
        abnSelectionTabs = new JTabbedPane();
        abnSelectionTabs.setBorder(
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
        
        
        SCTTANConfigurationFactory factory = new SCTTANConfigurationFactory();
        SCTTANConfiguration dummyConfig = factory.createConfiguration(null, frameManager, null, false);        
        
        tanDerivationWizardPanel = new SCTTANDerivationWizardPanel(dummyConfig, (patriarchs) -> {
            
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
        
        targetAbNDerivationWizardPanel = new TargetAbNDerivationWizardPanel(dummyConfig,
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
                new AttributeRelationshipRootSelectionPanel<>(dummyConfig));

        JPanel targetPanel = new JPanel(new BorderLayout());
        targetPanel.add(targetAbNDerivationWizardPanel, BorderLayout.CENTER);
        
        
        abnSelectionTabs.addTab("Partial-area Taxonomy", pareaPanel);
        abnSelectionTabs.addTab("Diff Partial-area Taxonomy", diffPAreaPanel);
        abnSelectionTabs.addTab("Tribal Abstraction Network", tanPanel);
        abnSelectionTabs.addTab("Target Abstraction Network", targetPanel);

        centerPanel.add(abnSelectionTabs, BorderLayout.CENTER);
                
        JPanel browserPanel = new JPanel(new BorderLayout());
        browserPanel.add(Box.createHorizontalStrut(10), BorderLayout.CENTER);
        browserPanel.add(createConceptBrowserPanel(), BorderLayout.EAST);
                
        centerPanel.add(browserPanel, BorderLayout.EAST);

        this.add(centerPanel, BorderLayout.CENTER);

        
        this.setBackground(Color.WHITE);
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
        
        if(this.optCurrentRelease.isPresent()) {
             this.targetAbNDerivationWizardPanel.setEnabled(value &&
                     this.optCurrentRelease.get().supportsStatedRelationships());
        }
       
        
        this.openBrowserBtn.setEnabled(value);
    }
    
    public void setCurrentRelease(SCTRelease release) {
        this.optCurrentRelease = Optional.of(release);

        pareaTaxonomyDerivationWizardPanel.initialize(release);

        diffPAreaTaxonomyDerivationWizardPanel.initialize(release);

        tanDerivationWizardPanel.initialize(release);

        
        if(release.supportsStatedRelationships()) {
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
    
    public void resetView() {
        
    }
    
    private JPanel createConceptBrowserPanel() {
        JPanel browserPanel = new JPanel(new BorderLayout());
        browserPanel.setPreferredSize(new Dimension(250, -1));
        
        browserPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.BLUE, 2),
                "Concept Browser"));

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        openBrowserBtn = new JButton("<html><div align='center'>Open<br>Concept<br>Browser");

        openBrowserBtn.addActionListener( (ae) -> {
            openConceptBrowser();
        });

        openBrowserBtn.setEnabled(false);

        topPanel.add(openBrowserBtn, BorderLayout.CENTER);
        topPanel.add(Box.createVerticalStrut(10), BorderLayout.SOUTH);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        JEditorPane detailsPane = new JEditorPane();
        detailsPane.setContentType("text/html");

        String detailsString = "<html><div align='justify'>The OAF SNOMED CT "
                + "concept browser allows you to browse individual concepts and their "
                + "relationships. ";

        detailsPane.setText(detailsString);

        bottomPanel.add(detailsPane, BorderLayout.CENTER);

        browserPanel.add(topPanel, BorderLayout.NORTH);
        browserPanel.add(bottomPanel, BorderLayout.CENTER);

        return browserPanel;
    }
    
    private void openConceptBrowser() {
        if (!optCurrentRelease.isPresent()) {
            JOptionPane.showMessageDialog(
                    null, 
                    "Please open a SNOMED CT release.",
                    "No Local Release Opened", JOptionPane.ERROR_MESSAGE);

            return;
        }

        CreateAndDisplaySCTNAT createNATDialog = new CreateAndDisplaySCTNAT(frameManager, optCurrentRelease.get());
        createNATDialog.run();
    }

}
