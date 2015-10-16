package edu.njit.cs.saboc.blu.sno.gui.abnselection;

import SnomedShared.Concept;
import edu.njit.cs.saboc.blu.core.gui.dialogs.LoadStatusDialog;
import edu.njit.cs.saboc.blu.sno.abn.tan.TANGenerator;
import edu.njit.cs.saboc.blu.sno.datastructure.hierarchy.SCTConceptHierarchy;
import edu.njit.cs.saboc.blu.sno.localdatasource.load.ImportLocalData;
import edu.njit.cs.saboc.blu.sno.localdatasource.load.LoadLocalRelease;
import edu.njit.cs.saboc.blu.sno.localdatasource.load.LocalLoadStateMonitor;
import edu.njit.cs.saboc.blu.sno.abn.generator.SCTPAreaTaxonomyGenerator;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTPAreaTaxonomy;
import edu.njit.cs.saboc.blu.sno.abn.tan.local.SCTTribalAbstractionNetwork;
import edu.njit.cs.saboc.blu.sno.abn.target.SCTTargetAbstractionNetwork;
import edu.njit.cs.saboc.blu.sno.abn.target.SCTTargetAbstractionNetworkGenerator;
import edu.njit.cs.saboc.blu.sno.abn.target.SCTTargetGroup;
import edu.njit.cs.saboc.blu.sno.localdatasource.load.ImportLocalDataRF2;
import edu.njit.cs.saboc.blu.sno.localdatasource.load.RelationshipsRetrieverFactory;
import edu.njit.cs.saboc.blu.sno.sctdatasource.SCTDataSource;
import edu.njit.cs.saboc.blu.sno.sctdatasource.SCTLocalDataSource;
import edu.njit.cs.saboc.blu.sno.sctdatasource.SCTLocalDataSourceWithStated;
import edu.njit.cs.saboc.blu.sno.sctdatasource.SCTRemoteDataSource;
import edu.njit.cs.saboc.blu.sno.sctdatasource.middlewareproxy.MiddlewareAccessorProxy;
import edu.njit.cs.saboc.blu.sno.utils.UtilityMethods;
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
import java.util.Collection;
import java.util.HashSet;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

/**
 *
 * @author Chris
 */
public class SCTLoaderPanel extends JPanel {

    private JTabbedPane blusnoTabbedPane = new JTabbedPane();
    
    private final JRadioButton remoteSourceBtn = new JRadioButton("NJIT Hosted Releases");
    private final JRadioButton localSourceBtn = new JRadioButton("Local SNOMED CT Release");

    private RemoteReleaseSelectionPanel remoteReleasePanel;
    private final LocalReleaseSelectionPanel localReleasePanel;
    
    private final SCTHierarchySelectionPanel pareaTaxonomySelectionPanel;
    private final SCTHierarchySelectionPanel tanSelectionPanel;
    
    private final SCTHierarchySelectionPanel targetSelectionPanel;
    
    private JButton openBrowserBtn;

    private SCTDisplayFrameListener displayFrameListener;
   
    private final JFrame parentFrame;

