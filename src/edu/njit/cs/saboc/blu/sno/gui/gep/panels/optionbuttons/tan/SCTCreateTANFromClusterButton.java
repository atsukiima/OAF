package edu.njit.cs.saboc.blu.sno.gui.gep.panels.optionbuttons.tan;

import edu.njit.cs.saboc.blu.core.abn.tan.Cluster;
import edu.njit.cs.saboc.blu.core.abn.tan.TribalAbstractionNetwork;
import edu.njit.cs.saboc.blu.core.abn.tan.TribalAbstractionNetworkGenerator;
import edu.njit.cs.saboc.blu.core.gui.dialogs.LoadStatusDialog;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.optionbuttons.CreateTANButton;
import edu.njit.cs.saboc.blu.sno.gui.abnselection.SCTDisplayFrameListener;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.tan.configuration.SCTTANConfiguration;
import java.util.Optional;
import javax.swing.SwingUtilities;

/**
 *
 * @author Den
 */
public class SCTCreateTANFromClusterButton extends CreateTANButton {
    private Optional<Cluster> currentPArea = Optional.empty();
    
    private final SCTTANConfiguration config;
    
    public SCTCreateTANFromClusterButton(SCTTANConfiguration config) {
        super("partial-area");
        
        this.config = config;
    }
    
    public void setCurrentCluster(Cluster parea) {
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
                    
                    Cluster cluster = currentPArea.get();

                    loadStatusDialog = LoadStatusDialog.display(null,
                            String.format("Creating %s Tribal Abstraction Network (TAN)", cluster.getName()),
                            new LoadStatusDialog.LoadingDialogClosedListener() {

                            @Override
                            public void dialogClosed() {
                                doLoad = false;
                            }
                        });
                    
                    TribalAbstractionNetworkGenerator generator = new TribalAbstractionNetworkGenerator();
                    
                    TribalAbstractionNetwork tan = generator.deriveTANFrom(cluster.getHierarchy());

                    SwingUtilities.invokeLater(() -> {
                        if (doLoad) {
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