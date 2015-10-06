package edu.njit.cs.saboc.blu.sno.gui.gep.panels.optionbuttons;

import SnomedShared.Concept;
import edu.njit.cs.saboc.blu.core.gui.dialogs.LoadStatusDialog;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.optionbuttons.OpenBrowserButton;
import edu.njit.cs.saboc.blu.sno.gui.abnselection.SCTDisplayFrameListener;
import edu.njit.cs.saboc.blu.sno.sctdatasource.SCTDataSource;
import java.util.Optional;
import javax.swing.SwingUtilities;

/**
 *
 * @author Chris O
 */
public class SCTOpenBrowserButton extends OpenBrowserButton {

    private Optional<Concept> currentRootConcept = Optional.empty();

    private final SCTDisplayFrameListener displayListener;
    
    private final SCTDataSource dataSource;
    
    public SCTOpenBrowserButton(SCTDataSource dataSource, String groupType, SCTDisplayFrameListener displayListener) {
        super(String.format("View %s's root in NAT concept browser.", groupType));
        
        this.dataSource = dataSource;
        this.displayListener = displayListener;
    }
    
    public void setCurrentRootConcept(Concept root) {
        currentRootConcept = Optional.ofNullable(root);
    }
    
    public void displayBrowserWindowAction() {
        if (currentRootConcept.isPresent()) {
            Thread loadThread = new Thread(new Runnable() {

                private LoadStatusDialog loadStatusDialog = null;
                private boolean doLoad = true;

                public void run() {

                    loadStatusDialog = LoadStatusDialog.display(null,
                            String.format("Opening SNOMED CT NAT Browser to %s", currentRootConcept.get().getName()),
                            new LoadStatusDialog.LoadingDialogClosedListener() {

                            @Override
                            public void dialogClosed() {
                                doLoad = false;
                            }
                        });

                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            if (doLoad) {
                                displayListener.addNewBrowserFrame(currentRootConcept.get(), dataSource);

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
