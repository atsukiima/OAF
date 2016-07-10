package edu.njit.cs.saboc.blu.sno.gui.abnselection;

import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.PAreaTaxonomy;
import edu.njit.cs.saboc.blu.core.abn.pareataxonomy.PAreaTaxonomyGenerator;
import edu.njit.cs.saboc.blu.core.abn.tan.ClusterTribalAbstractionNetwork;
import edu.njit.cs.saboc.blu.core.abn.tan.TribalAbstractionNetworkGenerator;
import edu.njit.cs.saboc.blu.core.abn.targetbased.TargetAbstractionNetwork;
import edu.njit.cs.saboc.blu.core.abn.targetbased.TargetAbstractionNetworkGenerator;
import edu.njit.cs.saboc.blu.core.datastructure.hierarchy.Hierarchy;
import edu.njit.cs.saboc.blu.core.gui.dialogs.LoadStatusDialog;
import edu.njit.cs.saboc.blu.core.ontology.Concept;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.SCTInferredPAreaTaxonomyFactory;
import edu.njit.cs.saboc.blu.sno.localdatasource.concept.SCTConcept;
import edu.njit.cs.saboc.blu.sno.localdatasource.load.RF1ReleaseLoader;
import edu.njit.cs.saboc.blu.sno.localdatasource.load.LoadLocalRelease;
import edu.njit.cs.saboc.blu.sno.localdatasource.load.LocalLoadStateMonitor;
import edu.njit.cs.saboc.blu.sno.localdatasource.load.RF2ReleaseLoader;
import edu.njit.cs.saboc.blu.sno.sctdatasource.SCTRelease;
import edu.njit.cs.saboc.blu.sno.sctdatasource.SCTReleaseWithStated;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTabbedPane;
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

/**
 *
 * @author Chris
 */
public class SCTLoaderPanel extends JPanel {

    private final JTabbedPane blusnoTabbedPane = new JTabbedPane();

    private final LocalReleaseSelectionPanel localReleasePanel;

    private final SCTHierarchySelectionPanel pareaTaxonomySelectionPanel;
    private final SCTHierarchySelectionPanel tanSelectionPanel;

    private JButton openBrowserBtn;

    private final SCTDisplayFrameListener displayFrameListener;

    private final JFrame parentFrame;

    public SCTLoaderPanel(final JFrame parentFrame, SCTDisplayFrameListener displayFrameListener) {
        this.parentFrame = parentFrame;
        this.displayFrameListener = displayFrameListener;

        this.setLayout(new BorderLayout());

        final JPanel versionSelectPanel = new JPanel(new CardLayout());

        localReleasePanel = new LocalReleaseSelectionPanel(this);
        localReleasePanel.addLocalDataSourceLoadedListener(new LocalReleaseSelectionPanel.LocalDataSourceListener() {
            public void localDataSourceLoaded(SCTRelease dataSource) {
                enableLocalDataSourceOptions(dataSource);
            }

            public void localDataSourceUnloaded() {
                disableLocalDataSourceOptions();
            }
        });

        versionSelectPanel.add(localReleasePanel, "Local");

        JPanel dataSelectionPanel = new JPanel();
        dataSelectionPanel.setLayout(new BoxLayout(dataSelectionPanel, BoxLayout.X_AXIS));

        dataSelectionPanel.add(versionSelectPanel);

        this.add(dataSelectionPanel, BorderLayout.NORTH);

        ArrayList<Long> taxonomyHierarchies = new ArrayList<>(
                Arrays.asList(272379006l, 71388002l, 404684003l, 123037004l,
                        123038009l, 373873005l, 243796009l));

        ArrayList<DummyConcept> rootConcepts = getRootConcepts();
        ArrayList<DummyConcept> pareaEabledHierarchies = new ArrayList<>();

        rootConcepts.forEach((root) -> {
            if (taxonomyHierarchies.contains(root.getID())) {
                pareaEabledHierarchies.add(root);
            }
        });

        pareaTaxonomySelectionPanel = new SCTHierarchySelectionPanel(rootConcepts, pareaEabledHierarchies, "Partial-area Taxonomy",
                new SCTHierarchySelectionPanel.HierarchySelectionAction() {
                    public void performHierarchySelectionAction(DummyConcept root, boolean useStated) {
                        loadPAreaTaxonomy(root, useStated);
                    }
                });

        pareaTaxonomySelectionPanel.setEnabled(false);

        tanSelectionPanel = new SCTHierarchySelectionPanel(rootConcepts, rootConcepts, "Tribal Abstraction Network (TAN)",
                new SCTHierarchySelectionPanel.HierarchySelectionAction() {
                    public void performHierarchySelectionAction(DummyConcept root, boolean useStated) {

                        loadTAN(root, useStated);
                    }
                });
        tanSelectionPanel.setEnabled(false);


        tanSelectionPanel.setEnabled(false);

        blusnoTabbedPane.addTab("Partial-area Taxonomy", pareaTaxonomySelectionPanel);
        blusnoTabbedPane.addTab("Tribal Abstraction Network", tanSelectionPanel);

        blusnoTabbedPane.setEnabled(false);

        JPanel blusnoTabPanel = new JPanel(new BorderLayout());

        blusnoTabPanel.add(blusnoTabbedPane, BorderLayout.CENTER);
        blusnoTabPanel.setBorder(BorderFactory.createTitledBorder("3: Select Abstraction Network and Hierarchy"));

        this.add(blusnoTabPanel, BorderLayout.CENTER);
        this.add(createConceptBrowserPanel(), BorderLayout.SOUTH);
        
        
    }

