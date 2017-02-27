package edu.njit.cs.saboc.blu.sno.gui.abnselection;

import edu.njit.cs.saboc.blu.core.datastructure.hierarchy.Hierarchy;
import edu.njit.cs.saboc.blu.core.gui.panels.abnderivationwizard.targetbased.TargetAbNDerivationWizardPanel;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.SCTInheritableProperty;
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
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Optional;
import java.util.Set;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

/**
 *
 * @author cro3
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
        abnSelectionTabs.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK), 
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
        SCTTANConfiguration dummyConfig = factory.createConfiguration(null, frameManager);        
        
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
                            "Creating Range Abstraction Network",
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
        centerPanel.add(createConceptBrowserPanel(), BorderLayout.SOUTH);

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
        
        this.openBrowserBtn.setEnabled(value);
    }
    
    public void setCurrentRelease(SCTRelease release) {
        this.optCurrentRelease = Optional.of(release);
        
        pareaTaxonomyDerivationWizardPanel.initialize(release);
        
        diffPAreaTaxonomyDerivationWizardPanel.initialize(release);
        
        tanDerivationWizardPanel.initialize(release);
        
        targetAbNDerivationWizardPanel.initialize(
                release, 
                release, 
                new SCTInheritablePropertyRetriever((SCTReleaseWithStated)release), 
                new SCTTargetHierarchyRetriever((SCTReleaseWithStated)release));
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
        JPanel browserPanel = new JPanel();
        
        browserPanel.setLayout(new BoxLayout(browserPanel, BoxLayout.X_AXIS));
        browserPanel.setBorder(BorderFactory.createTitledBorder("BLUSNO Neighborhood Auditing Tool (NAT) Concept Browser"));

        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        openBrowserBtn = new JButton("<html><div align='center'>Open NAT Concept Browser");
        openBrowserBtn.setFont(openBrowserBtn.getFont().deriveFont(Font.BOLD, 14));

        openBrowserBtn.addActionListener( (ae) -> {
            openConceptBrowser();
        });

        openBrowserBtn.setEnabled(false);

        leftPanel.add(openBrowserBtn, BorderLayout.CENTER);

        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        JEditorPane detailsPane = new JEditorPane();
        detailsPane.setContentType("text/html");

        String detailsString = "<html>The BLUSNO Neighborhood Auditing Tool (NAT) concept browser allows you to browse individual concepts. "
                + "BLUSNO's concept browser is unique in that it displays information derived "
                + "from various SNOMED CT abstraction networks alongside information about a chosen concept's neighborhood. "
                + "<b>Subject subtaxonomies</b>, <b>Focus subtaxonomies</b>, and <b>Tribal Abstraction Networks</b> can be derived "
                + "for individual concepts from within the concept browser when using a local SNOMED CT release.";

        detailsPane.setText(detailsString);

        rightPanel.add(detailsPane, BorderLayout.CENTER);

        browserPanel.add(leftPanel);
        browserPanel.add(Box.createHorizontalStrut(8));
        browserPanel.add(rightPanel);

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

        SwingUtilities.invokeLater( () -> {
            frameManager.displayConceptBrowserFrame(optCurrentRelease.get());
        });
    }

}
