package edu.njit.cs.saboc.blu.sno.gui.gep.panels.optionbuttons.tan;

import edu.njit.cs.saboc.blu.core.abn.tan.Cluster;
import edu.njit.cs.saboc.blu.core.abn.tan.ClusterTribalAbstractionNetwork;
import edu.njit.cs.saboc.blu.core.gui.dialogs.LoadStatusDialog;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.tan.buttons.CreateRootTANButton;
import edu.njit.cs.saboc.blu.sno.gui.abnselection.SCTDisplayFrameListener;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.tan.configuration.SCTTANConfiguration;
import java.util.Optional;
import javax.swing.SwingUtilities;

/**
 *
 * @author Chris O
 */
public class SCTCreateRootTANButton extends CreateRootTANButton {

    private Optional<Cluster> currentCluster = Optional.empty();
    
    private final SCTTANConfiguration config;
    
    public SCTCreateRootTANButton(SCTTANConfiguration config) {
        this.config = config;
    }
    
    public void setCurrentCluster(Cluster cluster) {
        currentCluster = Optional.ofNullable(cluster);
    }
    
    @Override
    public void createRootTANAction() {
        if (currentCluster.isPresent()) {
            Thread loadThread = new Thread(new Runnable() {

                private LoadStatusDialog loadStatusDialog = null;
                private boolean doLoad = true;

                public void run() {
                    SCTDisplayFrameListener displayListener = config.getUIConfiguration().getDisplayFrameListener();
                    
                    Cluster cluster = currentCluster.get();

                    loadStatusDialog = LoadStatusDialog.display(null,
                            String.format("Creating %s descendant TAN", cluster.getName()),
                            new LoadStatusDialog.LoadingDialogClosedListener() {

                            @Override
                            public void dialogClosed() {
                                doLoad = false;
                            }
                        });
                    
                    ClusterTribalAbstractionNetwork subTAN = config.getTribalAbstractionNetwork().createRootSubTAN(cluster);

                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            if (doLoad) {
                                displayListener.addNewClusterGraphFrame(subTAN, true, true);
                                
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
