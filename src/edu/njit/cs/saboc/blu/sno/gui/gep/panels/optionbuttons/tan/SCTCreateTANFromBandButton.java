package edu.njit.cs.saboc.blu.sno.gui.gep.panels.optionbuttons.tan;

import SnomedShared.Concept;
import SnomedShared.overlapping.CommonOverlapSet;
import edu.njit.cs.saboc.blu.core.gui.dialogs.LoadStatusDialog;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.details.optionbuttons.CreateTANButton;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTAggregatePArea;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTPArea;
import edu.njit.cs.saboc.blu.sno.abn.tan.TANGenerator;
import edu.njit.cs.saboc.blu.sno.abn.tan.local.SCTCluster;
import edu.njit.cs.saboc.blu.sno.abn.tan.local.SCTTribalAbstractionNetwork;
import edu.njit.cs.saboc.blu.sno.datastructure.hierarchy.SCTMultiRootedConceptHierarchy;
import edu.njit.cs.saboc.blu.sno.gui.abnselection.SCTDisplayFrameListener;
import edu.njit.cs.saboc.blu.sno.gui.gep.panels.SCTTANConfiguration;
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

                public void run() {
                    SCTDisplayFrameListener displayListener = config.getDisplayListener();

                    CommonOverlapSet band = currentBand.get();

                    loadStatusDialog = LoadStatusDialog.display(null,
                            String.format("Creating %s Tribal Abstraction Network (TAN)", config.getContainerName(band)));
                    
                    HashSet<Concept> patriarchs = new HashSet<>();
                    
                    ArrayList<SCTCluster> clusters = config.convertClusterSummaryList(band.getAllClusters());
                    
                    clusters.forEach((SCTCluster cluster) -> {
                        patriarchs.add(cluster.getRoot());
                    });
                    
                    SCTMultiRootedConceptHierarchy hierarchy = new SCTMultiRootedConceptHierarchy(patriarchs);
                    
                    clusters.forEach((SCTCluster cluster) -> {
                        hierarchy.addAllHierarchicalRelationships(cluster.getConceptHierarchy());
                    });
  
                    SCTTribalAbstractionNetwork tan = TANGenerator.deriveTANFromMultiRootedHierarchy(hierarchy, 
                            (SCTLocalDataSource)config.getTribalAbstractionNetwork().getDataSource(),  
                            config.getTribalAbstractionNetwork().getSCTVersion());

                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            
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