    private void enableLocalDataSourceOptions(SCTRelease dataSource) {
        blusnoTabbedPane.setEnabled(true);
        blusnoTabbedPane.setEnabledAt(1, true);

        if (dataSource.supportsStatedRelationships()) {
            pareaTaxonomySelectionPanel.setStatedReleaseAvailable(true);
            tanSelectionPanel.setStatedReleaseAvailable(true); // TODO: When Stated works for TANs, update this
        } else {
            pareaTaxonomySelectionPanel.setStatedReleaseAvailable(false);
            tanSelectionPanel.setStatedReleaseAvailable(false);
        }

        pareaTaxonomySelectionPanel.setEnabled(true);
        tanSelectionPanel.setEnabled(true);
        openBrowserBtn.setEnabled(true);
    }

    private void disableLocalDataSourceOptions() {
        blusnoTabbedPane.setEnabled(false);
        pareaTaxonomySelectionPanel.setEnabled(false);
        tanSelectionPanel.setEnabled(false);
        openBrowserBtn.setEnabled(false);
    }

    private ArrayList<DummyConcept> getRootConcepts() {

        return new ArrayList<>(Arrays.asList(new DummyConcept[]{
            new DummyConcept(123037004, "Body structure (body structure)"),
            new DummyConcept(404684003, "Clinical finding (finding)"),
            new DummyConcept(308916002, "Environment or geographical location (environment / location)"),
            new DummyConcept(272379006, "Event (event)"),
            new DummyConcept(106237007, "Linkage concept (linkage concept)"),
            new DummyConcept(363787002, "Observable entity (observable entity)"),
            new DummyConcept(410607006, "Organism (organism)"),
            new DummyConcept(373873005, "Pharmaceutical / biologic product (product)"),
            new DummyConcept(78621006, "Physical force (physical force)"),
            new DummyConcept(260787004, "Physical object (physical object)"),
            new DummyConcept(71388002, "Procedure (procedure)"),
            new DummyConcept(362981000, "Qualifier value (qualifier value)"),
            new DummyConcept(419891008, "Record artifact (record artifact)"),
            new DummyConcept(243796009, "Situation with explicit context (situation)"),
            new DummyConcept(48176007, "Social context (social concept)"),
            new DummyConcept(370115009, "Special concept (special concept)"),
            new DummyConcept(123038009, "Specimen (specimen)"),
            new DummyConcept(254291000, "Staging and scales (staging scale)"),
            new DummyConcept(105590001, "Substance (substance)")
        }));
    }

