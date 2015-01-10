package edu.njit.cs.saboc.blu.sno.gui.abnselection;

import SnomedShared.Concept;
import edu.njit.cs.saboc.blu.core.gui.dialogs.AbNLoadStatusDialog;
import edu.njit.cs.saboc.blu.core.gui.iconmanager.IconManager;
import edu.njit.cs.saboc.blu.sno.abn.tan.TANGenerator;
import edu.njit.cs.saboc.blu.sno.abn.tan.TribalAbstractionNetwork;
import edu.njit.cs.saboc.blu.sno.datastructure.hierarchy.SCTConceptHierarchy;
import edu.njit.cs.saboc.blu.sno.localdatasource.load.ImportLocalData;
import edu.njit.cs.saboc.blu.sno.localdatasource.load.LoadLocalRelease;
import edu.njit.cs.saboc.blu.sno.localdatasource.load.LocalLoadStateMonitor;
import edu.njit.cs.saboc.blu.sno.abn.generator.SCTPAreaTaxonomyGenerator;
import edu.njit.cs.saboc.blu.sno.abn.pareataxonomy.local.SCTPAreaTaxonomy;
import edu.njit.cs.saboc.blu.sno.localdatasource.load.RelationshipsRetrieverFactory;
import edu.njit.cs.saboc.blu.sno.sctdatasource.SCTDataSource;
import edu.njit.cs.saboc.blu.sno.sctdatasource.SCTLocalDataSource;
import edu.njit.cs.saboc.blu.sno.sctdatasource.SCTRemoteDataSource;
import edu.njit.cs.saboc.blu.sno.sctdatasource.middlewareproxy.MiddlewareAccessorProxy;
import edu.njit.cs.saboc.blu.sno.utils.UtilityMethods;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
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
import javax.swing.border.BevelBorder;

/**
 *
 * @author Chris
 */
public class SCTLoaderPanel extends JPanel {

    private JTabbedPane blusnoTabbedPane = new JTabbedPane();
    
    private final JRadioButton remoteSourceBtn = new JRadioButton("NJIT Hosted Releases");
    private final JRadioButton localSourceBtn = new JRadioButton("Local SNOMED CT Release");

    private RemoteReleaseSelectionPanel remoteReleasePanel;
    private LocalReleaseSelectionPanel localReleasePanel;

    private SCTDisplayFrameListener displayFrameListener;
    
    private JCheckBox chkUseStatedRelationships;
    
    private JFrame parentFrame;

