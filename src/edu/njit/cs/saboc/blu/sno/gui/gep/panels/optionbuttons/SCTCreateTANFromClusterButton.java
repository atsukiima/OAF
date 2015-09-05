package edu.njit.cs.saboc.blu.sno.gui.gep.panels.optionbuttons;

import edu.njit.cs.saboc.blu.core.gui.dialogs.LoadStatusDialog;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.optionbuttons.CreateTANButton;
import edu.njit.cs.saboc.blu.sno.abn.tan.TANGenerator;
import edu.njit.cs.saboc.blu.sno.abn.tan.local.SCTCluster;
import edu.njit.cs.saboc.blu.sno.abn.tan.local.SCTTribalAbstractionNetwork;
import edu.njit.cs.saboc.blu.sno.gui.abnselection.SCTDisplayFrameListener;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.SCTTANConfiguration;
import edu.njit.cs.saboc.blu.sno.sctdatasource.SCTLocalDataSource;
import java.util.Optional;
import javax.swing.SwingUtilities;

/**
 *
 * @author Den
 */
public class SCTCreateTANFromClusterButton extends CreateTANButton {
    private Optional<SCTCluster> currentPArea = Optional.empty();
    
    private final SCTTANConfiguration config;
    
    public SCTCreateTANFromClusterButton(SCTTANConfiguration config) {
        super("partial-area");
        
        this.config = config;
    }
    
    public void setCurrentCluster(SCTCluster parea) {
        currentPArea = Optional.ofNullable(parea);
    }
    
    @Override
    public void deriveTANAction() {
        if (currentPArea.isPresent()) {
            Thread loadThread = new Thread(new Runnable() {

                private LoadStatusDialog loadStatusDialog = null;

                public void run() {
                    SCTDisplayFrameListener displayListener = config.getDisplayListener();
                    
                    SCTCluster cluster = currentPArea.get();

                    loadStatusDialog = LoadStatusDialog.display(null,
                            String.format("Creating %s Tribal Abstraction Network (TAN)", config.getGroupName(cluster)));
                    
                    SCTTribalAbstractionNetwork tan = TANGenerator.createTANFromConceptHierarchy(
                            config.getTribalAbstractionNetwork().getSCTVersion(),
                            cluster.getConceptHierarchy(),
                            (SCTLocalDataSource)config.getTribalAbstractionNetwork().getDataSource());

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