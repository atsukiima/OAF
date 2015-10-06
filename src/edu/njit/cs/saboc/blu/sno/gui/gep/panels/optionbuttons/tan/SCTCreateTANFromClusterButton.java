package edu.njit.cs.saboc.blu.sno.gui.gep.panels.optionbuttons.tan;

import edu.njit.cs.saboc.blu.core.gui.dialogs.LoadStatusDialog;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.optionbuttons.CreateTANButton;
import edu.njit.cs.saboc.blu.sno.abn.tan.TANGenerator;
import edu.njit.cs.saboc.blu.sno.abn.tan.local.SCTCluster;
import edu.njit.cs.saboc.blu.sno.abn.tan.local.SCTTribalAbstractionNetwork;
import edu.njit.cs.saboc.blu.sno.gui.abnselection.SCTDisplayFrameListener;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.tan.configuration.SCTTANConfiguration;
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
                private boolean doLoad = true;

                public void run() {
                    SCTDisplayFrameListener displayListener = config.getUIConfiguration().getDisplayFrameListener();
                    
                    SCTCluster cluster = currentPArea.get();

                    loadStatusDialog = LoadStatusDialog.display(null,
                            String.format("Creating %s Tribal Abstraction Network (TAN)", config.getTextConfiguration().getGroupName(cluster)),
                            new LoadStatusDialog.LoadingDialogClosedListener() {

                            @Override
                            public void dialogClosed() {
                                doLoad = false;
                            }
                        });
                    
                    SCTTribalAbstractionNetwork tan = TANGenerator.createTANFromConceptHierarchy(
                            config.getDataConfiguration().getTribalAbstractionNetwork().getSCTVersion(),
                            cluster.getConceptHierarchy(),
                            (SCTLocalDataSource)config.getDataConfiguration().getTribalAbstractionNetwork().getDataSource());

                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            if (doLoad) {
                                displayListener.addNewClusterGraphFrame(tan, true, true);

                                loadStatusDialog.setVisible(false);
                                loadStatusDialog.dispose();
                            }
                        }
                    });
                }
            });

            loadThread.start();
        }
    }
}