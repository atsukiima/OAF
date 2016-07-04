package edu.njit.cs.saboc.blu.sno.gui.gep.panels.optionbuttons.pareataxonomy;

import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.AggregatePArea;
import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.AggregatePAreaTaxonomyGenerator;
import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.PAreaTaxonomy;
import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.PAreaTaxonomyGenerator;
import edu.njit.cs.saboc.blu.core.gui.dialogs.LoadStatusDialog;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.optionbuttons.ExpandAggregateButton;
import edu.njit.cs.saboc.blu.sno.gui.abnselection.SCTDisplayFrameListener;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy.configuration.SCTPAreaTaxonomyConfiguration;
import java.util.Optional;
import javax.swing.SwingUtilities;

/**
 *
 * @author Chris O
 */
public class SCTCreateExpandedSubtaxonomyButton extends ExpandAggregateButton {

    private Optional<AggregatePArea> currentPArea = Optional.empty();
    
    private final SCTPAreaTaxonomyConfiguration config;

    public SCTCreateExpandedSubtaxonomyButton(SCTPAreaTaxonomyConfiguration config) {
        super("Partial-area Subtaxonomy", "Partial-area");
        
        this.config = config;
    }
    
    public void setCurrentPArea(AggregatePArea parea) {
        currentPArea = Optional.ofNullable(parea);
    }
    
    @Override
    public void expandAggregateAction() {
        if (currentPArea.isPresent() && !currentPArea.get().getAggregatedNodes().isEmpty()) {
            Thread loadThread = new Thread(new Runnable() {

                private LoadStatusDialog loadStatusDialog = null;
                private boolean doLoad = true;

                public void run() {
                    SCTDisplayFrameListener displayListener = config.getUIConfiguration().getDisplayFrameListener();

                    AggregatePArea parea = currentPArea.get();

                    loadStatusDialog = LoadStatusDialog.display(null,
                            String.format("Creating %s Expanded Partial-area Subtaxonomy", parea.getName()),
                            new LoadStatusDialog.LoadingDialogClosedListener() {

                            @Override
                            public void dialogClosed() {
                                doLoad = false;
                            }
                        });
                    
                    PAreaTaxonomyGenerator taxonomyGenerator = new PAreaTaxonomyGenerator();
                    AggregatePAreaTaxonomyGenerator aggregateGenerator = new AggregatePAreaTaxonomyGenerator();
                    
                    PAreaTaxonomy expandedSubtaxonomy = aggregateGenerator.createExpandedSubtaxonomy(parea, taxonomyGenerator);
                    
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            if (doLoad) {
                                displayListener.addNewPAreaGraphFrame(expandedSubtaxonomy, true);

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
