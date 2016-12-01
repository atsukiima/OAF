/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.njit.cs.saboc.blu.sno.gui.abnselection;

import edu.njit.cs.saboc.blu.core.abn.PartitionedAbstractionNetwork;
import edu.njit.cs.saboc.blu.core.gui.dialogs.LoadStatusDialog;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 *
 * @author Kevyn
 */
public abstract class ShowAbNSelection {

    protected JFrame parentFrame;
    protected String text;
    protected LoadReleasePanel localReleasePanel;
    protected DummyConcept root;
    protected boolean useStatedRelationships;
    protected SCTDisplayFrameListener displayFrameListener;

    public ShowAbNSelection(JFrame parentFrame, String text, LoadReleasePanel localReleasePanel, DummyConcept root,
            boolean useStatedRelationships, SCTDisplayFrameListener displayFrameListener) {
        this.parentFrame = parentFrame;
        this.text = text;
        this.localReleasePanel = localReleasePanel;
        this.root = root;
        this.useStatedRelationships = useStatedRelationships;
        this.displayFrameListener = displayFrameListener;

        Thread loadThread = new Thread(new Runnable() {
            private LoadStatusDialog loadStatusDialog = null;
            private boolean doLoad = true;

            public void run() {

                loadStatusDialog = LoadStatusDialog.display(parentFrame, text,
                        new LoadStatusDialog.LoadingDialogClosedListener() {

                    @Override
                    public void dialogClosed() {
                        doLoad = false;
                    }
                });

                loadDataSource(doLoad, loadStatusDialog);
            }
        });

        loadThread.start();
    }

    protected void doLater(boolean doLoad, LoadStatusDialog loadStatusDialog, PartitionedAbstractionNetwork taxonomy) {
        SwingUtilities.invokeLater(() -> {
            if (doLoad) {
                displayFrame(taxonomy);

                loadStatusDialog.setVisible(false);
                loadStatusDialog.dispose();
            }
        });
    }

    protected abstract void loadDataSource(boolean doLoad, LoadStatusDialog loadStatusDialog);

    protected abstract void displayFrame(PartitionedAbstractionNetwork taxonomy);

}
