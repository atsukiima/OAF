package edu.njit.cs.saboc.blu.sno.gui.main;

import edu.njit.cs.saboc.blu.core.utils.toolstate.OAFRecentlyOpenedFileManager;
import edu.njit.cs.saboc.blu.core.utils.toolstate.OAFStateFileManager;
import edu.njit.cs.saboc.blu.sno.gui.openrelease.LoadReleasePanel;
import edu.njit.cs.saboc.blu.sno.gui.openrelease.LoadReleasePanel.LocalDataSourceListener;
import edu.njit.cs.saboc.blu.sno.nat.SCTNATLayout;
import edu.njit.cs.saboc.blu.sno.sctdatasource.SCTRelease;
import edu.njit.cs.saboc.nat.generic.NATBrowserPanel;
import java.awt.BorderLayout;
import java.awt.GraphicsEnvironment;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author Chris Ochs
 */
public class NATOnlyMainFrame extends JFrame {
    
    private final LoadReleasePanel loadReleasePanel;
    private final NATBrowserPanel browserPanel;
    
    private OAFStateFileManager stateFileManager;
    
    public NATOnlyMainFrame() {
        
        try {
            this.stateFileManager = new OAFStateFileManager("BLUSNO");
        } catch (OAFRecentlyOpenedFileManager.RecentlyOpenedFileException rofe) {
            this.stateFileManager = null;
        }
        
        this.loadReleasePanel = new LoadReleasePanel(null, stateFileManager);
        this.loadReleasePanel.setBorder(BorderFactory.createEtchedBorder());
        
        this.loadReleasePanel.addLocalDataSourceLoadedListener(new LocalDataSourceListener() {
            @Override
            public void localDataSourceLoaded(SCTRelease release) {
                browserPanel.setDataSource(release.getConceptBrowserDataSource());
                
                browserPanel.getFocusConceptManager().navigateToRoot();
                
                browserPanel.setEnabled(true);
            }

            @Override
            public void dataSourceLoading() {
                browserPanel.setEnabled(false);
                browserPanel.reset();
            }

            @Override
            public void localDataSourceUnloaded() {
                browserPanel.setEnabled(false);
                browserPanel.reset();
            }
        });
        
        this.browserPanel = new NATBrowserPanel<>(this, new SCTNATLayout());
        this.browserPanel.setEnabled(false);
        
        JPanel contentPanel = new JPanel(new BorderLayout());
        
        contentPanel.add(loadReleasePanel, BorderLayout.NORTH);
        contentPanel.add(browserPanel, BorderLayout.CENTER);
        
        this.add(contentPanel);
        
        setTitle("Ontology Abstraction Framework (OAF) Concept Browser by SABOC at NJIT");
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        setBounds(GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds());
        
        setVisible(true);
    }
}
