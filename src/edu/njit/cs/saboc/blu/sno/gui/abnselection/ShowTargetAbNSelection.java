/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.njit.cs.saboc.blu.sno.gui.abnselection;

import edu.njit.cs.saboc.blu.core.abn.PartitionedAbstractionNetwork;
import edu.njit.cs.saboc.blu.core.abn.targetbased.TargetAbstractionNetwork;
import edu.njit.cs.saboc.blu.core.abn.targetbased.TargetAbstractionNetworkGenerator;
import edu.njit.cs.saboc.blu.core.gui.dialogs.LoadStatusDialog;
import edu.njit.cs.saboc.blu.sno.sctdatasource.SCTRelease;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 *
 * @author Kevyn
 */
public class ShowTargetAbNSelection extends ShowAbNSelection {

    public ShowTargetAbNSelection(JFrame parentFrame, String text, LoadReleasePanel localReleasePanel, DummyConcept root, boolean useStatedRelationships, SCTAbNFrameManager displayFrameListener) {
        super(parentFrame, text, localReleasePanel, root, useStatedRelationships, displayFrameListener);
    }

    @Override
    protected void loadDataSource(boolean doLoad, LoadStatusDialog loadStatusDialog) {
        try {
            SCTRelease dataSource = localReleasePanel.getLoadedDataSource();

            TargetAbstractionNetworkGenerator generator;

            TargetAbstractionNetwork targetAbN;

            doLater(doLoad, loadStatusDialog, null);
        } catch (NoSCTDataSourceLoadedException e) {
            // TODO: Show error...
        }
    }

    @Override
    protected void displayFrame(PartitionedAbstractionNetwork taxonomy) {

    }
}