    public SCTLoaderPanel(final JFrame parentFrame, SCTDisplayFrameListener displayFrameListener) {
        this.parentFrame = parentFrame;
        this.displayFrameListener = displayFrameListener;

        this.setLayout(new BorderLayout());

        final JPanel versionSelectPanel = new JPanel(new CardLayout());

        remoteSourceBtn.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                CardLayout cl = (CardLayout) (versionSelectPanel.getLayout());
                cl.show(versionSelectPanel, "Remote");
                
                blusnoTabbedPane.setEnabledAt(1, false);
                blusnoTabbedPane.setSelectedIndex(0);
            }
        });

        localSourceBtn.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                CardLayout cl = (CardLayout) (versionSelectPanel.getLayout());
                cl.show(versionSelectPanel, "Local");
                
                blusnoTabbedPane.setEnabledAt(1, true);
                blusnoTabbedPane.setSelectedIndex(0);
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

        JPanel pareaTaxonomySelectionPanel = new JPanel(new GridLayout(4, 5, 2, 2));
        pareaTaxonomySelectionPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));

        JPanel tanSelectionPanel = new JPanel(new GridLayout(4, 5, 2, 2));
        tanSelectionPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));

        Concept [] rootConcepts = getRootConcepts();

        ArrayList<Long> taxonomyHierarchies = new ArrayList<Long>(
                Arrays.asList(272379006l, 71388002l, 404684003l, 123037004l,
                        123038009l, 373873005l, 243796009l));

        for (Concept rootConcept : rootConcepts) {
            final Concept root = rootConcept;

            String hierarchyName = root.getName().substring(0, root.getName().lastIndexOf("("));

            JButton pareaTaxonomyButton = new JButton("<html><div align=\"center\">" + hierarchyName);
            pareaTaxonomyButton.setIcon(getImageIconFor(root));

            JButton overlapTaxonomyButton = new JButton("<html><div align=\"center\">" + hierarchyName);
            overlapTaxonomyButton.setIcon(getImageIconFor(root));

            if (!taxonomyHierarchies.contains(root.getId())) {
                pareaTaxonomyButton.setEnabled(false);
            } else {
                pareaTaxonomyButton.setToolTipText("Click me to view the " + hierarchyName + " Partial-area taxonomy.");
                pareaTaxonomyButton.setBorder(BorderFactory.createLineBorder(Color.BLACK));

                pareaTaxonomyButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent ae) {
                        loadPAreaTaxonomy(root);
                    }
                });
            }

            overlapTaxonomyButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    loadTAN(root);
                }
            });

            overlapTaxonomyButton.setToolTipText("Click me to view the " + hierarchyName + " Overlap taxonomy.");
            overlapTaxonomyButton.setBorder(BorderFactory.createLineBorder(Color.BLACK));

            pareaTaxonomySelectionPanel.add(pareaTaxonomyButton);
            tanSelectionPanel.add(overlapTaxonomyButton);
        }

        blusnoTabbedPane.addTab("Partial-area Taxonomy", pareaTaxonomySelectionPanel);
        blusnoTabbedPane.addTab("Tribal Abstraction Network", tanSelectionPanel);
        blusnoTabbedPane.addTab("Concept Browser", createConceptBrowserPanel());

        chkUseStatedRelationships = new JCheckBox("Use Stated Relationships");
        
        JPanel blusnoTabPanel = new JPanel(new BorderLayout());
        blusnoTabPanel.add(chkUseStatedRelationships, BorderLayout.NORTH);
        
        blusnoTabPanel.add(blusnoTabbedPane, BorderLayout.CENTER);
        blusnoTabPanel.setBorder(BorderFactory.createTitledBorder("3: Select Abstraction Network and Hierarchy"));

        this.add(blusnoTabPanel, BorderLayout.CENTER);
        
        this.setStatedRelationshipsEnabled(false);
    }
    
    private Concept[] getRootConcepts() {
        
        return new Concept[] {
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
        };
    }

    /**
     * Panel with options to start the concept browser.
     *
     * @return
     */
    private JPanel createConceptBrowserPanel() {
        JPanel browserPanel = new JPanel(new GridLayout(1, 2, 2, 2));

        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        JButton openBrowserBtn = new JButton("Open Concept Browser");

        openBrowserBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                openConceptBrowser();
            }
        });

        SCTConceptSearchPanel searchPanel = new SCTConceptSearchPanel(new SCTConceptSearchActions() {
            public void doAction(Concept c) {
                openConceptBrowser(c);
            }

            public SCTDataSource getDataSource() {
                return getSelectedDataSource();
            }
        });

        searchPanel.setBorder(BorderFactory.createTitledBorder("Search for SNOMED CT Concepts"));

        JPanel browserBtnPanel = new JPanel();
        browserBtnPanel.add(openBrowserBtn);

        leftPanel.add(browserBtnPanel, BorderLayout.NORTH);
        leftPanel.add(searchPanel, BorderLayout.CENTER);

        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        
        JEditorPane detailsPane = new JEditorPane();
        detailsPane.setContentType("text/html");
        
        String detailsString = "<html>The BLUSNO concept browser allows you to review individual concepts. "
                + "BLUSNO's concept browser is unique in that it displays information derived "
                + "from various abstraction networks. Additionally, when a local release is loaded, BLUSNO's concept browser displays information from "
                + "both the inferred and stated versions of SNOMED CT. <p>"
                + ""
                + "<b>Subject subtaxonomies</b>, <b>Focus subtaxonomies</b>, and <b>Tribal Abstraction Networks</b> can be derived "
                + "for individual concepts from within the concept browser when using a local SNOMED CT release."
                + "<p>"
                + "Click \"Open Concept Browser\" or search for a concept and then double click on a search result to begin.";
        
        detailsPane.setText(detailsString);
        
        rightPanel.add(detailsPane, BorderLayout.CENTER);

        browserPanel.add(leftPanel);
        browserPanel.add(rightPanel);

        return browserPanel;
    }

    /**
     * Loads a partial-area taxonomy for the given root concept's hierarchy
     *
     * @param root
     */
    private void loadPAreaTaxonomy(final Concept root) {
        
               
        Thread loadThread = new Thread(new Runnable() {
            private AbNLoadStatusDialog loadStatusDialog = null;

            public void run() {
                
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        loadStatusDialog = AbNLoadStatusDialog.display(parentFrame);
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

                        final SCTPAreaTaxonomyGenerator generator
                                = new SCTPAreaTaxonomyGenerator(localConcept, dataSource,
                                        (SCTConceptHierarchy) dataSource.getConceptHierarchy().getSubhierarchyRootedAt(localConcept),
                                        RelationshipsRetrieverFactory.getRelationshipsRetriever(chkUseStatedRelationships.isSelected()));

                        taxonomy = generator.derivePAreaTaxonomy();

                    } catch (NoSCTDataSourceLoadedException e) {
                        // TODO: Show error...

                        return;
                    }
                }

                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        displayFrameListener.addNewPAreaGraphFrame(taxonomy, true, false);
                        
                        if (loadStatusDialog != null) {
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
    private void loadTAN(final Concept root) {
        
        Thread loadThread = new Thread(new Runnable() {
            private AbNLoadStatusDialog loadStatusDialog = null;

            public void run() {
                
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        loadStatusDialog = AbNLoadStatusDialog.display(parentFrame);
                    }
                });
                
                
                if (localSourceBtn.isSelected()) {
                    final TribalAbstractionNetwork tan;

                    try {
                        SCTLocalDataSource dataSource = localReleasePanel.getLoadedDataSource();

                        tan = TANGenerator.createTANFromConceptHierarchy(
                                root,
                                dataSource.getSelectedVersion(),
                                (SCTConceptHierarchy) dataSource.getConceptHierarchy().getSubhierarchyRootedAt(dataSource.getConceptFromId(root.getId())));

                        SwingUtilities.invokeLater(new Runnable() {
                            public void run() {
                                displayFrameListener.addNewClusterGraphFrame(tan, true, false);

                                if (loadStatusDialog != null) {
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
        
        displayFrameListener.addNewBrowserFrame(getSelectedDataSource());
    }

    private void openConceptBrowser(Concept c) {
        displayFrameListener.addNewBrowserFrame(c, getSelectedDataSource());
    }

    /**
     * Returns the image icon for the specified hierarchy root concept.
     *
     * @param root The root concept of the hierarchy.
     * @return Icon, if one exists, for the given root's hierarchy.
     */
    private ImageIcon getImageIconFor(Concept root) {

        String rootName = root.getName();
        String iconName = "";

        if (rootName.startsWith("Body structure")) {
            iconName = "bodystructure-icon.png";
        } else if (rootName.startsWith("Procedure")) {
            iconName = "procedure-icon.png";
        } else if (rootName.startsWith("Pharmaceutical")) {
            iconName = "pharmaceuticalproduct-icon.png";
        } else if (rootName.startsWith("Specimen")) {
            iconName = "specimen-icon.png";
        } else if (rootName.startsWith("Record artifact")) {
            iconName = "recordartifact-icon.png";
        } else if (rootName.startsWith("Clinical finding")) {
            iconName = "clinicalfinding-icon.png";
        } else if (rootName.startsWith("Environment or")) {
            iconName = "environmentgeoloc-icon.png";
        } else if (rootName.startsWith("Organism")) {
            iconName = "organism-icon.png";
        } else if (rootName.startsWith("Physical force")) {
            iconName = "physicalforce-icon.png";
        } else if (rootName.startsWith("Event")) {
            iconName = "event-icon.png";
        } else if (rootName.startsWith("Observable entity")) {
            iconName = "observableentity-icon.png";
        } else if (rootName.startsWith("Substance")) {
            iconName = "substance-icon.png";
        } else if (rootName.startsWith("Physical object")) {
            iconName = "physicalobject-icon.png";
        } else if (rootName.startsWith("Linkage concept")) {
            iconName = "linkage-icon.png";
        } else if (rootName.startsWith("Social context")) {
            iconName = "social-icon.png";
        } else if (rootName.startsWith("Staging")) {
            iconName = "scales-icon.png";
        } else if (rootName.startsWith("Situation ")) {
            iconName = "situation-icon.png";
        } else {
            return null;
        }

        return IconManager.getIconManager().getIcon(iconName);
    }
    
    public void setTabsEnabled(boolean value) {
        blusnoTabbedPane.setEnabled(value);
    }
    
    public void setStatedRelationshipsEnabled(boolean value) {
        chkUseStatedRelationships.setEnabled(value);
        chkUseStatedRelationships.setVisible(value);
        
        if(value == false){
            chkUseStatedRelationships.setSelected(false);
        }
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

    private SCTLocalDataSource loadedDataSource = null;

    private JToggleButton loadButton = new JToggleButton("Load");

    private JProgressBar loadProgressBar;

    public LocalReleaseSelectionPanel(final SCTLoaderPanel sctLoaderPanel) {
        this.sctLoaderPanel = sctLoaderPanel;

        localVersionBox = new JComboBox();
        localVersionBox.setBackground(Color.WHITE);
        localVersionBox.addItem("Choose a directory");

        final JButton chooserButton = new JButton("Open Folder");

        chooserButton.addActionListener(new ActionListener() {
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
                    chooserButton.setEnabled(false);

                    startLocalReleaseThread();
                } else {
                    loadButton.setText("Load");
                    localVersionBox.setEnabled(true);
                    chooserButton.setEnabled(true);
                }
            }
        });

        loadProgressBar = new JProgressBar(0, 100);
        loadProgressBar.setVisible(false);

        JPanel localReleasePanel = new JPanel();
        localReleasePanel.add(chooserButton);
        localReleasePanel.add(localVersionBox);
        localReleasePanel.add(loadButton);
        localReleasePanel.add(loadProgressBar);
        localReleasePanel.setBorder(BorderFactory.createTitledBorder("2: Select a Folder Containing SNOMED CT Release(s)"));

        this.setLayout(new BorderLayout());
        this.add(localReleasePanel);
    }

    private void loadComplete() {
        loadButton.setText("Unload");
        loadButton.setEnabled(true);

        loadProgressBar.setVisible(false);
        
        if(loadedDataSource.supportsStatedRelationships()) {
            sctLoaderPanel.setStatedRelationshipsEnabled(true);
        } else {
            sctLoaderPanel.setStatedRelationshipsEnabled(false);
        }

        sctLoaderPanel.setTabsEnabled(true);
    }

    private void startLocalReleaseThread() {
        new Thread(new Runnable() {
            public void run() {

                try {
                    ImportLocalData importer = new ImportLocalData();
                    final LocalLoadStateMonitor loadMonitor = importer.getLoadStateMonitor();

                    LoadMonitorTask task = new LoadMonitorTask(loadMonitor);

                    task.addPropertyChangeListener(new PropertyChangeListener() {
                        public void propertyChange(PropertyChangeEvent pce) {
                            loadProgressBar.setValue(loadMonitor.getOverallProgress());
                            loadProgressBar.setString(loadMonitor.getProcessName());
                        }
                    });

                    task.execute();

                    SCTLocalDataSource dataSource
                            = importer.loadLocalSnomedRelease(getSelectedVersion(), getSelectedVersionName(), loadMonitor);

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
