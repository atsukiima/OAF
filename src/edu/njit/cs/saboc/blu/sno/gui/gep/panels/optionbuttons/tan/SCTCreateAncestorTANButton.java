package edu.njit.cs.saboc.blu.sno.gui.gep.panels.optionbuttons.tan;

import edu.njit.cs.saboc.blu.core.gui.dialogs.LoadStatusDialog;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.tan.buttons.CreateAncestorTANButton;
import edu.njit.cs.saboc.blu.sno.abn.tan.local.SCTCluster;
import edu.njit.cs.saboc.blu.sno.abn.tan.local.SCTTribalAbstractionNetwork;
import edu.njit.cs.saboc.blu.sno.gui.abnselection.SCTDisplayFrameListener;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.tan.configuration.SCTTANConfiguration;
import java.util.Optional;
import javax.swing.SwingUtilities;

/**
 *
 * @author Chris O
 */
public class SCTCreateAncestorTANButton extends CreateAncestorTANButton {

    private Optional<SCTCluster> currentCluster = Optional.empty();
    
    private final SCTTANConfiguration config;
    
    public SCTCreateAncestorTANButton(SCTTANConfiguration config) {
        this.config = config;
    }
    
    public void setCurrentCluster(SCTCluster parea) {
        currentCluster = Optional.ofNullable(parea);
    }
    
    @Override
    public void createAncestorTANAction() {
        if (currentCluster.isPresent()) {
            Thread loadThread = new Thread(new Runnable() {

                private LoadStatusDialog loadStatusDialog = null;
                private boolean doLoad = true;

                public void run() {
                    SCTDisplayFrameListener displayListener = config.getUIConfiguration().getDisplayFrameListener();
                    
                    SCTCluster cluster = currentCluster.get();

                    loadStatusDialog = LoadStatusDialog.display(null,
                            String.format("Creating %s ancestor TAN", config.getTextConfiguration().getGroupName(cluster)),
                            new LoadStatusDialog.LoadingDialogClosedListener() {

                            @Override
                            public void dialogClosed() {
                                doLoad = false;
                            }
                        });
                    
                    SCTTribalAbstractionNetwork tan = config.getDataConfiguration().getTribalAbstractionNetwork().createAncestorTAN(cluster);

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
