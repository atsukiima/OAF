package edu.njit.cs.saboc.blu.sno.gui.gep.panels.optionbuttons.pareataxonomy;

import SnomedShared.Concept;
import edu.njit.cs.saboc.blu.core.gui.dialogs.LoadStatusDialog;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.optionbuttons.CreateTANButton;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTAggregatePArea;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTArea;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTPArea;
import edu.njit.cs.saboc.blu.sno.abn.tan.SCTTribalAbstractionNetworkGenerator;
import edu.njit.cs.saboc.blu.sno.abn.tan.local.SCTTribalAbstractionNetwork;
import edu.njit.cs.saboc.blu.sno.datastructure.hierarchy.SCTMultiRootedConceptHierarchy;
import edu.njit.cs.saboc.blu.sno.gui.abnselection.SCTDisplayFrameListener;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.pareataxonomy.configuration.SCTPAreaTaxonomyConfiguration;
import edu.njit.cs.saboc.blu.sno.sctdatasource.SCTLocalDataSource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import javax.swing.SwingUtilities;

/**
 *
 * @author Chris O
 */
public class SCTCreateTANFromAreaButton extends CreateTANButton {

    private Optional<SCTArea> currentArea = Optional.empty();
    
    private final SCTPAreaTaxonomyConfiguration config;

    public SCTCreateTANFromAreaButton(SCTPAreaTaxonomyConfiguration config) {
        super("area");
        
        this.config = config;
    }
        
    public void setCurrentArea(SCTArea area) {
        currentArea = Optional.ofNullable(area);
    }
    
    @Override
    public void deriveTANAction() {
        if (currentArea.isPresent()) {

            Thread loadThread = new Thread(new Runnable() {

                private LoadStatusDialog loadStatusDialog = null;
                private boolean doLoad = true;

                public void run() {
                    SCTDisplayFrameListener displayListener = config.getUIConfiguration().getDisplayFrameListener();

                    SCTArea area = currentArea.get();

                    loadStatusDialog = LoadStatusDialog.display(null,
                            String.format("Creating %s Tribal Abstraction Network (TAN)", config.getTextConfiguration().getContainerName(area)),
                            new LoadStatusDialog.LoadingDialogClosedListener() {

                            @Override
                            public void dialogClosed() {
                                doLoad = false;
                            }
                        });
                    
                    HashSet<Concept> patriarchs = new HashSet<>();
                    
                    ArrayList<SCTPArea> pareas = area.getAllPAreas();
                    
                    pareas.forEach((SCTPArea parea) -> {
                        patriarchs.add(parea.getRoot());
                    });
                    
                    SCTMultiRootedConceptHierarchy hierarchy = new SCTMultiRootedConceptHierarchy(patriarchs);
                    
                    pareas.forEach((SCTPArea parea) -> {
                        if(config.getDataConfiguration().getPAreaTaxonomy().isReduced()) {
                            SCTAggregatePArea aggregatePArea = (SCTAggregatePArea)parea;
                            
                            hierarchy.addAllHierarchicalRelationships(config.getDataConfiguration().getAggregatedPAreaHierarchy(aggregatePArea));
                        } else {
                            hierarchy.addAllHierarchicalRelationships(parea.getHierarchy());
                        }
                    });
                    
                    SCTTribalAbstractionNetworkGenerator generator = new SCTTribalAbstractionNetworkGenerator(
                            config.getTextConfiguration().getContainerName(area), 
                            (SCTLocalDataSource)config.getDataConfiguration().getPAreaTaxonomy().getDataSource());
  
                    SCTTribalAbstractionNetwork tan = generator.deriveTANFromMultiRootedHierarchy(hierarchy);

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
