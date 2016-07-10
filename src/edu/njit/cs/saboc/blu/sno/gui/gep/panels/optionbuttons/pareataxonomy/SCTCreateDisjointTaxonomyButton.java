package edu.njit.cs.saboc.blu.sno.gui.gep.panels.optionbuttons.pareataxonomy;

import edu.njit.cs.saboc.blu.core.abn.disjoint.DisjointAbstractionNetwork;
import edu.njit.cs.saboc.blu.core.abn.node.Node;
import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.Area;
import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.PArea;
import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.PAreaTaxonomy;
import edu.njit.cs.saboc.blu.core.gui.dialogs.LoadStatusDialog;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.optionbuttons.CreateDisjointAbNButton;
import edu.njit.cs.saboc.blu.sno.gui.abnselection.SCTDisplayFrameListener;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy.configuration.SCTPAreaTaxonomyConfiguration;
import java.util.Optional;
import javax.swing.SwingUtilities;

/**
 *
 * @author Chris O
 */
public class SCTCreateDisjointTaxonomyButton extends CreateDisjointAbNButton {

    private Optional<Area> currentArea = Optional.empty();
    
    private final SCTPAreaTaxonomyConfiguration config;

    public SCTCreateDisjointTaxonomyButton(SCTPAreaTaxonomyConfiguration config) {
        super("Create Disjoint Partial-area Taxonomy for Selected Area");
        
        this.config = config;
    }

    @Override
    public void setEnabledFor(Node node) {
        Area area = (Area)node;
        
        this.setEnabled(area.hasOverlappingConcepts());
    }
    
    @Override
    public void createDisjointAbNAction() {
        if (currentArea.isPresent()) {
            Thread loadThread = new Thread(new Runnable() {

                private LoadStatusDialog loadStatusDialog = null;
                private boolean doLoad = true;

                public void run() {
                    SCTDisplayFrameListener displayListener = config.getUIConfiguration().getDisplayFrameListener();

                    Area area = currentArea.get();

                    loadStatusDialog = LoadStatusDialog.display(null,
                            String.format("Creating %s Disjoint Partial-area Taxonomy", area.getName()),
                            new LoadStatusDialog.LoadingDialogClosedListener() {

                            @Override
                            public void dialogClosed() {
                                doLoad = false;
                            }
                        });

                    DisjointAbstractionNetwork<PAreaTaxonomy, PArea> disjointTaxonomy = config.getDisjointAbstractionNetworkFor(area);

                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            if (doLoad) {
                                displayListener.addNewDisjointPAreaTaxonomyGraphFrame(disjointTaxonomy);

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