    public SCTLoaderPanel(final JFrame parentFrame, SCTDisplayFrameListener displayFrameListener) {
        this.parentFrame = parentFrame;
        this.displayFrameListener = displayFrameListener;

        this.setLayout(new BorderLayout());

        final JPanel versionSelectPanel = new JPanel(new CardLayout());

        remoteSourceBtn.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                CardLayout cl = (CardLayout) (versionSelectPanel.getLayout());
                cl.show(versionSelectPanel, "Remote");
                
                enableRemoteReleaseSourceOptions();
            }
        });
        
        remoteSourceBtn.setEnabled(false);

        localSourceBtn.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                CardLayout cl = (CardLayout) (versionSelectPanel.getLayout());
                cl.show(versionSelectPanel, "Local");
                
                SCTLocalDataSource dataSource;
                
                try {
                    dataSource = localReleasePanel.getLoadedDataSource();
                    enableLocalDataSourceOptions(dataSource);
                    
                    return;
                } catch(NoSCTDataSourceLoadedException e) {
                    
                }
                
                disableLocalDataSourceOptions();
            }
        });

        ButtonGroup sourceGroup = new ButtonGroup();
        sourceGroup.add(localSourceBtn);
        sourceGroup.add(remoteSourceBtn);

        localSourceBtn.setSelected(true);
        
        JPanel sctDataSourcePanel = new JPanel();
        sctDataSourcePanel.add(localSourceBtn);
        sctDataSourcePanel.add(remoteSourceBtn);
        sctDataSourcePanel.setBorder(BorderFactory.createTitledBorder("1: Select a SNOMED CT Data Source"));
        
        localReleasePanel = new LocalReleaseSelectionPanel(this);
        localReleasePanel.addLocalDataSourceLoadedListener(new LocalReleaseSelectionPanel.LocalDataSourceListener() {
            public void localDataSourceLoaded(SCTLocalDataSource dataSource) {
                enableLocalDataSourceOptions(dataSource);
            }
            
            public void localDataSourceUnloaded() {
                disableLocalDataSourceOptions();
            }
        });
        
        versionSelectPanel.add(localReleasePanel, "Local");
        
        if(MiddlewareAccessorProxy.getProxy().getSupportedSnomedVersions() == null) {
            remoteSourceBtn.setEnabled(false);
            
            JOptionPane.showMessageDialog(parentFrame, 
                    "No internet connection detected or we are experiencing technical difficulties.\n"
                            + "To use BLUSNO you will need to open a local SNOMED CT release.\n"
                            + "Once you have reconnected to the internet please restart BLUSNO.", "No Internet Connection Detected", JOptionPane.ERROR_MESSAGE);
        } else {
            remoteReleasePanel = new RemoteReleaseSelectionPanel();
            
            versionSelectPanel.add(remoteReleasePanel, "Remote");
        }
        
        JPanel dataSelectionPanel = new JPanel();
        dataSelectionPanel.setLayout(new BoxLayout(dataSelectionPanel, BoxLayout.X_AXIS));

        dataSelectionPanel.add(sctDataSourcePanel);
        dataSelectionPanel.add(versionSelectPanel);

        this.add(dataSelectionPanel, BorderLayout.NORTH);
        
        ArrayList<Long> taxonomyHierarchies = new ArrayList<Long>(
                Arrays.asList(272379006l, 71388002l, 404684003l, 123037004l,
                        123038009l, 373873005l, 243796009l));

        ArrayList<Concept> rootConcepts = getRootConcepts();
        ArrayList<Concept> pareaEabledHierarchies = new ArrayList<>();
        
        rootConcepts.forEach((Concept root) -> {
            if(taxonomyHierarchies.contains(root.getId())) {
                pareaEabledHierarchies.add(root);
            }
        });
        
        pareaTaxonomySelectionPanel = new SCTHierarchySelectionPanel(rootConcepts, pareaEabledHierarchies, "Partial-area Taxonomy",
            new SCTHierarchySelectionPanel.HierarchySelectionAction() {
                public void performHierarchySelectionAction(Concept root, boolean useStated) {
                    loadPAreaTaxonomy(root, useStated);
                }
            });
        
        pareaTaxonomySelectionPanel.setEnabled(false);

        tanSelectionPanel = new SCTHierarchySelectionPanel(rootConcepts, rootConcepts, "Tribal Abstraction Network (TAN)",
            new SCTHierarchySelectionPanel.HierarchySelectionAction() {
                public void performHierarchySelectionAction(Concept root, boolean useStated) {
                    loadTAN(root, useStated);
                }
            });
        tanSelectionPanel.setEnabled(false);
        
        ArrayList<Concept> targetEnabledHierarchies = new ArrayList<>();
        
        rootConcepts.forEach((Concept root) -> {
            if(root.getId() == 71388002L) { // Procedure
                targetEnabledHierarchies.add(root);
            }
        });
        
        targetSelectionPanel = new SCTHierarchySelectionPanel(rootConcepts, targetEnabledHierarchies, "Target AbN",
            new SCTHierarchySelectionPanel.HierarchySelectionAction() {
                public void performHierarchySelectionAction(Concept root, boolean useStated) {
                    loadTargetAbN(root);
                }
            });
        
        tanSelectionPanel.setEnabled(false);

        blusnoTabbedPane.addTab("Partial-area Taxonomy", pareaTaxonomySelectionPanel);
        blusnoTabbedPane.addTab("Tribal Abstraction Network", tanSelectionPanel);
        blusnoTabbedPane.addTab("Target Abstraction Network (Test)", targetSelectionPanel);
        
        blusnoTabbedPane.setEnabled(false);

        JPanel blusnoTabPanel = new JPanel(new BorderLayout());
        
        blusnoTabPanel.add(blusnoTabbedPane, BorderLayout.CENTER);
        blusnoTabPanel.setBorder(BorderFactory.createTitledBorder("3: Select Abstraction Network and Hierarchy"));
        
        this.add(blusnoTabPanel, BorderLayout.CENTER);
        
        this.add(createConceptBrowserPanel(), BorderLayout.SOUTH);
    }
    
    private void enableLocalDataSourceOptions(SCTLocalDataSource dataSource) {
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
    
    private void enableRemoteReleaseSourceOptions() {
        blusnoTabbedPane.setEnabled(true);
        blusnoTabbedPane.setEnabledAt(1, false);

        pareaTaxonomySelectionPanel.setEnabled(true);
        pareaTaxonomySelectionPanel.setStatedReleaseAvailable(false);

        tanSelectionPanel.setEnabled(true);
        tanSelectionPanel.setStatedReleaseAvailable(false);
        
        openBrowserBtn.setEnabled(true);
    }
    
    private ArrayList<Concept> getRootConcepts() {
        
        return new ArrayList<>(Arrays.asList(new Concept [] {
            new Concept(123037004, "Body structure (body structure)"),
            new Concept(404684003, "Clinical finding (finding)"),
            new Concept(308916002, "Environment or geographical location (environment / location)"),
            new Concept(272379006, "Event (event)"),
            new Concept(106237007, "Linkage concept (linkage concept)"),
            new Concept(363787002, "Observable entity (observable entity)"),
            new Concept(410607006, "Organism (organism)"),
            new Concept(373873005, "Pharmaceutical / biologic product (product)"),
            new Concept(78621006, "Physical force (physical force)"),
            new Concept(260787004, "Physical object (physical object)"),
            new Concept(71388002, "Procedure (procedure)"),
            new Concept(362981000, "Qualifier value (qualifier value)"),
            new Concept(419891008, "Record artifact (record artifact)"),
            new Concept(243796009, "Situation with explicit context (situation)"),
            new Concept(48176007, "Social context (social concept)"),
            new Concept(370115009, "Special concept (special concept)"),
            new Concept(123038009, "Specimen (specimen)"),
            new Concept(254291000, "Staging and scales (staging scale)"),
            new Concept(105590001, "Substance (substance)")
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
    private void loadPAreaTaxonomy(final Concept root, boolean useStatedRelationships) {

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

                final SCTPAreaTaxonomy taxonomy;

                if (remoteSourceBtn.isSelected()) {
                    taxonomy = MiddlewareAccessorProxy.getProxy().getPAreaHierarchyData(
                            remoteReleasePanel.getSelectedVersion(), root);
                } else {
                    try {
                        SCTLocalDataSource dataSource = localReleasePanel.getLoadedDataSource();

                        Concept localConcept = dataSource.getConceptFromId(root.getId());
                        
                        SCTConceptHierarchy hierarchy;
                        
                        if(useStatedRelationships) {
                            SCTLocalDataSourceWithStated statedDataSource = (SCTLocalDataSourceWithStated)dataSource;
                            
                            hierarchy = statedDataSource.getStatedHierarchy().getSubhierarchyRootedAt(localConcept);
                        } else {
                            hierarchy = dataSource.getConceptHierarchy().getSubhierarchyRootedAt(localConcept);
                        }
                        
                        final SCTPAreaTaxonomyGenerator generator
                                = new SCTPAreaTaxonomyGenerator(localConcept, dataSource,
                                        hierarchy,
                                        RelationshipsRetrieverFactory.getRelationshipsRetriever(useStatedRelationships));

                        taxonomy = generator.derivePAreaTaxonomy();

                    } catch (NoSCTDataSourceLoadedException e) {
                        // TODO: Show error...

                        return;
                    }
                }

                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        if (doLoad) {
                            displayFrameListener.addNewPAreaGraphFrame(taxonomy, true);

                            loadStatusDialog.setVisible(false);
                            loadStatusDialog.dispose();
                        }
                    }
                });
            }
        });

        loadThread.start();
    }

    /**
     * Loads a Tribal Abstraction Network for the given root's hierarchy
     *
     * @param root
     */
    private void loadTAN(final Concept root, boolean useStated) {
        
        Thread loadThread = new Thread(new Runnable() {
            private LoadStatusDialog loadStatusDialog = null;
            private boolean doLoad;

            public void run() {

                loadStatusDialog = LoadStatusDialog.display(parentFrame, String.format("Creating the %s Tribal Abstraction Network (TAN).", root.getName()),
                        new LoadStatusDialog.LoadingDialogClosedListener() {

                            @Override
                            public void dialogClosed() {
                                doLoad = false;
                            }
                        });

                if (localSourceBtn.isSelected()) {
                    final SCTTribalAbstractionNetwork tan;

                    try {
                        SCTLocalDataSource dataSource = localReleasePanel.getLoadedDataSource();
                        
                        SCTConceptHierarchy hierarchy;
                        
                        if (useStated) {
                            SCTLocalDataSourceWithStated statedDataSource = (SCTLocalDataSourceWithStated) dataSource;
                            hierarchy = statedDataSource.getStatedHierarchy().getSubhierarchyRootedAt(dataSource.getConceptFromId(root.getId()));
                        } else {
                            hierarchy = dataSource.getConceptHierarchy().getSubhierarchyRootedAt(dataSource.getConceptFromId(root.getId()));
                        }
                        
                        tan = TANGenerator.createTANFromConceptHierarchy(dataSource.getSelectedVersion(), hierarchy, dataSource);

                        SwingUtilities.invokeLater(new Runnable() {
                            public void run() {
                                if (doLoad) {
                                    displayFrameListener.addNewClusterGraphFrame(tan, true, false);

                                    loadStatusDialog.setVisible(false);
                                    loadStatusDialog.dispose();
                                }
                            }
                        });
                    } catch (NoSCTDataSourceLoadedException e) {
                        // TODO: Show error...

                    }
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

                if (localSourceBtn.isSelected()) {
                    final SCTTargetAbstractionNetwork abn;

                    try {
                        SCTLocalDataSource dataSource = localReleasePanel.getLoadedDataSource();
                        
                        SCTTargetAbstractionNetworkGenerator generator = new SCTTargetAbstractionNetworkGenerator(dataSource);
                        
                        SCTConceptHierarchy procedureHierarchyConcepts = dataSource.getConceptHierarchy().getSubhierarchyRootedAt(
                                dataSource.getConceptFromId(71388002l));
                        
                        HashSet<Concept> relTypes = new HashSet<>();
                        relTypes.add(dataSource.getConceptFromId(363704007L)); // Procedure site
                        relTypes.add(dataSource.getConceptFromId(405813007L)); // Procedure site - direct
                        relTypes.add(dataSource.getConceptFromId(405814001L)); // Procedure site - indirect
                        
                        
                        Concept targetHierarchyRoot = dataSource.getConceptFromId(123037004l);
                        
                        SCTTargetAbstractionNetwork targetAbN = generator.deriveTargetAbstractionNetwork(
                                procedureHierarchyConcepts.getConceptsInHierarchy(), relTypes, targetHierarchyRoot);
                        
                        Collection<SCTTargetGroup> targetGroups = (Collection<SCTTargetGroup>)(targetAbN.getGroups().values());
                        
                        targetGroups.forEach( (SCTTargetGroup group) -> {
                            
                            /*
                            HashMap<Concept, HashSet<Concept>> incomingRelSources = group.getGroupIncomingRelSources();
                            
                            incomingRelSources.forEach( (Concept c, HashSet<Concept> sourceConcepts) -> {
                                System.out.println(String.format("%s\t%s\t%d", 
                                        group.getRoot().getName(), 
                                        c.getName(),
                                        sourceConcepts.size()));
                            });
                            */
                            
                            System.out.println(String.format("%s\t%d\t%d", 
                                        group.getRoot().getName(), 
                                        group.getConceptCount(),
                                        group.getSourceConcepts().size()));
                            
                        });
                        
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
            }
        });

        loadThread.start();
    }

    private SCTDataSource getSelectedDataSource() {
        SCTDataSource dataSource;

        if (remoteSourceBtn.isSelected()) {
            dataSource = new SCTRemoteDataSource(remoteReleasePanel.getSelectedVersion());
        } else {
            try {
                dataSource = localReleasePanel.getLoadedDataSource();
            } catch (NoSCTDataSourceLoadedException e) {
                // TODO: Show error...
                return null;
            }
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
                displayFrameListener.addNewBrowserFrame(getSelectedDataSource());
            }
        });
    }

    public void setTabsEnabled(boolean value) {
        blusnoTabbedPane.setEnabled(value);
    }
}



