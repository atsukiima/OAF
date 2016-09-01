package edu.njit.cs.saboc.blu.sno.gui.abnselection;

import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.PAreaTaxonomy;
import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.PAreaTaxonomyGenerator;
import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.diff.DiffPAreaTaxonomy;
import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.diff.DiffPAreaTaxonomyFactory;
import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.diff.DiffPAreaTaxonomyGenerator;
import edu.njit.cs.saboc.blu.core.abn.tan.ClusterTribalAbstractionNetwork;
import edu.njit.cs.saboc.blu.core.abn.tan.TANFactory;
import edu.njit.cs.saboc.blu.core.abn.tan.TribalAbstractionNetworkGenerator;
import edu.njit.cs.saboc.blu.core.datastructure.hierarchy.Hierarchy;
import edu.njit.cs.saboc.blu.core.gui.dialogs.LoadStatusDialog;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.SCTInferredPAreaTaxonomyFactory;
import edu.njit.cs.saboc.blu.sno.gui.abnselection.LoadReleasePanel.LocalDataSourceListener;
import edu.njit.cs.saboc.blu.sno.localdatasource.concept.SCTConcept;
import edu.njit.cs.saboc.blu.sno.sctdatasource.SCTRelease;
import edu.njit.cs.saboc.blu.sno.sctdatasource.SCTReleaseWithStated;
import java.awt.GridLayout;
import java.util.Optional;
import javax.swing.BorderFactory;
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
    
    public DiffTaxonomyPanel(SCTDisplayFrameListener displayListener) {
        this.displayListener = displayListener;
        
        this.setLayout(new GridLayout(1, 2));

        this.loadFromPanel = new LoadReleasePanel();
        
        loadFromPanel.setBorder(BorderFactory.createTitledBorder("Select \"FROM\" SNOMED CT Release"));
        
        this.subjectSelectionPanel = new SubjectAbstractionNetworkPanel((dummyRoot, useStated) -> {
            
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

                        DiffPAreaTaxonomy diffTaxonomy
                                = diffTaxonomyGenerator.createDiffPAreaTaxonomy(
                                        new DiffPAreaTaxonomyFactory(),
                                        fromRelease,
                                        fromTaxonomy,
                                        theToRelease,
                                        toTaxonomy);

                        if (doLoad) {
                            SwingUtilities.invokeLater(() -> {
                                displayListener.addNewDiffPAreaTaxonomyGraphFrame(diffTaxonomy);
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
            }

            @Override
            public void dataSourceLoading() {
                
            }

            @Override
            public void localDataSourceUnloaded() {
                subjectSelectionPanel.clearCurrentRelease();
                subjectSelectionPanel.setEnabled(false);
            }
            
        });
        
        this.add(loadFromPanel);
        
        this.add(subjectSelectionPanel);
    }
    
    public void setCurrentRelease(SCTRelease release) {
        this.toRelease = Optional.of(release);
    }
    
    public void clearCurrentRelease() {
        this.toRelease = Optional.empty();
    }
}
