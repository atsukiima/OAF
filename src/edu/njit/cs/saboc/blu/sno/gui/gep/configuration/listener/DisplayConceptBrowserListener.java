package edu.njit.cs.saboc.blu.sno.gui.gep.configuration.listener;

import edu.njit.cs.saboc.blu.core.gui.dialogs.LoadStatusDialog;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.listeners.EntitySelectionAdapter;
import edu.njit.cs.saboc.blu.sno.gui.abnselection.SCTDisplayFrameListener;
import edu.njit.cs.saboc.blu.sno.localdatasource.concept.SCTConcept;
import edu.njit.cs.saboc.blu.sno.sctdatasource.SCTRelease;
import javax.swing.SwingUtilities;

/**
 *
 * @author Chris O
 */
public class DisplayConceptBrowserListener extends EntitySelectionAdapter<SCTConcept> {

    private final SCTDisplayFrameListener displayListener;
    
    private final SCTRelease dataSource;
    
    public DisplayConceptBrowserListener(SCTDisplayFrameListener displayListener, SCTRelease dataSource) {
        this.displayListener = displayListener;
        this.dataSource = dataSource;
    }
    
    @Override
    public void entityDoubleClicked(SCTConcept concept) {
        Thread loadThread = new Thread(new Runnable() {

            private LoadStatusDialog loadStatusDialog = null;
            private boolean doLoad = true;

            public void run() {

                loadStatusDialog = LoadStatusDialog.display(null,
                        String.format("Opening SNOMED CT NAT Browser to %s", concept.getName()),
                        new LoadStatusDialog.LoadingDialogClosedListener() {

                            @Override
                            public void dialogClosed() {
                                doLoad = false;
                            }
                        });

                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {

                        if (doLoad) {
                            // TODO: Reneable browser functionality
                            //displayListener.addNewBrowserFrame(concept, dataSource);

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
