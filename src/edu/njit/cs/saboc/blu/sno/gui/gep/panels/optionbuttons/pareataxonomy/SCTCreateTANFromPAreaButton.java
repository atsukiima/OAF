package edu.njit.cs.saboc.blu.sno.gui.gep.panels.optionbuttons.pareataxonomy;

import edu.njit.cs.saboc.blu.core.gui.dialogs.LoadStatusDialog;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.optionbuttons.CreateTANButton;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTPArea;
import edu.njit.cs.saboc.blu.sno.abn.tan.TANGenerator;
import edu.njit.cs.saboc.blu.sno.abn.tan.local.SCTTribalAbstractionNetwork;
import edu.njit.cs.saboc.blu.sno.gui.abnselection.SCTDisplayFrameListener;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.SCTPAreaTaxonomyConfiguration;
import edu.njit.cs.saboc.blu.sno.sctdatasource.SCTLocalDataSource;
import java.util.Optional;
import javax.swing.SwingUtilities;

/**
 *
 * @author Chris O
 */
public class SCTCreateTANFromPAreaButton extends CreateTANButton {
    private Optional<SCTPArea> currentPArea = Optional.empty();
    
    private final SCTPAreaTaxonomyConfiguration config;
    
    public SCTCreateTANFromPAreaButton(SCTPAreaTaxonomyConfiguration config) {
        super("partial-area");
        
        this.config = config;
    }
    
    public void setCurrentPArea(SCTPArea parea) {
        currentPArea = Optional.ofNullable(parea);
    }
    
    @Override
    public void deriveTANAction() {
        if (currentPArea.isPresent()) {
            Thread loadThread = new Thread(new Runnable() {

                private LoadStatusDialog loadStatusDialog = null;

                public void run() {
                    SCTDisplayFrameListener displayListener = config.getDisplayListener();
                    
                    SCTPArea parea = currentPArea.get();

                    loadStatusDialog = LoadStatusDialog.display(null,
                            String.format("Creating %s Tribal Abstraction Network (TAN)", config.getGroupName(parea)));
                    
                    SCTTribalAbstractionNetwork tan = TANGenerator.createTANFromConceptHierarchy(
                            config.getPAreaTaxonomy().getSCTVersion(), 
                            parea.getHierarchy(),
                            (SCTLocalDataSource)config.getPAreaTaxonomy().getDataSource());

                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            
                            displayListener.addNewClusterGraphFrame(tan, true, true);

                            loadStatusDialog.setVisible(false);
                            loadStatusDialog.dispose();
                        }
                    });
                }
            });

            loadThread.start();
        }
    }
}