    /**
     * Panel with options to start the concept browser.
     *
     * @return
     */
    private JPanel createConceptBrowserPanel() {
        JPanel browserPanel = new JPanel();
        browserPanel.setLayout(new BoxLayout(browserPanel, BoxLayout.X_AXIS));
        browserPanel.setBorder(BorderFactory.createTitledBorder("BLUSNO Neighborhood Auditing Tool (NAT) Concept Browser"));

        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        openBrowserBtn = new JButton("<html><div align='center'>Open NAT Concept Browser");
        openBrowserBtn.setFont(openBrowserBtn.getFont().deriveFont(Font.BOLD, 14));

        openBrowserBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                openConceptBrowser();
            }
        });

        openBrowserBtn.setEnabled(false);

        leftPanel.add(openBrowserBtn, BorderLayout.CENTER);

        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        JEditorPane detailsPane = new JEditorPane();
        detailsPane.setContentType("text/html");

        String detailsString = "<html>The BLUSNO Neighborhood Auditing Tool (NAT) concept browser allows you to browse individual concepts. "
                + "BLUSNO's concept browser is unique in that it displays information derived "
                + "from various SNOMED CT abstraction networks alongside information about a chosen concept's neighborhood. "
                + "<b>Subject subtaxonomies</b>, <b>Focus subtaxonomies</b>, and <b>Tribal Abstraction Networks</b> can be derived "
                + "for individual concepts from within the concept browser when using a local SNOMED CT release.";

        detailsPane.setText(detailsString);

        rightPanel.add(detailsPane, BorderLayout.CENTER);

        browserPanel.add(leftPanel);
        browserPanel.add(Box.createHorizontalStrut(8));
        browserPanel.add(rightPanel);

        return browserPanel;
    }

    /**
     * Loads a partial-area taxonomy for the given root concept's hierarchy
     *
     * @param root
     */
    private void loadPAreaTaxonomy(DummyConcept root, boolean useStatedRelationships) {

        Thread loadThread = new Thread(new Runnable() {
            private LoadStatusDialog loadStatusDialog = null;
            private boolean doLoad = true;

            public void run() {

                loadStatusDialog = LoadStatusDialog.display(parentFrame, String.format("Creating the %s partial-area taxonomy.", root.getName()),
                        new LoadStatusDialog.LoadingDialogClosedListener() {

                            @Override
                            public void dialogClosed() {
                                doLoad = false;
                            }
                        });

                try {
                    SCTRelease dataSource = localReleasePanel.getLoadedDataSource();

                    SCTConcept localConcept = dataSource.getConceptFromId(root.getID());

                    Hierarchy<SCTConcept> hierarchy;

                    if (useStatedRelationships) {
                        SCTReleaseWithStated statedDataSource = (SCTReleaseWithStated) dataSource;

                        hierarchy = statedDataSource.getStatedHierarchy().getSubhierarchyRootedAt(localConcept);
                    } else {
                        hierarchy = dataSource.getConceptHierarchy().getSubhierarchyRootedAt(localConcept);
                    }

                    PAreaTaxonomyGenerator generator = new PAreaTaxonomyGenerator();

                    PAreaTaxonomy taxonomy = generator.derivePAreaTaxonomy(
                        new SCTInferredPAreaTaxonomyFactory(hierarchy), 
                            hierarchy);
                    
                    SwingUtilities.invokeLater(() -> {
                        if (doLoad) {
                            displayFrameListener.addNewPAreaGraphFrame(taxonomy);
                            
                            loadStatusDialog.setVisible(false);
                            loadStatusDialog.dispose();
                        }
                    });

                } catch (NoSCTDataSourceLoadedException e) {
                    // TODO: Show error...
                }
            }
        });

        loadThread.start();
    }

    /**
     * Loads a Tribal Abstraction Network for the given root's hierarchy
     *
     * @param root
     */
    private void loadTAN(DummyConcept root, boolean useStated) {

        Thread loadThread = new Thread(new Runnable() {
            private LoadStatusDialog loadStatusDialog = null;
            private boolean doLoad = true;

            public void run() {

                loadStatusDialog = LoadStatusDialog.display(parentFrame, String.format("Creating the %s Tribal Abstraction Network (TAN).", root.getName()),
                        new LoadStatusDialog.LoadingDialogClosedListener() {

                            @Override
                            public void dialogClosed() {
                                doLoad = false;
                            }
                        });

                try {
                    SCTRelease dataSource = localReleasePanel.getLoadedDataSource();

                    Hierarchy<SCTConcept> hierarchy;

                    if (useStated) {
                        SCTReleaseWithStated statedDataSource = (SCTReleaseWithStated) dataSource;
                        hierarchy = statedDataSource.getStatedHierarchy().getSubhierarchyRootedAt(dataSource.getConceptFromId(root.getID()));
                    } else {
                        hierarchy = dataSource.getConceptHierarchy().getSubhierarchyRootedAt(dataSource.getConceptFromId(root.getID()));
                    }

                    TribalAbstractionNetworkGenerator generator = new TribalAbstractionNetworkGenerator();

                    ClusterTribalAbstractionNetwork tan = generator.deriveTANFromSingleRootedHierarchy(hierarchy);

                    SwingUtilities.invokeLater( () -> {
                        if (doLoad) {
                            displayFrameListener.addNewClusterGraphFrame(tan);
                            
                            loadStatusDialog.setVisible(false);
                            loadStatusDialog.dispose();
                        }
                    });
                } catch (NoSCTDataSourceLoadedException e) {
                    // TODO: Show error...

                }
            }

        });

        loadThread.start();

    }

    private void loadTargetAbN(final Concept root) {

        Thread loadThread = new Thread(new Runnable() {
            private LoadStatusDialog loadStatusDialog = null;
            private boolean doLoad = true;

            public void run() {

                loadStatusDialog = LoadStatusDialog.display(parentFrame, String.format("Creating the %s Target Abstraction Network.", root.getName()),
                        new LoadStatusDialog.LoadingDialogClosedListener() {

                            @Override
                            public void dialogClosed() {
                                doLoad = false;
                            }
                        });

                try {
                    SCTRelease dataSource = localReleasePanel.getLoadedDataSource();

                    TargetAbstractionNetworkGenerator generator;

                    TargetAbstractionNetwork targetAbN;

                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            if (doLoad) {
                                loadStatusDialog.setVisible(false);
                                loadStatusDialog.dispose();
                            }
                        }
                    });
                } catch (NoSCTDataSourceLoadedException e) {
                    // TODO: Show error...
                }

            }
        });

        loadThread.start();
    }

    private SCTRelease getSelectedDataSource() {
        SCTRelease dataSource;

        try {
            dataSource = localReleasePanel.getLoadedDataSource();
        } catch (NoSCTDataSourceLoadedException e) {
            
            // TODO: Show error...
            return null;
        }

        return dataSource;
    }

    private void openConceptBrowser() {
        if (getSelectedDataSource() == null) {
            JOptionPane.showMessageDialog(null, "Please open a SNOMED CT release.",
                    "No Local Release Opened", JOptionPane.ERROR_MESSAGE);

            return;
        }

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                //TODO: Reneable browser frame
                
                //displayFrameListener.addNewBrowserFrame(getSelectedDataSource());
            }
        });
    }

    public void setTabsEnabled(boolean value) {
        blusnoTabbedPane.setEnabled(value);
    }
}