/**
 * Panel which contains all of the options for a SCT release that is stored
 * remotely (e.g., on NJIT's servers)
 */
class RemoteReleaseSelectionPanel extends JPanel {

    private JComboBox remoteVersionBox;

    private ArrayList<String> availableVersions;

    public RemoteReleaseSelectionPanel() {
        availableVersions = MiddlewareAccessorProxy.getProxy().getSupportedSnomedVersions();

        ArrayList<String> printableVersions = new ArrayList<String>();

        for (String version : availableVersions) {
            printableVersions.add(UtilityMethods.getPrintableVersionName(version));
        }

        remoteVersionBox = new JComboBox(printableVersions.toArray());
        remoteVersionBox.setBackground(Color.WHITE);

        JPanel sctVersionPanel = new JPanel();
        sctVersionPanel.add(new JLabel("Available SNOMED CT Versions: "));
        sctVersionPanel.add(remoteVersionBox);
        sctVersionPanel.setBorder(BorderFactory.createTitledBorder("2: Select a NJIT Hosted SNOMED CT Version"));

        this.setLayout(new BorderLayout());
        this.add(sctVersionPanel);
    }

    public String getSelectedVersion() {
        return availableVersions.get(remoteVersionBox.getSelectedIndex());
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
        public void localDataSourceLoaded(SCTLocalDataSource dataSource);
        
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

    private ArrayList<File> availableReleases = new ArrayList<File>();
    
    private final JButton chooserBtn;

    private SCTLocalDataSource loadedDataSource = null;

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
                    final SCTLocalDataSource dataSource;

                    if (selectedFile.getAbsolutePath().contains("RF2Release")) {
                        ImportLocalDataRF2 rf2Importer = new ImportLocalDataRF2();

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
                        ImportLocalData importer = new ImportLocalData();

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

    public SCTLocalDataSource getLoadedDataSource() throws NoSCTDataSourceLoadedException {
        if (loadedDataSource == null) {
            throw new NoSCTDataSourceLoadedException();
        }

        return loadedDataSource;
    }
}
