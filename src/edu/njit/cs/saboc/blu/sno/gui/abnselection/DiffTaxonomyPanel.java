package edu.njit.cs.saboc.blu.sno.gui.abnselection;

import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.PAreaTaxonomy;
import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.PAreaTaxonomyGenerator;
import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.diff.DiffPAreaTaxonomy;
import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.diff.DiffPAreaTaxonomyFactory;
import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.diff.DiffPAreaTaxonomyGenerator;
import edu.njit.cs.saboc.blu.core.datastructure.hierarchy.Hierarchy;
import edu.njit.cs.saboc.blu.core.gui.dialogs.LoadStatusDialog;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.SCTInferredPAreaTaxonomyFactory;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.diffpareataxonomy.SCTDescriptiveDiffPAreaTaxonomy;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.diffpareataxonomy.SCTDescriptiveDiffPAreaTaxonomyFactory;
import edu.njit.cs.saboc.blu.sno.descriptivedelta.DescriptiveDelta;
import edu.njit.cs.saboc.blu.sno.descriptivedelta.derivation.DeltaRelationshipLoader;
import edu.njit.cs.saboc.blu.sno.descriptivedelta.derivation.DeltaRelationships;
import edu.njit.cs.saboc.blu.sno.descriptivedelta.derivation.DescriptiveDeltaGenerator;
import edu.njit.cs.saboc.blu.sno.gui.abnselection.LoadReleasePanel.LocalDataSourceListener;
import edu.njit.cs.saboc.blu.sno.localdatasource.concept.SCTConcept;
import edu.njit.cs.saboc.blu.sno.sctdatasource.SCTRelease;
import edu.njit.cs.saboc.blu.sno.sctdatasource.SCTReleaseInfo;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.Optional;
import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 *
 * @author Chris O
 */
public class DiffTaxonomyPanel extends JPanel {
    
    private final LoadReleasePanel loadFromPanel;
    
    private Optional<SCTRelease> toRelease = Optional.empty();
    
    private final SCTDisplayFrameListener displayListener;
    
    private final SubjectAbstractionNetworkPanel subjectSelectionPanel;
    
    private final JCheckBox createDescriptiveDeltaBox;
    
