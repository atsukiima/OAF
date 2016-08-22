package edu.njit.cs.saboc.blu.sno.gui.abnselection;

import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.PAreaTaxonomy;
import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.PAreaTaxonomyGenerator;
import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.diff.DiffPAreaTaxonomy;
import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.diff.DiffPAreaTaxonomyFactory;
import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.diff.DiffPAreaTaxonomyGenerator;
import edu.njit.cs.saboc.blu.core.datastructure.hierarchy.Hierarchy;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.SCTInferredPAreaTaxonomyFactory;
import edu.njit.cs.saboc.blu.sno.gui.abnselection.LoadReleasePanel.LocalDataSourceListener;
import edu.njit.cs.saboc.blu.sno.localdatasource.concept.SCTConcept;
import edu.njit.cs.saboc.blu.sno.sctdatasource.SCTRelease;
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
    
    public DiffTaxonomyPanel(SCTDisplayFrameListener displayListener) {
        this.displayListener = displayListener;
        
        this.setLayout(new GridLayout(1, 3));

        this.loadFromPanel = new LoadReleasePanel();
        
        loadFromPanel.setBorder(BorderFactory.createTitledBorder("Select \"FROM\" SNOMED CT Release"));
        
        loadFromPanel.addLocalDataSourceLoadedListener(new LocalDataSourceListener() {

            @Override
            public void localDataSourceLoaded(SCTRelease dataSource) {

                SCTRelease theToRelease = toRelease.get();
                SCTRelease fromRelease = dataSource;

                //SCTConcept specimen = theToRelease.getConceptFromId(123038009); // Specimen
                
                SCTConcept bid = theToRelease.getConceptFromId(87628006); // Bacterial infectious disease
                
                //SCTConcept infectiousDisease = theToRelease.getConceptFromId(40733004); // Bacterial infectious disease

                Hierarchy<SCTConcept> fromHierarchy = fromRelease.getConceptHierarchy().getSubhierarchyRootedAt(bid);

                PAreaTaxonomyGenerator generator = new PAreaTaxonomyGenerator();

                PAreaTaxonomy fromTaxonomy = generator.derivePAreaTaxonomy(
                        new SCTInferredPAreaTaxonomyFactory(fromHierarchy),
                        fromHierarchy);
                
                
                Hierarchy<SCTConcept> toHierarchy = theToRelease.getConceptHierarchy().getSubhierarchyRootedAt(bid);
                
                PAreaTaxonomy toTaxonomy = generator.derivePAreaTaxonomy(
                        new SCTInferredPAreaTaxonomyFactory(toHierarchy),
                        toHierarchy);

                DiffPAreaTaxonomyGenerator diffTaxonomyGenerator = new DiffPAreaTaxonomyGenerator();

                DiffPAreaTaxonomy diffTaxonomy
                        =  diffTaxonomyGenerator.createDiffPAreaTaxonomy(
                                new DiffPAreaTaxonomyFactory(),
                                fromRelease,
                                fromTaxonomy,
                                theToRelease,
                                toTaxonomy);

                SwingUtilities.invokeLater(() -> {
                    displayListener.addNewDiffPAreaTaxonomyGraphFrame(diffTaxonomy);
                });
            }

            @Override
            public void dataSourceLoading() {
                
            }

            @Override
            public void localDataSourceUnloaded() {
                
            }
            
        });
        
        this.add(loadFromPanel);
        
        this.add(new JPanel());
        this.add(new JPanel());
    }
    
    public void setCurrentRelease(SCTRelease release) {
        this.toRelease = Optional.of(release);
    }
    
    public void clearCurrentRelease() {
        this.toRelease = Optional.empty();
    }
}
