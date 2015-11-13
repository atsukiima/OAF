package edu.njit.cs.saboc.blu.sno.gui.gep.panels.optionbuttons.pareataxonomy;

import SnomedShared.Concept;
import SnomedShared.pareataxonomy.InheritedRelationship;
import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.AggregatePAreaTaxonomyGenerator;
import edu.njit.cs.saboc.blu.core.gui.dialogs.LoadStatusDialog;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.optionbuttons.ExpandAggregateButton;
import edu.njit.cs.saboc.blu.sno.abn.generator.SCTPAreaTaxonomyGenerator;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTAggregatePArea;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTArea;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTPArea;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTPAreaTaxonomy;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTRegion;
import edu.njit.cs.saboc.blu.sno.datastructure.hierarchy.SCTConceptHierarchy;
import edu.njit.cs.saboc.blu.sno.gui.abnselection.SCTDisplayFrameListener;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy.configuration.SCTPAreaTaxonomyConfiguration;
import edu.njit.cs.saboc.blu.sno.localdatasource.load.InferredRelationshipsRetriever;
import java.util.Optional;
import javax.swing.SwingUtilities;

/**
 *
 * @author Chris O
 */
public class SCTCreateExpandedSubtaxonomyButton extends ExpandAggregateButton {

    private Optional<SCTAggregatePArea> currentPArea = Optional.empty();
    
    private final SCTPAreaTaxonomyConfiguration config;

    public SCTCreateExpandedSubtaxonomyButton(SCTPAreaTaxonomyConfiguration config) {
        super("Partial-area Subtaxonomy", "Partial-area");
        
        this.config = config;
    }
    
    public void setCurrentPArea(SCTAggregatePArea parea) {
        currentPArea = Optional.ofNullable(parea);
    }
    
    @Override
    public void expandAggregateAction() {
        if (currentPArea.isPresent() && !currentPArea.get().getAggregatedGroups().isEmpty()) {
            Thread loadThread = new Thread(new Runnable() {

                private LoadStatusDialog loadStatusDialog = null;
                private boolean doLoad = true;

                public void run() {
                    SCTDisplayFrameListener displayListener = config.getUIConfiguration().getDisplayFrameListener();

                    SCTAggregatePArea parea = currentPArea.get();

                    loadStatusDialog = LoadStatusDialog.display(null,
                            String.format("Creating %s Expanded Partial-area Subtaxonomy", config.getTextConfiguration().getGroupName(parea)),
                            new LoadStatusDialog.LoadingDialogClosedListener() {

                            @Override
                            public void dialogClosed() {
                                doLoad = false;
                            }
                        });
                    
                    SCTPAreaTaxonomy sourceTaxonomy = config.getDataConfiguration().getPAreaTaxonomy();
                    
                    SCTPAreaTaxonomyGenerator taxonomyGenerator = new SCTPAreaTaxonomyGenerator(
                            sourceTaxonomy.getSCTRootConcept(), 
                            sourceTaxonomy.getDataSource(), 
                            sourceTaxonomy.getConceptHierarchy(), 
                            new InferredRelationshipsRetriever());

                    AggregatePAreaTaxonomyGenerator<
                            SCTPAreaTaxonomy, 
                            SCTPArea, 
                            SCTArea,
                            SCTRegion, 
                            Concept,
                            InheritedRelationship,
                            SCTConceptHierarchy,
                            SCTPAreaTaxonomy,
                            SCTAggregatePArea> aggregateGenerator = new AggregatePAreaTaxonomyGenerator<>();

                    SCTPAreaTaxonomy expandedSubtaxonomy = aggregateGenerator.createExpandedSubtaxonomy(parea, taxonomyGenerator);
                    
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
