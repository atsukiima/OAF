package edu.njit.cs.saboc.blu.sno.gui.abnselection.wizard;

import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.tan.configuration.TANConfiguration;
import edu.njit.cs.saboc.blu.core.gui.panels.abnderivationwizard.tan.TANDerivationWizardPanel;
import edu.njit.cs.saboc.blu.sno.sctdatasource.SCTRelease;
import edu.njit.cs.saboc.blu.sno.sctdatasource.SCTReleaseWithStated;
import java.util.Optional;
import javax.swing.JCheckBox;
import javax.swing.SwingUtilities;

/**
 * Panel for creating SNOMED CT Tribal Abstraction Networks. 
 * 
 * @author Chris O
 */
public class SCTTANDerivationWizardPanel extends TANDerivationWizardPanel {
    
    private final JCheckBox chkUseStatedRelationships;
    
    private Optional<SCTRelease> currentRelease = Optional.empty();
    
    public SCTTANDerivationWizardPanel(
            TANConfiguration config, 
            DeriveTANAction derivationAction) {
        
        super(config, derivationAction, new SCTRootSelectionPanel(config));
        
        this.chkUseStatedRelationships = new JCheckBox("<html>Use Stated <i>Is a</i> Relationships");
        this.chkUseStatedRelationships.addActionListener( (ae) -> {
            
            this.setEnabled(false);
            
            SwingUtilities.invokeLater(() -> {
                if (this.chkUseStatedRelationships.isSelected()) {
                    SCTReleaseWithStated statedRelease = (SCTReleaseWithStated) currentRelease.get();

                    this.initialize(statedRelease.getStatedHierarchyRelease(), false);
                } else {
                    this.initialize(currentRelease.get(), false);
                }
                
                this.setEnabled(true);
            });
        });
        
        super.addOptionsPanelItem(chkUseStatedRelationships);
    }
    
    @Override
    public void setEnabled(boolean value) {
        super.setEnabled(value);
        
        if(currentRelease.isPresent()) {
            chkUseStatedRelationships.setEnabled(currentRelease.get().supportsStatedRelationships() && value);
        } else {
            chkUseStatedRelationships.setEnabled(false);
        }
    }
    
    public boolean useStatedRelationshipsSelected() {
        return chkUseStatedRelationships.isEnabled() && chkUseStatedRelationships.isSelected();
    }
    
    public void initialize(SCTRelease release) {
        initialize(release, true);
    }
    
    public void initialize(SCTRelease release, boolean setCurrentRelease) {
        this.resetView();
        
        super.initialize(release, release);
        
        if(setCurrentRelease) {
            this.currentRelease = Optional.of(release); 
        }
        
        if(release.supportsStatedRelationships()) {
            this.chkUseStatedRelationships.setEnabled(true);
        }
    }
    
    public void clearRelease() {
        this.currentRelease = Optional.empty();
    }
}