    public DiffTaxonomyPanel(SCTDisplayFrameListener displayListener) {
        this.displayListener = displayListener;
        
        this.setLayout(new GridLayout(1, 2));

        this.loadFromPanel = new LoadReleasePanel();
        
        loadFromPanel.setBorder(BorderFactory.createTitledBorder("Select \"FROM\" SNOMED CT Release"));
        
        this.subjectSelectionPanel = new SubjectAbstractionNetworkPanel("Diff Partial-area Taxonomy", (dummyRoot, useStated) -> {
            
            Thread loadThread = new Thread(new Runnable() {
                private LoadStatusDialog loadStatusDialog = null;
                private boolean doLoad = true;

                public void run() {

                    loadStatusDialog = LoadStatusDialog.display(null, String.format("Creating the %s Diff Partial-area Taxonomy.", 
                            dummyRoot.getName()),
                            
                            new LoadStatusDialog.LoadingDialogClosedListener() {

                                @Override
                                public void dialogClosed() {
                                    doLoad = false;
                                }
                            });
                    try {
                        SCTRelease theToRelease = toRelease.get();

                        SCTRelease fromRelease = loadFromPanel.getLoadedDataSource();

                        // TODO: What if the concept doesn't exist in FROM?
                        SCTConcept root = theToRelease.getConceptFromId(dummyRoot.getID()); 
     
                        Hierarchy<SCTConcept> fromHierarchy = fromRelease.getConceptHierarchy().getSubhierarchyRootedAt(root);

                        PAreaTaxonomyGenerator generator = new PAreaTaxonomyGenerator();

                        PAreaTaxonomy fromTaxonomy = generator.derivePAreaTaxonomy(
                                new SCTInferredPAreaTaxonomyFactory(fromHierarchy),
                                fromHierarchy);

                        Hierarchy<SCTConcept> toHierarchy = theToRelease.getConceptHierarchy().getSubhierarchyRootedAt(root);

                        PAreaTaxonomy toTaxonomy = generator.derivePAreaTaxonomy(
                                new SCTInferredPAreaTaxonomyFactory(toHierarchy),
                                toHierarchy);

                        DiffPAreaTaxonomyGenerator diffTaxonomyGenerator = new DiffPAreaTaxonomyGenerator();
                        
                        DiffPAreaTaxonomy diffTaxonomy;
                        
                        if (createDescriptiveDeltaBox.isSelected()) {
                            DeltaRelationshipLoader deltaRelLoader = new DeltaRelationshipLoader();
                            
                            DeltaRelationships deltaRelationships = deltaRelLoader.loadDeltaRelationships(theToRelease);
                            
                            DescriptiveDeltaGenerator descriptiveDeltaGenerator = new DescriptiveDeltaGenerator();
                            
                            DescriptiveDelta descriptiveDelta = descriptiveDeltaGenerator.createDescriptiveDelta(
                                    fromRelease, 
                                    theToRelease, 
                                    root, 
                                    deltaRelationships);
                                                    
                            diffTaxonomy = diffTaxonomyGenerator.createDiffPAreaTaxonomy(
                                            new SCTDescriptiveDiffPAreaTaxonomyFactory(
                                                    fromRelease,
                                                    theToRelease,
                                                    fromTaxonomy,
                                                    toTaxonomy,
                                                    descriptiveDelta),
                                    
                                            fromRelease,
                                            fromTaxonomy,
                                            theToRelease,
                                            toTaxonomy);
                            
                            SCTDescriptiveDiffPAreaTaxonomy descriptiveDiffTaxonomy = (SCTDescriptiveDiffPAreaTaxonomy)diffTaxonomy;
                            
                        } else {
                            /*
                            diffTaxonomy
                                    = diffTaxonomyGenerator.createDiffPAreaTaxonomy(
                                            new DiffPAreaTaxonomyFactory(),
                                            fromRelease,
                                            fromTaxonomy,
                                            theToRelease,
                                            toTaxonomy);
                            
                            */
                            
                            diffTaxonomy = null;
                        }

                        if (doLoad) {
                            SwingUtilities.invokeLater(() -> {
                                displayListener.addNewDiffPAreaTaxonomyGraphFrame(diffTaxonomy);
                                
                                loadStatusDialog.setVisible(false);
                            });
                        }

                    } catch (NoSCTDataSourceLoadedException e) {
                        // TODO: Show error...

                    }
                }
            });

            loadThread.start();
        });
        
        this.subjectSelectionPanel.setEnabled(false);
        
        loadFromPanel.addLocalDataSourceLoadedListener(new LocalDataSourceListener() {

            @Override
            public void localDataSourceLoaded(SCTRelease dataSource) {
                subjectSelectionPanel.setCurrentRelease(toRelease.get());
                subjectSelectionPanel.setEnabled(true);
                
                createDescriptiveDeltaBox.setSelected(false);
                
                createDescriptiveDeltaBox.setEnabled(canDeriveDescriptiveDelta(dataSource, toRelease.get()));
            }

            @Override
            public void dataSourceLoading() {
                
            }

            @Override
            public void localDataSourceUnloaded() {
                subjectSelectionPanel.clearCurrentRelease();
                subjectSelectionPanel.setEnabled(false);
                createDescriptiveDeltaBox.setEnabled(false);
                createDescriptiveDeltaBox.setSelected(false);
            }
            
        });
        
        createDescriptiveDeltaBox = new JCheckBox("Integrate Descriptive Delta (requires subsequent international releases)");
        createDescriptiveDeltaBox.setEnabled(false);
        
        JPanel releaseSelectionPanel = new JPanel(new BorderLayout()); 
        
        releaseSelectionPanel.add(loadFromPanel, BorderLayout.CENTER);
        releaseSelectionPanel.add(createDescriptiveDeltaBox, BorderLayout.SOUTH);
        
        
        this.add(releaseSelectionPanel);
        
        this.add(subjectSelectionPanel);
    }
    
    public void setCurrentRelease(SCTRelease release) {
        this.toRelease = Optional.of(release);
    }
    
    public void clearCurrentRelease() {
        this.toRelease = Optional.empty();
    }
    
    /**
     * Both releases must be international releases and the releases have to be subsequence to each other
     * 
     * @param fromRelease
     * @param toRelease
     * @return 
     */
    private boolean canDeriveDescriptiveDelta(SCTRelease fromRelease, SCTRelease toRelease) {
        
        if (toRelease.getReleaseInfo().getReleaseFormat() == SCTReleaseInfo.ReleaseFormat.RF2 && 
                fromRelease.getReleaseInfo().getReleaseType() == SCTReleaseInfo.ReleaseType.International && 
                toRelease.getReleaseInfo().getReleaseType() == SCTReleaseInfo.ReleaseType.International) {
            
            if (fromRelease.getReleaseInfo().getReleaseYear() == toRelease.getReleaseInfo().getReleaseYear()) {
                return fromRelease.getReleaseInfo().getReleaseMonth() == 1 && toRelease.getReleaseInfo().getReleaseMonth() == 7;
            } else {
                if (fromRelease.getReleaseInfo().getReleaseYear() == toRelease.getReleaseInfo().getReleaseYear() - 1) {
                    return fromRelease.getReleaseInfo().getReleaseMonth() == 7 && toRelease.getReleaseInfo().getReleaseMonth() == 1;
                }
            }
        }

        return false;
    }
}
