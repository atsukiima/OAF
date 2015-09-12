package edu.njit.cs.saboc.blu.sno.gui.gep.configuration.listener;

import SnomedShared.Concept;
import edu.njit.cs.saboc.blu.core.gui.dialogs.LoadStatusDialog;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.listeners.EntitySelectionAdapter;
import edu.njit.cs.saboc.blu.sno.conceptbrowser.InternalConceptBrowserFrame;
import edu.njit.cs.saboc.blu.sno.gui.abnselection.SCTDisplayFrameListener;
import edu.njit.cs.saboc.blu.sno.sctdatasource.SCTDataSource;
import javax.swing.SwingUtilities;

/**
 *
 * @author Chris O
 */
public class DisplayConceptBrowserListener extends EntitySelectionAdapter<Concept> {

    private final SCTDisplayFrameListener displayListener;
    
    private final SCTDataSource dataSource;
    
    public DisplayConceptBrowserListener(SCTDisplayFrameListener displayListener, SCTDataSource dataSource) {
        this.displayListener = displayListener;
        this.dataSource = dataSource;
    }
    
    @Override
    public void entityDoubleClicked(Concept concept) {
        Thread loadThread = new Thread(new Runnable() {

            private LoadStatusDialog loadStatusDialog = null;

            public void run() {

                loadStatusDialog = LoadStatusDialog.display(null,
                        String.format("Opening SNOMED CT NAT Browser to %s", concept.getName()));
                
                displayListener.addNewBrowserFrame(concept, dataSource);

                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        
                        
                        loadStatusDialog.setVisible(false);
                        loadStatusDialog.dispose();
                    }
                });
            }
        });

        loadThread.start();
    }

}
