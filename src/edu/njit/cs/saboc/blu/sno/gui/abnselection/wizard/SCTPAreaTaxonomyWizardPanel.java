package edu.njit.cs.saboc.blu.sno.gui.abnselection.wizard;

import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.InheritableProperty;
import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.InheritableProperty.InheritanceType;
import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.PAreaTaxonomy;
import edu.njit.cs.saboc.blu.core.gui.panels.abnderivationwizard.InheritablePropertySelectionPanel;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.pareataxonomy.configuration.PAreaTaxonomyConfiguration;
import edu.njit.cs.saboc.blu.core.gui.panels.abnderivationwizard.AbNDerivationWizardPanel;
import edu.njit.cs.saboc.blu.core.gui.panels.abnderivationwizard.rootselection.BaseRootSelectionOptionsPanel.RootSelectionListener;
import edu.njit.cs.saboc.blu.core.gui.panels.abnderivationwizard.InheritablePropertySelectionPanel.SelectionType;
import edu.njit.cs.saboc.blu.core.gui.panels.abnderivationwizard.rootselection.BaseRootSelectionOptionsPanel;
import edu.njit.cs.saboc.blu.core.ontology.Concept;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.SCTInheritableProperty;
import edu.njit.cs.saboc.blu.sno.gui.abnselection.SCTAbNFrameManager;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy.configuration.SCTPAreaTaxonomyConfigurationFactory;
import edu.njit.cs.saboc.blu.sno.localdatasource.concept.SCTConcept;
import edu.njit.cs.saboc.blu.sno.sctdatasource.SCTRelease;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 *
 * @author Chris O
 */
public class SCTPAreaTaxonomyWizardPanel extends AbNDerivationWizardPanel {
    
    public interface SCTPAreaTaxonomyDerivationAction {

        public void derivePAreaTaxonomy(
                SCTRelease release,
                SCTConcept root,
                Set<SCTInheritableProperty> availableProperties,
                Set<SCTInheritableProperty> selectedProperties);
    }

    private Optional<SCTRelease> optRelease = Optional.empty();
    
    private final BaseRootSelectionOptionsPanel<PAreaTaxonomy> rootSelectionPanel;
        
    private final InheritablePropertySelectionPanel propertySelectionPanel;
    
    private final JButton deriveBtn;
    
    private final JLabel statusLabel;
    
    private final SCTPAreaTaxonomyDerivationAction taxonomyDerivationAction;
    