/**
 * Exception which is thrown by local selection panel when no data source has
 * been loaded
 */
class NoSCTDataSourceLoadedException extends Exception {

    public NoSCTDataSourceLoadedException() {
        super("No SCT Data Source Loaded");
    }
}

/**
 * Panel which contains all of the options for a SCT release that is stored
 * locally
 */
class LocalReleaseSelectionPanel extends JPanel {

    public interface LocalDataSourceListener {
        public void localDataSourceLoaded(SCTRelease dataSource);

        public void localDataSourceUnloaded();
    }

    private class LoadMonitorTask extends SwingWorker {

        private LocalLoadStateMonitor stateMonitor;

        public LoadMonitorTask(LocalLoadStateMonitor stateMonitor) {
            this.stateMonitor = stateMonitor;
        }

        public Void doInBackground() {

            setProgress(0);

            while (stateMonitor.getOverallProgress() < 100) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ignore) {

                }

                setProgress(stateMonitor.getOverallProgress());
            }

            return null;
        }
    }

    private SCTLoaderPanel sctLoaderPanel;

    private JComboBox localVersionBox;

    private ArrayList<File> availableReleases = new ArrayList<>();

    private final JButton chooserBtn;

    private SCTRelease loadedDataSource = null;

    private JToggleButton loadButton = new JToggleButton("Load");

    private JProgressBar loadProgressBar;

    private final ArrayList<LocalDataSourceListener> dataSourceLoadedListeners = new ArrayList<>();

    public LocalReleaseSelectionPanel(final SCTLoaderPanel sctLoaderPanel) {
        this.sctLoaderPanel = sctLoaderPanel;

        localVersionBox = new JComboBox();
        localVersionBox.setBackground(Color.WHITE);
        localVersionBox.addItem("Choose a directory");

        chooserBtn = new JButton("Open Folder");

        chooserBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                localVersionBox.removeAllItems();

                showReleaseFolderSelectionDialog();
            }
        });

        loadButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                if (loadButton.isSelected()) {
                    if (availableReleases.isEmpty()) {
                        loadButton.setSelected(false);
                        return;
                    }

                    sctLoaderPanel.setTabsEnabled(false);

                    loadProgressBar.setValue(0);
                    loadProgressBar.setString("Detecting Files...");
                    loadProgressBar.setStringPainted(true);
                    loadProgressBar.setVisible(true);

                    loadButton.setEnabled(false);
                    localVersionBox.setEnabled(false);
                    chooserBtn.setEnabled(false);

                    startLocalReleaseThread();
                } else {
                    dataSourceUnloaded();
                }
            }
        });

        loadProgressBar = new JProgressBar(0, 100);
        loadProgressBar.setVisible(false);

        JPanel localReleasePanel = new JPanel();

        localReleasePanel.add(chooserBtn);
        localReleasePanel.add(localVersionBox);
        localReleasePanel.add(loadButton);
        localReleasePanel.add(loadProgressBar);
        localReleasePanel.setBorder(BorderFactory.createTitledBorder("2: Select a Folder Containing SNOMED CT Release(s)"));

        this.setLayout(new BorderLayout());
        this.add(localReleasePanel);
    }

    public void addLocalDataSourceLoadedListener(LocalDataSourceListener listener) {
        dataSourceLoadedListeners.add(listener);
    }

    private void loadComplete() {
        loadButton.setText("Unload");
        loadButton.setEnabled(true);

        loadProgressBar.setVisible(false);

        dataSourceLoadedListeners.forEach((LocalDataSourceListener listener) -> {
            listener.localDataSourceLoaded(loadedDataSource);
        });

        sctLoaderPanel.setEnabled(true);
    }

    private void dataSourceUnloaded() {
        loadButton.setText("Load");
        localVersionBox.setEnabled(true);
        chooserBtn.setEnabled(true);

        this.loadedDataSource = null;

        dataSourceLoadedListeners.forEach((LocalDataSourceListener listener) -> {
            listener.localDataSourceUnloaded();
        });
    }

    private void startLocalReleaseThread() {
        new Thread(new Runnable() {
            public void run() {
                try {
                    File selectedFile = getSelectedVersion();

                    final LocalLoadStateMonitor loadMonitor;
                    final SCTRelease dataSource;

                    if (selectedFile.getAbsolutePath().contains("RF2Release")) {
                        RF2ReleaseLoader rf2Importer = new RF2ReleaseLoader();

                        loadMonitor = rf2Importer.getLoadStateMonitor();

                        LoadMonitorTask task = new LoadMonitorTask(loadMonitor);

                        task.addPropertyChangeListener(new PropertyChangeListener() {
                            public void propertyChange(PropertyChangeEvent pce) {
                                loadProgressBar.setValue(loadMonitor.getOverallProgress());
                                loadProgressBar.setString(loadMonitor.getProcessName());
                            }
                        });

                        task.execute();

                        dataSource = rf2Importer.loadLocalSnomedRelease(getSelectedVersion(), getSelectedVersionName(), loadMonitor);

                    } else {
                        RF1ReleaseLoader importer = new RF1ReleaseLoader();

                        loadMonitor = importer.getLoadStateMonitor();

                        LoadMonitorTask task = new LoadMonitorTask(loadMonitor);

                        task.addPropertyChangeListener(new PropertyChangeListener() {
                            public void propertyChange(PropertyChangeEvent pce) {
                                loadProgressBar.setValue(loadMonitor.getOverallProgress());
                                loadProgressBar.setString(loadMonitor.getProcessName());
                            }
                        });

                        task.execute();

                        dataSource = importer.loadLocalSnomedRelease(getSelectedVersion(), getSelectedVersionName(), loadMonitor);
                    }

                    loadedDataSource = dataSource;

                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            loadComplete();
                        }
                    });

                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
        }).start();
    }

    private void showReleaseFolderSelectionDialog() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        int returnVal = chooser.showOpenDialog(null);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            try {
                File file = chooser.getSelectedFile();
                this.availableReleases = LoadLocalRelease.findReleaseFolders(file);

                if (availableReleases.isEmpty()) {
                    localVersionBox.removeAllItems();
                    localVersionBox.addItem("Choose a directory");
                } else {
                    ArrayList<String> releaseNames = LoadLocalRelease.getReleaseFileNames(this.availableReleases);

                    for (String releaseName : releaseNames) {
                        localVersionBox.addItem(releaseName);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            if (availableReleases.isEmpty()) {
                localVersionBox.removeAllItems();
                localVersionBox.addItem("Choose a directory");
            }
        }
    }

    public File getSelectedVersion() {
        return availableReleases.get(localVersionBox.getSelectedIndex());
    }

    public String getSelectedVersionName() {
        return (String) localVersionBox.getItemAt(localVersionBox.getSelectedIndex());
    }

    public SCTRelease getLoadedDataSource() throws NoSCTDataSourceLoadedException {
        if (loadedDataSource == null) {
            throw new NoSCTDataSourceLoadedException();
        }

        return loadedDataSource;
    }
}
