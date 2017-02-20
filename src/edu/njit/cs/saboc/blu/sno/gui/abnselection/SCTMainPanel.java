package edu.njit.cs.saboc.blu.sno.gui.abnselection;

import edu.njit.cs.saboc.blu.sno.sctdatasource.SCTRelease;
import java.awt.BorderLayout;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

/**
 *
 * @author Chris O
 */
public class SCTMainPanel extends JPanel {
    
    private final LoadReleasePanel loadReleasePanel;
    
    private final SCTAbNCreationPanel abnCreationPanel;
    
    public SCTMainPanel(SCTAbNFrameManager frameManager) {
        super(new BorderLayout());
        
        loadReleasePanel = new LoadReleasePanel();
        loadReleasePanel.addLocalDataSourceLoadedListener(new LoadReleasePanel.LocalDataSourceListener() {
            
            @Override
            public void localDataSourceLoaded(SCTRelease dataSource) {
                setEnabled(true);

                abnCreationPanel.setCurrentRelease(dataSource);
                
                abnCreationPanel.setEnabled(true);
            }

            @Override
            public void localDataSourceUnloaded() {
                setEnabled(false);
                
                abnCreationPanel.setEnabled(false);
                abnCreationPanel.clear();
            }

            @Override
            public void dataSourceLoading() {
                
            }
        });

        loadReleasePanel.setBorder(BorderFactory.createTitledBorder("Select a Folder Containing SNOMED CT Release(s)"));
        
        this.add(loadReleasePanel, BorderLayout.NORTH);
        
        abnCreationPanel = new SCTAbNCreationPanel(frameManager);
        abnCreationPanel.setEnabled(false);
        
        this.add(abnCreationPanel, BorderLayout.CENTER);
    }
}
