package edu.njit.cs.saboc.blu.sno.gui.gep.panels.optionbuttons;

import edu.njit.cs.saboc.blu.core.gui.dialogs.LoadStatusDialog;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.optionbuttons.CreateDisjointAbNButton;
import edu.njit.cs.saboc.blu.sno.abn.disjointpareataxonomy.DisjointPAreaTaxonomy;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTArea;
import edu.njit.cs.saboc.blu.sno.gui.abnselection.SCTDisplayFrameListener;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.SCTPAreaTaxonomyConfiguration;
import java.util.Optional;
import javax.swing.SwingUtilities;

/**
 *
 * @author Chris O
 */
public class SCTCreateDisjointTaxonomyButton extends CreateDisjointAbNButton {

    
    private Optional<SCTArea> currentArea = Optional.empty();
    
    private final SCTPAreaTaxonomyConfiguration config;

    public SCTCreateDisjointTaxonomyButton(SCTPAreaTaxonomyConfiguration config) {
        super("Create Disjoint Partial-area Taxonomy for Selected Area");
        
        this.config = config;
    }
        
    public void setCurrentArea(SCTArea area) {
        currentArea = Optional.ofNullable(area);
    }
    
    @Override
    public void createDisjointAbNAction() {
        if (currentArea.isPresent()) {

            Thread loadThread = new Thread(new Runnable() {

                private LoadStatusDialog loadStatusDialog = null;

                public void run() {
                    SCTDisplayFrameListener displayListener = config.getDisplayListener();

                    SCTArea area = currentArea.get();

                    loadStatusDialog = LoadStatusDialog.display(null,
                            String.format("Creating %s Disjoint Partial-area Taxonomy", config.getContainerName(area)));

                    DisjointPAreaTaxonomy disjointTaxonomy = config.createDisjointAbN(area);

                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            displayListener.addNewDisjointPAreaTaxonomyGraphFrame(disjointTaxonomy);

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