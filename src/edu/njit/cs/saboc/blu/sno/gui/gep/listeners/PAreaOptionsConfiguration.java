package edu.njit.cs.saboc.blu.sno.gui.gep.listeners;

import edu.njit.cs.saboc.blu.core.gui.gep.panels.GroupOptionsPanelActionListener;
import edu.njit.cs.saboc.blu.core.gui.gep.panels.GroupOptionsPanelConfiguration;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTPArea;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTPAreaTaxonomy;
import edu.njit.cs.saboc.blu.sno.abn.tan.TANGenerator;
import edu.njit.cs.saboc.blu.sno.abn.tan.TribalAbstractionNetwork;
import edu.njit.cs.saboc.blu.sno.datastructure.hierarchy.SCTConceptHierarchy;
import edu.njit.cs.saboc.blu.sno.gui.abnselection.SCTDisplayFrameListener;
import edu.njit.cs.saboc.blu.sno.gui.dialogs.ConceptGroupDetailsDialog;
import edu.njit.cs.saboc.blu.sno.gui.graphframe.PAreaInternalGraphFrame;
import javax.swing.JFrame;
import javax.swing.SwingWorker;

/**
 *
 * @author Chris
 */
public class PAreaOptionsConfiguration extends GroupOptionsPanelConfiguration {

    public PAreaOptionsConfiguration(
            final JFrame parentFrame, 
            final PAreaInternalGraphFrame graphFrame,
            final SCTPAreaTaxonomy taxonomy, 
            final SCTDisplayFrameListener displayListener) {
        
        super.enableButtonWithAction(0, new GroupOptionsPanelActionListener<SCTPArea>() {
            public void actionPerformedOn(SCTPArea parea) {
                
                ConceptGroupDetailsDialog dialog = new ConceptGroupDetailsDialog(parea, taxonomy,
                            ConceptGroupDetailsDialog.DialogType.PartialArea, displayListener);
            }
        });
        
        super.enableButtonWithAction(1, new GroupOptionsPanelActionListener<SCTPArea>() {
            public void actionPerformedOn(SCTPArea parea) {
                displayListener.addNewBrowserFrame(parea.getRoot(), taxonomy.getDataSource());
            }
        });
        
        super.enableButtonWithAction(2, new GroupOptionsPanelActionListener<SCTPArea>() {
            public void actionPerformedOn(final SCTPArea parea) {
                final SwingWorker t = new SwingWorker() {

                    public Object doInBackground() {
                       
                        SCTPAreaTaxonomy subtaxonomy = taxonomy.getRootSubtaxonomy(parea);
                        
                        displayListener.addNewPAreaGraphFrame(
                                subtaxonomy,
                                true,
                                false);

                        return new Object();
                    }
                };
                
                t.execute();
            }
        });
        
        super.enableButtonWithAction(3, new GroupOptionsPanelActionListener<SCTPArea>() {
            public void actionPerformedOn(SCTPArea parea) {
                graphFrame.viewInTextBrowser(parea);
            }
        });
        
        super.enableButtonWithAction(4, new GroupOptionsPanelActionListener<SCTPArea>() {
            public void actionPerformedOn(SCTPArea parea) {
                SCTConceptHierarchy hierarchy = taxonomy.getDataSource().getPAreaConceptHierarchy(taxonomy, parea);

                TribalAbstractionNetwork chd = TANGenerator.createTANFromConceptHierarchy(
                        parea.getRoot(),
                        taxonomy.getSCTVersion(),
                        hierarchy);

                displayListener.addNewClusterGraphFrame(chd, true, false);
            }
        });
    }
}
