/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.njit.cs.saboc.blu.sno.gui.abnselection;

import edu.njit.cs.saboc.blu.core.abn.PartitionedAbstractionNetwork;
import edu.njit.cs.saboc.blu.core.abn.tan.ClusterTribalAbstractionNetwork;
import edu.njit.cs.saboc.blu.core.abn.tan.TANFactory;
import edu.njit.cs.saboc.blu.core.abn.tan.TribalAbstractionNetworkGenerator;
import edu.njit.cs.saboc.blu.core.datastructure.hierarchy.Hierarchy;
import edu.njit.cs.saboc.blu.core.gui.dialogs.LoadStatusDialog;
import edu.njit.cs.saboc.blu.sno.abn.tan.SCTStatedTANFactory;
import edu.njit.cs.saboc.blu.sno.localdatasource.concept.SCTConcept;
import edu.njit.cs.saboc.blu.sno.sctdatasource.SCTRelease;
import edu.njit.cs.saboc.blu.sno.sctdatasource.SCTReleaseWithStated;
import javax.swing.JFrame;

/**
 *
 * @author Kevyn
 */
public class ShowTANSelection extends ShowAbNSelection {

    public ShowTANSelection(JFrame parentFrame, 
            String text, 
            LoadReleasePanel localReleasePanel, 
            DummyConcept root, 
            boolean useStatedRelationships, 
            SCTAbNFrameManager displayFrameListener) {
        
        super(parentFrame, text, localReleasePanel, root, useStatedRelationships, displayFrameListener);
    }

    @Override
    public void loadDataSource(boolean doLoad, LoadStatusDialog loadStatusDialog) {
        try {
            SCTRelease dataSource = localReleasePanel.getLoadedDataSource();

            Hierarchy<SCTConcept> hierarchy;
            
            TANFactory factory;

            if (useStatedRelationships) {
                SCTReleaseWithStated statedDataSource = (SCTReleaseWithStated) dataSource;
                hierarchy = statedDataSource.getStatedHierarchy().getSubhierarchyRootedAt(dataSource.getConceptFromId(root.getID()));
                factory = new SCTStatedTANFactory(statedDataSource);
            } else {
                hierarchy = dataSource.getConceptHierarchy().getSubhierarchyRootedAt(dataSource.getConceptFromId(root.getID()));
                
                factory = new TANFactory(dataSource);
            }

            TribalAbstractionNetworkGenerator generator = new TribalAbstractionNetworkGenerator();

            ClusterTribalAbstractionNetwork tan = generator.deriveTANFromSingleRootedHierarchy(
                    hierarchy, 
                    factory);

            doLater(doLoad, loadStatusDialog, tan);

        } catch (NoSCTDataSourceLoadedException e) {
            // TODO: Show error...

        }
    }

    @Override
    protected void displayFrame(PartitionedAbstractionNetwork taxonomy) {
        displayFrameListener.displayTribalAbstractionNetwork((ClusterTribalAbstractionNetwork) taxonomy);
    }

}
