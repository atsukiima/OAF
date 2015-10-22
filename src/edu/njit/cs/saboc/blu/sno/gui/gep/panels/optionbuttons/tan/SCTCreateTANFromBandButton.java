package edu.njit.cs.saboc.blu.sno.gui.gep.panels.optionbuttons.tan;

import SnomedShared.Concept;
import SnomedShared.overlapping.CommonOverlapSet;
import edu.njit.cs.saboc.blu.core.gui.dialogs.LoadStatusDialog;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.optionbuttons.CreateTANButton;
import edu.njit.cs.saboc.blu.sno.abn.tan.TANGenerator;
import edu.njit.cs.saboc.blu.sno.abn.tan.local.SCTCluster;
import edu.njit.cs.saboc.blu.sno.abn.tan.local.SCTTribalAbstractionNetwork;
import edu.njit.cs.saboc.blu.sno.datastructure.hierarchy.SCTMultiRootedConceptHierarchy;
import edu.njit.cs.saboc.blu.sno.gui.abnselection.SCTDisplayFrameListener;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.tan.configuration.SCTTANConfiguration;
import edu.njit.cs.saboc.blu.sno.sctdatasource.SCTLocalDataSource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import javax.swing.SwingUtilities;

/**
 *
 * @author Chris O
 */
public class SCTCreateTANFromBandButton extends CreateTANButton {

    private Optional<CommonOverlapSet> currentBand = Optional.empty();
    
    private final SCTTANConfiguration config;

    public SCTCreateTANFromBandButton(SCTTANConfiguration config) {
        super("band");
        
        this.config = config;
    }
        
    public void setCurrentBand(CommonOverlapSet band) {
        currentBand = Optional.ofNullable(band);
    }
    
    @Override
    public void deriveTANAction() {
        if (currentBand.isPresent()) {

            Thread loadThread = new Thread(new Runnable() {

                private LoadStatusDialog loadStatusDialog = null;
                private boolean doLoad = true;

                public void run() {
                    SCTDisplayFrameListener displayListener = config.getUIConfiguration().getDisplayFrameListener();

                    CommonOverlapSet band = currentBand.get();

                    loadStatusDialog = LoadStatusDialog.display(null,
                            String.format("Creating %s Tribal Abstraction Network (TAN)", config.getTextConfiguration().getContainerName(band)),
                            new LoadStatusDialog.LoadingDialogClosedListener() {

                            @Override
                            public void dialogClosed() {
                                doLoad = false;
                            }
                        });
                    
                    HashSet<Concept> patriarchs = new HashSet<>();
                    
                    ArrayList<SCTCluster> clusters = config.getDataConfiguration().convertClusterSummaryList(band.getAllClusters());
                    
                    clusters.forEach((SCTCluster cluster) -> {
                        patriarchs.add(cluster.getRoot());
                    });
                    
                    SCTMultiRootedConceptHierarchy hierarchy = new SCTMultiRootedConceptHierarchy(patriarchs);
                    
                    clusters.forEach((SCTCluster cluster) -> {
                        hierarchy.addAllHierarchicalRelationships(cluster.getConceptHierarchy());
                    });
  
                    SCTTribalAbstractionNetwork tan = TANGenerator.deriveTANFromMultiRootedHierarchy(
                            config.getTextConfiguration().getContainerName(band),
                            hierarchy, 
                            (SCTLocalDataSource)config.getDataConfiguration().getTribalAbstractionNetwork().getDataSource(),  
                            config.getDataConfiguration().getTribalAbstractionNetwork().getSCTVersion());

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