    public SCTPAreaTaxonomyWizardPanel(
            SCTPAreaTaxonomyDerivationAction taxonomyDerivationAction,
            SCTAbNFrameManager displayFrameListener) {
        
        this.taxonomyDerivationAction = taxonomyDerivationAction;

        this.setLayout(new BorderLayout());
        
        SCTPAreaTaxonomyConfigurationFactory dummyFactory = new SCTPAreaTaxonomyConfigurationFactory();
        PAreaTaxonomyConfiguration config = dummyFactory.createConfiguration(null, null);

        this.rootSelectionPanel = new SCTRootSelectionPanel(config);
        
        this.rootSelectionPanel.setBorder(
                BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK), 
                "Select Partial-area Taxonomy Root Concept"));
        
        this.rootSelectionPanel.addRootSelectionListener(new RootSelectionListener() {

            @Override
            public void rootSelected(Concept root) {
                doRootSelectedAction((SCTConcept)root);
            }

            @Override
            public void rootDoubleClicked(Concept root) {
                rootSelected(root);
            }

            @Override
            public void noRootSelected() {
                propertySelectionPanel.setEnabled(false);
                
                propertySelectionPanel.resetView();
                
                displayStatus();
            }
        });

        this.propertySelectionPanel = new InheritablePropertySelectionPanel(SelectionType.Multiple);
        this.propertySelectionPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK), 
                "Select Attribute Relationships"));
        
        this.propertySelectionPanel.setPreferredSize(new Dimension(350, -1));
        
        JPanel optionsPanel = new JPanel();
        optionsPanel.setLayout(new BoxLayout(optionsPanel, BoxLayout.X_AXIS));
        
        optionsPanel.add(rootSelectionPanel);
        optionsPanel.add(propertySelectionPanel);
        
        this.add(optionsPanel, BorderLayout.CENTER);
        
        this.statusLabel = new JLabel(" ");
        
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        statusPanel.add(statusLabel);
        
        JPanel derivationPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        this.deriveBtn = new JButton("Derive Partial-area Taxonomy");
        this.deriveBtn.addActionListener( (ae) -> {
            performedDerivationAction();
        });
        
        derivationPanel.add(deriveBtn);
        
        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.add(statusPanel, BorderLayout.CENTER);
        
        southPanel.add(derivationPanel, BorderLayout.EAST);
        
        this.add(southPanel, BorderLayout.SOUTH);
    }
    
    public void setDerivationButtonText(String derivationBtnStr) {
        this.deriveBtn.setText(derivationBtnStr);
    }

    @Override
    public void setEnabled(boolean value) {
        super.setEnabled(value);
        
        this.rootSelectionPanel.setEnabled(value);
        this.propertySelectionPanel.setEnabled(value);
        
        this.deriveBtn.setEnabled(value);
    }
    
    public void initialize(SCTRelease release) {
        super.initialize(release);
        
        this.optRelease = Optional.of(release);

        rootSelectionPanel.initialize(release, release);
        
        deriveBtn.setEnabled(true);
    }
    
    private void doRootSelectedAction(SCTConcept root) {
        propertySelectionPanel.resetView();
        
        rootSelectionPanel.setEnabled(false);
        propertySelectionPanel.setEnabled(false);

        displayLoadingData();

        Thread loaderThread = new Thread(() -> {
            if (optRelease.isPresent()) {
                
                Set<InheritableProperty> properties = new HashSet<>();
                
                optRelease.get().getConceptHierarchy().getSubhierarchyRootedAt(root).getNodes().forEach( (concept) -> {
                    concept.getAttributeRelationships().forEach( (rel) -> {
                        properties.add(new SCTInheritableProperty(rel.getType(), InheritanceType.Introduced));
                    });
                });
                
                ArrayList<InheritableProperty> sortedProperties = new ArrayList<>(properties);

                sortedProperties.sort((a, b) -> {
                    return a.getName().compareToIgnoreCase(b.getName());
                });

                SwingUtilities.invokeLater( () -> {
                    propertySelectionPanel.initialize(sortedProperties);

                    propertySelectionPanel.setEnabled(true);
                    rootSelectionPanel.setEnabled(true);
                    
                    displayStatus();
                });
            }
        });
        
        loaderThread.start();
    }
    
    private void displayLoadingData() {
        statusLabel.setText("Loading data, please wait...");
        statusLabel.setForeground(Color.RED);
    }
    
    private void displayStatus() {
        statusLabel.setForeground(Color.BLUE);
        
        if(optRelease.isPresent()) {
            if(rootSelectionPanel.getSelectedRoot().isPresent()) {
                statusLabel.setText(String.format("Ready to derive the %s Partial-area Taxonomy", 
                        rootSelectionPanel.getSelectedRoot().get().getName()));
            } else {
                statusLabel.setText("Please select a root class...");
            }
        } else {
            statusLabel.setText("No SNOMED CT release selected...");
        }
    }

    @Override
    public void clearContents() {
        super.clearContents();
        
        this.optRelease = Optional.empty();
        
        this.rootSelectionPanel.clearContents();
        this.propertySelectionPanel.clearContents();
    }

    @Override
    public void resetView() {
        this.rootSelectionPanel.resetView();
        this.propertySelectionPanel.resetView();
    }
    
    private void performedDerivationAction() {
        if(!optRelease.isPresent()) {
           return;
        }
        
        if(!rootSelectionPanel.getSelectedRoot().isPresent()) {
            return;
        }
        
        if(propertySelectionPanel.getUserSelectedProperties().isEmpty()) {
            return;
        }
        
        SCTRelease release = optRelease.get();
        SCTConcept root = (SCTConcept)rootSelectionPanel.getSelectedRoot().get();
        Set<SCTInheritableProperty> availableProperties = (Set<SCTInheritableProperty>)(Set<?>)propertySelectionPanel.getAvailableProperties();
        Set<SCTInheritableProperty> selectedProperties = (Set<SCTInheritableProperty>)(Set<?>)propertySelectionPanel.getUserSelectedProperties();
        
        this.taxonomyDerivationAction.derivePAreaTaxonomy(release, root, availableProperties, selectedProperties);
    }
}

